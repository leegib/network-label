package dao

import java.util.Date
import javax.inject.Inject
import play.api.db.Database
import anorm._

class PalletLabel @Inject()(db: Database) {

  /**
   * 발주 목록
   * @param startDate 발주일(시작)
   * @param endDate 발주일(종료)
   * @param searchWord 검색어
   * @return
   */
  def orderList(startDate: Date, endDate: Date, searchWord: String): Either[String, Seq[models.Order]] = {
    db.withConnectionCover({ implicit c =>
      val searchWordSql = if (searchWord.isEmpty) "" else s"""
        AND o.id ILIKE '%$searchWord%' OR p.id ILIKE '%$searchWord%' OR p.name ILIKE '%$searchWord%'
      """

      SQL"""
        SELECT
          o.id, o.order_date, o.due_date, p.id AS product_id, p.name AS product_name,
          p.revision, o.quantity
        FROM "order" AS o LEFT OUTER JOIN product AS p ON o.product_uid = p.uid
        WHERE
          o.order_date >= $startDate AND o.order_date <= $endDate
          #$searchWordSql
      """.as(models.Order.parser.*)
    }, "pallet_label.order_list")
  }

  /**
   * 팔레트 라벨 목록
   * @param orderId 발주 번호
   * @return
   */
  def palletLabelList(orderId: String): Either[String, Seq[models.PalletLabel]] = {
    db.withConnectionCover({ implicit c =>
      SQL"""
        SELECT
          p.id, p.lot_no, p.quantity, (p.quantity * pr.weight) AS weight, u.symbol AS weight_unit,
          p.box_quantity, p.dispatch_date
        FROM pallet_label AS p
        LEFT OUTER JOIN "order" AS o ON p.order_id = o.id
        LEFT OUTER JOIN product AS pr ON o.product_uid = pr.uid
        LEFT OUTER JOIN unit AS u ON pr.weight_unit_id = u.id
        WHERE p.order_id = $orderId
      """.as(models.PalletLabel.parser.*)
    }, "pallet_label.pallet_label_list")
  }

  /**
   * 팔레트 라벨 등록 - 박스 라벨 목록
   * @param orderId 발주 번호
   * @return
   */
  def boxLabelList(orderId: String): Either[String, Seq[models.BoxLabel]] = {
    db.withConnectionCover({ implicit c =>
      SQL"""
        SELECT
          b.id, b.lot_no, b.quantity, (b.quantity * p.weight) AS weight, u.symbol AS weight_unit,
          b.production_date
        FROM "order" AS o
        LEFT OUTER JOIN box_label AS b ON o.product_uid = b.product_uid
        LEFT OUTER JOIN product AS p ON o.product_uid = p.uid
        LEFT OUTER JOIN unit AS u ON p.weight_unit_id = u.id
        LEFT OUTER JOIN label AS l ON b.id = l.box_label_id
        WHERE o.id = $orderId AND l.box_label_id IS NULL
      """.as(models.BoxLabel.parser.*)
    }, "pallet_label.box_label_list")
  }

  /**
   * 팔레트 라벨 등록
   * @param orderId 발주 번호
   * @param boxLabelList 박스라벨 목록
   * @return
   */
  def palletLabelInsert(orderId: String, boxLabelList: Seq[models.BoxLabel]): Either[String, Int] = {
    db.withTransactionCover({ implicit c =>

      val lotNo = boxLabelList.map(_.lotNo).distinct match {
        case l if l.length > 1 => "Mix"
        case l => l.headOption.getOrElse("")
      }

      SQL"""
        SELECT coalesce(max(number), 0) AS number FROM pallet_label
        WHERE order_id = $orderId
      """.as(SqlParser.get[Int]("number").singleOpt).map { number =>
        val orderIdNumber = number match {
          case n if n > 99 => n
          case n if n > 9 => s"0$n"
          case n => s"00$n"
        }
        val palletLabelId = s"$orderId$orderIdNumber"

        SQL"""
          INSERT INTO pallet_label(
            id, lot_no, quantity, box_quantity, dispatch_date,
            order_id, number
          ) VALUES(
            $palletLabelId, $lotNo, ${boxLabelList.map(_.quantity).sum}, ${boxLabelList.length}, now(),
            $orderId, ${number + 1}
          )
        """.executeUpdate

        boxLabelList.map { b =>
          SQL"""
            INSERT INTO label(pallet_label_id, box_label_id) VALUES($palletLabelId, ${b.id})
          """.executeUpdate
        }.sum
      }.getOrElse(0)
    }, "pallet_label.pallet_label_insert")
  }

}
package dao

import java.util.{UUID, Date}
import javax.inject.Inject
import play.api.db.Database
import anorm.SqlParser._
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
  def palletLabelInsert(orderId: String, boxLabelList: Seq[models.BoxLabel]): Either[String, Option[String]] = {
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
        }
        palletLabelId
      }
    }, "pallet_label.pallet_label_insert")
  }

  /**
   * 팔레트 라벨 출력
   * @param palletLabelId 팔레트 라벨 번호
   */
  def palletLabel(palletLabelId: String) = {
    db.withConnectionCover({ implicit c =>
      SQL"""
        SELECT
          o.id, o.order_date, o.due_date, o.quantity AS order_quantity, o.network_id,
          o.partner_id, o.product_uid, p.lot_no, p.quantity AS dispatch_quantity, p.box_quantity,
          p.dispatch_date
        FROM pallet_label AS p
        LEFT OUTER JOIN "order" AS o ON p.order_id = o.id
        WHERE p.id = $palletLabelId LIMIT 1
      """.as((
        get[String]("id") ~ get[Date]("order_date") ~ get[Date]("due_date") ~ get[Long]("order_quantity") ~ get[String]("network_id") ~
        get[String]("partner_id") ~ get[UUID]("product_uid") ~ get[String]("lot_no") ~ get[Long]("dispatch_quantity") ~ get[Long]("box_quantity") ~
        get[Date]("dispatch_date") map { case
          orderId ~ orderDate ~ dueDate ~ orderQuantity ~ networkId ~
          partnerId ~ productUid ~ lotNo ~ dispatchQuantity ~ boxQuantity ~
          dispatchDate => (
            orderId, orderDate, dueDate, orderQuantity, networkId,
            partnerId, productUid, lotNo, dispatchQuantity, boxQuantity,
            dispatchDate
          )
        }
      ).singleOpt).flatMap { case (
        orderId, orderDate, dueDate, orderQuantity, networkId,
        partnerId, productUid, lotNo, dispatchQuantity, boxQuantity,
        dispatchDate
      ) =>
        val network = SQL"""
          SELECT id, name, address FROM network WHERE id = $networkId LIMIT 1
        """.as(models.Network.parser.singleOpt)

        val partner = SQL"""
          SELECT uid, id, name, address FROM partner WHERE id = $partnerId LIMIT 1
        """.as(models.Partner.parser.singleOpt)

        val product = SQL"""
          SELECT id, name, revision, weight, weight_unit_id FROM product WHERE uid = $productUid::uuid LIMIT 1
        """.as((
          get[String]("id") ~ get[String]("name") ~ get[String]("revision") ~ get[Double]("weight") ~ get[String]("weight_unit_id") map { case
            productId ~ productName ~ revision ~ weight ~ weightUnitId => (
              productId, productName, revision, weight, weightUnitId
            )
          }
        ).singleOpt).map { case (productId, productName, revision, weight, weightUnitId) =>
          (productId, productName, revision, weight, weightUnitId)
        }
        for {
          n <- network
          p <- partner
          (productId, productName, revision, weight, weightUnitId) <- product
        } yield {
          (n, p, models.Order(
            orderId, orderDate, dueDate, productId, productName,
            revision, orderQuantity
          ), models.PalletLabel(
            palletLabelId, lotNo, dispatchQuantity, weight * dispatchQuantity, weightUnitId,
            boxQuantity, dispatchDate
          ))
        }
      }
    }, "pallet_label.pallet_label_print")
  }

}
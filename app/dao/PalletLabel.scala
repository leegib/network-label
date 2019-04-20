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
   * @param 발주 번호
   * @return
   */
  def palletLabelList(orderId: String): Either[String, Seq[models.PalletLabel]] = {
    db.withConnectionCover({ implicit c =>
      SQL"""
        SELECT
          p.id, p.lot_no, p.quantity, p.weight, u.symbol AS weight_unit,
          p.box_quantity, p.dispatch_date
        FROM pallet_label AS p
        LEFT OUTER JOIN unit AS u ON p.unit_id = u.id
        WHERE p.order_id = $orderId
      """.as(models.PalletLabel.parser.*)
    }, "pallet_label.pallet_label_list")
  }

}
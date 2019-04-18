package dao

import java.util.Date
import javax.inject.Inject
import play.api.db.Database
import anorm._

class PalletLabel @Inject()(db: Database) {

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

}
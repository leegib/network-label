package models

import java.util.Date

import play.api.libs.json.Json
import anorm.SqlParser._
import anorm._

case class Order(
  id         : String,
  orderDate  : Date,
  dueDate    : Date,
  productId  : String,
  productName: String,
  revision   : String,
  quantity   : Int
)

object Order {
  val parser = {
    get[String]("id") ~
    get[Date]("order_date") ~
    get[Date]("due_date") ~
    get[String]("product_id") ~
    get[String]("product_name") ~
    get[String]("revision") ~
    get[Int]("quantity") map { case
      id ~ orderDate ~ dueDate ~ productId ~ productName ~
      revision ~ quantity => Order(
        id, orderDate, dueDate, productId, productName,
        revision, quantity
      )
    }
  }
  implicit val jsonWrites = Json.writes[Order]
}
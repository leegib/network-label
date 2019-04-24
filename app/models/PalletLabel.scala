package models

import java.util.Date
import play.api.libs.json.Json
import anorm.SqlParser._
import anorm._

/**
 * 발주
 * @param id 발주 번호
 * @param orderDate 발주일
 * @param dueDate 납기일
 * @param productId 품번
 * @param productName 품명
 * @param revision 버전
 * @param quantity 발주 수량
 */
case class Order(
  id         : String,
  orderDate  : Date,
  dueDate    : Date,
  productId  : String,
  productName: String,
  revision   : String,
  quantity   : Long
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

/**
 * 팔레트 라벨
 * @param id 아이디
 * @param lotNo 로트번호
 * @param quantity 수량
 * @param weight 중량
 * @param weightUnit 중량 단위
 * @param boxQuantity 박스 수량
 * @param dispatchDate 출하일
 */
case class PalletLabel(
  id          : String,
  lotNo       : String,
  quantity    : Long,
  weight      : Double,
  weightUnit  : String,
  boxQuantity : Long,
  dispatchDate: Date
)

object PalletLabel {
  val parser = {
    get[String]("id") ~
    get[String]("lot_no") ~
    get[Long]("quantity") ~
    get[Double]("weight") ~
    get[String]("weight_unit") ~
    get[Long]("box_quantity") ~
    get[Date]("dispatch_date") map { case
      id ~ lotNo ~ quantity ~ weight ~ weightUnit ~
      boxQuantity ~ dispatchDate => PalletLabel(
        id, lotNo, quantity, weight, weightUnit,
        boxQuantity, dispatchDate
      )
    }
  }
  implicit val jsonWrites = Json.writes[PalletLabel]
}

/**
 * 박스 라벨
 * @param id 아이디
 * @param lotNo 로트 번호
 * @param quantity 수량
 * @param weight 중량
 * @param weightUnit 중량 단위
 * @param productionDate 생산일
 */
case class BoxLabel(
  id            : String,
  lotNo         : String,
  quantity      : Long,
  weight        : Double,
  weightUnit    : String,
  productionDate: Date
)

object BoxLabel {
  val parser = {
    get[String]("id") ~
    get[String]("lot_no") ~
    get[Long]("quantity") ~
    get[Double]("weight") ~
    get[String]("weight_unit") ~
    get[Date]("production_date") map { case
      id ~ lotNo ~ quantity ~ weight ~ weightUnit ~
      productionDate => BoxLabel(
        id, lotNo, quantity, weight, weightUnit,
        productionDate
      )
    }
  }
  implicit val jsonWrites = Json.writes[BoxLabel]
}
package models

import java.util.Date
import play.api.libs.json.Json
import anorm.SqlParser._
import anorm._

/**
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
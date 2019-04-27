package models

import java.util.UUID
import play.api.libs.json.Json
import anorm.SqlParser._
import anorm._

/**
 * 협력사
 * @param uid 협력사 번호
 * @param id 아이디
 * @param name 이름
 * @param address 주소
 */
case class Partner(
  uid    : UUID,
  id     : String,
  name   : String,
  address: String
)

object Partner {
  val parser = {
    get[UUID]("uid") ~
    get[String]("id") ~
    get[String]("name") ~
    get[String]("address") map { case uid ~ id ~ name ~ address =>
      Partner(uid, id, name, address)
    }
  }
  implicit val jsonWrites = Json.writes[Partner]
}
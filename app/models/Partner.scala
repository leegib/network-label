package models

import java.util.UUID
import play.api.libs.json.Json
import anorm.SqlParser._
import anorm._

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
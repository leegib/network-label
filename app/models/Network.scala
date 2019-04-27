package models

import play.api.libs.json.Json
import anorm.SqlParser._
import anorm._

/**
 * 네트워크
 * @param id 아이디
 * @param name 이름
 * @param address 주소
 */
case class Network(
  id     : String,
  name   : String,
  address: String
)

object Network {
  val parser = {
    get[String]("id") ~
    get[String]("name") ~
    get[String]("address") map { case id ~ name ~ address =>
      Network(id, name, address)
    }
  }
  implicit val jsonWrites = Json.writes[Network]
}
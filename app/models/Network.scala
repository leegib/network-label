package models

import play.api.libs.json.Json
import anorm.SqlParser._
import anorm._

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
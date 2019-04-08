package dao

import javax.inject.Inject
import play.api.db.Database
import anorm._

class Network @Inject()(db: Database) {

  def networkList: Either[String, Seq[models.Network]] = {
    db.withConnectionCover({ implicit c =>
      SQL"""
        SELECT id, name, address FROM network ORDER BY id
      """.as(models.Network.parser.*)
    }, "network_list")
  }

}
package dao

import java.util.UUID
import javax.inject.Inject
import play.api.db.Database
import anorm._

class Auth @Inject()(db: Database) {

  def networkList: Either[String, Seq[models.Network]] = {
    db.withConnectionCover({ implicit c =>
      SQL"""
        SELECT id, name, address FROM network ORDER BY id
      """.as(models.Network.parser.*)
    }, "network_list")
  }

  def login(networkId: String, partnerId: String, businessId: String): Either[String, Option[models.Partner]] = {
    db.withTransactionCover({ implicit c =>
      SQL"""
        SELECT ${UUID.randomUUID} AS uid, id, name, address FROM partner
        WHERE id = $partnerId AND network_id = $networkId AND business_id = $businessId
        LIMIT 1
      """.as(models.Partner.parser.singleOpt).map { partner =>
        SQL"""
          UPDATE partner SET uid = ${partner.uid}::uuid
          WHERE id = $partnerId AND network_id = $networkId AND business_id = $businessId
        """.executeUpdate
        partner
      }
    }, "partner")
  }

  def partner(partnerUid: UUID): Either[String, Option[models.Partner]] = {
    db.withConnectionCover({ implicit c =>
      SQL"""
        SELECT uid, id, name, address FROM partner
        WHERE uid = $partnerUid::uuid
        LIMIT 1
      """.as(models.Partner.parser.singleOpt)
    }, "partner")
  }

}
package dao

import java.util.UUID
import javax.inject.Inject
import play.api.db.Database
import anorm._

class Partner @Inject()(db: Database) {

  def partner(networkId: String, partnerId: String, businessId: String): Either[String, Option[models.Partner]] = {
    db.withConnectionCover({ implicit c =>
      SQL"""
        SELECT uid, id, name, address FROM partner
        WHERE id = $partnerId AND network_id = $networkId AND business_id = $businessId
        LIMIT 1
      """.as(models.Partner.parser.singleOpt)
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
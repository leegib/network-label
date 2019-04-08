package object dao {

  implicit class ImplicitDatabase(db: play.api.db.Database) {

    def throwable(e: Throwable, msg: String) = {
      play.Logger.error(e.getMessage)
      play.Logger.error(msg)
      Left(e.getMessage)
    }

    def withConnectionCover[T](block: (java.sql.Connection) => T, msg: String): Either[String, T] = {
      try Right(db.withConnection(block)) catch {
        case e: Throwable => throwable(e, msg)
      }
    }

    def withTransactionCover[T](block: (java.sql.Connection) => T, msg: String): Either[String, T] = {
      try Right(db.withTransaction(block)) catch {
        case e: Throwable => throwable(e, msg)
      }
    }
  }

}
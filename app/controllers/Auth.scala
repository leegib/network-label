package controllers

import java.util.UUID
import javax.inject.Inject
import scala.concurrent.Future
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.Results._
import play.api.mvc.{Result, ActionBuilder, WrappedRequest, Request}

class SessionRequest[A](val user: models.Partner, request: Request[A]) extends WrappedRequest[A](request)

class Auth @Inject()(daoAuth: dao.Auth)(implicit val messagesApi: MessagesApi) extends I18nSupport {

  object SessionAction extends ActionBuilder[SessionRequest] {

    def invokeBlock[A](request: Request[A], block: (SessionRequest[A] => Future[Result])) = {
      // 세션 키로 협력사 조회
      request.session.get(Auth.SESSION_PARTNER).map(UUID.fromString).map { partnerUid =>
        daoAuth.partner(partnerUid) match {
          // 협력사 정보가 유효하면 SessionRequest 생성
          case r if r.isRight => r.right.get.map { partner =>
            block(new SessionRequest(partner, request))
          // 협력사 정보 없음
          }.getOrElse(Future.successful(Unauthorized))
          // DB 조회 오류
          case r => Future.successful(BadRequest(requestMsg(r.left.get)))
        }
      // 세션 정보 없음
      }.getOrElse(Future.successful(Unauthorized))
    }
  }
}

object Auth {
  val SESSION_PARTNER = "network_label.partner"
  val SESSION_PARTNER_NAME = "network_label.partner_name"
}
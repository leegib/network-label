package controllers

import java.util.UUID
import javax.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import play.api.routing.JavaScriptReverseRouter
import jsmessages.JsMessagesFactory

class Index @Inject()(
  val messagesApi: MessagesApi, jsMessagesFactory: JsMessagesFactory,
  daoAuth: dao.Auth, daoOrder: dao.PalletLabel
) extends Controller with I18nSupport {

  /**
   * 페이지
   */
  def page = Action { implicit request =>
    request.session.get(Auth.SESSION_PARTNER).map(UUID.fromString).map { partnerUid =>
      daoAuth.partner(partnerUid) match {
        case r if r.isRight => r.right.get.map { networkId =>
          Ok(views.html.index())
        }.getOrElse(Unauthorized(views.html.login()))
        case r => Unauthorized(views.html.login())
      }
    }.getOrElse(Unauthorized(views.html.login()))
  }

  /**
   * 네트워크 목록
   */
  def networkList = Action { request =>
    daoAuth.networkList match {
      case r if r.isRight => Ok(Json.toJson(r.right.get))
      case r => BadRequest(requestMsg(r.left.get))
    }
  }

  /**
   * 로그인
   */
  def login = Action { implicit request =>
    forms.Auth.login.bindFromRequest.fold(
      error => BadRequest(error.errorsAsJson), {
      case (networkId, partnerId, businessId) => daoAuth.login(networkId, partnerId, businessId) match {
        case r if r.isRight && r.right.get.isEmpty => BadRequest(requestMsg("login.error_none"))
        case r if r.isRight => r.right.get.map { partner =>
          Ok(controllers.routes.Index.page.url).withSession(
            request.session +
            (Auth.SESSION_PARTNER -> partner.uid.toString) +
            (Auth.SESSION_PARTNER_NAME -> partner.name)
          )}.getOrElse(BadRequest(requestMsg("login.error_none")))
        case r => BadRequest(requestMsg(r.left.get))
      }}
    )
  }

  /**
   * 로그아웃
   */
  def logout = Action { implicit request =>
    Redirect(controllers.routes.Index.page.url).withSession(
      request.session -
      Auth.SESSION_PARTNER -
      Auth.SESSION_PARTNER_NAME
    )
  }

  def jsMessages = Action { request =>
    Ok(jsMessagesFactory.all(Some("msg")))
  }

  def jsRoutes = Action { implicit request =>
    Ok(JavaScriptReverseRouter("jsRoutes")(
      controllers.routes.javascript.Index.networkList,
      controllers.routes.javascript.Index.login,
      controllers.routes.javascript.PalletLabel.orderList
    )).as(JAVASCRIPT)
  }

}
package controllers

import javax.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.Controller

class PalletLabel @Inject()(val messagesApi: MessagesApi, auth: controllers.Auth, daoAuth: dao.Auth, daoOrder: dao.PalletLabel) extends Controller with I18nSupport {

  def orderList(searchWord: String) = auth.SessionAction { implicit request =>
    forms.PalletLabel.orderList.bindFromRequest.fold(
      error => BadRequest(error.errorsAsJson), {
      case (startDate, endDate) => daoOrder.orderList(startDate, endDate, searchWord) match {
        case r if r.isRight => Ok(Json.toJson(r.right.get))
        case r => BadRequest(requestMsg(r.left.get))
      }}
    )
  }

}
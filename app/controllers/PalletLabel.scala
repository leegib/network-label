package controllers

import javax.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.Controller

class PalletLabel @Inject()(val messagesApi: MessagesApi, auth: controllers.Auth,
daoAuth: dao.Auth, daoPalletLabel: dao.PalletLabel) extends Controller with I18nSupport {

  import auth.SessionAction

  /**
   * 발주 목록
   * @param searchWord 검색어
   * @return
   */
  def orderList(searchWord: String) = SessionAction { implicit request =>
    forms.PalletLabel.orderList.bindFromRequest.fold(
      error => BadRequest(error.errorsAsJson), {
      case (startDate, endDate) => daoPalletLabel.orderList(startDate, endDate, searchWord) match {
        case r if r.isRight => Ok(Json.toJson(r.right.get))
        case r => BadRequest(requestMsg(r.left.get))
      }}
    )
  }

  /**
   * 팔레트 라벨 목록
   * @param orderId 발주 번호
   * @return
   */
  def palletLabelList(orderId: String) = SessionAction { implicit request =>
    daoPalletLabel.palletLabelList(orderId) match {
      case r if r.isRight => Ok(Json.toJson(r.right.get))
      case r => BadRequest(requestMsg(r.left.get))
    }
  }

}
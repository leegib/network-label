package controllers

import java.text.{DecimalFormat, SimpleDateFormat}
import javax.inject.Inject
import play.api.Environment
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.Controller

class PalletLabel @Inject()(val messagesApi: MessagesApi, env: Environment, auth: controllers.Auth,
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

  /**
   * 팔레트 라벨 등록 - 박스 라벨 목록
   * @param orderId 발주 번호
   * @return
   */
  def boxLabelList(orderId: String) = SessionAction { implicit request =>
    daoPalletLabel.boxLabelList(orderId) match {
      case r if r.isRight => Ok(Json.toJson(r.right.get))
      case r => BadRequest(requestMsg(r.left.get))
    }
  }

  /**
   * 팔레트 라벨 등록
   * @param orderId 발주 번호
   * @return
   */
  def palletLabelAdd(orderId: String) = SessionAction { implicit request =>
    forms.PalletLabel.palletLabelAdd.bindFromRequest.fold(
      error => BadRequest(error.errorsAsJson),
      form => daoPalletLabel.palletLabelInsert(orderId, form) match {
        case r if r.isRight => r.right.get.map { palletLabelId =>
          Ok(palletLabelId)
        }.getOrElse(BadRequest)
        case r => BadRequest(requestMsg(r.left.get))
      }
    )
  }

  /**
   * 팔레트 라벨 출력
   * @param palletLabelId 팔레트 라벨 번호
   * @return
   */
  def palletLabelPrint(palletLabelId: String) = SessionAction { implicit request =>
    daoPalletLabel.palletLabel(palletLabelId) match {
      case r if r.isRight => r.right.get.map { case (network, partner, order, palletLabel) =>
        val yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd")
        val commaNumber = new DecimalFormat("#,###")
        PackageObjectLabel.pdf(Json.obj(
          "network" -> network,
          "partner" -> partner,
          "order" -> order,
          "palletLabel" -> palletLabel,
          "dueDate" -> yyyyMMdd.format(order.dueDate),
          "dispatchDate" -> yyyyMMdd.format(palletLabel.dispatchDate),
          "quantity" -> commaNumber.format(palletLabel.quantity),
          "weight" -> s"${commaNumber.format(palletLabel.weight)} ${palletLabel.weightUnit}",
          "qrCode" ->s"""{
            "orderId": "${order.id}",
            "partnerId": "${partner.id}",
            "quantity": "${palletLabel.quantity}"
          }"""
        ), env.getFile("conf/reports/pallet_label.jrxml"))
      }.getOrElse(BadRequest);
      case r => BadRequest(requestMsg(r.left.get))
    }
  }

}
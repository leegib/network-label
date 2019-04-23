package forms

import play.api.data.format.Formats._
import play.api.data.Form
import play.api.data.Forms._

object PalletLabel {

  def orderList = Form(
    tuple(
      "startDate" -> date,
      "endDate" -> date
    )
  )

  def palletLabelAdd = Form(
    "boxLabel" -> seq(mapping(
      "id" -> nonEmptyText,
      "lotNo" -> nonEmptyText,
      "quantity" -> longNumber,
      "weight" -> of[Double],
      "weightUnit" -> nonEmptyText,
      "productionDate" -> date
    )(models.BoxLabel.apply)(models.BoxLabel.unapply))
  )

}
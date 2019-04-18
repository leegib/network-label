package forms

import play.api.data.Form
import play.api.data.Forms._

object PalletLabel {

  def orderList = Form(
    tuple(
      "startDate" -> date,
      "endDate" -> date
    )
  )

}
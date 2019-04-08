package forms

import play.api.data.Form
import play.api.data.Forms._

object Auth {

  def login = Form(
    "" -> tuple(
      "networkId" -> nonEmptyText,
      "partnerId" -> nonEmptyText.transform(i => i.toUpperCase, (o: String) => o),
      "businessId" -> nonEmptyText
    )
  )

}
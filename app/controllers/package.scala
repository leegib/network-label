import play.api.i18n.Messages
import play.api.libs.json.Json

package object controllers {

  def requestMsg(msg: String)(implicit messages: Messages) = Json.arr(Seq(
    msg match {
      case m if m.contains("duplicate") =>
        play.Logger.info(s"$m")
        messages("data_exist", "\\(.+\\)".r.findAllIn(m).mkString)
      case m => messages(m)
    }
  ))

  implicit class ImplicitString(str: String) {
    def toUnderscoreCase: String = {
      "[A-Z]".r.replaceAllIn(str, s => s"_${s.matched.toLowerCase}")
    }
    def toCamelCase: String = {
      "_[a-z]".r.replaceAllIn(str, _.matched.replace("_", "").toUpperCase)
    }
  }

}
import java.io.File

import play.api.i18n.Messages
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Controller

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

  object PackageObjectLabel extends Controller {

    import java.io.ByteArrayInputStream
    import play.api.http.HttpEntity
    import akka.util.ByteString
    import net.sf.jasperreports.engine.data.JsonDataSource
    import net.sf.jasperreports.engine.xml.JRXmlLoader
    import net.sf.jasperreports.engine.{JasperExportManager, JasperCompileManager, JasperFillManager}

    def pdf(data: JsValue, file: File) = {
      val label = JasperExportManager.exportReportToPdf(JasperFillManager.fillReport(
        JasperCompileManager.compileReport(JRXmlLoader.load(file)),
        null,
        new JsonDataSource(new ByteArrayInputStream(data.toString().getBytes), "")
      ))
      Ok.sendEntity(HttpEntity.Strict(ByteString(label), Some("application/pdf")))
    }
  }

}
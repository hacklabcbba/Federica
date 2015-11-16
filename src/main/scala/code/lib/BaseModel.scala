package code
package lib

import net.liftmodules.extras.SnippetHelper
import net.liftweb.common._
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds.RedirectTo
import net.liftweb.http.{S, SHtml, Templates}
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.record.Field
import net.liftweb.util.Helpers._
import net.liftweb.util.{CssSel, Helpers}

import scala.xml.{NodeSeq, Text}

trait BaseModel[T <: MongoRecord[T]] extends MongoRecord[T] with SnippetHelper {

  this: T =>


  def title: String

  def entityListUrl: String

  def legend: Box[NodeSeq] = {
    val action = if (meta.find(this.id.toString).isEmpty) "Crear" else "Editar"
    Full(
      Text(s"$action / $title")
    )
  }

  def legendCssSel: CssSel = {
    for {
      node <- legend
    } yield {
      "data-name=legend *" #> node
    }
  }

  def formCssSel: CssSel = {
    legendCssSel &
    "data-name=fields *" #> toFieldset(fields) &
    "data-name=save" #> SHtml.ajaxOnSubmit(saveFunc _) &
    "#cancel-btn" #> SHtml.ajaxButton("Cancelar", () => RedirectTo(entityListUrl))
  }

  def toFieldset(fields: List[Field[_, T]]): NodeSeq = {
    fields.filter(_.shouldDisplay_?).zipWithIndex.flatMap { case (fld, index) =>
      (for {
        fieldId <-{
          fld.uniqueFieldId
        }
        form <- fld.toForm
      } yield {
        fieldsCss(fld, fieldId, form)
      }).openOr(NodeSeq.Empty)
    }
  }

  protected def label(fld: Field[_, T], fieldId: String): NodeSeq =
    <label for={fieldId} class="control-label">{S ? fld.displayName}</label>

  protected def help(fld: Field[_, T]): NodeSeq = fld.helpAsHtml match {
    case Full(html) => <span class="help-block">{html}</span>
    case _ => NodeSeq.Empty
  }

  protected def msg(fieldId: String): NodeSeq =
    <span data-alertid={fieldId} form-alert=""></span>

  def fieldsCss(fld: Field[_, T], fieldId: String, form: NodeSeq) = {
    "data-name=label" #> label(fld, fieldId) &
      "data-name=form" #> form &
      "data-name=help" #> help(fld) &
      "data-name=msg" #> msg(fieldId)
  } apply editFieldTemplate

  def editFieldTemplate =
    Templates("backend" :: "record" :: "field" :: Nil) openOr Text(S ? "No edit template found")

  def toForm: NodeSeq = {
    formCssSel.apply(template)
  }

  def template: NodeSeq = S.runTemplate(List("backend" , "record", "form")).openOr(<div>Template not found</div>)

  def saveFunc: JsCmd = this.validate match {
    case Nil =>
      tryo(this.save(true)) match {
        case Full(ret) => RedirectTo(entityListUrl, () => S.notice(s"$title se guardo exitosamente!"))
        case Failure(msg, e, _) => S.error(s"Error al guardar el registro: $msg")
        case Empty => S.error("Error desconocido")
      }
    case errs =>
      S.error(errs)
  }

}

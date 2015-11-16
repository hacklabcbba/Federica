package code
package snippet

import code.lib.BaseModel
import net.liftmodules.extras.SnippetHelper
import net.liftweb.common.{Box, Failure, Full}
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.{RequestVar, S, SHtml, Templates}
import net.liftweb.mongodb.record.{MongoMetaRecord, MongoRecord}
import net.liftweb.record.Field
import net.liftweb.util.Helpers._

import scala.xml.{NodeSeq, Text}

trait ListSnippet[BaseRecord <: MongoRecord[BaseRecord]] extends SnippetHelper {
  val meta: MongoMetaRecord[BaseRecord]
  val addUrl: String
  def entityListUrl: String

  object deletedItems extends RequestVar[List[BaseRecord]](Nil)

  def itemEditUrl(inst: BaseRecord): String

  def template: NodeSeq = Templates(List("templates-hidden", "backend", "listing-table")) openOr Text("Missing template")

  def listFields: List[Field[_, BaseRecord]] = meta.fieldOrder

  val title: String

  def render = {
    "*" #>
    {
      "data-name=title *" #> title &
      "data-name=column-name *" #> listFields.filter(field => field.shouldDisplay_?).map(field => field.displayName) &
      "data-name=add-item [href]" #> addUrl &
      "data-name=remove-items [onclick]" #> SHtml.ajaxInvoke(deleteItemsJsCmd _) &
      "data-name=items" #> meta.findAll.map(item => {
        "type=checkbox" #> SHtml.ajaxCheckbox(false, s => selectItem(s, item)) &
        "data-name=column-data *" #> listFields.map(field => item.fieldByName(field.name).dmap("")(_.toString)) &
        "data-name=edit-item [href]" #> itemEditUrl(item) &
        "data-name=remove-item [onclick]" #> SHtml.ajaxInvoke(() => deleteItemJsCmd(item))
      })
    }.apply(template)
  }

  def selectItem(selected: Boolean, item: BaseRecord): JsCmd = {
    if (selected)
      deletedItems.set(deletedItems.get ++ List(item))
    else
      deletedItems.set(deletedItems.get.filter(_ != item))
    Noop
  }

  def deleteItemsJsCmd: JsCmd = {
    tryo(deletedItems.get.foreach(meta.delete_!(_))) match {
      case Full(_) =>
        RedirectTo(entityListUrl, () => S.notice("Items eliminados"))
      case Failure(msg, _, _) =>
        S.error(msg)
      case _ =>
        S.error("Error desconocido")
    }
  }

  def deleteItemJsCmd(item: BaseRecord): JsCmd = {
    tryo(meta.delete_!(item)) match {
      case Full(_) =>
        RedirectTo(entityListUrl, () => S.notice("Item eliminado"))
      case Failure(msg, _, _) =>
        S.error(msg)
      case _ =>
        S.error("Error desconocido")
    }
  }
}

object CrudSnippet extends SnippetHelper {
  private def serve(snip: BaseModel[_] => NodeSeq): NodeSeq =
    (for {
      loc <- S.location ?~ "no value"
      value <- loc.currentValue ?~ "no value"
      bm <- Box.asA[BaseModel[_]](value) ?~ "value was the wrong type"
    } yield {
      snip(bm)
    })

  def edit(in: NodeSeq): NodeSeq = serve { baseModel => baseModel.toForm }

}
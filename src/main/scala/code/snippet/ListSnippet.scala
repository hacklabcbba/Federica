package code
package snippet

import code.lib.{SortableModel, BaseModel}
import net.liftmodules.extras.SnippetHelper
import net.liftweb.common.{Box, Failure, Full}
import net.liftweb.http.js.JE.JsVar
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.{RequestVar, S, SHtml, Templates}
import net.liftweb.json.JsonAST.JValue
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

  def items: List[BaseRecord] = meta.findAll

  def render = {
    "*" #>
    {
      "data-name=title *" #> title &
      "data-name=column-name *" #> listFields.map(field => field.displayName) &
      "data-name=add-item [href]" #> addUrl &
      "data-name=remove-items [onclick]" #> SHtml.ajaxInvoke(deleteItemsJsCmd _) &
      "data-name=items" #> items.map(item => {
        "data-name=items [id]" #> item.id.toString &
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

trait SortableSnippet[BaseRecord <: MongoRecord[BaseRecord] with SortableModel[BaseRecord]] extends ListSnippet[BaseRecord] {
  override def items: List[BaseRecord] = meta.findAll.sortBy(_.order.get)
  override def render = {
    val callbacks = Function(
      "updateOrderValue",
      List("json"),
      SHtml.jsonCall(
        JsVar("json"),
        updateOrderValue _).exp.cmd
    )
    val sorteableScript = Run(
      """
        |$("#items").sortable({}).on('sortupdate', function(e, obj) {
        |  console.log(obj);
        |  console.log(obj.item[0].id);
        |  updateOrderValue({ id: obj.item[0].id, order: obj.index});
        |});
      """.stripMargin)
    S.appendJs(callbacks)
    S.appendJs(sorteableScript)

    "*" #>
      {
        "data-name=title *" #> title &
        "data-name=column-name *" #> listFields.map(field => field.displayName) &
        "data-name=add-item [href]" #> addUrl &
        "data-name=remove-items [onclick]" #> SHtml.ajaxInvoke(deleteItemsJsCmd _) &
        "data-name=items" #> items.map(item => {
          "data-name=items [id]" #> item.id.toString &
          "type=checkbox" #> SHtml.ajaxCheckbox(false, s => selectItem(s, item)) &
          "data-name=column-data *" #> listFields.map(field => item.fieldByName(field.name).dmap("")(_.toString)) &
          "data-name=edit-item [href]" #> itemEditUrl(item) &
          "data-name=remove-item [onclick]" #> SHtml.ajaxInvoke(() => deleteItemJsCmd(item))
        }) &
        "data-name=pagination" #> NodeSeq.Empty
      }.apply(template)
  }

  def updateOrderValue(json: JValue): JsCmd
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
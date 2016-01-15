package code.snippet

import code.config.Site
import code.model.Widget
import com.foursquare.rogue.LiftRogue
import com.foursquare.rogue.LiftRogue._
import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds._
import net.liftweb.json.JsonAST.JValue
import net.liftweb.util.Helpers._
import net.liftweb.util.{CssSel, Helpers}

import scala.xml.NodeSeq

object WidgetSnippet extends ListSnippet[Widget] {

  val meta = Widget

  val title = "Widgets"

  val addUrl = Site.backendWidgetAdd.calcHref(Widget.createRecord)

  def entityListUrl: String = Site.backendWidgets.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Widget): String = Site.backendWidgetEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name)

  override def render = {
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
}

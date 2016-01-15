package code.snippet

import code.config.Site
import code.model.Page
import net.liftweb.common.Box
import net.liftweb.http.{S, SHtml}
import net.liftweb.util.CssSel
import net.liftweb.util.Helpers._

import scala.xml.NodeSeq

object PageSnippet extends ListSnippet[Page] {

  val meta = Page

  val title = "Paginas"

  val addUrl = Site.backendPageAdd.calcHref(Page.createRecord)

  def entityListUrl: String = Site.backendPages.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Page): String = Site.backendPageEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.url)

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

  private def serve(snip: Page => NodeSeq): NodeSeq =
    (for {
      loc <- S.location ?~ "no value"
      value <- loc.currentValue ?~ "no value"
      bm <- Box.asA[Page](value) ?~ "value was the wrong type"
    } yield {
      snip(bm)
    })

  def edit(in: NodeSeq): NodeSeq = serve {
    baseModel => baseModel.toForm
  }

  def renderViewFrontEnd: CssSel = {
    for {
      inst <- Site.pagina.currentValue
    } yield {
      "data-name=title *" #> inst.name.get &
      "data-name=page-content *" #> inst.body.asHtml &
      "data-name=page-widgets" #> inst.widgets.objs.map(widget => {
        "*" #> widget.body.asHtml
      })
    }
  }
}

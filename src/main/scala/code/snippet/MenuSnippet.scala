package code
package snippet

import code.config.Site
import code.model.Page
import code.model.network.Network
import code.model.page.{MenuItem, Menu}
import com.foursquare.rogue.LiftRogue
import net.liftmodules.extras.SnippetHelper
import net.liftweb.http.{IdMemoizeTransform, RequestVar, SHtml}
import net.liftweb.http.js.JsCmd
import net.liftweb.json.JsonAST.JValue
import LiftRogue._
import net.liftweb.util.{CssSel, Helpers}
import Helpers._
import net.liftweb.http.js.JsCmds._

object MenuSnippet extends SnippetHelper {

  object menusRequestVar extends RequestVar[List[Menu]](menus)

  object deletedMenuItemsRequestVar extends RequestVar[List[MenuItem]](Nil)

  def menus = Menu.findAll

  def render: CssSel = {
    val pages = Page.findAll
    "*" #> SHtml.idMemoize(mainBody => {
      "data-name=create-menu [onclick]" #> SHtml.ajaxInvoke(() => addMenu(mainBody)) &
      "data-name=menu" #> menusRequestVar.get.map(menu => {
        "*" #> SHtml.idMemoize(menuBody => {
          var externalMenuItemName = ""
          var externalMenuItemUrl = ""
          var selectedPages = Nil
          "data-name=page" #> pages.map(page => {
            "type=checkbox" #> SHtml.ajaxCheckbox(false, value => menuBody.setHtml())
          }) &
            "@menu-item-name" #> SHtml.ajaxText(externalMenuItemName, s => {
              externalMenuItemName = s
              Noop
            })
          "@menu-item-url" #> SHtml.ajaxText(externalMenuItemUrl, s => {
            externalMenuItemUrl = s
            Noop
          }) &
            "data-name=add-to-menu [onclick]" #> SHtml.ajaxInvoke(() => mainBody.setHtml()) &
            "@menu-name" #> SHtml.ajaxText("", s => Noop)
        })
      })
    })
  }

  private def addMenu(body: IdMemoizeTransform): JsCmd = {
    val menu = Menu.createRecord.name("Nuevo menú")
    menusRequestVar.set(menusRequestVar.get ++ List(menu))
    body.setHtml()
  }

}

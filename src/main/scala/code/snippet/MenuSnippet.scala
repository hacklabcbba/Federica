package code
package snippet

import code.config.Site
import code.model.Page
import code.model.network.Network
import code.model.page.{MenuItemKind, MenuItem, Menu}
import com.foursquare.rogue.LiftRogue
import net.liftmodules.extras.SnippetHelper
import net.liftweb.http.{IdMemoizeTransform, RequestVar, SHtml}
import net.liftweb.http.js.JsCmd
import net.liftweb.json.JsonAST.JValue
import LiftRogue._
import net.liftweb.util.{CssSel, Helpers}
import Helpers._
import net.liftweb.http.js.JsCmds._

import scala.annotation.tailrec
import scala.xml.NodeSeq

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
          var selectedPages: List[Page] = Nil
          def menuItems = menu.menuItems.get
          "data-name=page" #> pages.map(page => {
            "type=checkbox" #> SHtml.ajaxCheckbox(false, value => value match {
              case true => selectedPages = selectedPages ++ List(page)
              case false => selectedPages = selectedPages.filter(_.id.get != page.id.get)
            }) &
            "span" #> page.name.get
          }) &
          "data-name=add-to-menu-pages [onclick]" #> SHtml.ajaxInvoke(() => {
            selectedPages.foreach(page => {
              val menuItem = MenuItem
                .createRecord
                .name(page.name.get)
                .url(Site.pagina.calcHref(page))
                .kind(MenuItemKind.Page)
                .order(menu.menuItems.get.size)
              menu.menuItems.set(menu.menuItems.get ++ List(menuItem))
            })
            menuBody.setHtml()
          }) &
          "@menu-item-name" #> SHtml.ajaxText(externalMenuItemName, s => {
            externalMenuItemName = s
            Noop
          }) &
          "@menu-item-url" #> SHtml.ajaxText(externalMenuItemUrl, s => {
            externalMenuItemUrl = s
            Noop
          }) &
          "data-name=add-to-menu-custom [onclick]" #> SHtml.ajaxInvoke(() => {
            val menuItem = MenuItem
              .createRecord
              .name(externalMenuItemName)
              .url(externalMenuItemUrl)
              .kind(MenuItemKind.Custom)
              .order(menu.menuItems.get.size)
            menu.menuItems.set(menu.menuItems.get ++ List(menuItem))
            menuBody.setHtml()
          }) &
          "@menu-name" #> SHtml.ajaxText(menu.name.get, s => {
            menu.name(s)
            Noop
          }) &
          "data-name=menu-save [onclick]" #> SHtml.ajaxInvoke(() => {
            menu.save(true)
            Noop
          }) &
          "data-name=menu-item" #> menuItems.map(menuItem => {
            "data-name=menu-item-name" #> menuItem.name.get &
            "data-name=menu-item-childs" #> generateChildMenuItems(menuItem)
          })
        })
      })
    })
  }

  private def generateChildMenuItems(menuItem: MenuItem): NodeSeq = {
    val template =
      <ul class="list-group">
        <li data-name="menu-item" class="list-group-item">Cras justo odio</li>
        <ul data-name="menu-item-childs"></ul>
      </ul>

    if (menuItem.childs.get.isEmpty) {
      NodeSeq.Empty
    } else {
      ("data-name=menu-item" #> menuItem.childs.get.map(child => {
        "data-name=menu-item *" #> child.name.get &
        "data-name=menu-item-childs" #> generateChildMenuItems(child)
      })).apply(template)
    }

  }

  private def addMenu(body: IdMemoizeTransform): JsCmd = {
    val menu = Menu.createRecord.name("Nuevo menú")
    menusRequestVar.set(menusRequestVar.get ++ List(menu))
    body.setHtml()
  }

}

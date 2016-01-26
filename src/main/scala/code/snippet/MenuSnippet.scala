package code
package snippet

import code.config.Site
import code.model.Page
import code.model.network.Network
import code.model.page.{MenuItemKind, MenuItem, Menu}
import com.foursquare.rogue.LiftRogue
import net.liftmodules.extras.SnippetHelper
import net.liftweb.http.js.JE.{Call, JsVar}
import net.liftweb.http.{S, IdMemoizeTransform, RequestVar, SHtml}
import net.liftweb.http.js.{JsExp, JsCmd}
import JsExp._
import net.liftweb.json.DefaultFormats
import net.liftweb.json.JsonAST.{JString, JValue}
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

  private def toMenuItem(item: MenuItemCaseClass, menu: Menu): MenuItem = {
    MenuItem
      .createRecord
      .name(item.name)
      .url(item.url)
      .kind(MenuItemKind.withName(item.kind))
      .menu(menu.id.get)
      .children(item.children.map(child => toMenuItem(child, menu)))
  }

  private def updateTree(menu: Menu, json: JValue): JsCmd = {
    implicit val formats = DefaultFormats
    for {
      items <- tryo(json.extract[List[MenuItemCaseClass]])
    } yield {
      menu.menuItems.set(items.map(s => toMenuItem(s, menu)))
    }
    Noop
  }

  def render: CssSel = {
    val pages = Page.findAll
    "*" #> SHtml.idMemoize(mainBody => {
      "data-name=create-menu [onclick]" #> SHtml.ajaxInvoke(() => addMenu(mainBody)) &
      "data-name=menu" #> menusRequestVar.get.map(menu => {
        "*" #> SHtml.idMemoize(menuBody => {
          val menuContainerId = nextFuncName
          val callbacks = Function(
            "updateTree" + menuContainerId,
            List("json"),
            SHtml.jsonCall(
              JsVar("json"),
              (json: JValue) => updateTree(menu, json)
          ))
          val sorteableScript = Run(
            ("""
              $('#""" + menuContainerId + """').nestedSortable({
              |            handle: 'span',
              |            items: 'li',
              |            tolerance: 'pointer',
              |            maxLevels: 3,
              |            toleranceElement: '> span',
              |            isTree: true,
              |            sort: function(ev) {
              |              console.log(ev);
              |              //console.log($('#""" + menuContainerId + """').nestedSortable('toHierarchy', {startDepthCount: 0}));
              |            }
              |        });
            """).stripMargin)
          S.appendJs(callbacks)
          S.appendJs(sorteableScript)

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
              menu.menuItems.set(menu.menuItems.get ++ List(menuItem))
            })
            menuBody.setHtml() & sorteableScript
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
            menu.menuItems.set(menu.menuItems.get ++ List(menuItem))
            menuBody.setHtml() & sorteableScript
          }) &
          "data-name=menu-container [id]" #> menuContainerId &
          "@menu-name" #> SHtml.ajaxText(menu.name.get, s => {
            menu.name(s)
            Noop
          }) &
          "data-name=menu-save [onclick]" #> SHtml.jsonCall(Call("processData", JString(menuContainerId)), (json: JValue) => {
            println("JSON:" + json)
            updateTree(menu, json)
            println("MENUITEMS:"+menu.menuItems.get.size)
            menu.save(true)
            Noop & sorteableScript
          }) &
          "data-name=menu-item" #> menuItems.map(menuItem => {
            "data-name=menu-item [id]" #> s"menuItem_$nextFuncName" &
            "data-name=menu-item [data-url]" #> menuItem.url.get &
            "data-name=menu-item [data-title]" #> menuItem.name.get &
            "data-name=menu-item [data-kind]" #> menuItem.kind.get.toString &
            "data-name=menu-item-name *" #> menuItem.name.get &
            "data-name=menu-item [data-name]" #> menuItem.name.get &
            "data-name=menu-item-childs" #> generateChildMenuItems(menuItem)
          })
        })
      })
    })
  }

  private def generateChildMenuItems(menuItem: MenuItem): NodeSeq = {
    val template =
      <ol class="list-group" >
        <li data-name="menu-item" class="list-group-item">
          <span data-name="menu-item-name">Cras justo odio</span>
          <ol data-name="menu-item-childs" class="list-group sortable">
          </ol>
        </li>
      </ol>

    if (menuItem.children.get.isEmpty) {
      <ol data-name="menu-item-childs" class="list-group">
      </ol>
    } else {
      ("data-name=menu-item" #> menuItem.children.get.map(child => {
        "data-name=menu-item [data-url]" #> menuItem.url.get &
        "data-name=menu-item [data-title]" #> menuItem.name.get &
        "data-name=menu-item [data-kind]" #> menuItem.kind.get.toString &
        "data-name=menu-item-name *" #> menuItem.name.get &
        "data-name=menu-item [data-name]" #> menuItem.name.get &
        "data-name=menu-item [id]" #> s"menuItem_$nextFuncName" &
        "data-name=menu-item-childs" #> generateChildMenuItems(child)
      })).apply(template)
    }

  }

  private def addMenu(body: IdMemoizeTransform): JsCmd = {
    val menu = Menu.createRecord.name("Nuevo menú")
    menusRequestVar.set(menusRequestVar.get ++ List(menu))
    body.setHtml()
  }

  case class MenuItemCaseClass(name: String, url: String, kind: String, children: List[MenuItemCaseClass])

}

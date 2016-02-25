package code
package snippet

import code.config.Site
import code.model._
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
    val areas = Area.findAll
    val transversalAreas = TransversalArea.findAll
    val programs = Program.findAll
    val processes = Process.findAll
    val actionLines = ActionLine.findAll
    val values = Value.findAll
    val services = Service.findAll

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
              |            handle: 'div',
              |            items: 'li',
              |            tolerance: 'pointer',
              |            maxLevels: 3,
              |            toleranceElement: '> div',
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
          var selectedAreas: List[Area] = Nil
          var selectedPrograms: List[Program] = Nil
          var selectedTransversalAreas: List[TransversalArea] = Nil
          var selectedProcess: List[Process] = Nil
          var selectedActionLines: List[ActionLine] = Nil
          var selectedValues: List[Value] = Nil
          var selectedServices: List[Service] = Nil

          def menuItems = menu.menuItems.get

          "data-name=menu-name" #> menu.name.get &
          "#enlaces-header [id+]" #> menuContainerId &
          "#accordion [id+]" #> menuContainerId &
          "data-name=paginas [data-parent+]" #> menuContainerId &
          "data-name=paginas [href+]" #> menuContainerId &
          "#paginas [id+]" #> menuContainerId &
          "data-name=areas [data-parent+]" #> menuContainerId &
          "data-name=areas [href+]" #> menuContainerId &
          "#areas [id+]" #> menuContainerId &
          "data-name=programs [data-parent+]" #> menuContainerId &
          "data-name=programs [href+]" #> menuContainerId &
          "#programs [id+]" #> menuContainerId &
          "data-name=transversalareas [data-parent+]" #> menuContainerId &
          "data-name=transversalareas [href+]" #> menuContainerId &
          "#transversalareas [id+]" #> menuContainerId &
          "data-name=processes [data-parent+]" #> menuContainerId &
          "data-name=processes [href+]" #> menuContainerId &
          "#processes [id+]" #> menuContainerId &
          "data-name=actionlines [data-parent+]" #> menuContainerId &
          "data-name=actionlines [href+]" #> menuContainerId &
          "#actionlines [id+]" #> menuContainerId &
          "data-name=values [data-parent+]" #> menuContainerId &
          "data-name=values [href+]" #> menuContainerId &
          "#values [id+]" #> menuContainerId &
          "data-name=services [data-parent+]" #> menuContainerId &
          "data-name=services [href+]" #> menuContainerId &
          "#services [id+]" #> menuContainerId &
          "data-name=enlaces [data-parent+]" #> menuContainerId &
          "data-name=enlaces [href+]" #> menuContainerId &
          "#enlaces [id+]" #> menuContainerId &
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
          "data-name=area" #> areas.map(area => {
            "type=checkbox" #> SHtml.ajaxCheckbox(false, value => value match {
              case true => selectedAreas = selectedAreas ++ List(area)
              case false => selectedAreas = selectedAreas.filter(_.id.get != area.id.get)
            }) &
              "span" #> area.name.get
          }) &
          "data-name=add-to-menu-areas [onclick]" #> SHtml.ajaxInvoke(() => {
            selectedAreas.foreach(area => {
              val menuItem = MenuItem
                .createRecord
                .name(area.name.get)
                .url(Site.area.calcHref(area))
                .kind(MenuItemKind.Area)
              menu.menuItems.set(menu.menuItems.get ++ List(menuItem))
            })
            menuBody.setHtml() & sorteableScript
          }) &
          "data-name=program" #> programs.map(program => {
            "type=checkbox" #> SHtml.ajaxCheckbox(false, value => value match {
              case true => selectedPrograms = selectedPrograms ++ List(program)
              case false => selectedPrograms = selectedPrograms.filter(_.id.get != program.id.get)
            }) &
              "span" #> program.name.get
          }) &
          "data-name=add-to-menu-programs [onclick]" #> SHtml.ajaxInvoke(() => {
            selectedPrograms.foreach(program => {
              val menuItem = MenuItem
                .createRecord
                .name(program.name.get)
                .url(Site.programa.calcHref(program))
                .kind(MenuItemKind.Program)
              menu.menuItems.set(menu.menuItems.get ++ List(menuItem))
            })
            menuBody.setHtml() & sorteableScript
          }) &
          "data-name=transversalarea" #> transversalAreas.map(ta => {
            "type=checkbox" #> SHtml.ajaxCheckbox(false, value => value match {
              case true => selectedTransversalAreas = selectedTransversalAreas ++ List(ta)
              case false => selectedTransversalAreas = selectedTransversalAreas.filter(_.id.get != ta.id.get)
            }) &
              "span" #> ta.name.get
          }) &
          "data-name=add-to-menu-transversalareas [onclick]" #> SHtml.ajaxInvoke(() => {
            selectedTransversalAreas.foreach(ta => {
              val menuItem = MenuItem
                .createRecord
                .name(ta.name.get)
                .url(Site.areaTransversal.calcHref(ta))
                .kind(MenuItemKind.TransversalArea)
              menu.menuItems.set(menu.menuItems.get ++ List(menuItem))
            })
            menuBody.setHtml() & sorteableScript
          }) &
          "data-name=process" #> processes.map(process => {
            "type=checkbox" #> SHtml.ajaxCheckbox(false, value => value match {
              case true => selectedProcess = selectedProcess ++ List(process)
              case false => selectedProcess = selectedProcess.filter(_.id.get != process.id.get)
            }) &
              "span" #> process.name.get
          }) &
          "data-name=add-to-menu-processes [onclick]" #> SHtml.ajaxInvoke(() => {
            selectedProcess.foreach(ps => {
              val menuItem = MenuItem
                .createRecord
                .name(ps.name.get)
                .url(Site.proceso.calcHref(ps))
                .kind(MenuItemKind.Process)
              menu.menuItems.set(menu.menuItems.get ++ List(menuItem))
            })
            menuBody.setHtml() & sorteableScript
          }) &
          "data-name=actionline" #> actionLines.map(al => {
            "type=checkbox" #> SHtml.ajaxCheckbox(false, value => value match {
              case true => selectedActionLines = selectedActionLines ++ List(al)
              case false => selectedActionLines = selectedActionLines.filter(_.id.get != al.id.get)
            }) &
              "span" #> al.name.get
          }) &
          "data-name=add-to-menu-actionline [onclick]" #> SHtml.ajaxInvoke(() => {
            selectedActionLines.foreach(al => {
              val menuItem = MenuItem
                .createRecord
                .name(al.name.get)
                .url(Site.lineaDeAccion.calcHref(al))
                .kind(MenuItemKind.ActionLine)
              menu.menuItems.set(menu.menuItems.get ++ List(menuItem))
            })
            menuBody.setHtml() & sorteableScript
          }) &
          "data-name=value" #> values.map(v => {
            "type=checkbox" #> SHtml.ajaxCheckbox(false, value => value match {
              case true => selectedValues = selectedValues ++ List(v)
              case false => selectedValues = selectedValues.filter(_.id.get != v.id.get)
            }) &
              "span" #> v.name.get
          }) &
          "data-name=add-to-menu-values [onclick]" #> SHtml.ajaxInvoke(() => {
            selectedValues.foreach(value=> {
              val menuItem = MenuItem
                .createRecord
                .name(value.name.get)
                //.url(Site.principios.calcHref(value))
                .kind(MenuItemKind.ValueKind)
              menu.menuItems.set(menu.menuItems.get ++ List(menuItem))
            })
            menuBody.setHtml() & sorteableScript
          }) &
          "data-name=service" #> services.map(service => {
            "type=checkbox" #> SHtml.ajaxCheckbox(false, value => value match {
              case true => selectedServices = selectedServices ++ List(service)
              case false => selectedServices = selectedServices.filter(_.id.get != service.id.get)
            }) &
              "span" #> service.name.get
          }) &
          "data-name=add-to-menu-services [onclick]" #> SHtml.ajaxInvoke(() => {
            selectedServices.foreach(service => {
              val menuItem = MenuItem
                .createRecord
                .name(service.name.get)
                .url(Site.servicio.calcHref(service))
                .kind(MenuItemKind.Service)
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
            updateTree(menu, json)
            menu.save(true)
            RedirectTo(Site.backendMenus.fullUrl)
          }) &
          "data-name=menu-item" #> menuItems.map(menuItem => {
            val menuId = s"menuItem_$nextFuncName"
            "data-name=menu-item [id]" #> menuId &
            "data-name=menu-item [data-url]" #> menuItem.url.get &
            "data-name=menu-item [data-title]" #> menuItem.name.get &
            "data-name=menu-item [data-kind]" #> menuItem.kind.get.toString &
            "data-name=menu-item-name *" #> menuItem.name.get &
            "data-name=menu-item [data-name]" #> menuItem.name.get &
            "data-name=remove [onclick]" #> SHtml.ajaxInvoke(() => {
              Run("$('#" + menuId + "').remove();")
            }) &
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
          <div>
            <span data-name="menu-item-name">Cras justo odio</span>
            <button data-name="remove" type="button" class="btn btn-danger pull-right"><i class="fa fa-remove"></i></button>
          </div>
          <br/>
          <ol data-name="menu-item-childs" class="list-group sortable">
          </ol>
        </li>
      </ol>

    if (menuItem.children.get.isEmpty) {
      <ol data-name="menu-item-childs" class="list-group">
      </ol>
    } else {
      ("data-name=menu-item" #> menuItem.children.get.map(child => {
        val menuId = s"menuItem_$nextFuncName"
        "data-name=menu-item [data-url]" #> child.url.get &
        "data-name=menu-item [data-title]" #> child.name.get &
        "data-name=menu-item [data-kind]" #> child.kind.get.toString &
        "data-name=menu-item-name *" #> child.name.get &
        "data-name=menu-item [data-name]" #> child.name.get &
        "data-name=menu-item [id]" #> menuId &
        "data-name=remove [onclick]" #> SHtml.ajaxInvoke(() => {
          Run("$('#" + menuId + "').remove();")
        }) &
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

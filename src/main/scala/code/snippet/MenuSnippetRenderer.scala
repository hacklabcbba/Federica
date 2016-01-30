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

object MenuSnippetRenderer extends SnippetHelper {

  def render: CssSel = {
    for {
      name <- S.attr("name")
      menu <- Menu.findByName(name)
    } yield {
      "li" #> menu.menuItems.get.map(menuItem => {
        "a *" #> menuItem.name.get &
        "a [href]" #> menuItem.url.get &
        "data-name=menu-item-childs" #>  generateChildMenuItems(menuItem)
      })
    }
  }

  private def generateChildMenuItems(menuItem: MenuItem): NodeSeq = {
    val template =
      <ul class="dropdown-menu submenu-black">
        <li>
          <a href="#">
            Quiénes somos <span class="caret"></span>
          </a>
          <ul class="dropdown-menu submenu-black" data-name="menu-item-childs">
          </ul>
        </li>
      </ul>
    if (menuItem.children.get.isEmpty) {
      NodeSeq.Empty
    } else {
      ("li" #> menuItem.children.get.map(child => {
        "a *" #> child.name.get &
        "a [class]" #> (if (child.children.get.isEmpty) "" else "dropdown-toggle") &
        "a [href]" #> child.url.get &
        "data-name=menu-item-childs" #> generateChildMenuItems(child)
      })).apply(template)
    }
  }

}

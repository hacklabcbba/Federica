package code
package snippet

import scala.xml._

import net.liftweb._
import http.{LiftRules, S}
import sitemap.SiteMap
import util.Helpers._
import net.liftmodules.extras.snippet.BsMenu
import model.User

object CustomMenuBuilder extends BsMenu {
  /**
   * Produces a menu UL from a group, for use with Bootstrap.
   */
  override def group = {
    val menus: NodeSeq =
      for {
        group <- S.attr("group") ?~ "Group not specified"
        sitemap <- LiftRules.siteMap ?~ "Sitemap is empty"
        request <- S.request ?~ "Request is empty"
        curLoc <- request.location ?~ "Current location is empty"
      } yield ({
        val currentClass = S.attr("current_class").openOr("active")
        val menus = sitemap.locForGroup(group)
        menus flatMap { loc =>
          val nonHiddenKids = loc.menu.kids.filterNot(_.loc.hidden)
          val styles =
            if (curLoc.name == loc.name || loc.menu.kids.exists(_.loc.name == curLoc.name)) currentClass
            else ""

          if (nonHiddenKids.length == 0) {
            <li class={styles}>{SiteMap.buildLink(loc.name)}</li>
          }
          else {
            val childs: NodeSeq = nonHiddenKids.map{ kid =>
              SiteMap.buildLink(kid.loc.name)
            }.filter(_.nonEmpty).map(s => <li>{s}</li>)

            if (childs.nonEmpty)
              <li class={styles}>
                <a href={loc.calcDefaultHref}>{loc.linkText.openOr(Text("Empty Name"))}</a>
                <ul class="treeview-menu">{ childs }</ul>
              </li>
            else
              <li class={styles}>
                <a href={loc.calcDefaultHref}>{loc.linkText.openOr(Text("Empty Name"))}</a>
              </li>
          }
        }
      }): NodeSeq

    "*" #> menus
  }

}

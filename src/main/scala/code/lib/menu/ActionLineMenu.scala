package code
package lib
package menu

import code.config.{MenuGroups, MenuLoc}
import code.model.productive.ProductiveUnit
import net.liftmodules.mongoauth.Locs
import net.liftweb.common.Box
import net.liftweb.http.{Templates, S}
import net.liftweb.sitemap
import net.liftweb.sitemap.Loc.{Hidden, LocGroup, TemplateBox}
import net.liftweb.sitemap.Menu.Menuable
import net.liftweb.sitemap._
import sitemap.Loc._

object ActionLineMenu {

  val menuAdd = MenuLoc(Menu.i("Agregar Linea de accion") / "actionLine" / "add" >> Locs.RequireLoggedIn)
  val menuEdit = MenuLoc(Menu.i("Edit Linea de accion") / "actionLine" / "edit" >> Locs.RequireLoggedIn)
  val menuList = MenuLoc(Menu.i("Lineas de accion") / "actionLine" / "list" >> Locs.RequireLoggedIn >> MenuGroups.TopBarGroup)
}

package code.lib.menu

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

object AreaMenu {

  val menuAdd = MenuLoc(Menu.i("Agregar Area") / "area" / "add" >> Locs.RequireLoggedIn)
  val menuEdit = MenuLoc(Menu.i("Edit Area") / "area" / "edit" >> Locs.RequireLoggedIn)
  val menuList = MenuLoc(Menu.i("Areas") / "area" / "list" >> Locs.RequireLoggedIn >> MenuGroups.TopBarGroup)
}

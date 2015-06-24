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

object ConcreteResourceMenu {

  val menuAdd = MenuLoc(Menu.i("Agregar recurso") / "cresource" / "add" >> Locs.RequireLoggedIn)
  val menuEdit = MenuLoc(Menu.i("Editar recurso") / "cresource" / "edit" >> Locs.RequireLoggedIn)
  val menuList = MenuLoc(Menu.i("Recursos") / "cresource" / "list" >> Locs.RequireLoggedIn >> MenuGroups.AdminGroup)
}

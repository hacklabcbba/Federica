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

object ProgramMenu {

  val menuAdd = MenuLoc(Menu.i("Agregar Programa") / "program" / "add" >> Locs.RequireLoggedIn)
  val menuEdit = MenuLoc(Menu.i("Edit Programa") / "program" / "edit" >> Locs.RequireLoggedIn)
  val menuList = MenuLoc(Menu.i("Programas") / "program" / "list" >> Locs.RequireLoggedIn >> MenuGroups.AdminGroup)
}

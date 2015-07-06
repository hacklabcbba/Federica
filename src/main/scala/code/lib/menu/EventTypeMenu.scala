package code
package lib
package menu

import code.config.{MenuGroups, MenuLoc}
import code.model.ProductiveUnit
import net.liftmodules.mongoauth.Locs
import net.liftweb.common.Box
import net.liftweb.http.{Templates, S}
import net.liftweb.sitemap
import net.liftweb.sitemap.Loc.{Hidden, LocGroup, TemplateBox}
import net.liftweb.sitemap.Menu.Menuable
import net.liftweb.sitemap._
import sitemap.Loc._

object EventTypeMenu {

  val menuAdd = MenuLoc(Menu.i("Agregar Tipo de evento") / "eventType" / "add" >> Locs.RequireLoggedIn)
  val menuEdit = MenuLoc(Menu.i("Editar Tipo de evento") / "eventType" / "edit" >> Locs.RequireLoggedIn)
  val menuList = MenuLoc(Menu.i("Tipos de evento") / "eventType" / "list" >> Locs.RequireLoggedIn >> MenuGroups.AdminGroup)
}

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

object EventMenu {

  val menuAdd = MenuLoc(Menu.i("Agregar Evento") / "event" / "add" >> Locs.RequireLoggedIn)
  val menuEdit = MenuLoc(Menu.i("Edit Evento") / "event" / "edit" >> Locs.RequireLoggedIn)
  val menuList = MenuLoc(Menu.i("Eventos") / "event" / "events" >> Locs.RequireLoggedIn >> MenuGroups.AdminGroup)
}

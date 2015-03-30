package code
package lib
package menu

import code.config.{MenuGroups, MenuLoc}
import net.liftmodules.mongoauth.Locs
import net.liftweb.sitemap._

object NetworkMenu {

  val menuAdd = MenuLoc(Menu.i("Agregar red") / "network" / "add" >> Locs.RequireLoggedIn)
  val menuEdit = MenuLoc(Menu.i("Editar red") / "network" / "edit" >> Locs.RequireLoggedIn)
  val menuList = MenuLoc(Menu.i("Lista de redes") / "network" / "list" >> Locs.RequireLoggedIn >> MenuGroups.AdminGroup)
}

package code.lib.menu

import code.config.{MenuGroups, MenuLoc}
import net.liftmodules.mongoauth.Locs
import net.liftweb.sitemap._

object RoomMenu {

  val menuAdd = MenuLoc(Menu.i("Agregar sala") / "room" / "add" >> Locs.RequireLoggedIn)
  val menuEdit = MenuLoc(Menu.i("Editar sala") / "room" / "edit" >> Locs.RequireLoggedIn)
  val menuList = MenuLoc(Menu.i("Salas") / "room" / "list" >> Locs.RequireLoggedIn >> MenuGroups.AdminGroup)
}

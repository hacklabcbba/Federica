package code.lib.menu

import code.config.{MenuGroups, MenuLoc}
import net.liftmodules.mongoauth.Locs
import net.liftweb.sitemap._

object EquipmentMenu {

  val menuAdd = MenuLoc(Menu.i("Agregar equipo") / "equipment" / "add" >> Locs.RequireLoggedIn)
  val menuEdit = MenuLoc(Menu.i("Editar equipo") / "equipment" / "edit" >> Locs.RequireLoggedIn)
  val menuList = MenuLoc(Menu.i("Equipos") / "equipment" / "list" >> Locs.RequireLoggedIn >> MenuGroups.AdminGroup)
}

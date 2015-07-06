package code
package snippet

import code.config.Site
import code.model.resource.Room

object RoomSnippet extends ListSnippet[Room] {

  val meta = Room

  val addUrl = Site.backendRoomAdd.calcHref(Room.createRecord)

  def entityListUrl: String = Site.backendRooms.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Room): String = Site.backendRoomEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.code, meta.name, meta.status, meta.isBookable, meta.isBookableShift)

}

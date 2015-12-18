package code
package snippet

import code.config.Site
import code.model.resource.Room
import net.liftweb.util.{Helpers, CssSel}
import Helpers._

object RoomSnippet extends ListSnippet[Room] {

  val meta = Room

  val title = "Ambiente"

  val addUrl = Site.backendRoomAdd.calcHref(Room.createRecord)

  override def items: List[Room] = meta.findAll.sortBy(_.code.get)

  def entityListUrl: String = Site.backendRooms.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Room): String = Site.backendRoomEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.code, meta.name, meta.status, meta.isBookable, meta.isBookableShift)


  def renderFrontEnd: CssSel = {
    "data-name=space" #> items.map(room => {
      "data-name=code *" #> room.code.get &
      "data-name=name *" #> room.name.get &
      "data-name=image1" #> room.photo1.viewFile &
      "data-name=image2" #> room.photo2.viewFile &
      "data-name=description *" #> room.description.asHtml
    })
  }

}

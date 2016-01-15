package code
package snippet

import code.config.Site
import code.model.resource.Room
import net.liftweb.util.{Helpers, CssSel}
import com.foursquare.rogue.LiftRogue
import net.liftweb.http.js.JsCmd
import net.liftweb.json.JsonAST.JValue
import LiftRogue._
import net.liftweb.util.Helpers
import Helpers._
import net.liftweb.http.js.JsCmds._

object RoomSnippet extends SortableSnippet[Room] {

  val meta = Room

  val title = "Ambiente"

  val addUrl = Site.backendRoomAdd.calcHref(Room.createRecord)

  def entityListUrl: String = Site.backendRooms.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Room): String = Site.backendRoomEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.code, meta.name, meta.isBookable, meta.isBookableShift)


  def renderFrontEnd: CssSel = {
    "data-name=space" #> items.map(room => {
      "data-name=code *" #> room.code.get &
      "data-name=name *" #> room.name.get &
      "data-name=image1" #> room.photo1.viewFile &
      "data-name=image2" #> room.photo2.viewFile &
      "data-name=description *" #> room.description.asHtml
    })
  }


  def updateOrderValue(json: JValue): JsCmd = {
    implicit val formats = net.liftweb.json.DefaultFormats
    for {
      id <- tryo((json \ "id").extract[String])
      order <- tryo((json \ "order").extract[Long])
      item <- meta.find(id)
    } yield meta.where(_.id eqs item.id.get).modify(_.order setTo order).updateOne()
    Noop
  }

}

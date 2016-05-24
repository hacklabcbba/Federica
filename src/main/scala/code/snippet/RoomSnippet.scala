package code
package snippet

import code.config.Site
import code.model.resource.Room
import net.liftweb.util.{CssSel, Helpers, Props}
import com.foursquare.rogue.LiftRogue
import net.liftweb.http.js.JsCmd
import net.liftweb.json.JsonAST.JValue
import LiftRogue._
import Helpers._
import net.liftmodules.extras.SnippetHelper
import net.liftweb.common.{Empty, Full}
import net.liftweb.http.{S, SHtml}
import net.liftweb.http.js.JsCmds._

import scala.xml.NodeSeq

object RoomSnippet extends SortableSnippet[Room] with SnippetHelper {

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
      "data-name=link [onclick]" #> SHtml.ajaxInvoke(() => RedirectTo(Site.espacio.calcHref(room))) &
      "data-name=image1" #> room.photo1.viewFile &
      "data-name=image2" #> room.photo2.viewFile &
      "data-name=description *" #> room.description.asHtml
    })
  }

  def renderViewFrontEnd: CssSel = {
    for {
      room <- Site.espacio.currentValue
    } yield {
      "data-name=code *" #> room.code.get &
      "data-name=name *" #> room.name.get &
      "data-name=image1" #> room.photo1.viewFile &
      "data-name=image2" #> room.photo2.viewFile &
      "data-name=plane" #> room.plane.viewFile &
      "data-name=location" #> room.location.viewFile &
      "data-name=description *" #> room.description.asHtml &
      "data-name=capacity *" #> room.capacity.get &
      "data-name=download [href]" #> room.plane.downloadUrl &
      "data-name=isBookable *" #> {if(room.isBookable.get) "Si" else "No"} &
      "data-name=events" #> EventSnippet.relatedEvents(room.name.get, Empty, Empty, Empty, Empty, Empty, Empty, Empty,
        Full(room))
    }
  }

  override def facebookHeaders(in: NodeSeq) = {
    Site.espacio.currentValue match {
      case Full(space) =>
        <meta property="og:title" content={space.name.get} /> ++
        <meta property="og:url" content={Props.get("default.host", "http://localhost:8080") + S.uri} /> ++
        <meta property="og:description" content={space.description.asHtmlCutted(250).text} /> ++
        (if(space.facebookPhoto.get.fileId.get.isEmpty)
          NodeSeq.Empty
        else
          <meta property="og:image" content={space.facebookPhoto.fullUrl} />
        ) ++
        <meta property="og:type" content="article" />
      case _ =>
        NodeSeq.Empty
    }
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

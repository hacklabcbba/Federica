package code.snippet

import code.config.Site
import code.model.network.Space
import com.foursquare.rogue.LiftRogue
import net.liftweb.http.js.JsCmd
import net.liftweb.json.JsonAST.JValue
import LiftRogue._
import net.liftweb.util.Helpers
import Helpers._
import net.liftweb.http.js.JsCmds._

object SpaceSnippet extends SortableSnippet[Space] {

  val meta = Space

  val title = "Espacios"

  val addUrl = Site.backendSpaceAdd.calcHref(Space.createRecord)

  def entityListUrl: String = Site.backendSpaces.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Space): String = Site.backendSpaceEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.email)

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

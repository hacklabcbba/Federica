package code.snippet

import code.config.Site
import code.model.network.Network
import com.foursquare.rogue.LiftRogue
import net.liftweb.http.js.JsCmd
import net.liftweb.json.JsonAST.JValue
import LiftRogue._
import net.liftweb.util.Helpers
import Helpers._
import net.liftweb.http.js.JsCmds._

object NetworkSnippet extends SortableSnippet[Network] {

  val meta = Network

  val title = "Redes"

  val addUrl = Site.backendNetworkAdd.calcHref(Network.createRecord)

  def entityListUrl: String = Site.backendNetworks.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Network): String = Site.backendNetworkEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.scope)

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

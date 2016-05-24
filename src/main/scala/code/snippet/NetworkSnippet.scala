package code.snippet

import code.config.Site
import code.model.network.Network
import com.foursquare.rogue.LiftRogue
import net.liftmodules.extras.SnippetHelper
import net.liftweb.common.{Box, Full}
import net.liftweb.http.js.JsCmd
import net.liftweb.json.JsonAST.JValue
import LiftRogue._
import net.liftweb.util.{CssSel, Helpers, Props}
import Helpers._
import net.liftweb.http.S
import net.liftweb.http.js.JsCmds._

import scala.xml.NodeSeq

object NetworkSnippet extends SortableSnippet[Network] {

  val meta = Network

  val title = "Redes"

  val addUrl = Site.backendNetworkAdd.calcHref(Network.createRecord)

  def entityListUrl: String = Site.backendNetworks.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Network): String = Site.backendNetworkEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.scope, meta.url)

  override def facebookHeaders(in: NodeSeq) = {
    try {
      Site.red.currentValue match {
        case Full(red) =>
          <meta property="og:title" content={red.name.get} /> ++
              <meta property="og:url" content={Props.get("default.host", "http://localhost:8080") + S.uri} /> ++
          <meta property="og:description" content={red.description.asHtmlCutted(250).text} /> ++
          (if(red.facebookPhoto.get.fileId.get.isEmpty)
            NodeSeq.Empty
          else
            <meta property="og:image" content={red.facebookPhoto.fullUrl} />
          ) ++
          <meta property="og:type" content="article" />
        case _ =>
          NodeSeq.Empty
      }
    } catch {
      case np: NullPointerException =>
        NodeSeq.Empty
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


class RedesSnippet(red: Network) extends SnippetHelper {
  def renderViewFrontEnd: CssSel = {
    "data-name=name *" #> red.name.get &
    "data-name=name [href]" #> Site.red.calcHref(red) &
    "data-name=description *" #> red.description.asHtml
  }
}
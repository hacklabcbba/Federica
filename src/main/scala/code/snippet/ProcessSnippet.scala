package code.snippet

import code.config.Site
import code.model.Process
import com.foursquare.rogue.LiftRogue
import net.liftmodules.extras.SnippetHelper
import net.liftweb.http.js.JsCmd
import net.liftweb.json.JsonAST.JValue
import LiftRogue._
import net.liftweb.util.{CssSel, Helpers, Props}
import Helpers._
import net.liftweb.common.{Empty, Full, Loggable}
import net.liftweb.http.S
import net.liftweb.http.js.JsCmds._

import scala.xml.NodeSeq

object ProcessSnippet extends SortableSnippet[Process] {

  val meta = Process

  val title = "Procesos"

  val addUrl = Site.backendProcessAdd.calcHref(Process.createRecord)

  def entityListUrl: String = Site.backendProcess.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Process): String = Site.backendProcessEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.administrator, meta.url)

  override def facebookHeaders(in: NodeSeq) = {
    Site.proceso.currentValue match {
      case Full(process) =>
          <meta property="og:title" content={process.name.get} /> ++
          <meta property="og:url" content={Props.get("default.host", "http://localhost:8080") + S.uri} /> ++
          <meta property="og:description" content={process.description.asHtmlCutted(250).text} /> ++
          (if(process.facebookPhoto.get.fileId.get.isEmpty)
            NodeSeq.Empty
          else
            <meta property="og:image" content={process.facebookPhoto.fullUrl} />
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

//ToDo fix null pointer
class ProcesoSnippet(proceso: Process) extends SnippetHelper {

  def renderViewFrontEnd: CssSel = {
    "data-name=name *" #> proceso.name.get &
    "data-name=name [href]" #> Site.proceso.calcHref(proceso) &
    "data-name=description *" #> proceso.description.asHtml &
    "data-name=events" #> EventSnippet.relatedEvents(proceso.name.get, Empty, Empty, Empty, Empty, Empty, Empty,
      Full(proceso), Empty) &
    "data-name=posts" #> BlogSnippet.relatedPosts(proceso.name.get, Empty, Empty, Empty, Empty, Empty, Empty,
      Full(proceso)) &
    "data-name=calls" #> CallSnippet.relatedCalls(proceso.name.get, Empty, Empty, Empty, Empty, Empty, Empty,
      Full(proceso))
  }
}

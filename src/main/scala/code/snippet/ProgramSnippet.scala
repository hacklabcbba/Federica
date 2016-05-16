package code.snippet

import code.config.Site
import code.model.Program
import com.foursquare.rogue.LiftRogue
import net.liftweb.http.js.JsCmd
import net.liftweb.json.JsonAST.JValue
import LiftRogue._
import net.liftweb.util.{CssSel, Helpers, Props}
import Helpers._
import net.liftweb.common.{Empty, Full}
import net.liftweb.http.S
import net.liftweb.http.js.JsCmds._

import scala.xml.NodeSeq

object ProgramSnippet extends SortableSnippet[Program] {

  val meta = Program

  val title = "Programas"

  val addUrl = Site.backendProgramAdd.calcHref(Program.createRecord)

  def entityListUrl: String = Site.backendPrograms.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Program): String = Site.backendProgramEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.responsible, meta.email, meta.url)

  def renderViewFrontEnd: CssSel = {
    for {
      programa <- Site.programa.currentValue
    } yield {
      "data-name=name *" #> programa.name.get &
      "data-name=name [href]" #> Site.programa.calcHref(programa) &
      "data-name=description *" #> programa.description.asHtml &
      "data-name=email *" #> programa.email.get &
      "data-name=responsible *" #> programa.responsible.obj.dmap("")(_.name.get) &
      "data-name=events" #> EventSnippet.relatedEvents(programa.name.get, Empty, Full(programa), Empty, Empty, Empty,
        Empty, Empty, Empty) &
      "data-name=posts" #> BlogSnippet.relatedPosts(programa.name.get, Empty, Full(programa), Empty, Empty, Empty,
        Empty, Empty) &
      "data-name=calls" #> CallSnippet.relatedCalls(programa.name.get, Empty, Full(programa), Empty, Empty, Empty,
        Empty, Empty)
    }
  }

  override def facebookHeaders(in: NodeSeq) = {
    try {
      Site.programa.currentValue match {
        case Full(program) =>
          <meta property="og:title" content={program.name.get} /> ++
              <meta property="og:url" content={Props.get("default.host", "http://localhost:8080") + S.uri} /> ++
          <meta property="og:description" content={program.description.asHtmlCutted(250).text} /> ++
          (if(program.facebookPhoto.get.fileId.get.isEmpty)
            NodeSeq.Empty
          else
            <meta property="og:image" content={program.facebookPhoto.fullUrl} />
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

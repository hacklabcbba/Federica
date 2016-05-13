package code.snippet

import code.config.Site
import code.model.TransversalApproach
import com.foursquare.rogue.LiftRogue
import com.foursquare.rogue.LiftRogue._
import net.liftmodules.extras.SnippetHelper
import net.liftweb.common.{Empty, Full}
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds._
import net.liftweb.json.JsonAST.JValue
import net.liftweb.util.Helpers._
import net.liftweb.util.{CssSel, Helpers}

import scala.xml.NodeSeq

object TransversalApproachSnippet extends SortableSnippet[TransversalApproach] {

  val meta = TransversalApproach

  val title = "Enfoques transversales"

  val addUrl = Site.backendTransversalApproachAdd.calcHref(TransversalApproach.createRecord)

  def entityListUrl: String = Site.backendTransversalApproaches.menu.loc.calcDefaultHref

  def itemEditUrl(inst: TransversalApproach): String = Site.backendTransversalApproachEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.description, meta.url)

  def renderFrontEnd: CssSel = {
    "data-name=area" #> meta.findAll.map(area => {
      "data-name=name *" #> area.name.get &
      //"data-name=name [href]" #> Site.servicio.calcHref(area) &
      "data-name=description *" #> area.description.asHtml
    })
  }

  override def facebookHeaders(in: NodeSeq) = {
    try {
      Site.enfoqueTransversal.currentValue match {
        case Full(enfoque) =>
          <meta property="og:title" content={enfoque.name.get} /> ++
          <meta property="og:description" content={enfoque.description.asHtmlCutted(250).text} /> ++
          (if(enfoque.photo1.get.fileId.get.isEmpty)
            NodeSeq.Empty
          else
            <meta property="og:image" content={enfoque.photo1.fullUrl} />
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

class EnfoqueTransversal(ta: TransversalApproach) extends SnippetHelper {
  def renderViewFrontEnd: CssSel = {
    "data-name=name *" #> ta.name.get &
    "data-name=name [href]" #> Site.enfoqueTransversal.calcHref(ta) &
    "data-name=description *" #> ta.description.asHtml &
    "data-name=events" #> EventSnippet.relatedEvents(ta.name.get, Empty, Empty, Empty, Empty, Empty, Full(ta), Empty,
      Empty) &
    "data-name=posts" #> BlogSnippet.relatedPosts(ta.name.get, Empty, Empty, Empty, Empty, Empty, Full(ta), Empty) &
    "data-name=calls" #> CallSnippet.relatedCalls(ta.name.get, Empty, Empty, Empty, Empty, Empty, Full(ta), Empty)
  }
}
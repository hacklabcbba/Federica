package code.snippet

import code.config.Site
import code.model.{Area, TransversalArea}
import com.foursquare.rogue.LiftRogue
import com.foursquare.rogue.LiftRogue._
import net.liftweb.common.{Empty, Full}
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds._
import net.liftweb.json.JsonAST.JValue
import net.liftweb.util.Helpers._
import net.liftweb.util.{CssSel, Helpers}

import scala.xml.NodeSeq

object TransversalAreaSnippet extends SortableSnippet[TransversalArea] {

  val meta = TransversalArea

  val title = "Areas transversales"

  val addUrl = Site.backendTransversableAreaAdd.calcHref(TransversalArea.createRecord)

  def entityListUrl: String = Site.backendTransversableAreas.menu.loc.calcDefaultHref

  def itemEditUrl(inst: TransversalArea): String = Site.backendTransversableAreaEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.responsible, meta.email)

  def renderFrontEnd: CssSel = {
    "data-name=area" #> meta.findAll.map(area => {
      "data-name=name *" #> area.name.get &
      //"data-name=name [href]" #> Site.servicio.calcHref(area) &
      "data-name=description *" #> area.description.asHtml
    })
  }

  override def facebookHeaders(in: NodeSeq) = {
    try {
      Site.areaTransversal.currentValue match {
        case Full(areaT) =>
          <meta property="og:title" content={areaT.name.get} /> ++
          <meta property="og:description" content={areaT.description.asHtmlCutted(250).text} /> ++
          (if(areaT.facebookPhoto.get.fileId.get.isEmpty)
            NodeSeq.Empty
          else
            <meta property="og:image" content={areaT.facebookPhoto.fullUrl} />
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

  def renderViewFrontEnd: CssSel = {
    for {
      area <- Site.areaTransversal.currentValue
    } yield {
      "data-name=image [src]" #> area.photo1.url &
      "data-name=name *" #> area.name.get &
      "data-name=name [href]" #> Site.areaTransversal.calcHref(area) &
      "data-name=description *" #> area.description.asHtml &
      "data-name=email *" #> area.email.get &
      "data-name=responsible *" #> area.responsible.obj.dmap("")(_.name.get) &
      "data-name=events" #> EventSnippet.relatedEvents(area.name.get, Empty, Empty, Empty, Empty, Full(area), Empty,
        Empty, Empty) &
      "data-name=posts" #> BlogSnippet.relatedPosts(area.name.get, Empty, Empty, Empty, Empty, Full(area), Empty,
        Empty) &
      "data-name=calls" #> CallSnippet.relatedCalls(area.name.get, Empty, Empty, Empty, Empty, Full(area), Empty, Empty)
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

package code
package snippet

import code.config.Site
import code.model.ActionLine
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

object ActionLineSnippet extends SortableSnippet[ActionLine] {

  val meta = ActionLine

  val title = "Lineas de acción"

  val addUrl = Site.backendActionLineAdd.calcHref(ActionLine.createRecord)

  def entityListUrl: String = Site.backendActionLines.menu.loc.calcDefaultHref

  def itemEditUrl(inst: ActionLine): String = Site.backendActionLineEdit.toLoc.calcHref(inst)

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
      Site.lineaDeAccion.currentValue match {
        case Full(linea) =>
          <meta property="og:title" content={linea.name.get} /> ++
          <meta property="og:description" content={linea.description.asHtmlCutted(250).text} /> ++
          (if(linea.photo1.get.fileId.get.isEmpty)
            NodeSeq.Empty
          else
            <meta property="og:image" content={linea.photo1.fullUrl} />
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

class LineaAccion(linea: ActionLine) extends SnippetHelper {
  def renderViewFrontEnd: CssSel = {
    "data-name=name *" #> linea.name.get &
    "data-name=name [href]" #> Site.lineaDeAccion.calcHref(linea) &
    "data-name=description *" #> linea.description.asHtml &
    "data-name=events" #> EventSnippet.relatedEvents(linea.name.get, Empty, Empty, Empty, Full(linea), Empty, Empty,
      Empty, Empty) &
    "data-name=posts" #> BlogSnippet.relatedPosts(linea.name.get, Empty, Empty, Empty, Full(linea), Empty, Empty,
      Empty) &
    "data-name=calls" #> CallSnippet.relatedCalls(linea.name.get, Empty, Empty, Empty, Full(linea), Empty, Empty, Empty)
  }
}
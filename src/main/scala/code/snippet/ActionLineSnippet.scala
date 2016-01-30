package code
package snippet

import code.config.Site
import code.model.ActionLine
import com.foursquare.rogue.LiftRogue
import com.foursquare.rogue.LiftRogue._
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds._
import net.liftweb.json.JsonAST.JValue
import net.liftweb.util.Helpers._
import net.liftweb.util.{CssSel, Helpers}

object ActionLineSnippet extends SortableSnippet[ActionLine] {

  val meta = ActionLine

  val title = "Lineas de acción"

  val addUrl = Site.backendActionLineAdd.calcHref(ActionLine.createRecord)

  def entityListUrl: String = Site.backendActionLines.menu.loc.calcDefaultHref

  def itemEditUrl(inst: ActionLine): String = Site.backendActionLineEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.description)

  def renderFrontEnd: CssSel = {
    "data-name=area" #> meta.findAll.map(area => {
      "data-name=name *" #> area.name.get &
      //"data-name=name [href]" #> Site.servicio.calcHref(area) &
      "data-name=description *" #> area.description.asHtml
    })
  }

  def renderViewFrontEnd: CssSel = {
    for {
      linea <- Site.lineaDeAccion.currentValue
    } yield {
      "data-name=name *" #> linea.name.get &
      "data-name=name [href]" #> Site.lineaDeAccion.calcHref(linea) &
      "data-name=description *" #> linea.description.asHtml
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

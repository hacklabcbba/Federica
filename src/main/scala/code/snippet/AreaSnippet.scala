package code.snippet

import code.config.Site
import code.model.Area
import com.foursquare.rogue.LiftRogue
import net.liftweb.http.js.JsCmd
import net.liftweb.json.JsonAST.JValue
import net.liftweb.util.{CssSel, Helpers}
import Helpers._
import net.liftweb.http.js.JsCmds._
import LiftRogue._

object AreaSnippet extends SortableSnippet[Area] {

  val meta = Area

  val title = "Areas"

  val addUrl = Site.backendAreaAdd.calcHref(Area.createRecord)

  def entityListUrl: String = Site.backendAreas.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Area): String = Site.backendAreaEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.responsible, meta.email)

  def renderFrontEnd: CssSel = {
    "data-name=area" #> meta.findAll.map(area => {
      "data-name=name *" #> area.name.get &
      //"data-name=name [href]" #> Site.servicio.calcHref(area) &
      "data-name=description *" #> area.description.asHtml
    })
  }

  /*def renderViewFrontEnd: CssSel = {
    for {
      service <- Site.area.currentValue
    } yield {
      "data-name=image" #> service.photo.previewFile &
        "data-name=name *" #> service.name.get &
        "data-name=name [href]" #> Site.servicio.calcHref(service) &
        "data-name=description *" #> service.description.asHtml &
        "data-name=email *" #> service.email.get &
        "data-name=responsible *" #> service.responsible.obj.dmap("")(_.name.get)
    }
  }*/

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

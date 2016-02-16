package code
package snippet

import code.config.Site
import code.model.Service
import net.liftweb.util.{CssSel, Helpers}
import Helpers._
import com.foursquare.rogue.LiftRogue
import net.liftweb.http.js.JsCmd
import net.liftweb.json.JsonAST.JValue
import LiftRogue._
import net.liftweb.util.Helpers
import Helpers._
import net.liftweb.http.js.JsCmds._

object ServiceSnippet extends SortableSnippet[Service] {

  val meta = Service

  val title = "Servicios"

  val addUrl = Site.backendServiceAdd.calcHref(Service.createRecord)

  def entityListUrl: String = Site.backendServices.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Service): String = Site.backendServiceEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.responsible, meta.email)

  def renderFrontEnd: CssSel = {
    "data-name=upi" #> meta.findAllUPIs.sortBy(_.order.get).map(service => {
      "data-name=image" #> service.photo.previewFile &
      "data-name=name *" #> service.name.get &
      "data-name=name [href]" #> Site.servicio.calcHref(service) &
      "data-name=link [href]" #> Site.servicio.calcHref(service) &
      "data-name=description *" #> service.description.asHtmlCutted(200)
    }) &
    "data-name=upa" #> meta.findAllUPAs.sortBy(_.order.get).map(service => {
      "data-name=image" #> service.photo.previewFile &
      "data-name=name *" #> service.name.get &
      "data-name=name [href]" #> Site.servicio.calcHref(service) &
      "data-name=link [href]" #> Site.servicio.calcHref(service) &
      "data-name=description *" #> service.description.asHtmlCutted(200)
    })
  }

  def renderViewFrontEnd: CssSel = {
    for {
      service <- Site.servicio.currentValue
    } yield {
      "data-name=name *" #> service.name.get &
      "data-name=name [href]" #> Site.servicio.calcHref(service) &
      "data-name=description *" #> service.description.asHtml &
      "data-name=email *" #> service.email.get &
      "data-name=responsible *" #> service.responsible.obj.dmap("")(_.name.get)
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

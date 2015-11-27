package code
package snippet

import code.config.Site
import code.model.Service
import net.liftweb.util.{CssSel, Helpers}
import Helpers._

object ServiceSnippet extends ListSnippet[Service] {

  val meta = Service

  val title = "Servicios"

  val addUrl = Site.backendServiceAdd.calcHref(Service.createRecord)

  def entityListUrl: String = Site.backendServices.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Service): String = Site.backendServiceEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.responsible, meta.email)

  def renderFrontEnd: CssSel = {
    "data-name=service" #> meta.findAll.map(service => {
      "data-name=image" #> service.photo.previewFile &
      "data-name=name *" #> service.name.get &
      "data-name=name [href]" #> Site.servicio.calcHref(service) &
      "data-name=description *" #> service.description.asHtmlCutted(200)
    })
  }

  def renderViewFrontEnd: CssSel = {
    for {
      service <- Site.servicio.currentValue
    } yield {
      "data-name=image" #> service.photo.previewFile &
      "data-name=name *" #> service.name.get &
      "data-name=name [href]" #> Site.servicio.calcHref(service) &
      "data-name=description *" #> service.description.asHtml &
      "data-name=email *" #> service.email.get &
      "data-name=responsible *" #> service.responsible.obj.dmap("")(_.name.get)
    }
  }

}

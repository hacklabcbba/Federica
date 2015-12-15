package code.snippet

import code.config.Site
import code.model.Area
import net.liftweb.util.{CssSel, Helpers}
import Helpers._

object AreaSnippet extends ListSnippet[Area] {

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

}

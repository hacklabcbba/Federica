package code.snippet

import code.config.Site
import code.model.Call
import net.liftweb.util.Helpers._
import net.liftweb.util.{CssSel, Helpers}

object CallSnippet extends ListSnippet[Call] {

  val meta = Call

  val title = "Convocatorias"

  val addUrl = Site.backendCallAdd.calcHref(Call.createRecord)

  def entityListUrl: String = Site.backendCalls.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Call): String = Site.backendCallEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.deadline)

  def renderFrontEnd: CssSel = {
    "data-name=Call" #> meta.findAll.map(Call => {
      "data-name=name *" #> Call.name.get &
      //"data-name=name [href]" #> Site.servicio.calcHref(Call) &
      "data-name=description *" #> Call.description.asHtml
    })
  }

  /*def renderViewFrontEnd: CssSel = {
    for {
      service <- Site.Call.currentValue
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

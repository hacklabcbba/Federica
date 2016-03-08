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
    "data-name=call" #> meta.findAllCurrent.map(call => {
      "data-name=name *" #> call.name.get &
      "data-name=name [href]" #> Site.convocatoria.calcHref(call) &
      "data-name=description *" #> call.description.asHtmlCutted(300) &
      "data-name=deadline *" #> call.deadline.toString &
      "data-name=file [href]" #> (if (call.file.get.fileId.get.nonEmpty) call.file.url else Site.convocatoria.calcHref(call))
    })
  }

  def renderViewFrontEnd: CssSel = {
    for {
      call <- Site.convocatoria.currentValue
    } yield {
      "data-name=name *" #> call.name.get &
      "data-name=description *" #> call.description.asHtml &
      "data-name=deadline *" #> call.deadline.toString &
      "data-name=file [href]" #> call.file.url
    }
  }

}

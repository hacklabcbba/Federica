package code.snippet

import code.config.Site
import code.model.{Call, Value}
import net.liftweb.common.{Box, Full}
import net.liftweb.util.Helpers._
import net.liftweb.util.{CssSel, Helpers}

import scala.xml.NodeSeq

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


  def renderLastThreeCallByFilter(values: Box[Value]): CssSel = {
    Call.findLastThreeCallByFilter(values).size > 0 match {
      case true =>
        "data-name=calls" #> Call.findLastThreeCallByFilter(values).map(call => {
          "data-name=title *" #> call.name.get &
            "data-name=days *" #> call.deadline.toString &
            "data-name=description" #> call.description.asHtml
        })
      case false =>
        "data-name=callsH" #> NodeSeq.Empty
    }
  }

}

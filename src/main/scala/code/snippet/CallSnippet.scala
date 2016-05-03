package code.snippet

import code.config.Site
import code.model._
import net.liftweb.common.{Box, Full}
import net.liftweb.http.{S, Templates}
import net.liftweb.util.Helpers._
import net.liftweb.util.{CssSel, Helpers}

import scala.xml.{NodeSeq, Text}

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

  def templateRelatedCall =
    Templates("templates-hidden" :: "frontend" :: "_relatedCalls" :: Nil) openOr Text(S ? "No edit template found")

  def relatedCalls(title: String, values: Box[Value], program: Box[Program], area: Box[Area],
                   actionLine: Box[ActionLine], transversalArea: Box[TransversalArea],
                   transversalApproach: Box[TransversalApproach], process: Box[Process]): NodeSeq = {
    renderLastThreeCallByFilter(title, values, program, area, actionLine, transversalArea, transversalApproach,
      process).apply(templateRelatedCall)
  }

  def renderLastThreeCallByFilter(title: String, values: Box[Value], program: Box[Program], area: Box[Area],
                                  actionLine: Box[ActionLine],  transversalArea: Box[TransversalArea],
                                  transversalApproach: Box[TransversalApproach], process: Box[Process]): CssSel = {
    val listCalls = Call.findLastThreeCallByFilter(values, program, area, actionLine, transversalArea, transversalApproach,
      process)
    listCalls.size > 0 match {
      case true =>
        "data-name=title-module" #> title &
        "data-name=calls" #> listCalls.map(call => {
          "data-name=title *" #> call.name.get &
          "data-name=days *" #> call.deadline.toString &
          {
            call.file.valueBox match {
              case Full(image) =>
                val imageSrc = image.fileId.get
                "data-name=image [src]" #> s"/image/$imageSrc"
              case _ =>
                "data-name=image *" #> NodeSeq.Empty
            }
          } &
          "data-name=description" #> call.description.asHtmlCutted(250)
        })
      case false =>
        "data-name=callsH" #> NodeSeq.Empty
    }
  }
}

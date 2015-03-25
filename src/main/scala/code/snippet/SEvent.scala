package code
package snippet

import code.lib.request.request._
import net.liftweb.common.{Full, Failure, Empty}
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.{SHtml, PaginatorSnippet, StatefulSnippet}
import code.model.event._
import net.liftweb.util._
import Helpers._
import code.model.Setting
import net.liftmodules.extras.SnippetHelper
import scala.xml.NodeSeq

object SEvent extends SnippetHelper {

  def addForm: CssSel = {
    val e = Event.createRecord
    "data-name=name *" #> e.name.toForm &
    "data-name=shortDescription *" #> e.shortDescription.toForm &
    "data-name=description *" #> e.description.toForm &
    "data-name=schedule *" #> e.schedule.toForm &
    "data-name=eventTypes *" #> e.eventTypes.toForm &
    "data-name=area *" #> e.area.toForm &
    "data-name=program *" #> e.program.toForm &
    "data-name=process *" #> e.process.toForm &
    "data-name=actionLines *" #> e.actionLines.toForm &
    "data-name=productiveUnit *" #> e.productiveUnit.toForm &
    "data-name=expositors *" #> e.expositors.toForm &
    "data-name=organizer *" #> e.organizer.toForm &
    "data-name=handlers *" #> e.handlers.toForm &
    "data-name=sponsors *" #> e.sponsors.toForm &
    "data-name=supports *" #> e.supports.toForm &
    "data-name=collaborators *" #> e.collaborators.toForm &
    "data-name=city *" #> e.city.toForm &
    "data-name=country *" #> e.country.toForm &
    "data-name=place *" #> e.place.toForm &
    "data-name=activities *" #> e.activities.toForm &
    "data-name=requirements *" #> e.requirements.toForm &
    "data-name=goal *" #> e.goal.toForm &
    "data-name=quote *" #> e.quote.toForm &
    "data-name=tools *" #> e.tools.toForm &
    "data-name=supplies *" #> e.supplies.toForm &
    "data-name=registration *" #> e.registration.toForm &
    "data-name=costContributionByUse *" #> e.costContributionByUse.toForm &
    "type=submit" #> SHtml.ajaxOnSubmit(() => save(e))&
    "type=cancel" #> SHtml.ajaxButton("Cancelar", () => RedirectTo("/event/events"), "class"->"btn btn-default" )
  }

  def editForm: CssSel = {
    for {
        e <- eventRequestVar.get ?~ "Evento no definido"
    } yield {
      "data-name=eventName" #> e.name &
      "data-name=eventNumber *" #> e.eventNumber.toDisableForm &
      "data-name=name *" #> e.name.toForm &
      "data-name=shortDescription *" #> e.shortDescription.toForm &
      "data-name=description *" #> e.description.toForm &
      "data-name=schedule *" #> e.schedule.toForm &
      "data-name=eventTypes *" #> e.eventTypes.toForm &
      "data-name=area *" #> e.area.toForm &
      "data-name=program *" #> e.program.toForm &
      "data-name=process *" #> e.process.toForm &
      "data-name=actionLines *" #> e.actionLines.toForm &
      "data-name=productiveUnit *" #> e.productiveUnit.toForm &
      "data-name=expositors *" #> e.expositors.toForm &
      "data-name=organizer *" #> e.organizer.toForm &
      "data-name=handlers *" #> e.handlers.toForm &
      "data-name=sponsors *" #> e.sponsors.toForm &
      "data-name=supports *" #> e.supports.toForm &
      "data-name=collaborators *" #> e.collaborators.toForm &
      "data-name=city *" #> e.city.toForm &
      "data-name=country *" #> e.country.toForm &
      "data-name=place *" #> e.place.toForm &
      "data-name=activities *" #> e.activities.toForm &
      "data-name=requirements *" #> e.requirements.toForm &
      "data-name=goal *" #> e.goal.toForm &
      "data-name=quote *" #> e.quote.toForm &
      "data-name=tools *" #> e.tools.toForm &
      "data-name=supplies *" #> e.supplies.toForm &
      "data-name=registration *" #> e.registration.toForm &
      "data-name=costContributionByUse *" #> e.costContributionByUse.toForm &
      "type=submit" #> SHtml.ajaxOnSubmit(() => update(e)) &
      "type=cancel" #> SHtml.ajaxButton("Cancelar", () => RedirectTo("/event/events"), "class"->"btn btn-default" )
    }:CssSel
  }

  def showAll = {
    "data-name=list" #> page.map(
      e => {
        "data-name=checkbox *" #> customCheckbox(e) &
        "data-name=number *" #> e.eventNumber &
        "data-name=name *"  #> e.name &
        "data-name=date *"  #> e.schedule.toString &
        "data-name=cost *" #> e.costInfo.toString &
        "data-name=organizer *" #> e.organizer.toString &
        "data-name=areaProgram *" #> (e.area + " " + e.program) &
        "data-name=edit *" #> SHtml.ajaxButton(
          "Editar",
          () => RedirectTo("/event/edit", () => eventRequestVar.set(Full(e))),
          "class" -> "btn btn-default"
        )
      }) &
    "data-name=add" #> SHtml.ajaxButton("Agregar", () => RedirectTo("/event/add"), "class"->"btn btn-primary" ) &
    "data-name=delete" #> SHtml.ajaxButton(
      "Eliminar",
      () => {
        val listToDelete = eventDeleteRequestVar.get
        RedirectTo("/event/events", () => delete(listToDelete))
      },
      "class"->"btn btn-danger")
  }

  def customCheckbox(item: Event) = {
    SHtml.ajaxCheckbox(false, b => updateDeleteList(b, item), "class" -> "checkbox-list")
  }

  def updateDeleteList(value: Boolean, e: Event): JsCmd = value match {
    case true =>
      eventDeleteRequestVar.set(e :: eventDeleteRequestVar.is)
      Noop
    case false =>
      eventDeleteRequestVar.set(eventDeleteRequestVar.is.filter(b => b.id != e.id))
      Noop
  }

  def page = Event.findAll

  def save(e: Event) = {
    e.eventNumber(Setting.eventNumber)
    e.save(true)
    Setting.updateEventNumber
    redirectToHome
  }

  def update(e: Event) = {
    e.update
    redirectToHome
  }

  def delete(items: List[Event]) = {
    items.map(e => {
      e.delete_!
    })
  }

  def redirectToHome = {
    RedirectTo("/event/events")
  }

}

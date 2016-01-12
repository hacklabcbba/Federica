package code.snippet

import code.config.Site
import code.model.Area
import code.model.event.Event
import net.liftmodules.ng.Angular._
import net.liftweb.common.{Failure, Full}
import net.liftweb.util.Helpers._
import net.liftweb.util.{CssSel, Helpers}

object EventSnippet extends ListSnippet[Event] {

  val meta = Event

  val title = "Eventos"

  val addUrl = Site.backendEventAdd.calcHref(Event.createRecord)

  def entityListUrl: String = Site.backendEvents.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Event): String = Site.backendEventEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.place)

  def renderFrontEnd: CssSel = {
    "data-name=area" #> meta.findAll.map(event => {
      "data-name=name *" #> event.name.get &
      "data-name=description *" #> event.description.asHtml
    })
  }

}

object NgEventService {
  def render = {
    renderIfNotAlreadyDefined(
      angular.module("lift.pony")
        .factory("ponyService", jsObjFactory()
        .jsonCall("getBestPony", (arg: String) => {
        // Return the best pony (server-side)
        try {
          Full(BestPony("pony"))
        } catch {
          case e:Exception => Failure(e.getMessage)
        }
      })
        )
    )
  }

  case class BestPony(name: String)
}

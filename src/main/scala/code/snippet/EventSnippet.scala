package code.snippet

import code.config.Site
import code.model.Area
import code.model.event.Event
import code.model.resource.Room
import net.liftmodules.ng.Angular._
import net.liftweb.common.{Failure, Full}
import net.liftweb.json.JsonAST.{JValue, JArray}
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
      angular.module("federica.event")
        .factory("EventService", jsObjFactory()
          .jsonCall("listRooms", {
            try {
              Full(Room.findAllBookeableEnabled.map(_.asJValue))
            } catch {
              case e:Exception => Failure(e.getMessage)
            }
          })
          .jsonCall("fetchEvent", {
            try {
              Full(Site.backendEventEdit.currentValue.openOr(Event.createRecord).asJValue)
            } catch {
              case e:Exception => Failure(e.getMessage)
            }
          })
        )
    )
  }
}

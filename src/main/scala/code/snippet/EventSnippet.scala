package code
package snippet

import code.config.Site
import code.model.event.Event

object EventSnippet extends ListSnippet[Event] {

  val meta = Event

  val title = "Eventos"

  val addUrl = Site.backendEventAdd.calcHref(Event.createRecord)

  def entityListUrl: String = Site.backendEvents.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Event): String = "" //Site.backendEventEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.eventNumber, meta.name, meta.costInfo, meta.organizer, meta.area, meta.status)

}

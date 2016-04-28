package code.snippet

import code.config.Site
import code.model.Area
import code.model.event.Event
import code.model.resource.Room
import net.liftmodules.extras.SnippetHelper
import net.liftmodules.ng.Angular._
import net.liftweb.common.{Failure, Full}
import net.liftweb.json.JsonAST.{JArray, JValue}
import net.liftweb.util.Helpers._
import net.liftweb.util.{CssSel, Helpers}

import scala.xml.NodeSeq

object PendingEventSnippet extends ListSnippet[Event] with SnippetHelper {

  val meta = Event

  val title = "Solicitudes"

  val addUrl = Site.backendEventAdd.calcHref(Event.createRecord)

  def entityListUrl: String = Site.backendPendingEvents.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Event): String = Site.backendEventEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name)

  def renderFrontEnd: CssSel = {
    "data-name=area" #> meta.findAll.map(event => {
      "data-name=name *" #> event.name.get &
      "data-name=description *" #> event.description.asHtml
    })
  }

  def form: CssSel = {
    (for {
      event <- (Site.backendEventAdd.currentValue.toList ++ Site.backendEventEdit.currentValue.toList).headOption
    } yield {
      "data-name=form" #> event.toForm
    }) getOrElse "data-name=form" #> "ERROR"
  }

}

object EventSnippet extends ListSnippet[Event] {

  val meta = Event

  val title = "Eventos"

  val addUrl = Site.backendEventAdd.calcHref(Event.createRecord)

  def entityListUrl: String = Site.backendEvents.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Event): String = Site.backendEventEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.status)

  def renderFrontEnd: CssSel = {
    "data-name=area" #> meta.findAll.map(event => {
      "data-name=name *" #> event.name.get &
      "data-name=description *" #> event.description.asHtml
    })
  }

  def renderLastThreeEventByFilter(areas: List[Area]): CssSel = {
    "data-name=events" #> Event.findLastThreeEventsByFilter(areas).map(event => {
      "data-name=title *" #> event.name.get &
      "data-name=days *" #> event.activities.get.map(_.date.toString).mkString(",") &
      {
        event.image.valueBox match {
          case Full(image) =>
            val imageSrc = image.fileId.get
            "data-name=image [src]" #> s"/image/$imageSrc"
          case _ =>
            "data-name=image *" #> NodeSeq.Empty
        }
      } &
      "data-name=cost *" #> ("Costo: " + event.costInfo.get.toString) &
      "data-name=description" #> event.description.asHtml
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

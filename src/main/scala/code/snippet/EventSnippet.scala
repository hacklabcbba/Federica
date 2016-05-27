package code.snippet

import code.config.Site
import code.lib.Helper
import code.model._
import code.model.event.Event
import code.model.resource.Room
import net.liftmodules.extras.SnippetHelper
import net.liftmodules.ng.Angular._
import net.liftweb.common.{Failure, Full}
import net.liftweb.json.JsonAST.{JArray, JValue}
import net.liftweb.util.Helpers._
import net.liftweb.util.{CssSel, Helpers, Props}
import net.liftweb.common.{Box, Full}
import net.liftweb.http.{S, Templates}

import scala.collection.immutable.Stream.Empty
import scala.xml.{NodeSeq, Text}

object PendingEventSnippet extends ListSnippet[Event] with SnippetHelper {

  val meta = Event

  val title = "Solicitudes"

  val addUrl = Site.backendEventAdd.calcHref(Event.createRecord)

  def entityListUrl: String = Site.backendPendingEvents.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Event): String = Site.backendEventEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name)

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
    "data-name=event" #> meta.findAll.map(event => {
      "data-name=name *" #> event.name.get &
      "data-name=description *" #> event.description.asHtml &
      "data-name=name [href]" #> Site.frontendEvent.calcHref(event) &
      {
        event.image.valueBox match {
          case Full(image) =>
            val imageSrc = image.fileId.get
            "data-name=image [src]" #> s"/image/$imageSrc"
          case _ =>
            "data-name=image *" #> NodeSeq.Empty
        }
      }
    })
  }

  def facebookHeaders(in: NodeSeq) = {
    Site.frontendEvent.currentValue match {
      case Full(event) =>
          <meta property="og:title" content={event.name.get} /> ++
            <meta property="og:url" content={Props.get("default.host", "http://localhost:8080") + S.uri} /> ++
            <meta property="og:description" content={event.description.asHtmlCutted(250).text} /> ++
          (if(event.facebookPhoto.get.fileId.get.isEmpty)
            NodeSeq.Empty
          else
              <meta property="og:image" content={event.facebookPhoto.fullUrl} />
            ) ++
            <meta property="og:type" content="article" />
      case _ =>
        NodeSeq.Empty
    }
  }

  def renderView: CssSel = {
    for{
      event <- Site.frontendEvent.currentValue
    } yield {
      "data-name=name *" #> event.name.get &
      "data-name=days *" #> event.activities.get.map(_.date.toString).mkString(",") &
      "data-name=cost *" #> ("Costo: " + event.costInfo.get.toString) &
      "data-name=hours *" #> ("Hora: " + event.hours.get) &
      "data-name=description *" #> event.description.asHtml &
      {
        event.image.valueBox match {
          case Full(image) =>
            val imageSrc = image.fileId.get
            "data-name=image [src]" #> s"/image/$imageSrc"
          case _ =>
            "data-name=image *" #> NodeSeq.Empty
        }
      }
    }
  }

  def renderAgenda: CssSel = {
    val parameter = Helper.getParameter.headOption.fold("")(_._1)
    val value = Helper.getParameter.headOption.fold("")(_._2)
    "data-name=event" #> meta.findAll.map(event => {
      "data-name=name *" #> event.name.get &
      "data-name=name [href]" #> Site.frontendEvent.calcHref(event)
    }) &
    "data-name=events" #> meta.findLastFourEventsByFilter.map(event => {
      "data-name=name *" #> event.name.get &
      "data-name=description *" #> event.description.asHtml &
      "data-name=name [href]" #> Site.frontendEvent.calcHref(event) &
      "data-name=days *" #> event.activities.get.map(_.date.toString).mkString(",") &
      "data-name=hours *" #> ("Hora: " + event.hours.get) &
      "data-name=cost *" #> ("Costo: " + event.costInfo.get.toString) &
      {
        event.image.valueBox match {
          case Full(image) =>
            val imageSrc = image.fileId.get
            "data-name=image [src]" #> s"/image/$imageSrc"
          case _ =>
            "data-name=image *" #> NodeSeq.Empty
        }
      }
    }) &
    "data-name=all" #> {
      "li [class+]" #> (if (value.isEmpty && parameter.isEmpty) "active" else "") &
      "a [href]" #> Site.agenda.fullUrl
    } &
    "data-name=areas" #> meta.findAreas.map(area => {
      "li [class+]" #> (if ((parameter == "area") && (value == area.name.get)) "active" else "") &
      "a [href]" #> s"${Site.agenda.fullUrl}?area=${area.name.get}" &
      "a *" #> area.name.get
    }) &
    "data-name=areasT" #> meta.findAreasTransversal.map(area => {
      "li [class+]" #> (if ((parameter == "areaT") && (value == area.name.get)) "active" else "") &
      "a [href]" #> s"${Site.agenda.fullUrl}?areaT=${area.name.get}" &
      "a *" #> area.name.get
    })
  }

  def templateRelatedEvents =
    Templates("templates-hidden" :: "frontend" :: "_relatedEvents" :: Nil) openOr Text(S ? "No edit template found")

  def relatedEvents(title: String, values: Box[Value], program: Box[Program], area: Box[Area],
                    actionLine: Box[ActionLine], transversalArea: Box[TransversalArea],
                    transversalApproach: Box[TransversalApproach], process: Box[Process], room: Box[Room]): NodeSeq = {
    renderLastThreeEventByFilter(title, values, program, area, actionLine, transversalArea, transversalApproach,
      process, room).apply(templateRelatedEvents)
  }

  def renderLastThreeEventByFilter(title: String, values: Box[Value], program: Box[Program], area: Box[Area],
                                   actionLine: Box[ActionLine], transversalArea: Box[TransversalArea],
                                   transversalApproach: Box[TransversalApproach], process: Box[Process],
                                   room: Box[Room]): CssSel = {
    val listEvents = Event.findLastThreeEventsByFilter(values, program, area, actionLine, transversalArea,
      transversalApproach, process, room)
    !listEvents.isEmpty match {
      case true =>
        "data-name=title-module" #> title &
        "data-name=events" #> listEvents.map(event => {
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
      case false =>
        "data-name=eventsH" #> NodeSeq.Empty
    }
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

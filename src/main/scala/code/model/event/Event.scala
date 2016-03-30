package code
package model
package event

import code.config.{DefaultRoles, Site}
import code.lib.field.{BsDoubleField, BsCkTextareaField, FileField, BsStringField}
import code.lib.{BaseModel, RogueMetaRecord}
import code.model.activity.Activity
import code.model.resource.Room
import net.liftweb.common.{Box, Full}
import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmds._
import net.liftweb.json.JsonAST.JArray
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field._
import net.liftweb.record.field._
import net.liftweb.util.Helpers._

import scala.xml.Elem

class Event private() extends MongoRecord[Event] with ObjectIdPk[Event] with BaseModel[Event] {

  override def meta = Event

  def title = "Eventos"

  def entityListUrl = Site.backendEvents.menu.loc.calcDefaultHref

  object eventNumber extends StringField(this, 200) {
    override def displayName = "#"
    override def toForm = Full(SHtml.ajaxText(value, (s: String) => {
      set(s)
      Noop
    }))
    def toDisableForm = SHtml.span(<b>{get}</b>, Noop)
  }

  object name extends StringField(this, 200) {
    override def displayName = "Nombre"
    override def toString = get
    override def toForm = Full(SHtml.ajaxText(value, (s: String) => {
      set(s)
      Noop
    }, "class" -> "form-control", "data-placeholder" -> "Ingrese nombre.."))
  }

  object schedule extends ObjectIdRefField(this, Schedule) {
    override def toString = {
      this.obj.dmap("")(_.toString)
    }
  }

  object costInfo extends BsDoubleField(this, 0.0) {
    override def displayName = "Costo"
  }
  
  object area extends ObjectIdRefField(this, Area) {
    override def optional_? = true
    override def displayName = "Área"
    override def toString = {
      this.obj.dmap("Indefinido..")(_.name.get)
    }
    override def toForm = {
      Full(SHtml.selectObj[Option[Area]](availableOptions, Full(this.obj),
        (p: Option[Area]) => {
          setBox(p.map(_.id.get))
        },
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione area transversal.."))
    }

    def availableOptions = (None -> "Ninguna") :: Area.findAll.map(s => Some(s) -> s.toString)
  }

  object country extends ObjectIdRefField(this, Country) {

    override def displayName = "Pais"
    override def toString = this.obj.dmap("")(_.name.get)
    val list = Country.findAll
    val default = list.headOption
    override def toForm = {
      Full(SHtml.ajaxSelectElem(list, default,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione pais..")(p => {
        set(p.id.get)
        Noop
      }))
    }
  }

  object activities extends ObjectIdRefListField(this, Activity) {
    override def displayName = "Actividades"
  }

  object description extends BsCkTextareaField(this, 1000) {
    override def displayName = "Descripción"
  }

  object requirements extends BsCkTextareaField(this, 1000) {
    override def displayName = "Requerimientos"
  }

  object destinedTo extends BsStringField(this, 200) {
    override def displayName = "Destinado a"
  }

  object specificRequirements extends BsCkTextareaField(this, 1000) {
    override def displayName = "Requerimientos especificos"
    override def shouldDisplay_? =
      User.hasRole(DefaultRoles.Admin.id.get) || User.hasRole(DefaultRoles.CoordGeneral.id.get)
  }

  object organizer extends BsStringField(this, 500) {
    override def displayName = "Organiza"
  }

  object handlers extends BsStringField(this, 500) {
    override def displayName = "Gestiona"
  }

  object collaborators extends BsStringField(this, 500) {
    override def displayName = "Colabora"
  }

  object supports extends BsStringField(this, 500) {
    override def displayName = "Apoya"
  }

  object pressRoom extends ObjectIdRefField(this, PressNotes) {
    override def displayName = "Sala de prensa"
  }

  object quote extends BsDoubleField(this, 0.0) {
    override def displayName = "Cupo"
  }

  object status extends EnumNameField(this, StatusType) {
    override def displayName = "Estado"
    override def shouldDisplay_? = false
  }

  object eventKind extends EnumNameField(this, EventKind) {
    override def displayName = "Tipo de evento"
  }


  object rooms extends ObjectIdRefListField(this, Room) {
    override def shouldDisplay_? = false

    override def asJValue = JArray(objs.map(_.asJValue))
  }

  object isOutstanding extends BooleanField(this, false) {
    override def displayName = "Destacado"
    override def shouldDisplay_? =
      User.hasRole(DefaultRoles.Admin.id.get) || User.hasRole(DefaultRoles.CoordGeneral.id.get)
  }

  object hours extends BsStringField(this, 200) {
    override def displayName = "Horas"
  }

  object image extends FileField(this) {
    override def displayName = "Imágen"
    override def toString = {
      value.fileName.get
    }
  }

  object isLogoEnabled extends BooleanField(this, false) {
    override def displayName = "¿Pondrá el logo del mARTadero en sus materiales de difusión?"
  }

  object applicantType extends EnumNameField(this, ApplicantType) {
    override def displayName = "Tipo de solicitante"
  }


}

object Event extends Event with RogueMetaRecord[Event] {
  override def collectionName = "event.events"

  override def fieldOrder = List(
    name, eventKind, requirements, destinedTo,  area, country, eventNumber,
    isOutstanding, organizer, handlers, collaborators, supports,
    description, hours, costInfo, quote,
    image, isLogoEnabled, applicantType,
    activities, pressRoom, specificRequirements)
}

object StatusType extends Enumeration {
  type StatusType = Value
  val Approved, Rejected, Draft = Value
}

object ApplicantType extends Enumeration {
  type ApplicantType = Value
  val Coordinador = Value("Coordinador de área en mARTadero")
  val Asociado = Value("Asociado: personas afiliadas a la institución, con registro personal/institucional")
  val Artista1 = Value("Artista nacional emergente/internacional autogesstionado")
  val Artista2 = Value("Artista nacional e internacional cnsolidado")
  val Organizacion1 = Value("Organización de Gestión Cultural")
  val Oranizacion2 = Value("Organización sin fines de lucro")
  val Organizacion3 = Value("Organización con fines de lucro")
}

object EventKind extends Enumeration {
  type EventKind = Value
  val Taller = Value("Taller")
  val ResidenciaArtistica = Value("Residencia Artística")
  val Bienal = Value("Bienal")
  val Concierto = Value("Concierto")
  val Concurso = Value("")
  val ConferenciadePrensa = Value("Conferencia de Prensa")
  val Congreso = Value("Congreso")
  val Conversatorio = Value("Conversatorio")
  val Convocatoria = Value("Convocatoria")
  val Encuentro = Value("Encuentro")
  val Ensayo = Value("Ensayo")
  val Exposicion = Value("Exposición")
  val Feria = Value("Feria")
  val Festival = Value("Festival")
  val Instalacion = Value("Instalación")
  val Intervencion = Value("Intervención")
  val Laboratorio = Value("Laboratorio")
  val Performance = Value("Performance")
  val Presentacion = Value("Presentación")
  val Proyeccion = Value("Proyección")
  val Puestaenescena = Value("Puesta en escena")
  val Reunion = Value("Reunión")
  val Rodaje = Value("Rodaje")
  val Seminario = Value("Seminario")
  val Simposio = Value("Simposio")
  val Tertulia = Value("Tertulia")
  val Visitaguiada = Value("Visita guiada")

}

class Values private() extends MongoRecord[Values] with ObjectIdPk[Values] {

  override def meta = Values

  object name extends StringField(this, 100) {
    override def displayName = "Nombre"
  }

}

object Values extends Values with RogueMetaRecord[Values] {
  val Innovation = "Innovación"
  val Research = "Investigación"
  val Experimentation = "Experimentacion"
  val ConceptualAndFormaligor = "Rigor conceptual y formal"
  val Integration = "Integracion"
  val ExchangeOfKnowledgeAndExperiences = "Intercambio de conocimientos y experiencias"
  val Intercultural = "Interculturalidad"
  private lazy val data = List(
    "Innovación",
    "Investigación",
    "Experimentacion",
    "Rigor conceptual y formal",
    "Integracion",
    "Intercambio de conocimientos y experiencias",
    "Interculturalidad"
  )

  def seedData = {
    if (Values.count() == 0) data.foreach(d => {
      Values.createRecord.name(d).save(true)
    })
  }
}


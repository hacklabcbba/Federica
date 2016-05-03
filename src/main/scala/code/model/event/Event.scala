package code
package model
package event

import code.config.{DefaultRoles, Site}
import code.lib.field._
import code.lib.js.Bootstrap
import code.lib.{BaseModel, RogueMetaRecord}
import code.model.event.Activity
import code.model.resource.Room
import net.liftweb.common.{Box, Full}
import net.liftweb.http.{IdMemoizeTransform, S, SHtml}
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds._
import net.liftweb.json.JsonAST.JArray
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field._
import net.liftweb.record.field._
import net.liftweb.util.Helpers._
import org.bson.types.ObjectId
import net.liftweb.json.JsonDSL._
import java.util.{Date, Locale}

import code.model.BlogPost.process._

import scala.xml.{Elem, NodeSeq}

class Event private() extends MongoRecord[Event] with ObjectIdPk[Event] with BaseModel[Event] {

  override def meta = Event

  def title = "Eventos"

  def entityListUrl = Site.backendPendingEvents.menu.loc.calcDefaultHref

  object eventNumber extends StringField(this, 200) {
    override def displayName = "#"
    override def toForm = Full(SHtml.ajaxText(value, (s: String) => {
      set(s)
      Noop
    }))
    def toDisableForm = SHtml.span(<b>{get}</b>, Noop)
  }

  object user extends ObjectIdRefField(this, User) {
    override def defaultValueBox = User.currentUser.map(_.id.get)
    override def shouldDisplay_? = false
    override def displayName = "Organizador"
    override def toString = {
      this.obj.dmap("Indefinido..")(_.name.get)
    }
  }

  object name extends StringField(this, 200) {
    override def displayName = "Nombre"
    override def toString = get
    override def toForm = Full(SHtml.ajaxText(value, (s: String) => {
      set(s)
      Noop
    }, "class" -> "form-control", "data-placeholder" -> "Ingrese nombre.."))
  }

  object program extends ObjectIdRefField(this, Program) {
    override def displayName = "Programa"
    override def optional_? = true
    override def toString = obj.dmap("")(_.name.get)
    val list = (None -> "Ninguno") :: Program.findAll.map(s => Some(s) -> s.toString)
    override def toForm = {
      Full(SHtml.selectObj[Option[Program]](list, Full(this.obj),
        (p: Option[Program]) => {
          setBox(p.map(_.id.get))
        },
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione prorgrama.."))
    }
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

  object transversalArea extends ObjectIdRefField(this, TransversalArea) {
    override def optional_? = true
    override def displayName = "Área transversal"
    override def toString = this.obj.dmap("")(_.name.get)
    val list = (None -> "Ninguna") :: TransversalArea.findAll.map(s => Some(s) -> s.toString)
    override def toForm = {
      Full(SHtml.selectObj[Option[TransversalArea]](list, Full(this.obj),
        (p: Option[TransversalArea]) => {
          setBox(p.map(_.id.get))
        },
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione area transversal.."))
    }
  }

  object transversalApproach extends ObjectIdRefField(this, TransversalApproach) {
    override def optional_? = true
    override def displayName = "Enfoque transversal"
    override def toString = this.obj.dmap("")(_.name.get)
    val list = (None -> "Ninguna") :: TransversalApproach.findAll.map(s => Some(s) -> s.toString)
    override def toForm = {
      Full(SHtml.selectObj[Option[TransversalApproach]](list, Full(this.obj),
        (p: Option[TransversalApproach]) => {
          setBox(p.map(_.id.get))
        },
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione un enfoque.."))
    }
  }

  object process extends ObjectIdRefField(this, Process) {
    override def optional_? = true
    override def displayName = "Proceso"
    val list = (None -> "Ninguno") :: Process.findAll.map(s => Some(s) -> s.toString)
    override def toForm = {
      Full(SHtml.selectObj[Option[Process]](list, Full(this.obj),
        (p: Option[Process]) => {
          setBox(p.map(_.id.get))
        },
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione proceso.."))
    }
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

  object activities extends BsonRecordListField(this, Activity) {
    override def displayName = "Actividades"
    private def modalBody(body: IdMemoizeTransform, activity: Activity, isNew: Boolean): NodeSeq = {
      <div class="modal-dialog">
        <div class="modal-content">
          <form data-lift="form.ajax?classform-horizontal">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
              <h4 class="modal-title">Actividad</h4>
            </div>
            <div class="modal-body">
              {activity.toFieldset(activity.fields)}
            </div>
            <div class="modal-footer">
              {SHtml.ajaxSubmit(if (isNew) "Guardar" else "Actualizar", () => {
                if (isNew) {
                  activities.set(activities.get ++ List(activity))
                  Bootstrap.Modal.close() &
                  body.setHtml()
                } else {
                  Bootstrap.Modal.close() &
                  body.setHtml()
                }
              }, "class" -> "btn btn-success")}
              <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
            </div>
          </form>
        </div>
      </div>
    }

    private def elem = {
      ("*" #> SHtml.idMemoize(body => {
        "data-name=activity" #> this.get.map(activity => {
          "data-name=name *" #> activity.name.get &
          "data-name=kind *" #> activity.kind.get.toString &
          "data-name=room *" #> activity.room.obj.dmap("")(_.name.get) &
          "data-name=date *" #> activity.date.toString() &
          "data-name=edit [onclick]" #> SHtml.ajaxInvoke(() => Bootstrap.Modal(modalBody(body, activity, false))) &
          "data-name=delete [onclick]" #> SHtml.ajaxInvoke(() => {
            activities.set(activities.get.filter(s => s != activity))
            Bootstrap.Modal.close() &
            body.setHtml()
          })
        }) &
        "data-name=add [onclick]" #> SHtml.ajaxInvoke(() => Bootstrap.Modal(modalBody(body, Activity.createRecord, true)))
      })).apply(template)
    }
    private def template = {
      <div class="col-sm-12 col-lg-12">
        <table class="table">
          <thead>
            <tr>
              <th>Nombre</th>
              <th>Tipo</th>
              <th>Sala</th>
              <th>Fecha</th>
              <th>&nbsp;</th>
            </tr>
          </thead>
          <tbody>
            <tr data-name="activity">
              <td data-name="name"></td>
              <td data-name="kind"></td>
              <td data-name="room"></td>
              <td data-name="date"></td>
              <td>
                <a href="#" class="btn btn-success" data-name="edit">Editar</a>
                <a href="#" class="btn btn-danger" data-name="delete">Eliminar</a>
              </td>
            </tr>
          </tbody>
        </table>
        <a href="#" data-name="add" class="btn btn-success"><i class="fa fa-plus"></i></a>
      </div>
    }
    override def toForm = Full(elem)

  }

  object description extends BsCkTextareaField(this, 1000) {
    override def displayName = "Descripción"
  }

  object requirements extends BsCkTextareaField(this, 1000) {
    override def displayName = "Requerimientos"

    override def toForm = super.toForm.map(s => <div id="requirements_div">{s}</div>)
  }

  object destinedTo extends BsStringField(this, 200) {
    override def displayName = "Destinado a"
    override def toForm = super.toForm.map(s => <div id="destinedTo_div">{s}</div>)
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

  object pressRoom extends BsonRecordListField(this, PressNotes) {
    override def displayName = "Sala de prensa"
    private def modalBody(body: IdMemoizeTransform, pressNote: PressNotes, isNew: Boolean): NodeSeq = {
      <div class="modal-dialog">
        <div class="modal-content">
          <form data-lift="form.ajax?classform-horizontal">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
              <h4 class="modal-title">Actividad</h4>
            </div>
            <div class="modal-body">
              {pressNote.toFieldset(pressNote.fields)}
            </div>
            <div class="modal-footer">
              {SHtml.ajaxSubmit(if (isNew) "Guardar" else "Actualizar", () => {
              if (isNew) {
                pressRoom.set(pressRoom.get ++ List(pressNote))
                Bootstrap.Modal.close() &
                  body.setHtml()
              } else {
                Bootstrap.Modal.close() &
                  body.setHtml()
              }
            }, "class" -> "btn btn-success")}
              <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
            </div>
          </form>
        </div>
      </div>
    }

    private def elem = {
      ("*" #> SHtml.idMemoize(body => {
        "data-name=activity" #> this.get.map(pr => {
          "data-name=name *" #> pr.name.get &
          "data-name=edit [onclick]" #> SHtml.ajaxInvoke(() => Bootstrap.Modal(modalBody(body, pr, false))) &
          "data-name=delete [onclick]" #> SHtml.ajaxInvoke(() => {
            pressRoom.set(pressRoom.get.filter(s => s != pr))
            Bootstrap.Modal.close() &
              body.setHtml()
          })
        }) &
        "data-name=add [onclick]" #> SHtml.ajaxInvoke(() => Bootstrap.Modal(modalBody(body, PressNotes.createRecord, true)))
      })).apply(template)
    }
    private def template = {
      <div class="col-sm-12 col-lg-12">
        <table class="table">
          <thead>
            <tr>
              <th>Nombre</th>
              <th>&nbsp;</th>
            </tr>
          </thead>
          <tbody>
            <tr data-name="activity">
              <td data-name="name"></td>
              <td>
                <a href="#" class="btn btn-success" data-name="edit">Editar</a>
                <a href="#" class="btn btn-danger" data-name="delete">Eliminar</a>
              </td>
            </tr>
          </tbody>
        </table>
        <a href="#" data-name="add" class="btn btn-success"><i class="fa fa-plus"></i></a>
      </div>
    }
    override def toForm = Full(elem)
  }

  object quote extends BsDoubleField(this, 0.0) {
    override def displayName = "Cupo"
  }

  object status extends EnumNameField(this, StatusType) {
    override def displayName = "Estado"
    override def shouldDisplay_? =
      User.hasAnyRoles(
        List(
          DefaultRoles.Admin.id.get,
          DefaultRoles.CoordGeneral.id.get,
          DefaultRoles.SuperAdmin.id.get
        ))
  }

  object eventKind extends EnumNameField(this, EventKind) {
    override def displayName = "Tipo de evento"
    val toggleExtraFields = new JsCmd {
      def toJsCmd =
        """
           |var eventKindElem = jQuery('#eventKind_id');
           |var showHideFieldsFn = function() {
           |  if ($( "#eventKind_id option:selected" ).text() == 'Taller') {
           |    jQuery('#destinedTo_div').parent().show();
           |    jQuery('#requirements_div').parent().show();
           |    jQuery('#residenciaNorte_div').parent().hide();
           |    jQuery('#residenciaSud_div').parent().hide();
           |  } else if ($( "#eventKind_id option:selected" ).text() == 'Residencia Artística') {
           |    jQuery('#residenciaNorte_div').parent().show();
           |    jQuery('#residenciaSud_div').parent().show();
           |    jQuery('#destinedTo_div').parent().hide();
           |    jQuery('#requirements_div').parent().hide();
           |  } else {
           |    jQuery('#destinedTo_div').parent().hide();
           |    jQuery('#requirements_div').parent().hide();
           |    jQuery('#residenciaNorte_div').parent().hide();
           |    jQuery('#residenciaSud_div').parent().hide();
           |  }
           |}
           |eventKindElem.on('change', showHideFieldsFn);
           |showHideFieldsFn();
           """.stripMargin
    }
    override def toForm = {
      S.appendJs(OnLoad(toggleExtraFields))
      super.toForm
    }
  }

  object rooms extends ObjectIdRefListField(this, Room) {
    override def shouldDisplay_? = true

    override def displayName = ""

    override def asJValue = JArray(objs.map(_.asJValue))


    override def toForm = Full(
      SHtml.hidden(values => {
        val ids = values.split(",").toList.map(new ObjectId(_))
        ids.foreach(println(_))
        this.setFromAny(ids)
      }, this.objs.map(_.id.get).mkString(","), "id" -> "rooms")
    )
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

  object residenciaNorte extends BsIntField(this, 0) {
    override def displayName = "Residencias NORTE - Nº de personas. (Máximo 14)"
    override def toForm = super.toForm.map(s => <div id="residenciaNorte_div">{s}</div>)
  }

  object residenciaSud extends BsIntField(this, 0) {
    override def displayName = "Residencias SUR - Nº de personas. (Máximo 8)"
    override def toForm = super.toForm.map(s => <div id="residenciaSud_div">{s}</div>)
  }

  object isLogoEnabled extends BooleanField(this, false) {
    override def displayName = "¿Pondrá el logo del mARTadero en sus materiales de difusión?"
  }

  object applicantType extends EnumNameField(this, ApplicantType) {
    override def displayName = "Tipo de solicitante"
  }

  object values extends ObjectIdRefListField(this, Value) {
    override def displayName = "Principios"
    def currentValue = this.objs
    def availableOptions: List[(Value, String)] = Value.findAll.map(p => p -> p.name.get)
    override def optional_? = true

    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectObj(
        availableOptions,
        currentValue,
        (list: List[Value]) => set(list.map(_.id.get)),
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione uno o varios principios..."
      ))
    }
  }

  object actionLines extends ObjectIdRefListField(this, ActionLine) {
    override def displayName = "Lineas de acción"
    def currentValue = this.objs
    def availableOptions: List[(ActionLine, String)] = ActionLine.findAll.map(p => p -> p.name.get).toList
    override def optional_? = true

    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectObj(
        availableOptions,
        currentValue,
        (list: List[ActionLine]) => set(list.map(_.id.get)),
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione una o varias lineas de accion.."
      ))
    }
  }
}

object Event extends Event with RogueMetaRecord[Event] {
  override def collectionName = "event.events"

  override def fieldOrder = List(
    name, eventKind, requirements, destinedTo, area, transversalArea,  program, actionLines, process, values, country,
    eventNumber, isOutstanding, organizer, handlers, collaborators, supports, transversalApproach,
    description, hours, costInfo, quote,
    image, isLogoEnabled, applicantType,
    activities, pressRoom, specificRequirements, residenciaNorte, residenciaSud, status, rooms)

  def findLastEventsByUser(user: User): List[Event] = {

    Event.where(_.user eqs user.id.get).and(_.status eqs StatusType.Approved).orderDesc(_.id).fetch()
  }

  def findLastThreeEventsByFilter(values: Box[Value], program: Box[Program], area: Box[Area],
                                  actionLine: Box[ActionLine], transversalArea: Box[TransversalArea],
                                  transversalApproach: Box[TransversalApproach], process: Box[Process]): List[Event] = {
    Event.or(_.whereOpt(values.toOption)(_.values contains  _.id.get), _.whereOpt(program.toOption)(_.program eqs _.id.get),
      _.whereOpt(area.toOption)(_.area eqs _.id.get), _.whereOpt(actionLine.toOption)(_.actionLines contains _.id.get),
      _.whereOpt(transversalArea.toOption)(_.transversalArea eqs _.id.get),
      _.whereOpt(transversalApproach.toOption)(_.transversalApproach eqs _.id.get),
      _.whereOpt(process.toOption)(_.process eqs _.id.get))
      .orderDesc(_.id).fetch(3)
  }
}

object StatusType extends Enumeration {
  type StatusType = Value
  val Approved = Value("Aprobado")
  val Rejected = Value("Rechazado")
  val Draft = Value("Borrador")
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
  val Concurso = Value("Concurso")
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
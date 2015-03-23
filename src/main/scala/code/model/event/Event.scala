package code
package model
package event

import code.lib.RogueMetaRecord
import code.model.project._
import net.liftweb.util.Helpers._
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field._
import net.liftweb.record.field.{EnumNameField, TextareaField, LongField, DecimalField, StringField}
import code.model.proposal.{ActionLine, Area, Program}
import code.model.activity.{Activity, ActivityType}
import code.model.productive.ProductiveUnit
import net.liftweb.common.{Full, Empty, Box}
import scala.xml.Elem
import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmds._
import net.liftweb.common.Full
import org.bson.types.ObjectId

class Event private() extends MongoRecord[Event] with ObjectIdPk[Event]{

  override def meta = Event

  object eventNumber extends StringField(this, 200) {
    override def toForm = Full(SHtml.ajaxText(value, (s: String) => {
      set(s)
      Noop
    }))
    def toDisableForm = SHtml.span(<b>{get}</b>, Noop)
  }

  object name extends StringField(this, 200) {
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

  object costInfo extends ObjectIdRefField(this, CostInfo) {
    override def toString = this.obj.dmap("no definido..")(_.cost.get.toString)
  }

  object eventTypes extends ObjectIdRefListField(this, EventType) {
    def currentValue = EventType.findAll.headOption.toList
    def availableOptions: List[(EventType, String)] = EventType.findAll.map(p => p -> p.name.get).toList

    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectObj(
        availableOptions,
        currentValue,
        {(list: List[EventType]) => set(list.map(_.id.get))},
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione uno o varios tipos de evento.."
      ))
    }
  }

  object program extends ObjectIdRefField(this, Program) {

    override def optional_? = true
    override def toString = this.obj.dmap("")(_.name.get)
    val listProgram = Program.findAll.map(p => p)
    val defaultProgram = listProgram.headOption
    override def toForm = {
      Full(SHtml.ajaxSelectElem(listProgram, defaultProgram,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione programa..")(p => {
        set(p.id.get)
        Noop
      }))
    }
  }

  object area extends ObjectIdRefField(this, Area) {

    override def toString = this.obj.dmap("")(_.name.get)
    val list = Area.findAll.map(p => p)
    val default = list.headOption
    override def toForm = {
      Full(SHtml.ajaxSelectElem(list, default,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione area..")(p => {
          set(p.id.get)
          Noop
      }))
    }
  }

  object process extends ObjectIdRefField(this, Process) {
    val list = Process.findAll.map(p => p)
    val default = list.headOption
    override def toForm = {
      Full(SHtml.ajaxSelectElem(list, default,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione un proceso..")(p => {
        set(p.id.get)
        Noop
      }))
    }
  }

  object actionLines extends ObjectIdRefListField(this, ActionLine) {
    def currentValue = ActionLine.findAll.headOption.toList
    def availableOptions: List[(ActionLine, String)] = ActionLine.findAll.map(p => p -> p.name.get).toList

    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectObj(
        availableOptions,
        currentValue,
        {(list: List[ActionLine]) => set(list.map(_.id.get))},
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione uno o varios lineas de accion.."
      ))
    }
  }

  object productiveUnit extends ObjectIdRefField(this, ProductiveUnit) {

    override def optional_? = true
    override def toString = this.obj.dmap("")(_.name.get)
    val list = ProductiveUnit.findAll.map(p => p)
    val default = list.headOption
    override def toForm = {
      Full(SHtml.ajaxSelectElem(list, default,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione unidad productiva..")(p => {
        set(p.id.get)
        Noop
      }))
    }
  }

  object city extends ObjectIdRefField(this, City) {

    override def toString = this.obj.dmap("")(_.name.get)
    val list = City.findAll.map(p => p)
    val default = list.headOption
    override def toForm = {
      Full(SHtml.ajaxSelectElem(list, default,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione ciudad..")(p => {
        set(p.id.get)
        Noop
      }))
    }
  }

  object country extends ObjectIdRefField(this, Country) {

    override def toString = this.obj.dmap("")(_.name.get)
    val list = Country.findAll.map(p => p)
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

  object place extends StringField(this, 500) {
    override def toForm = Full(SHtml.ajaxText(value, (s: String) => {
      set(s)
      Noop
    }, "class" -> "form-control", "data-placeholder" -> "Ingrese el lugar del evento.."))
  }

  object shortDescription extends TextareaField(this, 1000) {
    override def toForm = {
      Full(SHtml.textarea(value, v => set(v), "class"->"form-control" ))
    }
  }

  object activities extends ObjectIdRefListField(this, Activity)

  object description extends TextareaField(this, 1000) {
    override def toForm = {
      Full(SHtml.textarea(value, v => set(v), "class"->"form-control" ))
    }
  }

  object requirements extends ObjectIdRefListField(this, EventRequirement) {
    def currentValue = EventRequirement.findAll.headOption.toList
    def availableOptions: List[(EventRequirement, String)] =
      EventRequirement.findAll.map(p => p -> p.title.get).toList

    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectObj(
        availableOptions,
        currentValue,
        {(list: List[EventRequirement]) => set(list.map(_.id.get))},
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione uno o varios tipos de evento.."
      ))
    }
  }

  object expositors extends ObjectIdRefListField(this, User) {
    def currentValue = this.objs
    //def currentValue = User.currentUser.getOrElse(User.createRecord) :: Nil
    def availableOptions: List[(User, String)] =
      User.findAll.map(p => p -> p.name.get).toList

    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectObj(availableOptions, currentValue,{(list: List[User]) =>
        set(list.map(_.id.get))
        },
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione uno o varios tipos de evento.."
      ))
    }
  }

  object organizer extends ObjectIdRefField(this, User) {
    override def toString = {
      this.obj.dmap("Indefinido..")(_.name.get)
    }
    override def defaultValue = User.currentUser.getOrElse(User.createRecord).id.get
    override def toForm: Box[Elem] = {
      Full(SHtml.selectObj(availableOptions, currentValue, {(u: User) => {
          set(u.id.get)
        }},
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione quien organiza este evento.."
      ))
    }
    def currentValue = User.currentUser
    def availableOptions = User.findAll.map(p => p -> p.name.get).toSeq
  }

  object handlers extends ObjectIdRefListField(this, User) {
    override def toString = {
      User.findAll(this.get).map(_.name.get).mkString(", ")
    }

    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectObj(availableOptions, currentValue, {(list: List[User]) =>
        set(list.map(_.id.get))
        },
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione uno o varios gestionadores.."
      ))
    }
    override def defaultValue = User.currentUser.getOrElse(User.createRecord).id.get :: Nil
    def currentValue = User.currentUser.getOrElse(User.createRecord) :: Nil
    def availableOptions = User.findAll.map(p => p -> p.name.get).toSeq
  }

  object sponsors extends ObjectIdRefListField(this, User) {
    override def toString = {
      User.findAll(this.get).map(_.name.get).mkString(", ")
    }

    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectObj(availableOptions, currentValue, {(list: List[User]) =>
        set(list.map(_.id.get))
      },
      "class" -> "select2 form-control",
      "data-placeholder" -> "Seleccione uno o varios auspiciadores.."
      ))
    }
    override def defaultValue = User.currentUser.getOrElse(User.createRecord).id.get :: Nil
    def currentValue = User.currentUser.getOrElse(User.createRecord) :: Nil
    def availableOptions = User.findAll.map(p => p -> p.name.get).toSeq
  }

  object supports extends ObjectIdRefListField(this, User) {
    override def toString = {
      User.findAll(this.get).map(_.name.get).mkString(", ")
    }

    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectObj(availableOptions, currentValue, {(list: List[User]) =>
        set(list.map(_.id.get))
      },
      "class" -> "select2 form-control",
      "data-placeholder" -> "Seleccione uno o varios soportes.."
      ))
    }
    override def defaultValue = User.currentUser.getOrElse(User.createRecord).id.get :: Nil
    def currentValue = User.currentUser.getOrElse(User.createRecord) :: Nil
    def availableOptions = User.findAll.map(p => p -> p.name.get).toSeq
  }

  object collaborators extends ObjectIdRefListField(this, User) {
    override def toString = {
      User.findAll(this.get).map(_.name.get).mkString(", ")
    }

    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectObj(availableOptions, currentValue, {(list: List[User]) =>
        set(list.map(_.id.get))
      },
      "class" -> "select2 form-control",
      "data-placeholder" -> "Seleccione uno o varios colaboradores.."
      ))
    }
    override def defaultValue = User.currentUser.getOrElse(User.createRecord).id.get :: Nil
    def currentValue = User.currentUser.getOrElse(User.createRecord) :: Nil
    def availableOptions = User.findAll.map(p => p -> p.name.get).toSeq
  }

  object pressRoom extends ObjectIdRefField(this, PressNotes)

  object goal extends TextareaField(this, 1000) {
    override def toForm = {
      Full(SHtml.textarea(value, v => set(v), "class"->"form-control" ))
    }
  }

  object quote extends StringField(this, "") {
    override def toForm = Full(SHtml.ajaxText(value, (s: String) => {
      set(s)
      Noop
    }, "class" -> "form-control", "data-placeholder" -> "Ingrese el cupo maximo.."))
  }

  object tools extends TextareaField(this, 1000) {
    override def toForm = {
      Full(SHtml.textarea(value, v => set(v), "class"->"form-control" ))
    }
  }

  object supplies extends TextareaField(this, 1000) {
    override def toForm = {
      Full(SHtml.textarea(value, v => set(v), "class"->"form-control" ))
    }
  }

  object registration extends TextareaField(this, 1000) {
    override def toForm = {
      Full(SHtml.textarea(value, v => set(v), "class"->"form-control" ))
    }
  }

  object costContributionByUse extends ObjectIdRefField(this, CostContributionByUse)
  object state extends EnumNameField(this, StateType)
}

object Event extends Event with RogueMetaRecord[Event]

object StateType extends Enumeration {
  type StateType = Value
  val Aproved, Rejected, Draft = Value
}
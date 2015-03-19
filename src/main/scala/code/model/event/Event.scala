package code
package model
package event

import code.lib.RogueMetaRecord
import code.model.project._
import net.liftweb.util.Helpers._
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field._
import net.liftweb.record.field.{TextareaField, LongField, DecimalField, StringField}
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

  object eventNumber extends LongField(this)

  object name extends StringField(this, 200)

  object schedule extends ObjectIdRefField(this, Schedule){
    override def toString = {
      Schedule.find(this.value).getOrElse(Schedule.createRecord) + ""
    }
  }
  /*
  * val sched = Schedule.find("_id", new ObjectId(this.value))
      println(sched)
  * */
  object costInfo extends ObjectIdRefField(this, CostInfo)

  object eventTypes extends ObjectIdRefListField(this, EventType){
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

  object program extends ObjectIdRefField(this, Program){

    override def optional_? = true
    override def toString = Program.find(get).dmap("")(_.name.get)
    val listProgram = Program.findAll.map(p => p)
    val defaultProgram = Program.findAll.headOption
    override def toForm = {
      Full(SHtml.ajaxSelectElem(listProgram, defaultProgram,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione programa..")(p => {
        set(p.id.get)
        Noop
      }))
    }
  }

  object area extends ObjectIdRefField(this, Area){

    override def toString = Area.find(get).dmap("")(_.name.get)
    val list = Area.findAll.map(p => p)
    val default = Area.findAll.headOption
    override def toForm = {
      Full(SHtml.ajaxSelectElem(list, default,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione area..")(p => {
          set(p.id.get)
          Noop
      }))
    }
  }

  object process extends ObjectIdRefField(this, Process){
    val list = Process.findAll.map(p => p)
    val default = Process.findAll.headOption
    override def toForm = {
      Full(SHtml.ajaxSelectElem(list, default,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione un proceso..")(p => {
        set(p.id.get)
        Noop
      }))
    }
  }

  object actionLines extends ObjectIdRefListField(this, ActionLine){
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

  object productiveUnit extends ObjectIdRefField(this, ProductiveUnit){

    override def optional_? = true
    override def toString = ProductiveUnit.find(get).dmap("")(_.name.get)
    val list = ProductiveUnit.findAll.map(p => p)
    val default = ProductiveUnit.findAll.headOption
    override def toForm = {
      Full(SHtml.ajaxSelectElem(list, default,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione unidad productiva..")(p => {
        set(p.id.get)
        Noop
      }))
    }
  }

  object city extends ObjectIdRefField(this, City){

    override def toString = City.find(get).dmap("")(_.name.get)
    val list = City.findAll.map(p => p)
    val default = City.findAll.headOption
    override def toForm = {
      Full(SHtml.ajaxSelectElem(list, default,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione ciudad..")(p => {
        set(p.id.get)
        Noop
      }))
    }
  }

  object country extends ObjectIdRefField(this, Country){

    override def toString = Country.find(get).dmap("")(_.name.get)
    val list = Country.findAll.map(p => p)
    val default = Country.findAll.headOption
    override def toForm = {
      Full(SHtml.ajaxSelectElem(list, default,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione pais..")(p => {
        set(p.id.get)
        Noop
      }))
    }
  }

  object place extends StringField(this, "")

  object shortDescription extends TextareaField(this, 1000){
    override def toForm = {
      Full(SHtml.textarea(value, {v => set(v)}, "class"->"form-control" ))
    }
  }

  object activities extends ObjectIdRefListField(this, Activity)

  object description extends TextareaField(this, 1000){
    override def toForm = {
      Full(SHtml.textarea(value, {v => set(v)}, "class"->"form-control" ))
    }
  }

  object requirements extends ObjectIdRefListField(this, EventRequirement){
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

  object expositors extends ObjectIdRefListField(this, User){
    def currentValue = User.currentUser.getOrElse(User.createRecord) :: Nil
    def availableOptions: List[(User, String)] =
      User.findAll.map(p => p -> p.name.get).toList

    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectObj(
      availableOptions,
      currentValue,
      {(list: List[User]) => set(list.map(_.id.get))},
      "class" -> "select2 form-control",
      "data-placeholder" -> "Seleccione uno o varios tipos de evento.."
      ))
    }
  }

  object organizer extends ObjectIdRefField(this, User){

    def currentValue = User.currentUser
    def availableOptions: List[(User, String)] =
      User.findAll.map(p => p -> p.name.get).toList

    override def toForm: Box[Elem] = {
      Full(SHtml.selectObj(availableOptions, currentValue,{(u: User) => {
          set(u.id.get)
          Noop
        }},
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione uno o varios tipos de evento.."
      ))
    }
  }

  object handlers extends ObjectIdRefListField(this, User){
    def currentValue = User.currentUser.getOrElse(User.createRecord) :: Nil
    def availableOptions: List[(User, String)] =
      User.findAll.map(p => p -> p.name.get).toList

    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectObj(
      availableOptions,
      currentValue,
      {(list: List[User]) => set(list.map(_.id.get))},
      "class" -> "select2 form-control",
      "data-placeholder" -> "Seleccione uno o varios tipos de evento.."
      ))
    }
  }

  object sponsors extends ObjectIdRefListField(this, User){
    def currentValue = User.currentUser.getOrElse(User.createRecord) :: Nil
    def availableOptions: List[(User, String)] =
      User.findAll.map(p => p -> p.name.get).toList

    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectObj(
      availableOptions,
      currentValue,
      {(list: List[User]) => set(list.map(_.id.get))},
      "class" -> "select2 form-control",
      "data-placeholder" -> "Seleccione uno o varios tipos de evento.."
      ))
    }
  }

  object supports extends ObjectIdRefListField(this, User){
    def currentValue = User.currentUser.getOrElse(User.createRecord) :: Nil
    def availableOptions: List[(User, String)] =
      User.findAll.map(p => p -> p.name.get).toList

    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectObj(
      availableOptions,
      currentValue,
      {(list: List[User]) => set(list.map(_.id.get))},
      "class" -> "select2 form-control",
      "data-placeholder" -> "Seleccione uno o varios tipos de evento.."
      ))
    }
  }

  object collaborators extends ObjectIdRefListField(this, User){
    def currentValue = User.currentUser.getOrElse(User.createRecord) :: Nil
    def availableOptions: List[(User, String)] =
      User.findAll.map(p => p -> p.name.get).toList

    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectObj(
      availableOptions,
      currentValue,
      {(list: List[User]) => set(list.map(_.id.get))},
      "class" -> "select2 form-control",
      "data-placeholder" -> "Seleccione uno o varios tipos de evento.."
      ))
    }
  }

  object pressRoom extends ObjectIdRefField(this, PressNotes)

  object goal extends TextareaField(this, 1000){
    override def toForm = {
      Full(SHtml.textarea(value, {v => set(v)}, "class"->"form-control" ))
    }
  }

  object quote extends StringField(this, "")

  object tools extends TextareaField(this, 1000){
    override def toForm = {
      Full(SHtml.textarea(value, {v => set(v)}, "class"->"form-control" ))
    }
  }

  object supplies extends TextareaField(this, 1000){
    override def toForm = {
      Full(SHtml.textarea(value, {v => set(v)}, "class"->"form-control" ))
    }
  }

  object registration extends TextareaField(this, 1000){
    override def toForm = {
      Full(SHtml.textarea(value, {v => set(v)}, "class"->"form-control" ))
    }
  }

  object costContributionByUse extends ObjectIdRefField(this, CostContributionByUse)
}

object Event extends Event with RogueMetaRecord[Event]
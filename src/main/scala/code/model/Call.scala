package code.model

import code.config.Site
import code.lib.{BaseModel, Helper, RogueMetaRecord}
import code.lib.field.{BsCkTextareaField, DatePickerField, FileField, TimePickerField}
import com.foursquare.rogue.LiftRogue
import net.liftweb.common.{Box, Full}
import net.liftweb.http.SHtml
import net.liftweb.mongodb
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdPk, ObjectIdRefField, ObjectIdRefListField}
import net.liftweb.record.field.{StringField, TextareaField}
import com.foursquare.rogue._
import org.joda.time.{DateTime, DateTimeZone}

import scala.xml.Elem



class Call private () extends MongoRecord[Call] with ObjectIdPk[Call] with BaseModel[Call] {

  override def meta = Call

  def title = "Convocatoria"

  def entityListUrl = Site.backendCalls.menu.loc.calcDefaultHref

  object name extends StringField(this, 500) {
    override def displayName = "Nombre"
    override def toString = get
    override def toForm = Full(SHtml.text(
      value,
      (s: String) => set(s),
      "class" -> "form-control", "data-placeholder" -> "Ingrese nombre.."))
  }

  object description extends BsCkTextareaField(this, 1000) {
    override def displayName = "Descripción"
  }

  object file extends FileField(this) {
    override def displayName = "Subir convocatoria"
    override def toString = {
      value.fileName.get
    }
  }

  object deadline extends DatePickerField(this) {
    override def displayName = "Plazo"
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

  object values extends ObjectIdRefListField(this, Value) {
    override def displayName = "Principios"
    def currentValue = this.objs
    def availableOptions: List[(Value, String)] = Value.findAll.map(p => p -> p.name.get)

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

  override def toString = name.get
}

object Call extends Call with RogueMetaRecord[Call] {

  import mongodb.BsonDSL._

  override def collectionName = "main.calls"

  def findAllCurrent: List[Call] = {
    val now = DateTime.now
    Call.where(_.deadline after now)
      .andOpt(getAreaValue)(_.area eqs _.id.get)
      .fetch()
  }

  def getAreaValue: Option[Area] = {
    Helper.getParameter.headOption match {
      case Some((p: String, v: String)) if(p == "area") =>
        Area.where(_.name eqs v).fetch().headOption
      case _ =>
        None
    }
  }

  def findLastThreeCallByFilter(values: Box[Value], program: Box[Program], area: Box[Area], actionLine: Box[ActionLine],
                                transversalArea: Box[TransversalArea], transversalApproach: Box[TransversalApproach],
                                process: Box[Process]): List[Call] = {
    Call.or(_.whereOpt(values.toOption)(_.values contains  _.id.get),
      _.whereOpt(program.toOption)(_.program eqs _.id.get),
      _.whereOpt(area.toOption)(_.area eqs _.id.get),
      _.whereOpt(actionLine.toOption)(_.actionLines contains _.id.get),
      _.whereOpt(transversalArea.toOption)(_.transversalArea eqs _.id.get),
      _.whereOpt(transversalApproach.toOption)(_.transversalApproach eqs _.id.get),
      _.whereOpt(process.toOption)(_.process eqs _.id.get))
      .orderDesc(_.id).fetch(3)
  }
}
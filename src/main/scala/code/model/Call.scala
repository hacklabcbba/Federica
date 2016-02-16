package code.model

import code.config.Site
import code.lib.{BaseModel, RogueMetaRecord}
import code.lib.field.{DatePickerField, BsCkTextareaField, TimePickerField, FileField}
import com.foursquare.rogue.LiftRogue
import net.liftweb.common.Full
import net.liftweb.http.SHtml
import net.liftweb.mongodb
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefField, ObjectIdPk}
import net.liftweb.record.field.{StringField, TextareaField}
import com.foursquare.rogue._
import org.joda.time.{DateTime, DateTimeZone}


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

  override def toString = name.get
}

object Call extends Call with RogueMetaRecord[Call] {

  import mongodb.BsonDSL._

  override def collectionName = "main.calls"

  def findAllCurrent: List[Call] = {
    val now = DateTime.now
    Call.where(_.deadline after now).fetch()
  }
}
package code.model

import code.config.Site
import code.lib.field._
import code.lib.{BaseModel, RogueMetaRecord}
import net.liftweb.common.{Box, Full}
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.{BsonMetaRecord, BsonRecord, MongoRecord}
import net.liftweb.mongodb.record.field.{ObjectIdPk, ObjectIdRefField}
import net.liftweb.record.field._
import net.liftweb.util.FieldError

import scala.xml.{Text, Elem}


class Area private () extends MongoRecord[Area] with ObjectIdPk[Area] with BaseModel[Area] {

  override def meta = Area

  def title = "Área"

  def entityListUrl = Site.backendAreas.menu.loc.calcDefaultHref

  object name extends BsStringField(this, 500) {
    override def displayName = "Nombre"
    override def toString = get
  }

  object description extends BsCkTextareaField(this, 1000) {
    override def displayName = "Descripción"
  }

  object responsible extends ObjectIdRefField(this, User) {
    override def displayName = "Coordinador"
    override def toString = {
      this.obj.dmap("Indefinido..")(_.name.get)
    }
    override def toForm: Box[Elem] = {
      Full(
        SHtml.selectElem(
          availableOptions,
          obj,
          "class" -> "select2 form-control",
          "data-placeholder" -> "Seleccione coordinador.."
        )(s => set(s.id.get))
      )
    }

    def availableOptions = User.findAll
  }

  object email extends BsEmailField(this, 100) {
    override def displayName = "Correo eléctronico"
  }

  object phone extends BsPhoneField(this, 16) {
    override def displayName = "Teléfono"
  }

  object officeHoursBegins extends TimePickerField(this) {
    override def displayName = "Horario de atención desde"
  }

  object officeHoursEnds extends TimePickerField(this) {
    override def displayName = "Horario de atención hasta"
  }

  object code extends BsStringField(this, 50) {
    override def displayName = "Código"
    override def toString = get
  }

  object isPublished extends BooleanField(this, false) {
    override def displayName = "Publicado"
  }

  override def toString = name.get
}

object Area extends Area with RogueMetaRecord[Area] {
  override def collectionName = "main.areas"
  override def fieldOrder = List(name, responsible, email, phone, code, description, officeHoursBegins, officeHoursEnds)
}

class OfficeHours extends BsonRecord[OfficeHours] {
  def meta = OfficeHours

  object hours extends IntField(this) {
    override def displayName = "Hora"
    override def validations =
      valHour("La hora debe estar entre 0 y 23") _ ::
        super.validations
    def valHour(msg: => String)(value: Int): List[FieldError] = {
      if (value >= 0 && value < 24) Nil
      else List(FieldError(this, Text(msg)))
    }
  }

  object minutes extends IntField(this) {
    override def displayName = "Minutos"
    override def validations =
      valMinute("El minuto debe estar entre 0 y 59") _ ::
        super.validations
    def valMinute(msg: => String)(value: Int): List[FieldError] = {
      if (value >= 0 && value < 59) Nil
      else List(FieldError(this, Text(msg)))
    }
  }
}

object OfficeHours extends OfficeHours with BsonMetaRecord[OfficeHours]

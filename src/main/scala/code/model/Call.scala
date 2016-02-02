package code.model

import code.config.Site
import code.lib.{BaseModel, RogueMetaRecord}
import code.lib.field.{DatePickerField, BsCkTextareaField, TimePickerField, FileField}
import com.foursquare.rogue.LiftRogue
import net.liftweb.common.Full
import net.liftweb.http.SHtml
import net.liftweb.mongodb
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.ObjectIdPk
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
package code.model.event

import code.config.Site
import code.lib.BaseModel
import code.lib.field._
import code.model.resource._
import net.liftweb.mongodb.record.field._
import net.liftweb.mongodb.record.{BsonMetaRecord, BsonRecord, MongoMetaRecord}
import net.liftweb.record.field.{EnumNameField, StringField}

class Activity private() extends BsonRecord[Activity] with BaseModel[Activity] {

  override def meta = Activity

  def id = name.get

  def title = "Actividad"

  def entityListUrl = Site.backendPendingEvents.menu.loc.calcDefaultHref

  object kind extends EnumNameField(this, EventKind) {
    override def displayName = "Tipo de actividad"
  }
  object name extends BsStringField(this, 200) {
    override def displayName = "Nombre"
  }
  object costInfo extends BsDoubleField(this, 0) {
    override def displayName = "Costo de entrada"
  }
  object description extends BsCkTextareaField(this, 500) {
    override def displayName = "Descripción"
  }
  object image extends FileField(this) {
    override def displayName = "Imágen"
    override def toString = {
      value.fileName.get
    }
  }
  object room extends ObjectIdRefField(this, Room) {
    override def displayName = "Sala"
  }
  object quota extends BsIntField(this, 0) {
    override def displayName = "Cupo"
  }
  object date extends DatePickerField(this) {
    override def displayName = "Fecha"
  }
}

object Activity extends Activity with BsonMetaRecord[Activity] with MongoMetaRecord[Activity] {
  override def fieldOrder = List(name, kind, room, date, costInfo, quota, description, image)
}



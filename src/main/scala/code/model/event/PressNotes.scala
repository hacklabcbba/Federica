package code
package model
package event

import code.config.Site
import code.lib.field.{BsStringField, FileField}
import code.lib.{BaseModel, RogueMetaRecord}
import net.liftweb.mongodb.record.{MongoMetaRecord, BsonMetaRecord, BsonRecord, MongoRecord}
import net.liftweb.mongodb.record.field.{ObjectIdPk, ObjectIdRefListField}
import net.liftweb.record.field.StringField


class PressNotes private() extends BsonRecord[PressNotes] with BaseModel[PressNotes] {

  override def meta = PressNotes

  def id = name.get

  def title = "Nota de Prensa"

  def entityListUrl = Site.backendPendingEvents.menu.loc.calcDefaultHref

  object name extends BsStringField(this, 100) {
    override def displayName = "Nombre"
  }
  object notes extends FileField(this) {
    override def displayName = "Nota de prensa"
  }
  object image extends FileField(this) {
    override def displayName = "Foto"
  }
  object link extends BsStringField(this, 100) {
    override def displayName = "Enlace"
  }
}
object PressNotes extends PressNotes with BsonMetaRecord[PressNotes] with MongoMetaRecord[PressNotes] {
  override def fieldOrder = List(name, notes, image, link)
}
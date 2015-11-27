package code
package model
package event

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.ObjectIdPk
import net.liftweb.record.field.StringField
import net.liftweb.common.Full
import net.liftweb.http.SHtml

class EventType private() extends MongoRecord[EventType] with ObjectIdPk[EventType]{

  override def meta = EventType

  object name extends StringField(this, 500){
    override def toString = get
    override def toForm =
      Full(SHtml.text(value, set(_), "class" -> "form-control", "data-placeholder" -> "Ingrese tipo de evento.."))
  }
}

object EventType extends EventType with RogueMetaRecord[EventType] {
  override def collectionName = "event.event_types"
}

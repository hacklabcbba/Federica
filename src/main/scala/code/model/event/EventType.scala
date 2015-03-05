package code
package model
package event

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.ObjectIdPk
import net.liftweb.record.field.StringField

class EventType private() extends MongoRecord[EventType] with ObjectIdPk[EventType]{

  override def meta = EventType

  object name extends StringField(this, 200)
}

object EventType extends EventType with RogueMetaRecord[EventType]

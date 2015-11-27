package code
package model
package event

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field._
import net.liftweb.record.field.{BooleanField, StringField}

class EventRequirement private() extends MongoRecord[EventRequirement] with ObjectIdPk[EventRequirement]{

  override def meta = EventRequirement
  object title extends StringField(this, "")
  object isOptional extends BooleanField(this, true)
}

object EventRequirement extends EventRequirement with RogueMetaRecord[EventRequirement] {
  override def collectionName = "event.event_requirements"
}






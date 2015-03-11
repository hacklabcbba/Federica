package code
package model
package event

import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field._
import net.liftweb.record.field.{BooleanField, LongField, DecimalField, StringField}
import code.lib.RogueMetaRecord

class EventRequirement private() extends MongoRecord[EventRequirement] with ObjectIdPk[EventRequirement]{

  override def meta = EventRequirement
  object title extends StringField(this, "")
  object isOptional extends BooleanField(this, true)
}

object EventRequirement extends EventRequirement with RogueMetaRecord[EventRequirement]






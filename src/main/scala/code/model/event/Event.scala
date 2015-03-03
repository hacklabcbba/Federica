package code
package model
package event

import code.lib.RogueMetaRecord
import code.model.project.{Organizer, Schedule}
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefListField, ObjectIdRefField, MongoListField, ObjectIdPk}
import net.liftweb.record.field.StringField
import code.model.activity.Activity

class Event private() extends MongoRecord[Event] with ObjectIdPk[Event]{

  override def meta = Event

  object name extends StringField(this, 200)
  object description extends StringField(this, 500)
  object schedule extends MongoListField[Event, Schedule](this)
  object responsible extends ObjectIdRefField(this, Organizer)
  object process extends ObjectIdRefField(this, Process)
  object activities extends ObjectIdRefListField(this, Activity)
}

object Event extends Event with RogueMetaRecord[Event]
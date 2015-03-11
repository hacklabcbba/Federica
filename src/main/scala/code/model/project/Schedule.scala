package code
package model
package project

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefField, DateField, ObjectIdPk}
import net.liftweb.record.field.StringField

class Schedule private() extends MongoRecord[Schedule] with ObjectIdPk[Schedule]{

  override def meta = Schedule

  object begins extends DateField(this)
  object ends extends DateField(this)
  object description extends StringField(this, 500)
  object city extends ObjectIdRefField(this, City)
  object country extends ObjectIdRefField(this, Country)
}

object Schedule extends Schedule with RogueMetaRecord[Schedule]
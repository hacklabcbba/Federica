package code
package model
package project

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefField, DateField, ObjectIdPk}
import net.liftweb.record.field.StringField

class Schedule private() extends MongoRecord[Schedule] with ObjectIdPk[Schedule]{

  override def meta = Schedule

  object startDate extends DateField(this)
  object endDate extends DateField(this)
  object description extends StringField(this, 500)

}

object Schedule extends Schedule with RogueMetaRecord[Schedule]
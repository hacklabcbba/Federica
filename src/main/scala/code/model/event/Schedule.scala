package code
package model
package event

import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.record.field.BooleanField
import net.liftweb.mongodb.record.field.{ObjectIdRefListField, DateField, ObjectIdPk}
import code.lib.RogueMetaRecord
import net.liftweb.common.Empty


class Schedule private() extends MongoRecord[Schedule] with ObjectIdPk[Schedule]{

  override def meta = Schedule

  object items extends ObjectIdRefListField(this, ScheduleItem)
  object isCorrelative extends BooleanField(this, false)
  object isAtSameHour extends BooleanField(this, false)
}

object Schedule extends Schedule with RogueMetaRecord[Schedule]{

  def getLiteralDate: String = {
    ""
  }

  def getLiteralHour: String = {
    ""
  }
}







package code
package model
package event

import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.record.field.{EnumNameField, BooleanField}
import net.liftweb.mongodb.record.field.{MongoListField, ObjectIdRefListField, DateField, ObjectIdPk}
import code.lib.RogueMetaRecord
import net.liftweb.common.Empty
import org.joda.time.DateTime


class Schedule private() extends MongoRecord[Schedule] with ObjectIdPk[Schedule]{

  override def meta = Schedule

  object isAtSameHour extends BooleanField(this, false)
  object rangeType extends EnumNameField(this, RangeType)
  object dateRange extends MongoListField[Schedule, DateTime](this)
}

object Schedule extends Schedule with RogueMetaRecord[Schedule]{

  def literalDate: String = {
    ""
  }

  def literalHour: String = {
    ""
  }
}

object RangeType extends Enumeration {
  type RangeType = Value
  val ContinuousInterval, DiscontinuousInterval, SimpleDate  = Value
}
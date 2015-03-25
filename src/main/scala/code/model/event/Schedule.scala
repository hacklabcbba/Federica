package code
package model
package event

import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.record.field.{EnumNameField, BooleanField}
import net.liftweb.mongodb.record.field.{MongoListField, ObjectIdRefListField, DateField, ObjectIdPk}
import code.lib.RogueMetaRecord
import org.joda.time.DateTime


class Schedule private() extends MongoRecord[Schedule] with ObjectIdPk[Schedule]{

  override def meta = Schedule

  object isAtSameHour extends BooleanField(this, false)
  object rangeType extends EnumNameField(this, RangeType)
  object dateRange extends MongoListField[Schedule, DateTime](this)
  override def toString = {
    val dates = dateRange.get
    rangeType.get match {
      case RangeType.ContinuousInterval =>
        "De: " + dates.headOption.getOrElse("no definido")+
        " a:" + dates.lastOption.getOrElse("no definido")

      case RangeType.DiscontinuousInterval =>
        "%s={%s}" format ("Fechas: ", dates.mkString(", "))

      case RangeType.SimpleDate =>
        "Fecha: " + dates.headOption.getOrElse("no definido")
    }
  }
}

object Schedule extends Schedule with RogueMetaRecord[Schedule]{

}

object RangeType extends Enumeration {
  type RangeType = Value
  val ContinuousInterval, DiscontinuousInterval, SimpleDate  = Value
}
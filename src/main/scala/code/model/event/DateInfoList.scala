package code
package model
package event

import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.record.field.BooleanField
import net.liftweb.mongodb.record.field.{ObjectIdRefListField, DateField, ObjectIdPk}
import code.lib.RogueMetaRecord
import net.liftweb.common.Empty


class DateInfoList private() extends MongoRecord[DateInfoList] with ObjectIdPk[DateInfoList]{

  override def meta = DateInfoList

  object items extends ObjectIdRefListField(this, DateInfo)
  object isCorrelative extends BooleanField(this, false)
  object isAtSameHour extends BooleanField(this, false)
}

object DateInfoList extends DateInfoList with RogueMetaRecord[DateInfoList]{

  def getLiteralDate: String = {
    ""
  }

  def getLiteralHour: String = {
    ""
  }
}







package code.model.event

import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.record.field.BooleanField
import net.liftweb.mongodb.record.field.{ObjectIdRefListField, DateField, ObjectIdPk}
import code.lib.RogueMetaRecord


class DateInfoList private() extends MongoRecord[DateInfoList] with ObjectIdPk[DateInfoList]{

  override def meta = DateInfoList

  object itemList extends ObjectIdRefListField(this, DateInfo)
  object isCorrelative extends BooleanField(this, false)
  object isAtSameHour extends BooleanField(this, false)
}

object DateInfoList extends DateInfoList with RogueMetaRecord[DateInfoList]{

  def getLiteralDate: String = ""
  def getLiteralHour: String = ""
}







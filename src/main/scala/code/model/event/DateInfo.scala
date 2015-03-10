package code
package model
package event

import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.record.field.{BooleanField, LongField, DecimalField, StringField}
import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefField, ObjectIdRefListField, DateField, ObjectIdPk}


class DateInfo private() extends MongoRecord[DateInfo] with ObjectIdPk[DateInfo]{

  override def meta = DateInfo
  object startDate extends DateField(this)
  object endDate extends DateField(this)
  object description extends StringField(this, "")
}

object DateInfo extends DateInfo with RogueMetaRecord[DateInfo]






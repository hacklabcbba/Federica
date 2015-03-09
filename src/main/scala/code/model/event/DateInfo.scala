package code.model.event

import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.record.field.{BooleanField, LongField, DecimalField, StringField}
import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefField, ObjectIdRefListField, DateField, ObjectIdPk}
import code.model.resource.Room


class DateInfo private() extends MongoRecord[DateInfo] with ObjectIdPk[DateInfo]{

  override def meta = DateInfo
  object date extends DateField(this)
  object description extends StringField(this, "")
  object startHour extends DateField(this)
  object endHour extends DateField(this)
  object room extends ObjectIdRefField(this, Room)
}

object DateInfo extends DateInfo with RogueMetaRecord[DateInfo]






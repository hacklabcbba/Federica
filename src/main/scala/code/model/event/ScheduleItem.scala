package code
package model
package event

import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.record.field.{BooleanField, LongField, DecimalField, StringField}
import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefField, ObjectIdRefListField, DateField, ObjectIdPk}
import code.model.project.{Country, City}


class ScheduleItem private() extends MongoRecord[ScheduleItem] with ObjectIdPk[ScheduleItem]{

  override def meta = ScheduleItem
  object begins extends DateField(this)
  object ends extends DateField(this)
  object description extends StringField(this, 500)
  object city extends ObjectIdRefField(this, City)
  object country extends ObjectIdRefField(this, Country)
}

object ScheduleItem extends ScheduleItem with RogueMetaRecord[ScheduleItem]






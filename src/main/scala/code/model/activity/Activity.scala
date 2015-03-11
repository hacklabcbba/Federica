package code
package model
package activity

import code.lib.RogueMetaRecord
import code.model.resource._
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefListField, ObjectIdRefField, MongoListField, ObjectIdPk}
import net.liftweb.record.field.{DecimalField, EnumNameField, StringField}
import code.model.event.CostInfo

class Activity private() extends MongoRecord[Activity] with ObjectIdPk[Activity]{

  override def meta = Activity

  object activityType extends ObjectIdRefField(this, ActivityType)
  object name extends StringField(this, 200)
  object costInfo extends ObjectIdRefField(this, CostInfo)
  object description extends StringField(this, 500)
  object image extends StringField(this, 100)
  object rooms extends MongoListField[Activity, Room](this)
  object packages extends MongoListField[Activity, ResourcePackage](this)
}

object Activity extends Activity with RogueMetaRecord[Activity]



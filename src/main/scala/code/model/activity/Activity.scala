package code
package model
package activity

import code.lib.RogueMetaRecord
import code.model.resource._
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefField, MongoListField, ObjectIdPk}
import net.liftweb.record.field.{DecimalField, EnumNameField, StringField}

class Activity private() extends MongoRecord[Activity] with ObjectIdPk[Activity]{

  override def meta = Activity

  object activityType extends ObjectIdRefField(this, ActivityType)
  object name extends StringField(this, 200)
  object cost extends DecimalField(this, 0)
  object description extends StringField(this, 500)
  object image extends StringField(this, 100)
  object rooms extends MongoListField[Activity, Room](this)
  object packages extends MongoListField[Activity, ResourcePackage](this)
  object activity extends ObjectIdRefField(this, Activity)
}

object Activity extends Activity with RogueMetaRecord[Activity]



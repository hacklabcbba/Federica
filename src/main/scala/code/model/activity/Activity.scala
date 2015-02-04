package code
package model
package activity

import code.lib.RogueMetaRecord
import code.model.resource.{PackageResource, Environment, Resource}
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{MongoListField, ObjectIdPk}
import net.liftweb.record.field.{DecimalField, EnumNameField, StringField}

class Activity private() extends MongoRecord[Activity] with ObjectIdPk[Activity]{

  override def meta = Activity

  object activityType extends EnumNameField(this, ActivityType)
  object name extends StringField(this, 200)
  object cost extends DecimalField(this, 0)
  object description extends StringField(this, 500)
  object image extends StringField(this, 100)
  object resources extends MongoListField[Activity, Resource](this)
  object environment extends MongoListField[Activity, Environment](this)
  object packages extends MongoListField[Activity, PackageResource](this)
}

object Activity extends Activity with RogueMetaRecord[Activity]

object ActivityType extends Enumeration {

  type ActivityType = Value

  val Other = Value
}

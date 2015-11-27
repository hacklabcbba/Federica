package code
package model
package activity

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.ObjectIdPk
import net.liftweb.record.field.StringField

class ActivityType private() extends MongoRecord[ActivityType] with ObjectIdPk[ActivityType]{

  override def meta = ActivityType

  object name extends StringField(this, 200)
}

object ActivityType extends ActivityType with RogueMetaRecord[ActivityType] {
  override def collectionName = "activity.activity_types"
}

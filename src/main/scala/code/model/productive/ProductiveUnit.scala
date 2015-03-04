package code
package model
package productive

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefField, ObjectIdPk}
import net.liftweb.record.field.{BooleanField, StringField}

class ProductiveUnit private () extends MongoRecord[ProductiveUnit] with ObjectIdPk[ProductiveUnit]{

  override def meta = ProductiveUnit

  object name extends StringField(this, 500)
  object description extends StringField(this, 500)
  object administrator extends ObjectIdRefField(this, User)
}

object ProductiveUnit extends ProductiveUnit with RogueMetaRecord[ProductiveUnit]
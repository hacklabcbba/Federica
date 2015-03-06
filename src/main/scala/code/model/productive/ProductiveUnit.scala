package code
package model
package productive

import code.lib.RogueMetaRecord
import code.model.proposal.Area
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefField, ObjectIdPk}
import net.liftweb.record.field.{BooleanField, StringField}

class ProductiveUnit private () extends MongoRecord[ProductiveUnit] with ObjectIdPk[ProductiveUnit]{

  override def meta = ProductiveUnit

  object name extends StringField(this, 500)
  object description extends StringField(this, 500)
  object administrator extends ObjectIdRefField(this, User)
  object area extends ObjectIdRefField(this, Area) {
    override def optional_? = true
  }
  // when merge object program extends ObjectIdRefField(this, Program){
  //override def optional_? = true
  //}
}

object ProductiveUnit extends ProductiveUnit with RogueMetaRecord[ProductiveUnit]
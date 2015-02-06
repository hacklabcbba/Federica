package code
package model
package resource

import code.lib.RogueMetaRecord
import code.model.proposal.Area
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{MongoListField, ObjectIdPk}
import net.liftweb.record.field.StringField

class Consumer private() extends MongoRecord[Consumer] with ObjectIdPk[Consumer]{

  override def meta = Consumer

  object name extends StringField(this, 500)
  object description extends StringField(this, 500)
  object areas extends MongoListField[Consumer, Area](this)
}

object Consumer extends Consumer with RogueMetaRecord[Consumer]
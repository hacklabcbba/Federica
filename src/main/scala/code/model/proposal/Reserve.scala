package code
package model
package proposal

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{MongoListField, DateField, ObjectIdRefField, ObjectIdPk}
import net.liftweb.record.field.StringField
import code.model.resource.ConcreteResource

class Reserve private() extends MongoRecord[Reserve] with ObjectIdPk[Reserve]{

  override def meta = Reserve

  object email extends StringField(this, 500)
  object code extends StringField(this, 50)
  object user extends ObjectIdRefField(this, User)
  object proposal extends ObjectIdRefField(this, Proposal)
  object date extends DateField(this)
  object room extends MongoListField[Reserve, ConcreteResource](this)
}

object Reserve extends Reserve with RogueMetaRecord[Reserve]

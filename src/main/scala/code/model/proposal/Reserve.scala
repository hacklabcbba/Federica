package code
package model
package proposal

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{MongoListField, DateField, ObjectIdRefField, ObjectIdPk}
import net.liftweb.record.field.{BooleanField, StringField}
import code.model.resource.Environment

class Reserve private() extends MongoRecord[Reserve] with ObjectIdPk[Reserve]{

  override def meta = Reserve

  object name extends StringField(this, 500)
  object description extends StringField(this, 500)
  object email extends StringField(this, 500)
  object isWorkshop extends BooleanField(this, false)
  object code extends StringField(this, 50)

  object user extends ObjectIdRefField(this, User)
  object proposal extends ObjectIdRefField(this, Proposal)
  object date extends DateField(this)
  object environment extends MongoListField[Reserve, Environment](this)
  //uncoment when module package will be done
  //object packages extends MongoListField[Reserve, Package](this)
  //uncoment when module resource will be done
  //object packages extends MongoListField[Reserve, Resource](this)
}

object Reserve extends Reserve with RogueMetaRecord[Reserve]

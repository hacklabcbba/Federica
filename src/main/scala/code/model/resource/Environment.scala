package code
package model
package resource

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.ObjectIdPk
import net.liftweb.record.field.{BooleanField, IntField, StringField}

class Environment private() extends MongoRecord[Environment] with ObjectIdPk[Environment]{

  override def meta = Environment

  object name extends StringField(this, 500)
  object description extends StringField(this, 500)
  object capacity extends IntField(this, 0)
  object code extends StringField(this, 50)
  object state extends StringField(this, 50)
  object plane extends StringField(this, 500)
  object isReservable extends BooleanField(this, false)

}

object Environment extends Environment with RogueMetaRecord[Environment]
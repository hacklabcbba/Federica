package code
package model
package resource

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.ObjectIdPk
import net.liftweb.record.field.{BooleanField, IntField, StringField}

class Room private() extends MongoRecord[Room] with ObjectIdPk[Room]{

  override def meta = Room

  object name extends StringField(this, 500)
  object description extends StringField(this, 500)
  object capacity extends IntField(this, 0)
  object code extends StringField(this, 50)
  object state extends StringField(this, 50)
  object plane extends StringField(this, 500)
  object isReservable extends BooleanField(this, false)

}

object Room extends Room with RogueMetaRecord[Room]
package code
package model
package proposal

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.field.ObjectIdPk
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.record.field.{BooleanField, StringField}

class Area private () extends MongoRecord[Area] with ObjectIdPk[Area]{

  override def meta = Area

  object name extends StringField(this, 500)
  object description extends StringField(this, 500)
  object email extends StringField(this, 500)
  object code extends StringField(this, 50)

  override def toString = name.get
}

object Area extends Area with RogueMetaRecord[Area]

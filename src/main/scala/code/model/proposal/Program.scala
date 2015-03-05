package code
package model
package proposal

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefListField, ObjectIdRefField, ObjectIdPk}
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.record.field.{BooleanField, StringField}
import code.model.project.Process
import code.model.event.Event


class Program private () extends MongoRecord[Program] with ObjectIdPk[Program]{

  override def meta = Program

  object name extends StringField(this, 500)
  object description extends StringField(this, 500)
}

object Program extends Program with RogueMetaRecord[Program]

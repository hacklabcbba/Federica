package code
package model
package proposal

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefListField, ObjectIdRefField, ObjectIdPk}
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.record.field.StringField


class ActionLine private () extends MongoRecord[ActionLine] with ObjectIdPk[ActionLine]{

  override def meta = ActionLine

  object name extends StringField(this, 500)
  object description extends StringField(this, 500)
}

object ActionLine extends ActionLine with RogueMetaRecord[ActionLine]
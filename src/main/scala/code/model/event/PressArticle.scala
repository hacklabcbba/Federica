package code
package model
package event

import net.liftweb.mongodb.record.field.{ObjectIdRefListField, ObjectIdPk}
import code.lib.RogueMetaRecord
import net.liftweb.record.field.{BinaryField, StringField, BooleanField}
import net.liftweb.mongodb.record.MongoRecord


class PressArticle private() extends MongoRecord[PressArticle] with ObjectIdPk[PressArticle]{

  override def meta = PressArticle
  object title extends StringField(this, "")
  object file extends BinaryField(this)
  object notes extends StringField(this, "")
}

object PressArticle extends PressArticle with RogueMetaRecord[PressArticle]{
}









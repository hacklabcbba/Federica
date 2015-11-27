package code
package model
package event

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.ObjectIdPk
import net.liftweb.record.field.{BinaryField, StringField}


class PressArticle private() extends MongoRecord[PressArticle] with ObjectIdPk[PressArticle]{

  override def meta = PressArticle
  object title extends StringField(this, "")
  object file extends BinaryField(this)
  object notes extends StringField(this, "")
}

object PressArticle extends PressArticle with RogueMetaRecord[PressArticle]{
  override def collectionName = "event.press_articless"
}









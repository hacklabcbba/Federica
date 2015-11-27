package code
package model
package event

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdPk, ObjectIdRefListField}
import net.liftweb.record.field.StringField


class PressNotes private() extends MongoRecord[PressNotes] with ObjectIdPk[PressNotes]{

  override def meta = PressNotes

  object title extends StringField(this, "")
  object article extends ObjectIdRefListField(this, PressArticle)
}
object PressNotes extends PressNotes with RogueMetaRecord[PressNotes]{
  override def collectionName = "event.press_notes"
}
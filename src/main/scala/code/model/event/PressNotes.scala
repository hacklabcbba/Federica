package code
package model
package event

import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.record.field.{StringField, BooleanField}
import net.liftweb.mongodb.record.field.{ObjectIdRefListField, ObjectIdPk}
import code.lib.RogueMetaRecord


class PressNotes private() extends MongoRecord[PressNotes] with ObjectIdPk[PressNotes]{

  override def meta = PressNotes

  object title extends StringField(this, "")
  object article extends ObjectIdRefListField(this, PressArticle)
}
object PressNotes extends PressNotes with RogueMetaRecord[PressNotes]{
}
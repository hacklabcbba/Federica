package code.model.event

import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.record.field.{StringField, BooleanField}
import net.liftweb.mongodb.record.field.{ObjectIdRefListField, ObjectIdPk}
import code.lib.RogueMetaRecord


class Gallery private() extends MongoRecord[Gallery] with ObjectIdPk[Gallery]{

  override def meta = Gallery

  object title extends StringField(this, "")
  object itemList extends ObjectIdRefListField(this, GalleryItem)
}
object Gallery extends Gallery with RogueMetaRecord[Gallery]{
}
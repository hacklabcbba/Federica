package code
package model
package event

import net.liftweb.mongodb.record.field.{ObjectIdRefListField, ObjectIdPk}
import code.lib.RogueMetaRecord
import net.liftweb.record.field.{BinaryField, StringField, BooleanField}
import net.liftweb.mongodb.record.MongoRecord


class GalleryItem private() extends MongoRecord[GalleryItem] with ObjectIdPk[GalleryItem]{

  override def meta = GalleryItem
  object title extends StringField(this, "")
  object file extends BinaryField(this)
  object notes extends StringField(this, "")
}

object GalleryItem extends GalleryItem with RogueMetaRecord[GalleryItem]{
}









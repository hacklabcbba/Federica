package code.model

import net.liftweb.mongodb.record.{BsonMetaRecord, BsonRecord}
import net.liftweb.record.field.StringField

class Tag private() extends BsonRecord[Tag] {
  override def meta = Tag

  object tag extends StringField(this, 128)
}

object Tag extends Tag with BsonMetaRecord[Tag]

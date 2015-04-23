package code.model

import net.liftweb.mongodb.record._
import net.liftweb.mongodb.record.field._
import net.liftweb.record.field._


class FileRecord private () extends BsonRecord[FileRecord] {
  def meta = FileRecord
  
  object fileId extends StringField(this, 50)
  object fileName extends StringField(this, 50)
  object fileType extends StringField(this, 50)
  object fileSize extends LongField(this)
  object creationDate extends DateField(this)
}

object FileRecord extends FileRecord with BsonMetaRecord[FileRecord]

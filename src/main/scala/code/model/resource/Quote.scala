package code
package model
package resource

import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefField, ObjectIdPk}
import net.liftweb.record.field.{DecimalField, StringField}

trait Quote[T <: MongoRecord[T]] extends MongoRecord[T] with ObjectIdPk[T] {
  this: T =>

  object characteristics extends StringField(this.asInstanceOf[T], 256)
  object parameter extends StringField(this.asInstanceOf[T], 256)
  object cost extends DecimalField(this.asInstanceOf[T], 0)
  object consumer extends ObjectIdRefField(this.asInstanceOf[T], Consumer)
}
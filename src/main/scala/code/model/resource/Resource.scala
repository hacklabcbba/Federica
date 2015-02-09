package code
package model
package resource

import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefField, ObjectIdPk}
import net.liftweb.record.field.StringField

trait Resource[T <: MongoRecord[T]] extends MongoRecord[T] with ObjectIdPk[T] {
  this: T =>

  object name extends StringField(this.asInstanceOf[T], 500)
  object description extends StringField(this.asInstanceOf[T], 500)
  object cost extends ObjectIdRefField(this.asInstanceOf[T], ConcreteQuote)

}
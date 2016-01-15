package code
package lib

import net.liftmodules.extras.SnippetHelper
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.record.field.{LongField, DateTimeField, IntField}

trait SortableModel[T <: MongoRecord[T]] extends MongoRecord[T] with SnippetHelper {

  this: T =>

  object order extends LongField(this.asInstanceOf[T], defaultSortValue) {
    override def shouldDisplay_? = false
  }

  def defaultSortValue: Long = meta.count - 1

}

package code
package lib

import code.lib.field.BsStringField
import net.liftmodules.extras.SnippetHelper
import net.liftweb.mongodb.record.{BsonRecord, MongoRecord}

trait WithUrl[T <: BsonRecord[T]] extends BsonRecord[T] with SnippetHelper {

  this: T =>

  object url extends BsStringField(this.asInstanceOf[T], 500) {
    override def displayName = "Url"
    override def toString: String = urlString
  }

  def urlString: String

}

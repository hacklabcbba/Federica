package code
package lib.field


import net.liftmodules.extras.SnippetHelper
import net.liftweb.common._
import net.liftweb.http.S
import net.liftweb.http.S._
import net.liftweb.mongodb.record.BsonRecord
import net.liftweb.mongodb.record.field.MongoListField
import net.liftweb.util.Helpers._

import scala.xml._

class TagsField[OwnerType <: BsonRecord[OwnerType]](rec: OwnerType) extends MongoListField[OwnerType, String](rec) with SnippetHelper {
  private def elem = {
    val fieldId: String = uniqueFieldId.openOr(randomString(12))
    S.fmapFunc(SFuncHolder(s => {
      this.set(s.split(",").map(_.trim).filter(_.length > 0).toList)
    })) {
      funcName =>
        <input type="text" id={fieldId} tabindex={tabIndex.toString} class="form-control" value={valueBox.dmap("")(_.mkString(","))} />
    }
  }

  override def toForm: Box[NodeSeq] = Full(elem)

  override def setFilter = toLowerCase _ :: distinct _ :: super.setFilter

  private def distinct(list: List[String]) =  list.distinct

  private def toLowerCase(list: List[String]) = list.map(_.toLowerCase)

  def removeTag(tag: String) = set(get.filterNot(_ == tag))

  def addTag(tag: String) = set((tag :: get).distinct)

  override def displayName = "Etiquetas"

}


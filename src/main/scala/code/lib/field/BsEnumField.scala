package code
package lib
package field

import net.liftweb.record._
import net.liftweb.record.field._
import net.liftmodules.extras.SnippetHelper
import net.liftweb.common._
import scala.xml.NodeSeq
import net.liftweb.util.Helpers._
import net.liftweb.http.{SHtml, S}
import net.liftweb.http.js.JsCmds.Noop
import scala.xml.Text
import reflect.Manifest

trait BsEnumTypedField[EnumType <: Enumeration] extends EnumTypedField[EnumType] with SnippetHelper {
  def isAutoFocus: Boolean = false

  def isEditable = true

  private def elem = {
    SHtml.selectObj[Box[EnumType#Value]](buildDisplayList, Full(valueBox), setBox(_), "tabindex" -> tabIndex.toString, "class" -> "form-control")
  }


  override def toForm: Box[NodeSeq] = Full(elem)
}

class BsEnumField[OwnerType <: Record[OwnerType], EnumType <: Enumeration]
  (rec: OwnerType, override protected val enum: EnumType)(implicit m: Manifest[EnumType#Value])
  extends Field[EnumType#Value, OwnerType] with MandatoryTypedField[EnumType#Value] with BsEnumTypedField[EnumType] {
  def this(rec: OwnerType, enum: EnumType, value: EnumType#Value)(implicit m: Manifest[EnumType#Value]) = {
    this(rec, enum)
    set(value)
  }

  def owner = rec
  protected val valueManifest = m
}

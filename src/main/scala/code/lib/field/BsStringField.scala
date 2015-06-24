package code
package lib
package field

import net.liftweb.record._
import net.liftweb.record.field.{StringField, OptionalStringField, StringTypedField}
import net.liftmodules.extras.SnippetHelper
import net.liftweb.common._
import scala.xml.NodeSeq
import net.liftweb.util.Helpers._
import net.liftweb.http.{SHtml, S}
import net.liftweb.http.js.JsCmds.Noop
import scala.xml.Text

trait BsStringTypedField extends StringTypedField with SnippetHelper {
  def isAutoFocus: Boolean = false

  def isEditable = true

  private def elem = {
    val fieldId: String = uniqueFieldId.openOr(randomString(12))

    S.fmapFunc(S.SFuncHolder(this.setFromAny(_))) {
      funcName =>
          <input type={formInputType} maxlength={maxLength.toString}
                 id={fieldId}
                 name={funcName}
                 value={valueBox openOr ""}
                 tabindex={tabIndex.toString}
                 class="form-control" /> % autofocus(isAutoFocus)
    }
  }

  def asHtmlCutted(size: Int) = {
    (get.toString.size > size)  match {
      case true => Text(get.toString.take(size) + "...")
      case _ => Text(get.toString.take(size))
    }
  }

  private def readOnlyField = <div class="form-control-static">{asHtml}</div>

  override def toForm: Box[NodeSeq] = if (isEditable) Full(elem) else Full(readOnlyField)

  def toAjaxForm: Box[NodeSeq] = Full(
    SHtml.ajaxText(valueBox openOr "", (s: String) => {
      setFromString(s)
      Noop
    }, "class" -> "form-control", "tabindex" -> tabIndex.toString)
  )
}

class BsStringField[OwnerType <: Record[OwnerType]](rec: OwnerType, maxLength: Int)
  extends StringField(rec, maxLength) with BsStringTypedField

class OptionalBsStringField[OwnerType <: Record[OwnerType]](rec: OwnerType, maxLength: Int)
  extends OptionalStringField(rec, maxLength) with BsStringTypedField
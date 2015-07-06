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
import net.liftweb.http.js.JsCmds.{Run, Noop}
import scala.xml.Text

trait BsPhoneTypedField extends StringTypedField with SnippetHelper {
  def isAutoFocus: Boolean = false

  def isEditable = true

  private def elem = {
    val fieldId: String = uniqueFieldId.openOr(randomString(12))
    val js = Run("$('#" + fieldId + "').inputmask('999-99999999');")

    S.appendJs(js)

    S.fmapFunc(S.SFuncHolder(this.setFromAny(_))) {
      funcName =>
        <div class="input-group">
          <div class="input-group-addon">
            <i class="fa fa-phone"></i>
          </div>
          {<input type={formInputType} maxlength={maxLength.toString}
                  id={fieldId}
                  name={funcName}
                  value={valueBox openOr ""}
                  tabindex={tabIndex.toString}
                  data-inputmask="'mask': '(999) 9999999'" data-mask=""
                  class="form-control" /> % autofocus(isAutoFocus)
          }
        </div>
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

}

class BsPhoneField[OwnerType <: Record[OwnerType]](rec: OwnerType, maxLength: Int)
  extends StringField(rec, maxLength) with BsPhoneTypedField

class OptionalBsPhoneField[OwnerType <: Record[OwnerType]](rec: OwnerType, maxLength: Int)
  extends OptionalStringField(rec, maxLength) with BsPhoneTypedField
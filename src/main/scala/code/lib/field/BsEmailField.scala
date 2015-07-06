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

trait BsEmailTypedField extends EmailTypedField with SnippetHelper {
  def isAutoFocus: Boolean = false

  def isEditable = true

  private def elem = {
    val fieldId: String = uniqueFieldId.openOr(randomString(12))

    S.fmapFunc(S.SFuncHolder(this.setFromAny(_))) {
      funcName =>
        <div class="input-group">
          <span class="input-group-addon"><i class="fa fa-envelope"></i></span>
          {<input type="email"
                 id={fieldId}
                 name={funcName}
                 value={valueBox openOr ""}
                 tabindex={tabIndex.toString}
                 class="form-control" /> % autofocus(isAutoFocus)}
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

class BsEmailField[OwnerType <: Record[OwnerType]](rec: OwnerType, maxLength: Int)
  extends EmailField(rec, maxLength) with BsEmailTypedField

class OptionalBsEmailField[OwnerType <: Record[OwnerType]](rec: OwnerType, maxLength: Int)
  extends OptionalEmailField(rec, maxLength) with BsEmailTypedField
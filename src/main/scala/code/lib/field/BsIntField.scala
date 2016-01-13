package code.lib.field

import net.liftweb.record._
import net.liftweb.record.field._
import net.liftmodules.extras.SnippetHelper
import net.liftweb.common._
import scala.xml.NodeSeq
import net.liftweb.util.Helpers._
import net.liftweb.http.{SHtml, S}
import net.liftweb.http.js.JsCmds.Noop
import scala.xml.Text

trait BsIntTypedField extends IntTypedField with SnippetHelper {
  def isAutoFocus: Boolean = false

  def isEditable = true

  private def elem = S.fmapFunc((s: List[String]) => setFromAny(s)) {
    funcName => <input type={formInputType} name={funcName} value={valueBox.map(_.toString) openOr ""} tabindex={tabIndex toString} class="form-control" />
  }


  private def readOnlyField = <div class="form-control-static">{asHtml}</div>

  override def toForm: Box[NodeSeq] = if (isEditable) Full(elem) else Full(readOnlyField)
}

class BsIntField[OwnerType <: Record[OwnerType]](rec: OwnerType, value: Int)
  extends IntField(rec, value) with BsIntTypedField {
}

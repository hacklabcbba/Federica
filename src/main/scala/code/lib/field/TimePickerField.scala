package code.lib.field

import code.model.OfficeHours
import net.liftweb.mongodb.record.BsonRecord
import net.liftweb.mongodb.record.field.BsonRecordField
import net.liftweb.record._
import net.liftweb.record.field.{StringField, OptionalStringField, StringTypedField}
import net.liftmodules.extras.SnippetHelper
import net.liftweb.common._
import scala.xml.NodeSeq
import net.liftweb.util.Helpers._
import net.liftweb.http.{SHtml, S}
import net.liftweb.http.js.JsCmds.{Run, Noop}
import scala.xml.Text


class TimePickerField[OwnerType <: BsonRecord[OwnerType]](rec: OwnerType)
  extends BsonRecordField(rec, OfficeHours) with SnippetHelper {

  def isAutoFocus: Boolean = false

  def setFromTimePicker(in: String) = {
    println("TIMEPICKER:"+in)
  }

  override def toString = valueBox.dmap("08:00")(s => s"${s.hours.get}:${s.minutes.get}")

  private def elem = {
    val fieldId: String = uniqueFieldId.openOr(randomString(12))
    val js = Run(
      "$('#" + fieldId + "').timepicker();" ++ s"$$('#$fieldId').on('click', function(ev) { $$('#$fieldId').timepicker('showWidget')});")

    S.appendJs(js)
    S.fmapFunc(S.SFuncHolder(this.setFromTimePicker(_))) {
      funcName =>
        <div class="input-group">
          {<input type={formInputType}
                  id={fieldId}
                  name={funcName}
                  value={toString}
                  tabindex={tabIndex.toString}
                  class="form-control timepicker" /> % autofocus(isAutoFocus)
          }
          <div class="input-group-addon">
            <i class="fa fa-clock-o"></i>
          </div>
        </div>
    }
  }

  override def toForm = Full(elem)

}
package code
package lib
package field


import java.text.SimpleDateFormat
import java.util.{Date, Locale}

import net.liftweb.common._
import net.liftweb.http.S
import net.liftweb.http.js.JsCmds._
import net.liftweb.json._
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.DateField
import net.liftweb.util.Helpers._
import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTimeField => JDateTimeField, DateTime}

import scala.xml._

class DatePickerField[OwnerType <: MongoRecord[OwnerType]](rec: OwnerType) extends DateField(rec) with Loggable {
  private val dateString = "MM/dd/yyyy"

  private val dateStringLiteral = "EEEE d 'de' MMMM 'de' yyyy"

  private val dateStringLiteralAndHour = "EEEE d 'de' MMMM 'de' yyyy - HH:mm"

  private def parseDate(s: String): Box[Date] = {
    val formatter = DateTimeFormat.forPattern(dateString)
    val dt = tryo( formatter.parseDateTime( s ).toDate)
    dt
  }

  private def formatDate(d: Date): String = {
    logger.info("formatDate " + d)
    val fmt = new SimpleDateFormat(dateString, Locale.US)
    fmt.format(d)
  }

  private def formatDateLiteral(d: Date, locale: Locale): String = {
    logger.info("formatDate " + d + S.locale)
    val fmt = new SimpleDateFormat(dateStringLiteral, locale)
    fmt.format(d)
  }

  private def formateDateLiteralWithHour(d: Date, locale: Locale) : String = {
    logger.info("formatDate " + d + S.locale)
    val fmt = new SimpleDateFormat(dateStringLiteralAndHour, locale)
    fmt.format(d)
  }

  private def elem: NodeSeq = {
    val dateButtonId: String = randomString(12)
    val dateId: String = uniqueFieldId.map(id => s"${id}_date").openOr(randomString(12))
    val date: NodeSeq = {


      S.fmapFunc((s: String) => setBox(parseDate(s))) { funcName =>
        <input
        class="form-control"
        id={dateId}
        type="text"
        name={funcName}
        value={valueBox.map(v => formatDate(v)).openOr("")}
        tabindex={tabIndex toString}
        readonly=""
        />
      }
    }

    val js = Run(
      "$(function(){" +
        "$('#" + dateId + "').datepicker().on('changeDate', function(ev) {" +
        "$('#"+ dateId + "').datepicker('hide')" +
        "});" +
      "})"
    )

    S.appendJs(js)


    date
  }

  override def toForm: Box[NodeSeq] = Full(elem)

  override def asJValue: JValue = JInt(value.getTime)

  private def viewElem: NodeSeq = {
    val dateButtonId: String = randomString(12)
    val date: NodeSeq = {
      <div class="input-group date">
        {valueBox.map(v => formatDate(v)).openOr("")}
        <span class="input-group-btn">
          <button id={dateButtonId} class="btn btn-default" type="button"><i class="fa fa-calendar"></i></button>
        </span>
      </div>
    }

    <div class="row col-sm-12">
      <div class="col-sm-7">{date}</div>
    </div>

  }

  override def asHtml = viewElem

  def formatedDate(locale: Locale) = formatDateLiteral(this.value, locale)

  def formatedDateLiteralWithHour(locale: Locale) = formateDateLiteralWithHour(this.value, locale)

  override def toString = formatDateLiteral(this.value, new Locale("es", "ES"))

}

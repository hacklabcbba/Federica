package code.lib.field

import net.liftweb.record._
import field.{TextareaTypedField, TextareaField, OptionalTextareaField}
import net.liftmodules.extras.SnippetHelper
import net.liftweb.common._
import scala.xml.NodeSeq
import net.liftweb.util.Helpers._
import net.liftweb.http.S
import net.liftweb.http.js.JsCmds.{Script, Run}
import net.liftweb.util.{PCDataXmlParser, Html5}
import scala.xml.Text

trait BsTextareaTypedField extends TextareaTypedField with SnippetHelper {
  def isAutoFocus: Boolean = false

  def isEditable = true

  private def elem = {
    val fieldId: String = uniqueFieldId.openOr(randomString(12))

    S.fmapFunc(S.SFuncHolder(this.setFromAny(_))) {
      funcName =>
      <textarea name={funcName}
        rows={textareaRows.toString}
        cols={textareaCols.toString}
        tabindex={tabIndex.toString}
        class="form-control">{valueBox openOr ""}</textarea> % autofocus(isAutoFocus)
    }
  }

  private def readOnlyField = <div class="form-control-static">{asHtml}</div>

  override def toForm: Box[NodeSeq] = if (isEditable) Full(elem) else Full(readOnlyField)

  override def asHtml = {
    valueBox.dmap(NodeSeq.Empty)(Html5.parse(_) openOr NodeSeq.Empty)
  }
}

class BsTextareaField[OwnerType <: Record[OwnerType]](rec: OwnerType, maxLength: Int)
  extends TextareaField(rec, maxLength) with BsTextareaTypedField {

    override def asHtml = {
      valueBox.dmap(NodeSeq.Empty)(v => Text(v))
    }
  }

class OptionalBsTextareaField[OwnerType <: Record[OwnerType]](rec: OwnerType, maxLength: Int)
  extends OptionalTextareaField(rec, maxLength) with BsTextareaTypedField {

  override def asHtml = {
    PCDataXmlParser(value.getOrElse("")) match {
      case node@Full(_) => {
        node
      }
      case _ => {
        PCDataXmlParser(s"<div>${value.getOrElse("")}</div>")}
    }
  }

  def asHtmlCutted(size: Int) = {
    (get.toString.size > size)  match {
      case true => Text(get.toString.take(size) + "...")
      case _ => Text(get.toString.take(size))
    }
  }

}

class BsCkTextareaField[OwnerType <: Record[OwnerType]](rec: OwnerType, maxLength: Int)
  extends TextareaField(rec, maxLength) with BsTextareaTypedField {

  private def elem = {
    val fieldId: String = randomString(12)

    S.fmapFunc(S.SFuncHolder(this.setFromAny(_))) {
      funcName =>

        <textarea name={funcName}
        rows={textareaRows.toString}
        cols={textareaCols.toString}
        tabindex={tabIndex.toString}
        id={fieldId}
        class="form-control">{valueBox openOr ""}</textarea> % autofocus(isAutoFocus)  ++ script(fieldId)
        //S.appendJs(script(fieldId))
    }
  }

  override def asHtml = {
    PCDataXmlParser(value) match {
      case node@Full(_) => node
      case _ => PCDataXmlParser(s"<div>$value</div>")
    }
  }

  def asHtmlCutted(size: Int) = {

    def value(s: String) = (s.size > size)  match {
      case true => s.take(size) + "..."
      case _ => s
    }
    PCDataXmlParser(get) match {
      case node@Full(_) => {
        node.map(s => <div>{value(s.text)}</div>)
      }
      case _ => PCDataXmlParser(s"<div>${get}</div>").map(s => <div>{value(s.text)}</div>)
    }
  }

  private def readOnlyField = <div class="form-control-static">{asHtml}</div>

  override def toForm: Box[NodeSeq] = if (isEditable) Full(this.elem) else Full(readOnlyField)

  override def asJValue = super.asJValue

  private def script(id: String) = Script(
    Run (
      """
        $(function() {
          CKEDITOR.replace('""" + id + """');
          for (var i in CKEDITOR.instances) {
            CKEDITOR.instances[i].on('blur', function() {
              CKEDITOR.instances[i].updateElement();// to update the textarea
            });
            CKEDITOR.instances[i].on('keyup', function() {
              CKEDITOR.instances[i].updateElement();// to update the textarea
            });
            CKEDITOR.instances[i].on('paste', function() {
              CKEDITOR.instances[i].updateElement();// to update the textarea
            });
            CKEDITOR.instances[i].on('change', function() {
              CKEDITOR.instances[i].updateElement();// to update the textarea
            });
            CKEDITOR.instances[i].on('keypress', function() {
              CKEDITOR.instances[i].updateElement();// to update the textarea
            });
            CKEDITOR.instances[i].on('keydown', function() {
              CKEDITOR.instances[i].updateElement();// to update the textarea
            });
          };
        });""".stripMargin
    )
  )
}


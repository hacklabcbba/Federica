package code
package model
package resource

import code.lib.field.{BsTextareaField, BsStringField}
import net.liftweb.common.Full
import net.liftweb.http.{S, SHtml}
import net.liftweb.http.js.JsCmds._
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefField, ObjectIdPk}

import scala.xml.Text

trait Resource[T <: MongoRecord[T]] extends MongoRecord[T] with ObjectIdPk[T] {
  this: T =>

  val name = new BsStringField[T](this, 500) {
    override def toString = get
    override def isAutoFocus = false
    override def displayName: String = "Name"
    override val fieldId = Some(Text("name"))
    override def validations = valMinLen(2, "longitud minima") _ :: super.validations
    override def toForm = Full(SHtml.ajaxText(value, (s: String) => {
      set(s)
      Noop
    }, "class" -> "form-control", "data-placeholder" -> "Ingrese descripcion.."))
  }

  val description = new BsTextareaField[T](this, 5000) {
    override def displayName = S ? "Descripcion"
    override def validations: List[ValidationFunction] =  valMinLen(1, S.?("comment.must.not.be.empty")) _ ::
      super.validations
  }

  val cost = new BsStringField[T](this, 10) {
    override def toString = get
    override def isAutoFocus = false
    override def displayName: String = "Name"
    override val fieldId = Some(Text("cost"))
    override def validations = valMinLen(1, "longitud minima") _ :: super.validations
    override def toForm = Full(SHtml.ajaxText(value, (s: String) => {
      set(s)
      Noop
    }, "class" -> "form-control", "data-placeholder" -> "Ingrese descripcion.."))
  }


}
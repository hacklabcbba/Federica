package code
package model
package resource

import code.lib.RogueMetaRecord
import net.liftweb.common.Full
import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmds._
import net.liftweb.record.field.{TextareaField, StringField}
import code.lib.field._
import xml.{Elem, NodeSeq, Text}

class ConcreteResource private() extends Resource[ConcreteResource] {

  override def meta = ConcreteResource

  /*object name extends StringField(this, 200) {
    override def toString = get
    override def toForm = Full(SHtml.ajaxText(value, (s: String) => {
      set(s)
      Noop
    }, "class" -> "form-control", "data-placeholder" -> "Ingrese nombre.."))
  }*/

  /*
  object description extends TextareaField(this, 1000) {
    override def toForm = {
      Full(SHtml.textarea(value, v => set(v), "class"->"form-control" ))
    }
  }*/

  protected class name(obj: ConcreteResource, size: Int) extends BsStringField(obj, size) {
    override def toString = get
    override def isAutoFocus = false
    override def displayName: String = owner.firstNameDisplayName
    override val fieldId = Some(Text("name"))
    override def validations = valMinLen(2, "longitud minima") _ :: super.validations
    override def toForm = Full(SHtml.ajaxText(value, (s: String) => {
      set(s)
      Noop
    }, "class" -> "form-control", "data-placeholder" -> "Ingrese nombre.."))
  }

  def firstNameDisplayName: String = "Nombre"
}

object ConcreteResource extends ConcreteResource with RogueMetaRecord[ConcreteResource] {

  override def collectionName = "resource"
}

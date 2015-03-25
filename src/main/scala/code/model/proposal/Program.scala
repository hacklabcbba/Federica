package code
package model
package proposal

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefListField, ObjectIdRefField, ObjectIdPk}
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.record.field.{TextareaField, BooleanField, StringField}
import code.model.project.Process
import code.model.event.Event
import net.liftweb.common.Full
import net.liftweb.http.SHtml


class Program private () extends MongoRecord[Program] with ObjectIdPk[Program]{

  override def meta = Program

  object name extends StringField(this, 500){
    override def toString = get
    override def toForm = Full(SHtml.text(
      value,
      (s: String) => set(s),
      "class" -> "form-control", "data-placeholder" -> "Ingrese nombre.."))
  }

  object description extends TextareaField(this, 1000) {
    override def toForm = {
      Full(SHtml.textarea(value, v => set(v), "class"->"form-control", "data-placeholder" -> "Ingrese descripcion.." ))
    }
  }

  override def toString = name.get
}

object Program extends Program with RogueMetaRecord[Program]

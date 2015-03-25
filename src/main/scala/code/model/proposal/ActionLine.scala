package code
package model
package proposal

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefListField, ObjectIdRefField, ObjectIdPk}
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.record.field.{TextareaField, StringField}
import net.liftweb.common.Full
import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmds._
import net.liftweb.common.Full


class ActionLine private () extends MongoRecord[ActionLine] with ObjectIdPk[ActionLine]{

  override def meta = ActionLine

  object name extends StringField(this, 500){
    override def toString = get
    override def toForm = Full(SHtml.ajaxText(value, (s: String) => {
      set(s)
      Noop
    }, "class" -> "form-control", "data-placeholder" -> "Ingrese nombre.."))
  }

  object description extends TextareaField(this, 1000) {
    override def toForm = {
      Full(SHtml.textarea(value, v => set(v), "class"->"form-control", "data-placeholder" -> "Ingrese descripcion.." ))
    }
  }
}

object ActionLine extends ActionLine with RogueMetaRecord[ActionLine]
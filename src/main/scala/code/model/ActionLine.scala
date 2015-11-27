package code.model

import code.lib.RogueMetaRecord
import net.liftweb.common.Full
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.ObjectIdPk
import net.liftweb.record.field.{StringField, TextareaField}


class ActionLine private () extends MongoRecord[ActionLine] with ObjectIdPk[ActionLine]{

  override def meta = ActionLine

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

object ActionLine extends ActionLine with RogueMetaRecord[ActionLine] {
  override def collectionName = "main.action_lines"
}
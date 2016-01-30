package code.model

import code.lib.{SortableModel, RogueMetaRecord}
import net.liftweb.common.Full
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.ObjectIdPk
import net.liftweb.record.field.{StringField, TextareaField}


class Value private () extends MongoRecord[Value] with ObjectIdPk[Value] with SortableModel[Value]{

  override def meta = Value

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

  //ToDo mapa de area -> descripcion de como interactura, programa -> descripcion, area de apoyo -> descripcion

  override def toString = name.get
}

object Value extends Value with RogueMetaRecord[Value] {
  override def collectionName = "main.values"
}
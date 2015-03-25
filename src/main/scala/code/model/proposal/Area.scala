package code
package model
package proposal

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefField, MongoRefField, ObjectIdPk}
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.record.field.{EmailField, TextareaField, StringField}
import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmds._
import net.liftweb.common.{Box, Full}
import scala.xml.Elem


class Area private () extends MongoRecord[Area] with ObjectIdPk[Area]{

  override def meta = Area

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

  object responsible extends ObjectIdRefField(this, User) {
    override def toString = {
      this.obj.dmap("Indefinido..")(_.name.get)
    }
    override def defaultValue = User.currentUser.getOrElse(User.createRecord).id.get
    override def toForm: Box[Elem] = {
      Full(SHtml.selectObj(availableOptions, currentValue, (u: User) =>
        set(u.id.get),
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione responsable.."
      ))
    }
    def currentValue = this.obj
    def availableOptions = User.findAll.map(p => p -> p.name.get)
  }

  object code extends StringField(this, 50){
    override def toString = get
    override def toForm = Full(SHtml.ajaxText(value, (s: String) => {
      set(s)
      Noop
    }, "class" -> "form-control", "data-placeholder" -> "Ingrese nombre.."))
  }

  override def toString = name.get
}

object Area extends Area with RogueMetaRecord[Area]

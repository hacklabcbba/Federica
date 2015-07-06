package code.model

import code.lib.RogueMetaRecord
import code.lib.field.{BsStringField, BsCkTextareaField}
import net.liftweb.common.{Box, Full}
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdPk, ObjectIdRefField}
import net.liftweb.record.field.{EnumNameField, StringField, TextareaField}

class Project private () extends MongoRecord[Project] with ObjectIdPk[Project] {

  override def meta = Project

  object name extends BsStringField(this, 500) {
    override def toForm = Full(SHtml.text(value, set(_), "class" -> "form-control"))
  }

  object goal extends BsCkTextareaField(this, 1000) {
    override def toForm = {
      Full(SHtml.textarea(value, v => set(v), "class"->"form-control" ))
    }
  }

  object description extends BsCkTextareaField(this, 1000) {
    override def toForm = {
      Full(SHtml.textarea(value, v => set(v), "class"->"form-control" ))
    }
  }

  object administrator extends ObjectIdRefField(this, User) {
    override def toString = {
      obj.dmap("")(_.name.get)
    }
    val listUsers = User.findAll
    override def toForm = {
      Full(SHtml.selectElem(
        listUsers,
        User.currentUser,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione administrador.."
      )(u => set(u.id.get)))
    }
  }

  object area extends ObjectIdRefField(this, Area) {
    override def optional_? = true
    override def toString = obj.dmap("")(_.name.get)
    val listAreas = Area.findAll
    override def toForm = {
      Full(SHtml.selectElem(
        listAreas,
        obj,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione area.."
      )(a => set(a.id.get)))
    }
  }

  object program extends ObjectIdRefField(this, Program){
    override def optional_? = true
    override def toString = obj.dmap("")(_.name.get)
    val listProgram = Program.findAll
    override def toForm = {
      Full(SHtml.selectElem(
        listProgram,
        obj,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione program.."
      )(s => set(s.id.get)))
    }
  }

  object history extends BsCkTextareaField(this, 1000) {
    override def toForm = {
      Full(SHtml.textarea(value, v => set(v), "class"->"form-control" ))
    }
  }
}

object Project extends Project with RogueMetaRecord[Project]

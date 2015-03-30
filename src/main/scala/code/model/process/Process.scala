package code
package model
package process

import code.lib.RogueMetaRecord
import code.model.proposal.{Program, Area}
import net.liftweb.common.{Box, Full}
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefField, ObjectIdPk}
import net.liftweb.record.field.{TextareaField, EnumNameField, StringField}

class Process private () extends MongoRecord[Process] with ObjectIdPk[Process] {

  override def meta = Process

  object name extends StringField(this, 500) {
    override def toForm = Full(SHtml.text(value, set(_), "class" -> "form-control"))
  }

  object goal extends TextareaField(this, 1000) {
    override def toForm = {
      Full(SHtml.textarea(value, v => set(v), "class"->"form-control" ))
    }
  }

  object description extends TextareaField(this, 1000) {
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

  object processType extends EnumNameField(this, ProcessType) {
    override def toForm =
      Full(SHtml.selectObj[Box[ProcessType.Value]](
        buildDisplayList,
        Full(valueBox),
        s => setBox(s),
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione tipo.."
      ))
  }

  object history extends TextareaField(this, 1000) {
    override def toForm = {
      Full(SHtml.textarea(value, v => set(v), "class"->"form-control" ))
    }
  }
}

object Process extends Process with RogueMetaRecord[Process]

object ProcessType extends Enumeration {
  type ProcessType = Value
  val Intern = Value("Interno")
  val Associate = Value("Asociado")
}
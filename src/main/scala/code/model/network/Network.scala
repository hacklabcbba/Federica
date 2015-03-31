package code
package model
package network

import code.lib.RogueMetaRecord
import code.model.proposal.{Program, Area}
import net.liftweb.common.{Box, Full}
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefField, ObjectIdPk}
import net.liftweb.record.field.{TextareaField, EnumNameField, BooleanField, StringField}

class Network private () extends MongoRecord[Network] with ObjectIdPk[Network] {

  override def meta = Network

  object name extends StringField(this, 500) {
    override def toForm = Full(SHtml.text(
      value,
      set(_),
      "class" -> "form-control", "data-placeholder" -> "Ingrese nombre.."
    ))
  }

  object description extends TextareaField(this, 500){
    override def toForm = Full(SHtml.text(
      value,
      set(_),
      "class"->"form-control", "data-placeholder" -> "Ingrese descripcion.."
    ))
  }
  object administrator extends ObjectIdRefField(this, User) {
    override def toString = {
      obj.dmap("")(_.name.get)
    }
    val listUsers = User.findAll

    override def toForm = {
      Full(SHtml.selectElem(
        listUsers,
        obj,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione administrador.."
      )(s => set(s.id.get)))
    }
  }

  object area extends ObjectIdRefField(this, Area) {
    override def optional_? = true
    override def toString = obj.dmap("")(_.name.get)
    val listAreas = Area.findAll
    val defaultArea = Area.findAll.headOption
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
        "data-placeholder" -> "Seleccione programa.."
      )(p => set(p.id.get)))
    }
  }

  object networkType extends EnumNameField(this, NetworkType) {
    override def toForm =
      Full(SHtml.selectObj[Box[NetworkType.Value]](buildDisplayList, Full(valueBox), s => setBox(s)))
  }
}

object Network extends Network with RogueMetaRecord[Network]

object NetworkType extends Enumeration {
  type NetworkType = Value
  val Intern = Value("Interno")
  val Associate = Value("Asociado")
}
package code
package model
package network

import code.config.Site
import code.lib.{BaseModel, RogueMetaRecord}
import code.lib.field.{BsStringField, BsCkTextareaField}
import net.liftweb.common.{Box, Full}
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefListField, ObjectIdRefField, ObjectIdPk}
import net.liftweb.record.field.{TextareaField, EnumNameField, BooleanField, StringField}

class Network private () extends MongoRecord[Network] with ObjectIdPk[Network] with BaseModel[Network] {

  override def meta = Network

  def title = "Red"

  def entityListUrl = Site.backendNetworks.menu.loc.calcDefaultHref

  object name extends BsStringField(this, 500) {
    override def displayName = "Nombre"
  }

  object description extends BsCkTextareaField(this, 500){
    override def displayName = "Descripción"
  }

  object networkType extends EnumNameField(this, NetworkType) {
    override def displayName = "Tipo"
    override def toForm =
      Full(SHtml.selectObj[Box[NetworkType.Value]](buildDisplayList, Full(valueBox), s => setBox(s), "class" -> "form-control"))
  }

  object administrator extends ObjectIdRefField(this, User) {
    override def displayName = "Coordinador"
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
    override def displayName = "Área"
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

  object program extends ObjectIdRefField(this, Program) {
    override def displayName = "Programa"
    override def optional_? = true

    override def toString = obj.dmap("")(_.name.get)

    val programs = Program.findAll

    override def toForm = {
      Full(SHtml.selectElem(
        programs,
        obj,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione programa.."
      )(p => set(p.id.get)))
    }
  }

  object projects extends ObjectIdRefListField(this, Project) {
    override def displayName = "Proyectos"
    val projects = Project.findAll

    override def toForm = {
      Full(SHtml.multiSelectElem(
        projects,
        objs,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione proceso.."
      )(p => set(p.map(_.id.get))))
    }
  }

  object process extends ObjectIdRefField(this, Process) {
    override def displayName = "Proceso"
    override def optional_? = true

    override def toString = obj.dmap("")(_.name.get)

    val listProgram = Process.findAll

    override def toForm = {
      Full(SHtml.selectElem(
        listProgram,
        obj,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione proceso.."
      )(p => set(p.id.get)))
    }
  }

  object actionLine extends ObjectIdRefField(this, ActionLine) {
    override def displayName = "Línea de acción"
    override def optional_? = true

    override def toString = obj.dmap("")(_.name.get)

    val actionLines = ActionLine.findAll

    override def toForm = {
      Full(SHtml.selectElem(
        actionLines,
        obj,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione línea de acción.."
      )(p => set(p.id.get)))
    }
  }

  object spaces extends ObjectIdRefListField(this, Space) {
    override def displayName = "Espacios conectados"
    val spaces = Space.findAll
    override def toForm = {
      Full(SHtml.multiSelectElem(
        spaces,
        objs,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione proceso.."
      )(p => set(p.map(_.id.get))))
    }
  }

}

object Network extends Network with RogueMetaRecord[Network] {
  override def fieldOrder =
    List(name, description, networkType, administrator, area, program, projects, process, actionLine, spaces)
}

object NetworkType extends Enumeration {
  type NetworkType = Value
  val Intern = Value("Interno")
  val Associate = Value("Asociado")
}
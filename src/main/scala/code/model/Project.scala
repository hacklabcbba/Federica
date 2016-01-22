package code.model

import code.config.Site
import code.lib.field.{BsCkTextareaField, BsStringField}
import code.lib.{SortableModel, BaseModel, RogueMetaRecord}
import net.liftweb.common.Full
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdPk, ObjectIdRefField}
import net.liftweb.http.js.JsCmds.Noop

class Project private () extends MongoRecord[Project] with ObjectIdPk[Project] with BaseModel[Project] with SortableModel[Project] {

  override def meta = Project

  def title = "Proyecto"

  def entityListUrl = Site.backendProjects.menu.loc.calcDefaultHref

  object name extends BsStringField(this, 500) {
    override def displayName = "Nombre"
  }

  object goal extends BsCkTextareaField(this, 1000) {
    override def displayName = "Objetivo"
  }

  object description extends BsCkTextareaField(this, 1000) {
    override def displayName = "Descripción"
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
        User.currentUser,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione administrador.."
      )(u => set(u.id.get)))
    }
  }

  object area extends ObjectIdRefField(this, Area) {
    override def displayName = "Área"
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

  object transversalArea extends ObjectIdRefField(this, TransversalArea) {

    override def displayName = "Área transversal"
    override def toString = this.obj.dmap("")(_.name.get)
    val list = TransversalArea.findAll
    val default = list.headOption
    override def toForm = {
      Full(SHtml.ajaxSelectElem(list, default,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione area transversal..")(p => {
        set(p.id.get)
        Noop
      }))
    }
  }

  object program extends ObjectIdRefField(this, Program) {
    override def displayName = "Programa"
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
    override def displayName = "Historia"
  }

  override def toString = name.get
}

object Project extends Project with RogueMetaRecord[Project] {
  override def collectionName = "main.projects"
}

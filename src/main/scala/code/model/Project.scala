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
    override def optional_? = true
    override def displayName = "Área"
    override def toString = {
      this.obj.dmap("Indefinido..")(_.name.get)
    }
    override def toForm = {
      Full(SHtml.selectObj[Option[Area]](availableOptions, Full(this.obj),
        (p: Option[Area]) => {
          setBox(p.map(_.id.get))
        },
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione area transversal.."))
    }

    def availableOptions = (None -> "Ninguna") :: Area.findAll.map(s => Some(s) -> s.toString)
  }

  object transversalArea extends ObjectIdRefField(this, TransversalArea) {
    override def optional_? = true
    override def displayName = "Área transversal"
    override def toString = this.obj.dmap("")(_.name.get)
    val list = (None -> "Ninguna") :: TransversalArea.findAll.map(s => Some(s) -> s.toString)
    override def toForm = {
      Full(SHtml.selectObj[Option[TransversalArea]](list, Full(this.obj),
        (p: Option[TransversalArea]) => {
          setBox(p.map(_.id.get))
        },
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione area transversal.."))
    }
  }

  object program extends ObjectIdRefField(this, Program) {
    override def displayName = "Programa"
    override def optional_? = true
    override def toString = obj.dmap("")(_.name.get)
    val list = (None -> "Ninguno") :: Program.findAll.map(s => Some(s) -> s.toString)
    override def toForm = {
      Full(SHtml.selectObj[Option[Program]](list, Full(this.obj),
        (p: Option[Program]) => {
          setBox(p.map(_.id.get))
        },
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione programa.."))
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

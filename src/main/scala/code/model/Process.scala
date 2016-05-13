package code.model

import code.config.Site
import code.lib.field.{BsCkTextareaField, BsCkUnsecureTextareaField, BsStringField, FileField}
import code.lib.{BaseModel, RogueMetaRecord, SortableModel, WithUrl}
import net.liftweb.common.{Box, Full}
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdPk, ObjectIdRefField}
import net.liftweb.record.field.EnumNameField
import net.liftweb.http.js.JsCmds.Noop

class Process private () extends MongoRecord[Process] with ObjectIdPk[Process] with BaseModel[Process] with SortableModel[Process] with WithUrl[Process] {

  override def meta = Process

  def title = "Proceso"

  def entityListUrl = Site.backendProcess.menu.loc.calcDefaultHref

  object name extends BsStringField(this, 500) {
    override def displayName = "Nombre"
  }

  object photo1 extends FileField(this) {
    override def optional_? = true
    override def displayName = "Foto"
    override def toString = {
      value.fileName.get
    }
  }

  object description extends BsCkUnsecureTextareaField(this, 1000) {
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
        "data-placeholder" -> "Seleccione prorgrama.."))
    }
  }

  def urlString: String = Site.proceso.calcHref(this)

  override def toString = name.get
}

object Process extends Process with RogueMetaRecord[Process] {
  override def collectionName = "main.process"
  override def fieldOrder = List(name, administrator, area, program, transversalArea, order, description)

  def findByArea(area: Area): List[Process] = {
    Process
      .where(_.area eqs area.id.get)
      .fetch()
  }

  def findByProgram(program: Program): List[Process] = {
    Process
      .where(_.program eqs program.id.get)
      .fetch()
  }

  def findByUrl(url: String): Box[Process] = {
    Process.where(_.url eqs url).fetch(1).headOption
  }
}

object ProcessType extends Enumeration {
  type ProcessType = Value
  val Intern = Value("Interno")
  val Associate = Value("Asociado")
}
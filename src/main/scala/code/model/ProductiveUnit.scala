package code.model

import code.lib.{SortableModel, RogueMetaRecord}
import code.lib.field.BsStringField
import net.liftweb.common.{Box, Full}
import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmds._
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdPk, ObjectIdRefField}
import net.liftweb.record.field.{EnumNameField, StringField}

class ProductiveUnit private () extends MongoRecord[ProductiveUnit] with ObjectIdPk[ProductiveUnit] with SortableModel[ProductiveUnit] {

  override def meta = ProductiveUnit

  object name extends BsStringField(this, 500) {
    override def toForm = Full(SHtml.ajaxText(value, (s: String) => {
      set(s)
      Noop
    }))
  }

  object description extends BsStringField(this, 500){
    override def toForm = Full(SHtml.ajaxText(value, (s: String) => {
      set(s)
      Noop
    }))
  }
  object administrator extends ObjectIdRefField(this, User) {
    override def toString = {
      User.find(get).dmap("")(_.name.get)
    }
    val listUsers = User.findAll
    val defaultUser = User.currentUser

    override def toForm = {
      Full(SHtml.ajaxSelectElem(listUsers, defaultUser)(u => {
        set(u.id.get)
        Noop
      }))
    }

    override def defaultValue = User.currentUser.getOrElse(User.createRecord).id.get
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

  object program extends ObjectIdRefField(this, Program){
    override def optional_? = true
    override def toString = Program.find(get).dmap("")(_.name.get)
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

  object productiveType extends EnumNameField(this, ProductiveUnitType) {
    override def toForm = Full(SHtml.ajaxSelectObj[Box[ProductiveUnitType.Value]](buildDisplayList, Full(valueBox),
      (v: Box[ProductiveUnitType.Value]) => {
      setBox(v)
      Noop
    }))
  }
}

object ProductiveUnit extends ProductiveUnit with RogueMetaRecord[ProductiveUnit] {
  override def collectionName = "main.prductive_units"
}

object ProductiveUnitType extends Enumeration {
  type ProductiveUnitType = Value
  val Intern = Value("Interno")
  val Associate = Value("Asociado")
}
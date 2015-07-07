package code
package model

import code.config.Site
import code.lib.{BaseModel, RogueMetaRecord}
import code.lib.field.{FileField, BsEmailField, BsStringField, BsCkTextareaField}
import net.liftweb.common.{Box, Full}
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefListField, ObjectIdRefField, ObjectIdPk}
import net.liftweb.record.field.{TextareaField, EnumNameField, BooleanField, StringField}

import scala.xml.Elem

class Service private () extends MongoRecord[Service] with ObjectIdPk[Service] with BaseModel[Service] {

  override def meta = Service

  def title = "Servicio"

  def entityListUrl = Site.backendServices.menu.loc.calcDefaultHref

  object name extends BsStringField(this, 500) {
    override def displayName = "Nombre"
  }

  object description extends BsCkTextareaField(this, 500) {
    override def displayName = "Descripción"
  }

  object responsible extends ObjectIdRefField(this, User) {
    override def displayName = "Responsable"
    override def toString = {
      this.obj.dmap("Indefinido..")(_.name.get)
    }
    override def toForm: Box[Elem] = {
      Full(
        SHtml.selectElem(
          availableOptions,
          obj,
          "class" -> "select2 form-control",
          "data-placeholder" -> "Seleccione responsable.."
        )(s => set(s.id.get))
      )
    }

    def availableOptions = User.findAll
  }

  object email extends BsEmailField(this, 100) {
    override def displayName = "Correo eléctronico"
  }

  object photo extends FileField(this) {
    override def displayName = "Foto"
    override def toString = {
      value.fileName.get
    }
  }

  override def toString = name.get
}

object Service extends Service with RogueMetaRecord[Service] {
  override def fieldOrder = List(name, description, responsible, photo, email)
}

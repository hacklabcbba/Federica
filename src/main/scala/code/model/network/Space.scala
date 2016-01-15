package code
package model
package network

import code.config.Site
import code.lib.field.{BsCkTextareaField, BsEmailField, BsStringField}
import code.lib.{SortableModel, BaseModel, RogueMetaRecord}
import net.liftweb.common.Full
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdPk, ObjectIdRefField}

class Space private () extends MongoRecord[Space] with ObjectIdPk[Space] with BaseModel[Space] with SortableModel[Space] {

  override def meta = Space

  def title = "Espacio"

  def entityListUrl = Site.backendSpaces.menu.loc.calcDefaultHref

  object name extends BsStringField(this, 500) {
    override def displayName = "Nombre"
  }

  object description extends BsCkTextareaField(this, 500) {
    override def displayName = "Descripción"
  }

  object city extends ObjectIdRefField(this, City) {
    override def displayName = "Ciudad"
    override def toString = {
      obj.dmap("")(_.name.get)
    }
    val cities = City.findAll

    override def toForm = {
      Full(SHtml.selectElem(
        cities,
        obj,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione ciudad.."
      )(s => set(s.id.get)))
    }
  }

  object country extends ObjectIdRefField(this, Country) {
    override def displayName = "País"
    override def toString = {
      obj.dmap("")(_.name.get)
    }
    val countries = Country.findAll

    override def toForm = {
      Full(SHtml.selectElem(
        countries,
        obj,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione país.."
      )(s => set(s.id.get)))
    }
  }

  object email extends BsEmailField(this, 100) {
    override def displayName = "Correo eléctronico"
  }

  object web extends BsStringField(this, 100) {
    override def displayName = "Web"
  }

  override def toString = name.get
}

object Space extends Space with RogueMetaRecord[Space] {
  override def collectionName = "main.spaces"
  override def fieldOrder = List(name, description, city, country, email, web)
}

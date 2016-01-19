package code
package model
package network

import code.config.Site
import code.lib.field.{BsCkTextareaField, BsStringField}
import code.lib.{SortableModel, BaseModel, RogueMetaRecord}
import net.liftweb.common.{Box, Full}
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdPk, ObjectIdRefField, ObjectIdRefListField}
import net.liftweb.record.field.EnumNameField

class Network private () extends MongoRecord[Network] with ObjectIdPk[Network] with BaseModel[Network] with SortableModel[Network] {

  override def meta = Network

  def title = "Red"

  def entityListUrl = Site.backendNetworks.menu.loc.calcDefaultHref

  object name extends BsStringField(this, 500) {
    override def displayName = "Nombre"
  }

  object description extends BsCkTextareaField(this, 500){
    override def displayName = "Descripción"
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

  object scope extends EnumNameField(this, Scope) {
    override def displayName = "Alcance"
  }

}

object Network extends Network with RogueMetaRecord[Network] {
  override def collectionName = "main.networks"
  override def fieldOrder =
    List(name, description, spaces)
}

object NetworkType extends Enumeration {
  type NetworkType = Value
  val Intern = Value("Interno")
  val Associate = Value("Asociado")
}

object Scope extends Enumeration {
  type StateType = Value
  val Neighborhood = Value(0, "Barrial")
  val Local = Value(1, "Local")
  val National = Value(2, "Nacional")
  val Regional = Value(3, "Regional")
  val Global = Value(4, "Internacional")
}
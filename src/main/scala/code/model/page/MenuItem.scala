package code
package model

import code.config.Site
import code.lib.field._
import code.lib.{BaseModel, RogueMetaRecord}
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.ObjectIdPk


class MenuItem private () extends MongoRecord[MenuItem] with ObjectIdPk[MenuItem] with BaseModel[MenuItem]{

  override def meta = MenuItem

  def title = "MenuItem"

  def entityListUrl = Site.backendMenuItems.menu.loc.calcDefaultHref

  object name extends BsStringField(this, 500) {
    override def displayName = "Nombre"
    override def toString = get
  }

  object body extends BsCkTextareaField(this, 1000) {
    override def displayName = "Contenido"
  }

  override def toString = name.get
}

object MenuItem extends MenuItem with RogueMetaRecord[MenuItem] {
  override def collectionName = "page.widgets"
  override def fieldOrder = List(name, body)

}

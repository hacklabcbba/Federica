package code
package model

import code.config.Site
import code.lib.field._
import code.lib.{BaseModel, RogueMetaRecord}
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefField, ObjectIdPk}


class MenuItem private () extends MongoRecord[MenuItem] with ObjectIdPk[MenuItem] with BaseModel[MenuItem]{

  override def meta = MenuItem

  def title = "Menú"

  def entityListUrl = Site.backendMenuItems.menu.loc.calcDefaultHref

  object name extends BsStringField(this, 100) {
    override def displayName = "Nombre"
    override def toString = get
  }

  object page extends ObjectIdRefField(this, Page) {
    override def displayName = "Página"
    override def shouldDisplay_? = false
    override def optional_? = true
  }

  object custom extends BsStringField(this, 500) {
    override def displayName = "Enlace personalizado"
    override def shouldDisplay_? = false
    override def optional_? = true
  }

  object parent extends ObjectIdRefField(this, MenuItem) {
    override def displayName = "Padre"
    override def shouldDisplay_? = false
    override def optional_? = true
  }

  override def toString = name.get
}

object MenuItem extends MenuItem with RogueMetaRecord[MenuItem] {
  override def collectionName = "page.menus"
  override def fieldOrder = List(name)

}

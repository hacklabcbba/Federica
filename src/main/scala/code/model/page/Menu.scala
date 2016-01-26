package code
package model
package page

import code.config.Site
import code.lib.field._
import code.lib.{BaseModel, RogueMetaRecord}
import net.liftweb.mongodb.record.{BsonMetaRecord, BsonRecord, MongoRecord}
import net.liftweb.mongodb.record.field.{BsonRecordListField, ObjectIdRefField, ObjectIdPk}

import scala.annotation.tailrec


class Menu private () extends MongoRecord[Menu] with ObjectIdPk[Menu] with BaseModel[Menu]{

  override def meta = Menu

  def title = "Menú"

  def entityListUrl = Site.backendMenus.menu.loc.calcDefaultHref

  object name extends BsStringField(this, 100) {
    override def displayName = "Nombre"
    override def toString = get
  }

  object menuItems extends BsonRecordListField(this, MenuItem) {
    override def shouldDisplay_? = false
  }

  override def toString = name.get
}

object Menu extends Menu with RogueMetaRecord[Menu] {
  override def collectionName = "page.menus"
  override def fieldOrder = List(name)

}

class MenuItem extends BsonRecord[MenuItem] {

  def meta = MenuItem

  object name extends BsStringField(this, 100) {
    override def displayName = "Nombre"
  }

  object url extends BsStringField(this, 500) {
    override def displayName = "URL"
  }

  object children extends BsonRecordListField(this, MenuItem)

  object kind extends BsEnumField(this, MenuItemKind) {
    override def displayName = "Tipo"
  }

  object menu extends ObjectIdRefField(this, Menu) {
    override def shouldDisplay_? = false
  }
}

object MenuItem extends MenuItem with BsonMetaRecord[MenuItem]

object MenuItemKind extends Enumeration {
  type MenuItemKind = Value
  val Page = Value(1, "Página")
  val Custom = Value(2, "Personalizado")
}

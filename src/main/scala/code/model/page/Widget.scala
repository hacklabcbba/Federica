package code
package model

import code.config.Site
import code.lib.field._
import code.lib.{BaseModel, RogueMetaRecord}
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.ObjectIdPk


class Widget private () extends MongoRecord[Widget] with ObjectIdPk[Widget] with BaseModel[Widget]{

  override def meta = Widget

  def title = "Widget"

  def entityListUrl = Site.backendWidgets.menu.loc.calcDefaultHref

  object name extends BsStringField(this, 500) {
    override def displayName = "Nombre"
    override def toString = get
  }

  object body extends BsCkTextareaField(this, 1000) {
    override def displayName = "Contenido"
  }

  override def toString = name.get
}

object Widget extends Widget with RogueMetaRecord[Widget] {
  override def collectionName = "page.widgets"
  override def fieldOrder = List(name, body)

}

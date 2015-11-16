package code.model

import code.config.Site
import code.lib.field._
import code.lib.{BaseModel, RogueMetaRecord}
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.ObjectIdPk


class Page private () extends MongoRecord[Page] with ObjectIdPk[Page] with BaseModel[Page]{

  override def meta = Page

  def title = "Página"

  def entityListUrl = Site.backendPages.menu.loc.calcDefaultHref

  object name extends BsStringField(this, 500) {
    override def displayName = "Nombre"
    override def toString = get
  }

  object body extends BsCkTextareaField(this, 1000) {
    override def displayName = "Contenido"
  }

  object key extends BsStringField(this, 100) {
    override def displayName = "Key"
  }

  override def toString = name.get
}

object Page extends Page with RogueMetaRecord[Page] {
  override def fieldOrder = List(name, body)

}

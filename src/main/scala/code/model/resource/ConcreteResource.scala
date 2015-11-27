package code
package model
package resource

import code.config.Site
import code.lib.RogueMetaRecord

class ConcreteResource private() extends Resource[ConcreteResource] {

  def title = "Recurso"

  def entityListUrl = Site.backendEquipments.menu.loc.calcDefaultHref

  override def meta = ConcreteResource
}

object ConcreteResource extends ConcreteResource with RogueMetaRecord[ConcreteResource] {

  override def collectionName = "resource.resources"
}

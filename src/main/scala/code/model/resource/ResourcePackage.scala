package code
package model
package resource

import code.config.Site
import code.lib.RogueMetaRecord
import net.liftweb.record.field.StringField

class ResourcePackage private() extends Resource[ResourcePackage] {

  def title = "Sala"

  def entityListUrl = Site.backendRooms.menu.loc.calcDefaultHref

  override def meta = ResourcePackage
  object comboName extends StringField(this, 500)

}

object ResourcePackage extends ResourcePackage with RogueMetaRecord[ResourcePackage] {
  override def collectionName = "resource.resources"
}

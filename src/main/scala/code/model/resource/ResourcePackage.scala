package code
package model
package resource

import code.lib.RogueMetaRecord
import net.liftweb.record.field.{EnumNameField, StringField}

class ResourcePackage private() extends Resource[ResourcePackage] {

  override def meta = ResourcePackage
  object comboName extends StringField(this, 500)
  object classType extends EnumNameField(this, ClassType)
}

object ResourcePackage extends ResourcePackage with RogueMetaRecord[ResourcePackage] {
  override def collectionName = "resource"
}

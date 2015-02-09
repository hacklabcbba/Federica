package code
package model
package resource

import code.lib.RogueMetaRecord
import net.liftweb.record.field.{EnumNameField, StringField}

class Equipment private() extends Resource[Equipment] {

  override def meta = Equipment
  object nameGroup extends StringField(this, 500)
  object classType extends EnumNameField(this, ClassType)
}

object Equipment extends Equipment with RogueMetaRecord[Equipment] {
  override def collectionName = "resource"
}

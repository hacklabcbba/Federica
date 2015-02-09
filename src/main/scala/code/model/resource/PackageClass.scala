package code
package model
package resource

import code.lib.RogueMetaRecord
import net.liftweb.record.field.{EnumNameField, StringField}

class PackageClass private() extends Resource[PackageClass] {

  override def meta = PackageClass
  object comboName extends StringField(this, 500)
  object classType extends EnumNameField(this, ClassType)
}

object PackageClass extends PackageClass with RogueMetaRecord[PackageClass] {
  override def collectionName = "resource"
}

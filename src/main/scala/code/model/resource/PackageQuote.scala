package code
package model
package resource

import code.lib.RogueMetaRecord
import net.liftweb.record.field.EnumNameField

class PackageQuote private() extends Quote[PackageQuote] {

  override def meta = PackageQuote

  object costType extends EnumNameField(this, CostType)
}

object PackageQuote extends PackageQuote with RogueMetaRecord[PackageQuote] {
  override def collectionName = "resource.quotes"
}
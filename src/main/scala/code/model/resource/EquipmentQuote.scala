package code
package model
package resource

import code.lib.RogueMetaRecord
import net.liftweb.record.field.EnumNameField

class EquipmentQuote private() extends Quote[EquipmentQuote] {

  override def meta = EquipmentQuote

  object costType extends EnumNameField(this, CostType)
}

object EquipmentQuote extends EquipmentQuote with RogueMetaRecord[EquipmentQuote] {
  override def collectionName = "quote"
}

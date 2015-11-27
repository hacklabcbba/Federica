package code
package model
package resource

import code.lib.RogueMetaRecord
import net.liftweb.record.field.EnumNameField

class RoomQuote private() extends Quote[RoomQuote] {

  override def meta = RoomQuote

  object costType extends EnumNameField(this, CostType)
}

object RoomQuote extends RoomQuote with RogueMetaRecord[RoomQuote] {
  override def collectionName = "resource.quotes"
}
package code
package model
package resource

import code.lib.RogueMetaRecord


class ConcreteQuote private() extends Resource[ConcreteQuote] {
  override def meta = ConcreteQuote
}

object ConcreteQuote extends ConcreteQuote with RogueMetaRecord[ConcreteQuote] {

  override def collectionName = "quote"
}
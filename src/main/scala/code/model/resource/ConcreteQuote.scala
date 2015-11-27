package code
package model
package resource

import code.config.Site
import code.lib.RogueMetaRecord


class ConcreteQuote private() extends Resource[ConcreteQuote] {
  override def meta = ConcreteQuote

  def title = ""

  def entityListUrl = Site.backendEquipments.menu.loc.calcDefaultHref
}

object ConcreteQuote extends ConcreteQuote with RogueMetaRecord[ConcreteQuote] {

  override def collectionName = "resource.quotes"
}
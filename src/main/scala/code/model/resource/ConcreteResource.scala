package code
package model
package resource

import code.lib.RogueMetaRecord

class ConcreteResource private() extends Resource[ConcreteResource] {
  override def meta = ConcreteResource
}

object ConcreteResource extends ConcreteResource with RogueMetaRecord[ConcreteResource] {

  override def collectionName = "resource"
}

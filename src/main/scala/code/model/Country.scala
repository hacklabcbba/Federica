package code.model

import code.lib.RogueMetaRecord
import code.lib.field.BsStringField
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.ObjectIdPk

class Country private() extends MongoRecord[Country] with ObjectIdPk[Country]{

  override def meta = Country

  object name extends BsStringField(this, 200)
}

object Country extends Country with RogueMetaRecord[Country] {
  override def collectionName = "main.countries"
}

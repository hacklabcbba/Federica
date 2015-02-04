package code
package model
package project

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.ObjectIdPk
import net.liftweb.record.field.StringField

class Country private() extends MongoRecord[Country] with ObjectIdPk[Country]{

  override def meta = Country

  object name extends StringField(this, 200)
}

object Country extends Country with RogueMetaRecord[Country]

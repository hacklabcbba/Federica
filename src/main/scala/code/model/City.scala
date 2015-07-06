package code.model

import code.lib.RogueMetaRecord
import code.lib.field.BsStringField
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.ObjectIdPk
import net.liftweb.record.field.StringField

class City private() extends MongoRecord[City] with ObjectIdPk[City]{

  override def meta = City

  object name extends BsStringField(this, 200)
}

object City extends City with RogueMetaRecord[City]
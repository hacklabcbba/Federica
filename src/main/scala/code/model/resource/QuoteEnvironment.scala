package code
package model
package resource

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefField, ObjectIdPk}
import net.liftweb.record.field.{DecimalField, StringField}

class QuoteEnvironment private() extends MongoRecord[QuoteEnvironment] with ObjectIdPk[QuoteEnvironment]{

  override def meta = QuoteEnvironment

  object consumer extends ObjectIdRefField(this, Consumer)
  object feature extends StringField(this, 200)
  object parameter extends StringField(this, 200)
  object cost extends DecimalField(this, 0)

}

object QuoteEnvironment extends QuoteEnvironment with RogueMetaRecord[QuoteEnvironment]
package code
package model
package proposal

import code.lib.RogueMetaRecord
import code.model.project.{Country, City}
import net.liftweb.mongodb.record.field.{ObjectIdPk, ObjectIdRefField}
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.record.field.StringField

class Proposal private () extends MongoRecord[Proposal] with ObjectIdPk[Proposal] {
  override def meta = Proposal

  object name extends StringField(this, 500)
  object description extends StringField(this, 500)
  object concept extends StringField(this, 500)
  object review extends StringField(this, 500)
  object state extends StringField(this, 100)
  object city extends ObjectIdRefField(this, City)
  object country extends ObjectIdRefField(this, Country)
  object area extends ObjectIdRefField(this, Area)
}

object Proposal extends Proposal with RogueMetaRecord[Proposal]

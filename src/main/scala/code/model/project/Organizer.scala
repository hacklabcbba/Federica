package code
package model
package project

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.ObjectIdPk
import net.liftweb.record.field.StringField

class Organizer private() extends MongoRecord[Organizer] with ObjectIdPk[Organizer]{

  override def meta = Organizer

  object name extends StringField(this, 200)
  object lastName extends StringField(this, 200)

}

object Organizer extends Organizer with RogueMetaRecord[Organizer]
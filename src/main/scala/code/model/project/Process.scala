package code
package model
package project

import code.lib.RogueMetaRecord
import code.model.proposal.{Program, Area}
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{MongoListField, ObjectIdRefField, ObjectIdPk}
import net.liftweb.record.field.StringField

class Process private() extends MongoRecord[Process] with ObjectIdPk[Process]{

  override def meta = Process

  object name extends StringField(this, 200)
  object goal extends StringField(this, 300)
  object description extends StringField(this, 500)
  object responsible extends ObjectIdRefField(this, Organizer)
  object history extends StringField(this, 1000)
  object area extends ObjectIdRefField(this, Area)
  object program extends ObjectIdRefField(this, Program)
}

object Process extends Process with RogueMetaRecord[Process]
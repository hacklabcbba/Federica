package code
package model
package project

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{MongoListField, ObjectIdRefField, ObjectIdPk}
import net.liftweb.record.field.StringField

class Project private() extends MongoRecord[Project] with ObjectIdPk[Project]{

  override def meta = Project

  object name extends StringField(this, 200)
  object goal extends StringField(this, 300)
  object description extends StringField(this, 500)
  object responsible extends ObjectIdRefField(this, Organizer)
  object history extends StringField(this, 1000)
  object schedule extends MongoListField[Project, Schedule](this)

}

object Project extends Project with RogueMetaRecord[Project]
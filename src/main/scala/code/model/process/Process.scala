package code
package model
package process

import code.lib.RogueMetaRecord
import code.model.proposal.{Program, Area}
import net.liftweb.common.{Box, Full}
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefField, ObjectIdPk}
import net.liftweb.record.field.{EnumNameField, BooleanField, StringField}
import net.liftweb.http.js.JsCmds._
import org.bson.types.ObjectId

class Process private () extends MongoRecord[Process] with ObjectIdPk[Process] {

  override def meta = Process

  object name extends StringField(this, 500) {
    override def toForm = Full(SHtml.text(value, set(_)))
  }

  object description extends StringField(this, 500){
    override def toForm = Full(SHtml.text(value, set(_)))
  }
  object administrator extends ObjectIdRefField(this, User) {
    override def toString = {
      obj.dmap("")(_.name.get)
    }
    val listUsers = User.findAll

    override def toForm = {
      Full(SHtml.selectElem(listUsers, User.currentUser)(u => set(u.id.get)))
    }
  }

  object area extends ObjectIdRefField(this, Area) {
    override def optional_? = true
    override def toString = obj.dmap("")(_.name.get)
    val listAreas = Area.findAll
    override def toForm = {
      Full(SHtml.selectElem(listAreas, obj)(a => set(a.id.get)))
    }
  }

  object program extends ObjectIdRefField(this, Program){
    override def optional_? = true

    override def toString = obj.dmap("")(_.name.get)

    val listProgram = Program.findAll

    override def toForm = {
      Full(SHtml.selectElem(listProgram, obj)(s => set(s.id.get)))
    }
  }

  object productiveType extends EnumNameField(this, ProcessType) {
    override def toForm =
      Full(SHtml.selectObj[Box[ProcessType.Value]](buildDisplayList, Full(valueBox), s => setBox(s)))
  }
}

object Process extends Process with RogueMetaRecord[Process]

object ProcessType extends Enumeration {
  type ProcessType = Value
  val Intern = Value("Interno")
  val Associate = Value("Asociado")
}
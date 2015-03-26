package code
package model
package network

import code.lib.RogueMetaRecord
import code.model.proposal.{Program, Area}
import net.liftweb.common.{Box, Full}
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefField, ObjectIdPk}
import net.liftweb.record.field.{EnumNameField, BooleanField, StringField}
import net.liftweb.http.js.JsCmds._
import org.bson.types.ObjectId

class Network private () extends MongoRecord[Network] with ObjectIdPk[Network] {

  override def meta = Network

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
      Full(SHtml.selectElem(listUsers, obj)(s => set(s.id.get)))
    }

  }

  object area extends ObjectIdRefField(this, Area) {
    override def optional_? = true
    override def toString = obj.dmap("")(_.name.get)
    val listAreas = Area.findAll
    val defaultArea = Area.findAll.headOption
    override def toForm = {
      Full(SHtml.selectElem(listAreas, defaultArea)(a => set(a.id.get)))
    }
  }

  object program extends ObjectIdRefField(this, Program){
    override def optional_? = true

    override def toString = obj.dmap("")(_.name.get)

    val listProgram = Program.findAll

    override def toForm = {
      Full(SHtml.selectElem(listProgram, this.obj)(p => set(p.id.get)))
    }
  }

  object productiveType extends EnumNameField(this, NetworkType) {
    override def toForm =
      Full(SHtml.selectObj[Box[NetworkType.Value]](buildDisplayList, Full(valueBox), s => setBox(s)))
  }
}

object Network extends Network with RogueMetaRecord[Network]

object NetworkType extends Enumeration {
  type NetworkType = Value
  val Intern = Value("Interno")
  val Associate = Value("Asociado")
}
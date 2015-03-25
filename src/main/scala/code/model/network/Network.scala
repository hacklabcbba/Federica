package code
package  model
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
    override def toForm = Full(SHtml.ajaxText(value, (s: String) => {
      set(s)
      Noop
    }))
  }

  object description extends StringField(this, 500){
    override def toForm = Full(SHtml.ajaxText(value, (s: String) => {
      set(s)
      Noop
    }))
  }
  object administrator extends ObjectIdRefField(this, User) {
    override def toString = {
      User.find(get).dmap("")(_.name.get)
    }
    val listUsers = User.findAll.map(u => u).toSeq
    val defaultUser = User.currentUser

    override def toForm = {
      Full(SHtml.ajaxSelectElem(listUsers, defaultUser)(u => {
        set(u.id.get)
        Noop
      }))
    }

    override def defaultValue = User.currentUser.getOrElse(User.createRecord).id.get
  }

  object area extends ObjectIdRefField(this, Area) {
    override def optional_? = true
    override def toString = Area.find(get).dmap("")(_.name.get)
    val listAreas = Area.findAll.map(a => a)
    val defaultArea = Area.findAll.headOption
    override def toForm = {
      Full(SHtml.ajaxSelectElem(listAreas, defaultArea)(a => {
        set(a.id.get)
        Noop
      }))
    }
  }

  object program extends ObjectIdRefField(this, Program){
    override def optional_? = true

    override def toString = Program.find(get).dmap("")(_.name.get)

    val listProgram = Program.findAll.map(p => p)
    val defaultProgram= Program.findAll.headOption

    override def toForm = {
      Full(SHtml.ajaxSelectElem(listProgram, defaultProgram)(p => {
        set(p.id.get)
        Noop
      }))
    }
  }

  object productiveType extends EnumNameField(this, NetworkType) {
    override def toForm = Full(SHtml.ajaxSelectObj[Box[NetworkType.Value]](buildDisplayList, Full(valueBox),
      (v: Box[NetworkType.Value]) => {
      setBox(v)
      Noop
    }))
  }
}

object Network extends Network with RogueMetaRecord[Network]

object NetworkType extends Enumeration {
  type NetworkType = Value
  val Intern = Value("Interno")
  val Associate = Value("Asociado")
}
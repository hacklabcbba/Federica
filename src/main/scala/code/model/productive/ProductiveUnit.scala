package code
package model
package productive

import code.lib.RogueMetaRecord
import code.model.proposal.{Program, Area}
import net.liftweb.common.{Box, Full}
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefField, ObjectIdPk}
import net.liftweb.record.field.{EnumNameField, BooleanField, StringField}
import net.liftweb.http.js.JsCmds._
import org.bson.types.ObjectId

class ProductiveUnit private () extends MongoRecord[ProductiveUnit] with ObjectIdPk[ProductiveUnit] {

  override def meta = ProductiveUnit

  object name extends StringField(this, 500) {
    override def toForm = Full(SHtml.ajaxText(value, (s: String) => {set(s); Noop}))
  }

  object description extends StringField(this, 500){
    override def toForm = Full(SHtml.ajaxText(value, (s: String) => {set(s); Noop}))
  }
  object administrator extends ObjectIdRefField(this, User) {
    override def toString = {
      User.find(get).dmap("")(_.name.get)
    }
    val listUsers = User.findAll.map(u => u.id.get.toString -> u.name.get)
    val defaultUser = Full(User.currentUser.dmap("")(_.name.get))

    override def toForm = {
      Full(SHtml.ajaxSelect(listUsers, defaultUser, (u: String) => {
        println("selected: " + u)
        set(new ObjectId(u))
        Noop
      }))
    }

    override def defaultValue = User.currentUser.getOrElse(User.createRecord).id.get
  }

  object area extends ObjectIdRefField(this, Area) {
    override def optional_? = true
    override def toString = Area.find(get).dmap("")(_.name.get)
    val listAreas = Area.findAll.map(u => u.id.get.toString -> u.name.get)
    val defaultArea = Full(Area.findAll.headOption.getOrElse(Area.createRecord).name.get)
    override def toForm = {
      Full(SHtml.ajaxSelect(listAreas, defaultArea, (u: String) => {
        println("selected: " + u)
        set(new ObjectId(u))
        Noop
      }))
    }
  }

  object program extends ObjectIdRefField(this, Program){
    override def optional_? = true

    override def toString = Program.find(get).dmap("")(_.name.get)

    val listProgram = Program.findAll.map(u => u.id.get.toString -> u.name.get)
    val defaultProgram= Full(Program.findAll.headOption.getOrElse(Program.createRecord).name.get)

    override def toForm = {
      Full(SHtml.ajaxSelect(listProgram, defaultProgram, (u: String) => {
        println("selected: " + u)
        set(new ObjectId(u))
        Noop
      }))
    }
  }

  object productiveType extends EnumNameField(this, ProductiveUnitType) {
    override def toForm = Full(SHtml.ajaxSelectObj[Box[ProductiveUnitType.Value]](buildDisplayList, Full(valueBox),
      (v: Box[ProductiveUnitType.Value]) => {
      setBox(v)
      Noop
    }))
  }
}

object ProductiveUnit extends ProductiveUnit with RogueMetaRecord[ProductiveUnit]

object ProductiveUnitType extends Enumeration {
  type ProductiveUnitType = Value
  val Intern = Value("Interno")
  val Associate = Value("Asociado")
}
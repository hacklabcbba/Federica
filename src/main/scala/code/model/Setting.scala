package code
package model

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field._
import net.liftweb.record.field._
import net.liftweb.common.{Box, Full}


class Setting private() extends MongoRecord[Setting] with ObjectIdPk[Setting]{

  override def meta = Setting
  object name extends EnumNameField(this, SettingType)
  object value extends MongoMapField[Setting, String](this)
}

object Setting extends Setting with RogueMetaRecord[Setting]{
  def getEventNumber: String = {
    val settings = Setting.where(_.name eqs SettingType.EventCode).fetch()
    val setting = if (settings.isEmpty){
      createEventNumber()
    }else{
      settings.headOption.get
    }
    //setting.value.get.map(v => v._1 +"-"+ v._2 ).head
    "EVT-001"
  }

  def updateEventNumber() = {

    val setting = Setting.where(_.name eqs SettingType.EventCode).fetch().headOption match {
      case Some(s) =>
        s
      case _ =>
        createEventNumber()
    }

    //for { (k,v) <- setting.value.get } yield  newValue:Map = Map()
    setting.save(true)
  }

  def createEventNumber() = {
    val info = Map(
      "code" -> "EVT",
      "currentNumber" -> "1"
    )
    Setting.createRecord.name(SettingType.EventCode).value(info).save(true)
  }
}
object SettingType extends Enumeration {
  type SettingType = Value
  val EventCode = Value("EventCode")
}
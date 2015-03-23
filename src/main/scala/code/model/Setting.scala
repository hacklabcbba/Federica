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
  object settingValue extends MongoMapField[Setting, String](this)
}

object Setting extends Setting with RogueMetaRecord[Setting]{
  def getEventNumber: String = {
    val settings = Setting.where(_.name eqs SettingType.EventCode).fetch()
    val setting = if (settings.isEmpty){
      createEventNumber()
    }else{
      settings.headOption.get
    }

    val code = setting.settingValue.get("code")
    val number = setting.settingValue.get("currentNumber")
    s"$code - $number"
  }

  def updateEventNumber() = {
    val setting = Setting.where(_.name eqs SettingType.EventCode).fetch().headOption match {
      case Some(s) => s
      case _ => createEventNumber()
    }
    val nextNumber = setting.settingValue.get("currentNumber")
    val info = Map(
      "code" -> "EVT",
      "currentNumber" -> (nextNumber.toInt + 1 ).toString
    )
    setting.settingValue(info).update
  }

  def createEventNumber() = {
    val info = Map(
      "code" -> "EVT",
      "currentNumber" -> "1"
    )
    Setting.createRecord.name(SettingType.EventCode).settingValue(info).save(true)
  }
}
object SettingType extends Enumeration {
  type SettingType = Value
  val EventCode = Value("EventCode")
}
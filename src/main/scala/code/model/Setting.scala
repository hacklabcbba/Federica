package code
package model

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field._
import net.liftweb.record.field._
import net.liftweb.common.{Box, Full}
import code.model.SettingType.SettingType


class Setting private() extends MongoRecord[Setting] with ObjectIdPk[Setting]{

  override def meta = Setting
  object name extends EnumNameField(this, SettingType)
  object settingValue extends MongoMapField[Setting, String](this)
}

object Setting extends Setting with RogueMetaRecord[Setting]{
  def eventNumber: String = {
    val settings = findByName(SettingType.EventCode)
    val setting = settings.headOption.getOrElse(createEventNumber)
    val code = setting.settingValue.get.getOrElse("code", "EVT")
    val number = setting.settingValue.get.getOrElse("currentNumber", "1")
    s"$code - $number"
  }

  def updateEventNumber = {
    val settings = findByName(SettingType.EventCode)
    val setting = settings.headOption.getOrElse(createEventNumber)
    val code = setting.settingValue.get.getOrElse("code", "EVT")
    val nextNumber = setting.settingValue.get.getOrElse("currentNumber", "1")
    val info = Map(
      "code" -> code,
      "currentNumber" -> (nextNumber.toInt + 1 ).toString
    )
    setting.settingValue(info).update
  }

  def createEventNumber = {
    val info = Map(
      "code" -> "EVT",
      "currentNumber" -> "1"
    )
    Setting.createRecord.name(SettingType.EventCode).settingValue(info).save(true)
  }

  def findByName(name: SettingType): List[Setting] = {
    Setting.where(_.name eqs name).fetch()
  }
}

object SettingType extends Enumeration {
  type SettingType = Value
  val EventCode = Value("EventCode")
}
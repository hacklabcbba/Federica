package code
package model
package resource

import code.lib.RogueMetaRecord
import net.liftweb.record.field.{EnumNameField, BooleanField, IntField, StringField}

class Room private() extends Resource[Room] {

  override def meta = Room
  object capacity extends IntField(this, 0)
  object code extends StringField(this, 50)
  object state extends StringField(this, 50)
  object plane extends StringField(this, 500)
  object isReservable extends BooleanField(this, false)
  object classType extends EnumNameField(this, ClassType)
}

object Room extends Room with RogueMetaRecord[Room] {
  override def collectionName = "resource"
}
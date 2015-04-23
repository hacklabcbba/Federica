package code
package model
package resource

import code.lib.RogueMetaRecord
import code.lib.field.FileField
import net.liftweb.common.Full
import net.liftweb.http.SHtml
import net.liftweb.record.field.{EnumNameField, BooleanField, IntField, StringField}

class Room private() extends Resource[Room] {

  override def meta = Room
  object capacity extends StringField(this, 500){
    override def toForm = Full(SHtml.text(value,
      (s: String) => set(s),
      "class" -> "form-control",
      "data-placeholder" -> "Ingrese capacidad.."))
  }

  object code  extends StringField(this, 50){
    override def toForm = Full(SHtml.text(value,
      (s: String) => set(s),
      "class" -> "form-control",
      "data-placeholder" -> "Ingrese capacidad.."))
  }

  object state extends StringField(this, 50){
    override def toForm = Full(SHtml.text(value,
      (s: String) => set(s),
      "class" -> "form-control",
      "data-placeholder" -> "Ingrese capacidad.."))
  }

  /*object plane extends StringField(this, 500){
    override def toForm = Full(SHtml.text(value,
      (s: String) => set(s),
      "class" -> "form-control",
      "data-placeholder" -> "Ingrese capacidad.."))
  }*/

  object plane extends FileField(this) {
    override def displayName = "Plano"
  }

  object isReservable extends BooleanField(this, false)
}

object Room extends Room with RogueMetaRecord[Room] {
  override def collectionName = "resource"
}
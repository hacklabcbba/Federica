package code
package model
package resource

import code.config.Site
import code.lib.RogueMetaRecord
import code.lib.field.FileField
import net.liftweb.common.Full
import net.liftweb.http.SHtml
import net.liftweb.record.field.{EnumNameField, BooleanField, IntField, StringField}

class Room private() extends Resource[Room] {

  override def meta = Room

  def title = "Sala"

  def entityListUrl = Site.backendRooms.menu.loc.calcDefaultHref

  object capacity extends StringField(this, 500) {
    override def displayName = "Capacidad"
    override def toForm = Full(SHtml.text(value,
      (s: String) => set(s),
      "class" -> "form-control",
      "data-placeholder" -> "Ingrese capacidad.."))
  }

  object code  extends StringField(this, 50) {
    override def displayName = "Código"
    override def toForm = Full(SHtml.text(value,
      (s: String) => set(s),
      "class" -> "form-control",
      "data-placeholder" -> "Ingrese capacidad.."))
  }

  object status extends StringField(this, 50) {
    override def displayName = "Estado"
    override def toForm = Full(SHtml.text(value,
      (s: String) => set(s),
      "class" -> "form-control",
      "data-placeholder" -> "Ingrese capacidad.."))
  }

  object plane extends FileField(this) {
    override def displayName = "Plano"
    override def toString = {
      value.fileName.get
    }
  }

  object isBookable extends BooleanField(this, false) {
    override def displayName = "Reservable"
  }

  object isBookableShift extends BooleanField(this, false) {
    override def displayName = "Reservable por turnos"
  }

}

object Room extends Room with RogueMetaRecord[Room] {
  override def collectionName = "resource"

  override def fieldOrder = List(code, name)
}
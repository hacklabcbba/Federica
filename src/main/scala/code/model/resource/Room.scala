package code
package model
package resource

import code.config.Site
import code.lib.RogueMetaRecord
import code.lib.field.{BsStringField, FileField}
import net.liftweb.record.field.BooleanField

class Room private() extends Resource[Room] {

  override def meta = Room

  def title = "Sala"

  def entityListUrl = Site.backendRooms.menu.loc.calcDefaultHref

  object capacity extends BsStringField(this, 500) {
    override def displayName = "Capacidad"
  }

  object code  extends BsStringField(this, 50) {
    override def displayName = "Código"
  }

  object status extends BsStringField(this, 50) {
    override def displayName = "Estado"
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
  override def collectionName = "resource.resources"

  override def fieldOrder = List(code, name)
}
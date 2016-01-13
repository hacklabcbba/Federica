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

  object photo1 extends FileField(this) {
    override def displayName = "Foto"
    override def toString = {
      value.fileName.get
    }
  }

  object photo2 extends FileField(this) {
    override def displayName = "Foto"
    override def toString = {
      value.fileName.get
    }
  }

  object location extends FileField(this) {
    override def displayName = "Ubicación"
    override def toString = {
      value.fileName.get
    }
  }

}

object Room extends Room with RogueMetaRecord[Room] {
  override def collectionName = "resource.resources"

  override def fieldOrder = List(code, name, photo1, photo2)

  def findAllBookeableEnabled: List[Room] = {
    Room
      .where(_.isBookable eqs true)
      .and(_.classType eqs ClassType.RoomType)
      .fetch()
  }

  override def findAll: List[Room] = {
    Room.where(_.classType eqs ClassType.RoomType).fetch()
  }
}
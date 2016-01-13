package code
package model
package resource

import code.config.Site
import code.lib.RogueMetaRecord
import code.lib.field._
import net.liftweb.record.field.{BooleanField, DoubleField, EnumField}

class Equipment private() extends Resource[Equipment] {

  def title = if (this.equipmentType.get == EquipmentType.Equipment) "Equipo" else "Herramienta"

  def entityListUrl = Site.backendEquipments.menu.loc.calcDefaultHref

  override def meta = Equipment

  object category extends BsStringField(this, 100) {
    override def toString = get
    override def isAutoFocus = false
    override def displayName = "Categoría"
  }

  object photo extends FileField(this) {
    override def displayName = "Foto"
    override def toString = {
      value.fileName.get
    }
  }

  object units extends BsIntField(this, 1) {
    override def displayName = "Unidades"
  }

  object costType extends BsEnumField(this, CostType) {
    override def displayName = "Tipo de Costo"
  }

  object cost extends BsDoubleField(this, 0.0) {
    override def displayName = "Costo"
  }

  object equipmentType extends BsEnumField(this, EquipmentType) {
    override def displayName = "Tipo"
  }

  object isBookable extends BooleanField(this, false) {
    override def displayName = "Reservable"
  }
}

object Equipment extends Equipment with RogueMetaRecord[Equipment] {
  override def collectionName = "resource.resources"

  override def findAll: List[Equipment] = {
    Equipment.where(_.classType eqs ClassType.EquipmentType).fetch()
  }
}

object CostType extends Enumeration {
  type ClassType = Value
  val Free = Value(1, "Gratuito")
  val Hour = Value(2, "Por hora")
  val Day = Value(3, "Por día")
}

object EquipmentType extends Enumeration {
  type ClassType = Value
  val Equipment = Value(1, "Equipo")
  val Tool = Value(2, "Herramienta")
}

package code
package model
package resource

import code.lib.{SortableModel, BaseModel}
import code.lib.field.{BsCkTextareaField, BsTextareaField, BsStringField}
import net.liftweb.common.Full
import net.liftweb.http.{S, SHtml}
import net.liftweb.http.js.JsCmds._
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefField, ObjectIdPk}
import net.liftweb.record.LifecycleCallbacks
import net.liftweb.record.field.{IntField, EnumField, EnumNameField}

import scala.xml.Text

trait Resource[T <: MongoRecord[T]] extends MongoRecord[T] with ObjectIdPk[T] with BaseModel[T] with SortableModel[T] {
  this: T =>

  val name = new BsStringField[T](this, 100) {
    override def toString = get
    override def isAutoFocus = false
    override def displayName = "Nombre"
    override def validations = valMinLen(2, "longitud mínima") _ :: super.validations
  }

  val description = new BsCkTextareaField[T](this, 512) {
    override def displayName = "Descripción"
  }

  object classType extends EnumField(this.asInstanceOf[T], ClassType) with LifecycleCallbacks {
    override def shouldDisplay_? = false
    override def beforeSave: Unit = this.owner match {
      case r: Room => classType.set(ClassType.RoomType)
      case e: Equipment => classType.set(ClassType.EquipmentType)
      case _ => classType.set(ClassType.PackageType)
    }
  }
}

object ClassType extends Enumeration {
  type ClassType = Value
  val RoomType = Value(1, "Sala")
  val PackageType = Value(2, "Paquete")
  val EquipmentType = Value(3, "Equipo")
}

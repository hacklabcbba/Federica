package code
package model
package resource

import code.lib.RogueMetaRecord
import net.liftweb.common.Full
import net.liftweb.http.SHtml
import net.liftweb.record.field.{EnumNameField, StringField}

class Equipment private() extends Resource[Equipment] {

  override def meta = Equipment
  object nameGroup extends StringField(this, 500){
    override def toForm = Full(SHtml.text(value,
      (s: String) => set(s),
      "class" -> "form-control", "data-placeholder" -> "Ingrese descripcion.."))
  }
}

object Equipment extends Equipment with RogueMetaRecord[Equipment] {
  override def collectionName = "resource"
}

package code.model

import code.config.Site
import code.lib.field.BsCkTextareaField
import code.lib.{BaseModel, SortableModel, RogueMetaRecord}
import net.liftweb.common.Full
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.ObjectIdPk
import net.liftweb.record.field.{StringField, TextareaField}

class ActionLine private () extends MongoRecord[ActionLine] with ObjectIdPk[ActionLine] with BaseModel[ActionLine] with SortableModel[ActionLine] {

  override def meta = ActionLine

  def title = "Linea de Acción"

  def entityListUrl = Site.backendActionLines.menu.loc.calcDefaultHref

  object name extends StringField(this, 500) {
    override def displayName = "Nombre"
    override def toString = get
    override def toForm = Full(SHtml.text(
      value,
      (s: String) => set(s),
      "class" -> "form-control", "data-placeholder" -> "Ingrese nombre.."))
  }

  object description extends BsCkTextareaField(this, 1000) {
    override def displayName = "Descripción"
  }

  override def toString = name.get
}

object ActionLine extends ActionLine with RogueMetaRecord[ActionLine] {

  override def collectionName = "main.action_lines"

  override def fieldOrder = List(name, description)
}
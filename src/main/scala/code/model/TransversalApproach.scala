package code.model

import code.config.Site
import code.lib.field.{BsCkUnsecureTextareaField, BsStringField, BsCkTextareaField}
import code.lib.{WithUrl, BaseModel, SortableModel, RogueMetaRecord}
import net.liftweb.common.{Box, Full}
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.ObjectIdPk
import net.liftweb.record.field.{StringField, TextareaField}

class TransversalApproach private () extends MongoRecord[TransversalApproach] with ObjectIdPk[TransversalApproach] with BaseModel[TransversalApproach] with SortableModel[TransversalApproach] with WithUrl[TransversalApproach] {

  override def meta = TransversalApproach

  def title = "Linea de Acción"

  def entityListUrl = Site.backendTransversalApproaches.menu.loc.calcDefaultHref

  object name extends StringField(this, 500) {
    override def displayName = "Nombre"
    override def toString = get
    override def toForm = Full(SHtml.text(
      value,
      (s: String) => set(s),
      "class" -> "form-control", "data-placeholder" -> "Ingrese nombre.."))
  }

  object description extends BsCkUnsecureTextareaField(this, 1000) {
    override def displayName = "Descripción"
  }

  def urlString: String = Site.enfoqueTransversal.calcHref(this)

  override def toString = name.get
}

object TransversalApproach extends TransversalApproach with RogueMetaRecord[TransversalApproach] {

  override def collectionName = "main.transversal_approaches"

  override def fieldOrder = List(name, description)

  def findByUrl(url: String): Box[TransversalApproach] = {
    TransversalApproach.where(_.url eqs url).fetch(1).headOption
  }
}
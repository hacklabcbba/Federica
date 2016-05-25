package code.model

import code.config.Site
import code.lib._
import code.lib.field.{BsCkTextareaField, BsCkUnsecureTextareaField, BsStringField, FileField}
import code.lib.{BaseModel, RogueMetaRecord, SortableModel, WithUrl}
import net.liftweb.common.{Box, Full}
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.ObjectIdPk
import net.liftweb.record.field.{StringField, TextareaField}
import net.liftweb.json.JsonDSL._

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

  object facebookPhoto extends FileField(this) {
    override def optional_? = true
    override def displayName = "Imagen para compartir en facebook"
    override def toString = {
      value.fileName.get
    }
  }

  def urlString: String = Site.enfoqueTransversal.calcHref(this)

  override def toString = name.get
}

object TransversalApproach extends TransversalApproach with RogueMetaRecord[TransversalApproach] {

  override def collectionName = "main.transversal_approaches"

  override def fieldOrder = List(name, description, facebookPhoto)

  def findByUrl(url: String): Box[TransversalApproach] = {
    TransversalApproach.where(_.url eqs url).fetch(1).headOption
  }

  def updateElasticSearch(transversalApproach: TransversalApproach) = {
    ElasticSearch.mongoindexSave(
      ElasticSearch.elasticSearchPath ++ List(s"transversal_approach_${transversalApproach.id.get}"),
      ("url" -> Site.enfoqueTransversal.calcHref(transversalApproach)) ~
      ("name" -> transversalApproach.name.get) ~
      ("content" -> transversalApproach.description.asHtml.text)
    )
  }
}
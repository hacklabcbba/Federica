package code.model

import code.config.Site
import code.lib.field.{BsCkTextareaField, BsCkUnsecureTextareaField, BsStringField}
import code.lib._
import net.liftweb.common.{Box, Full}
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.ObjectIdPk
import net.liftweb.record.field.{StringField, TextareaField}
import net.liftweb.json.JsonDSL._

class ActionLine private () extends MongoRecord[ActionLine] with ObjectIdPk[ActionLine] with BaseModel[ActionLine] with SortableModel[ActionLine] with WithUrl[ActionLine] {

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

  object description extends BsCkUnsecureTextareaField(this, 1000) {
    override def displayName = "Descripción"
  }

  def urlString: String = Site.lineaDeAccion.calcHref(this)

  override def toString = name.get
}

object ActionLine extends ActionLine with RogueMetaRecord[ActionLine] {

  override def collectionName = "main.action_lines"

  override def fieldOrder = List(name, description)

  def findByUrl(url: String): Box[ActionLine] = {
    ActionLine.where(_.url eqs url).fetch(1).headOption
  }

  def updateElasticSearch(actionLine: ActionLine) = {
    ElasticSearch.mongoindexSave(
      ElasticSearch.elasticSearchPath ++ List(s"action_line_${actionLine.id.get}"),
      ("url" -> Site.lineaDeAccion.calcHref(actionLine)) ~
      ("name" -> actionLine.name.get) ~
      ("content" -> actionLine.description.asHtml.text)
    )
  }
}
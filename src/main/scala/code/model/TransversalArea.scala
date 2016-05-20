package code.model

import code.config.Site
import code.lib.field._
import code.lib._
import net.liftweb.common.{Box, Full}
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.{BsonMetaRecord, BsonRecord, MongoRecord}
import net.liftweb.mongodb.record.field.{ObjectIdPk, ObjectIdRefField}
import net.liftweb.record.field._
import net.liftweb.util.FieldError
import scala.xml.{Elem, Text}
import net.liftweb.json.JsonDSL._


class TransversalArea private () extends MongoRecord[TransversalArea] with ObjectIdPk[TransversalArea]
  with BaseModel[TransversalArea] with SortableModel[TransversalArea] with WithUrl[TransversalArea] {

  override def meta = TransversalArea

  def title = "Área"

  def entityListUrl = Site.backendTransversableAreas.menu.loc.calcDefaultHref

  object name extends BsStringField(this, 500) {
    override def displayName = "Nombre"
    override def toString = get
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

  object description extends BsCkUnsecureTextareaField(this, 1000) {
    override def displayName = "Descripción"
  }

  object responsible extends ObjectIdRefField(this, User) {
    override def displayName = "Coordinador"
    override def toString = {
      this.obj.dmap("Indefinido..")(_.name.get)
    }
    override def toForm: Box[Elem] = {
      Full(
        SHtml.selectElem(
          availableOptions,
          obj,
          "class" -> "select2 form-control",
          "data-placeholder" -> "Seleccione coordinador.."
        )(s => set(s.id.get))
      )
    }

    def availableOptions = User.findAll
  }

  object email extends BsEmailField(this, 100) {
    override def displayName = "Correo eléctronico"
  }

  object phone extends BsPhoneField(this, 16) {
    override def displayName = "Teléfono"
  }

  object officeHoursBegins extends TimePickerField(this) {
    override def displayName = "Horario de atención desde"
  }

  object officeHoursEnds extends TimePickerField(this) {
    override def displayName = "Horario de atención hasta"
  }

  object code extends BsStringField(this, 50) {
    override def displayName = "Código"
    override def toString = get
  }

  object isPublished extends BooleanField(this, false) {
    override def displayName = "Publicado"
  }

  def urlString: String = Site.areaTransversal.calcHref(this)

  override def toString = name.get

}

object TransversalArea extends TransversalArea with RogueMetaRecord[TransversalArea] {
  override def collectionName = "main.transversal_areas"
  override def fieldOrder = List(name, responsible, email, phone, code, photo1, photo2, description, officeHoursBegins, officeHoursEnds)

  def findAllPublished: List[TransversalArea] = {
    TransversalArea.where(_.isPublished eqs true).fetch()
  }

  def findByUrl(url: String): Box[TransversalArea] = {
    TransversalArea.where(_.url eqs url).fetch(1).headOption
  }

  def updateElasticSearch(transversalArea: TransversalArea) = {
    ElasticSearch.mongoindexSave(
      ElasticSearch.elasticSearchPath ++ List(s"transversal_area_${transversalArea.id.get}"),
      ("url" -> Site.areaTransversal.calcHref(transversalArea)) ~
      ("name" -> transversalArea.name.get) ~
      ("content" -> transversalArea.description.asHtml.text)
    )
  }
}

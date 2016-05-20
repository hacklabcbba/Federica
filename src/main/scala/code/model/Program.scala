package code.model

import code.config.Site
import code.lib._
import code.lib.field._
import net.liftweb.common.{Box, Full}
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdPk, ObjectIdRefField}
import net.liftweb.record.field.{StringField, TextareaField}
import net.liftweb.json.JsonDSL._
import scala.xml.Elem


class Program private () extends MongoRecord[Program] with ObjectIdPk[Program] with BaseModel[Program] with SortableModel[Program] with WithUrl[Program] {

  override def meta = Program

  def title = "Programa"

  def entityListUrl = Site.backendPrograms.menu.loc.calcDefaultHref

  object name extends BsStringField(this, 500) {
    override def displayName = "Nombre"
    override def toString = get
  }

  object responsible extends ObjectIdRefField(this, User) {
    override def displayName = "Responsable"
    override def toString = {
      this.obj.dmap("Indefinido..")(_.name.get)
    }
    override def toForm: Box[Elem] = {
      Full(
        SHtml.selectElem(
          availableOptions,
          obj,
          "class" -> "select2 form-control",
          "data-placeholder" -> "Seleccione responsable.."
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

  object code extends BsStringField(this, 50) {
    override def displayName = "Código"
    override def toString = get
  }

  object description extends BsCkUnsecureTextareaField(this, 1000) {
    override def displayName = "Descripción"
  }

  def urlString: String = Site.programa.calcHref(this)

  override def toString = name.get
}

object Program extends Program with RogueMetaRecord[Program] {
  override def collectionName = "main.programs"
  override def fieldOrder = List(name, responsible, email, phone, code, description)

  def findAllPublished: List[Program] = {
    Program.findAll
  }

  def findByUrl(url: String): Box[Program] = {
    Program.where(_.url eqs url).fetch(1).headOption
  }

  def updateElasticSearch(program: Program) = {
    ElasticSearch.mongoindexSave(
      ElasticSearch.elasticSearchPath ++ List(s"program_${program.id.get}"),
      ("url" -> Site.programa.calcHref(program)) ~
      ("name" -> program.name.get) ~
      ("content" -> program.description.asHtml.text)
    )
  }
}

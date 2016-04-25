package code
package model

import code.config.Site
import code.lib.{BaseModel, RogueMetaRecord}
import code.lib.field._
import code.model.event.Values
import com.foursquare.rogue.LiftRogue
import net.liftweb.common.{Box, Full}
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdRefListField, ObjectIdPk, ObjectIdRefField}
import LiftRogue._
import net.liftweb.http.js.JsCmds.Noop
import net.liftweb.record.field.BooleanField

import scala.xml.Elem

class BlogPost private() extends MongoRecord[BlogPost] with ObjectIdPk[BlogPost] with BaseModel[BlogPost] {

  override def meta = BlogPost

  def title = "Entrada de blog"

  def entityListUrl = Site.backendBlog.menu.loc.calcDefaultHref

  object name extends BsStringField(this, 200) {
    override def displayName = "Título"
  }

  object area extends ObjectIdRefField(this, Area) {
    override def optional_? = true
    override def displayName = "Área"
    override def toString = {
      this.obj.dmap("Indefinido..")(_.name.get)
    }
    override def toForm = {
      Full(SHtml.selectObj[Option[Area]](availableOptions, Full(this.obj),
        (p: Option[Area]) => {
          setBox(p.map(_.id.get))
        },
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione area transversal.."))
    }

    def availableOptions = (None -> "Ninguna") :: Area.findAll.map(s => Some(s) -> s.toString)
  }

  object transversalArea extends ObjectIdRefField(this, TransversalArea) {
    override def optional_? = true
    override def displayName = "Área transversal"
    override def toString = this.obj.dmap("")(_.name.get)
    val list = (None -> "Ninguna") :: TransversalArea.findAll.map(s => Some(s) -> s.toString)
    override def toForm = {
      Full(SHtml.selectObj[Option[TransversalArea]](list, Full(this.obj),
        (p: Option[TransversalArea]) => {
          setBox(p.map(_.id.get))
        },
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione area transversal.."))
    }
  }

  object program extends ObjectIdRefField(this, Program) {
    override def displayName = "Programa"
    override def optional_? = true
    override def toString = obj.dmap("")(_.name.get)
    val list = (None -> "Ninguno") :: Program.findAll.map(s => Some(s) -> s.toString)
    override def toForm = {
      Full(SHtml.selectObj[Option[Program]](list, Full(this.obj),
        (p: Option[Program]) => {
          setBox(p.map(_.id.get))
        },
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione prorgrama.."))
    }
  }

  object date extends DatePickerField(this) {
    override def displayName = "Fecha"
  }

  object photo extends FileField(this) {
    override def displayName = "Foto"
    override def toString = {
      value.fileName.get
    }
  }

  object content extends BsCkTextareaField(this, 500) {
    override def displayName = "Contenido"
  }

  object categories extends TagField(this) {
    override def displayName = "Categorias"
  }

  object tags extends TagField(this) {
    override def displayName = "Etiquetas"
  }

  object author extends ObjectIdRefField(this, User) {
    override def defaultValueBox = User.currentUser.map(_.id.get)
    override def shouldDisplay_? = false
    override def displayName = "Autor"
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

  object values extends ObjectIdRefListField(this, Values) {
    override def displayName = "Principios"
    def currentValue = this.objs
    def availableOptions: List[(Values, String)] = Values.findAll.map(p => p -> p.name.get).toList

    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectObj(
        availableOptions,
        currentValue,
        (list: List[Values]) => set(list.map(_.id.get)),
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione uno o varios principios..."
      ))
    }
  }

  object actionLines extends ObjectIdRefListField(this, ActionLine) {
    override def displayName = "Lineas de acción"
    def currentValue = this.objs
    def availableOptions: List[(ActionLine, String)] = ActionLine.findAll.map(p => p -> p.name.get).toList

    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectObj(
        availableOptions,
        currentValue,
        (list: List[ActionLine]) => set(list.map(_.id.get)),
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione una o varias lineas de accion.."
      ))
    }
  }

  object process extends ObjectIdRefField(this, Process) {
    override def optional_? = true
    override def displayName = "Proceso"
    val list = (None -> "Ninguno") :: Process.findAll.map(s => Some(s) -> s.toString)
    override def toForm = {
      Full(SHtml.selectObj[Option[Process]](list, Full(this.obj),
        (p: Option[Process]) => {
          setBox(p.map(_.id.get))
        },
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione proceso.."))
    }
  }

  object isPublished extends BooleanField(this, false) {
    override def displayName = "Publicado?"
    override def name = "is_published"
  }

  override def toString = name.get


}

object BlogPost extends BlogPost with RogueMetaRecord[BlogPost] {

  override def collectionName = "main.blog_posts"

  override def fieldOrder = List(
    name, categories, tags, photo,
    area, program, transversalArea,
    values, actionLines, process,
    date, content, isPublished)

  def findNext(inst: BlogPost): Box[BlogPost] = {
    BlogPost
      .where(_.id gt inst.id.get)
      .and(_.id neqs inst.id.get)
      .orderAsc(_.id)
      .fetch(1)
      .headOption
  }

  def findPrev(inst: BlogPost): Box[BlogPost] = {
    BlogPost
      .where(_.id lt inst.id.get)
      .and(_.id neqs inst.id.get)
      .orderAsc(_.id)
      .fetch(1)
      .headOption
  }

  def findRelated(inst: BlogPost, limit: Int): List[BlogPost] = {
    BlogPost
      .or(
        _.where(_.tags in inst.tags.get),
        _.where(_.area eqs inst.area.get))
      .and(_.id neqs inst.id.get)
      .limit(limit)
      .fetch()
  }

  def countByCategory(category: Box[String]): Long = category match {
    case Full(cat) =>
      BlogPost
        .where(_.categories contains Tag.createRecord.tag(cat))
        .count
    case _ =>
      BlogPost
        .count
  }

  def findByCategoryPage(category: Box[String], limit: Int, page: Int): List[BlogPost] = category match {
    case Full(cat) =>
      BlogPost
        .where(_.categories contains Tag.createRecord.tag(cat))
        .paginate(limit)
        .setPage(page)
        .fetch()
    case _ =>
      BlogPost
        .paginate(limit)
        .setPage(page)
        .fetch()
  }

  def countPublishedByCategory(category: Box[String]): Long = category match {
    case Full(cat) =>
      BlogPost
        .where(_.categories contains Tag.createRecord.tag(cat))
        .and(_.isPublished eqs true)
        .count
    case _ =>
      BlogPost
        .where(_.isPublished eqs true)
        .count
  }

  def findPublishedByCategoryPage(category: Box[String], limit: Int, page: Int): List[BlogPost] = category match {
    case Full(cat) =>
      BlogPost
        .where(_.categories contains Tag.createRecord.tag(cat))
        .and(_.isPublished eqs true)
        .paginate(limit)
        .setPage(page)
        .fetch()
    case _ =>
      BlogPost
        .where(_.isPublished eqs true)
        .paginate(limit)
        .setPage(page)
        .fetch()
  }

  def findCategories: List[String] = {
    BlogPost.distinct(_.categories.subfield(_.tag)).toList.asInstanceOf[List[String]]
  }

  def findPostByUser(user: User): List[BlogPost] = {
    BlogPost.where(_.author eqs user.id.get).and(_.isPublished eqs true).fetch()
  }

  def findLastPostByUser(user: User): List[BlogPost] = {
    val result = BlogPost.where(_.author eqs user.id.get).and(_.isPublished eqs true).orderDesc(_.date).fetch()
    if(result.size >= 3){
      result.slice(0,3)
    } else {
      result
    }
  }
}
package code
package model

import code.config.Site
import code.lib._
import code.lib.field._
import com.foursquare.rogue.LiftRogue
import net.liftweb.common.{Box, Full}
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdPk, ObjectIdRefField, ObjectIdRefListField}
import LiftRogue._
import code.lib.request.request._
import net.liftweb.http.js.JsCmds.Noop
import net.liftweb.record.field.BooleanField

import scala.reflect.macros.whitebox
import scala.xml.Elem
import net.liftweb.json.JsonDSL._

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

  object facebookPhoto extends FileField(this) {
    override def optional_? = true
    override def displayName = "Imagen para compartir en facebook"
    override def toString = {
      value.fileName.get
    }
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

  object transversalApproach extends ObjectIdRefField(this, TransversalApproach) {
    override def optional_? = true
    override def displayName = "Enfoque transversal"
    override def toString = this.obj.dmap("")(_.name.get)
    val list = (None -> "Ninguna") :: TransversalApproach.findAll.map(s => Some(s) -> s.toString)
    override def toForm = {
      Full(SHtml.selectObj[Option[TransversalApproach]](list, Full(this.obj),
        (p: Option[TransversalApproach]) => {
          setBox(p.map(_.id.get))
        },
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione un enfoque.."))
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

  object values extends ObjectIdRefListField(this, Value) {
    override def displayName = "Principios"
    def currentValue = this.objs
    def availableOptions: List[(Value, String)] = Value.findAll.map(p => p -> p.name.get)

    override def toForm: Box[Elem] = {
      Full(SHtml.multiSelectObj(
        availableOptions,
        currentValue,
        (list: List[Value]) => set(list.map(_.id.get)),
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
    name, categories, tags, photo, facebookPhoto,
    area, program, transversalArea, transversalApproach,
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

  def countPublishedByFilters: Long = {
    BlogPost.where(_.isPublished eqs true)
      .andOpt(getCategoryValue)(_.categories contains _)
      .andOpt(getAuthorValue)(_.author eqs _.id.get)
      .andOpt(getAreaValue)(_.area eqs _.id.get)
      .andOpt(getTransversalAreaValue)(_.transversalArea eqs _.id.get)
      .andOpt(getTagValue)(_.tags contains _)
      .andOpt(getValue)(_.values contains _.id.get)
      .andOpt(getActionLineValue)(_.actionLines contains _.id.get)
      .andOpt(getProcessValue)(_.process eqs _.id.get)
      .count()
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

  def findPublishedByFilters(values: Box[Value], program: Box[Program], area: Box[Area], actionLine: Box[ActionLine],
                             transversalArea: Box[TransversalArea], transversalApproach: Box[TransversalApproach],
                             process: Box[Process]): List[BlogPost] = {
    BlogPost.or(_.whereOpt(values.toOption)(_.values contains  _.id.get),
      _.whereOpt(program.toOption)(_.program eqs _.id.get),
      _.whereOpt(area.toOption)(_.area eqs _.id.get),
      _.whereOpt(actionLine.toOption)(_.actionLines contains _.id.get),
      _.whereOpt(transversalArea.toOption)(_.transversalArea eqs _.id.get),
      _.whereOpt(transversalApproach.toOption)(_.transversalApproach eqs _.id.get),
      _.whereOpt(process.toOption)(_.process eqs _.id.get))
      .orderDesc(_.id).fetch(3)
  }

  def findPostPublishedByFilters(limit: Int, page: Int): List[BlogPost] = {

    BlogPost.where(_.isPublished eqs true)
      .andOpt(getCategoryValue)(_.categories contains _)
      .andOpt(getAuthorValue)(_.author eqs _.id.get)
      .andOpt(getAreaValue)(_.area eqs _.id.get)
      .andOpt(getTransversalAreaValue)(_.transversalArea eqs _.id.get)
      .andOpt(getTagValue)(_.tags contains _)
      .andOpt(getValue)(_.values contains _.id.get)
      .andOpt(getActionLineValue)(_.actionLines contains _.id.get)
      .andOpt(getProcessValue)(_.process eqs _.id.get)
      .paginate(limit)
      .setPage(page)
      .fetch()
  }

  def getProcessValue: Option[Process] = {
    Helper.getParameter.headOption match {
      case Some((p: String, v: String)) if(p == "proceso") =>
        Process.where(_.name eqs v).fetch().headOption
      case _ =>
        None
    }
  }

  def getActionLineValue: Option[ActionLine] = {
    Helper.getParameter.headOption match {
      case Some((p: String, v: String)) if(p == "lineaAccion") =>
        ActionLine.where(_.name eqs v).fetch().headOption
      case _ =>
        None
    }
  }

  def getValue: Option[Value] = {
    Helper.getParameter.headOption match {
      case Some((p: String, v: String)) if(p == "principio") =>
        Value.where(_.name eqs v).fetch().headOption
      case _ =>
        None
    }
  }

  def getTransversalAreaValue: Option[TransversalArea] = {
    Helper.getParameter.headOption match {
      case Some((p: String, v: String)) if(p == "areaT") =>
        TransversalArea.where(_.name eqs v).fetch().headOption
      case _ =>
        None
    }
  }

  def getAreaValue: Option[Area] = {
    Helper.getParameter.headOption match {
      case Some((p: String, v: String)) if(p == "area") =>
        Area.where(_.name eqs v).fetch().headOption
      case _ =>
        None
    }
  }

  def getCategoryValue: Option[Tag] = {
    Helper.getParameter.headOption match {
      case Some((p: String, v: String)) if(p == "categoria") =>
        BlogPost.where(_.categories.subfield(_.tag) eqs v).select(_.categories).fetch().flatten.headOption
      case _ =>
        None
    }
  }

  def getTagValue: Option[Tag] = {
    Helper.getParameter.headOption match {
      case Some((p: String, v: String)) if(p == "etiqueta") =>
        BlogPost.where(_.tags.subfield(_.tag) eqs v).select(_.tags).fetch().flatten.headOption
      case _ =>
        None
    }
  }

  def getAuthorValue: Option[User] = {
    Helper.getParameter.headOption match {
      case Some((p: String, v: String)) if(p == "autor") =>
        User.where(_.name eqs v).fetch().headOption
      case _ =>
        None
    }
  }

  def findCategories: List[String] = {
    BlogPost.distinct(_.categories.subfield(_.tag)).toList.asInstanceOf[List[String]]
  }

  def findLastPostByUser(user: Box[User]): List[BlogPost] = {
    BlogPost.whereOpt(user.toOption)(_.author eqs _.id.get).and(_.isPublished eqs true).orderDesc(_.date).fetch(3)
  }

  def findAllLastPost: List[BlogPost] = {
    BlogPost.orderDesc(_.date).fetch(3)
  }

  def updateElasticSearch(blogPost: BlogPost) = {
    ElasticSearch.mongoindexSave(
      ElasticSearch.elasticSearchPath ++ List(s"post_${blogPost.id.get}"),
      ("url" -> Site.entradaBlog.calcHref(blogPost)) ~
      ("name" -> blogPost.name.get) ~
      ("content" -> blogPost.content.asHtml.text)
    )
  }
}
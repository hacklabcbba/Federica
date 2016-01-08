package code
package model

import code.config.Site
import code.lib.{BaseModel, RogueMetaRecord}
import code.lib.field._
import com.foursquare.rogue.LiftRogue
import net.liftweb.common.{Box, Full}
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdPk, ObjectIdRefField}
import LiftRogue._

import scala.xml.Elem

class BlogPost private() extends MongoRecord[BlogPost] with ObjectIdPk[BlogPost] with BaseModel[BlogPost] {

  override def meta = BlogPost

  def title = "Entrada de blog"

  def entityListUrl = Site.backendBlog.menu.loc.calcDefaultHref

  object name extends BsStringField(this, 200) {
    override def displayName = "Título"
  }

  object area extends ObjectIdRefField(this, Area) {
    override def displayName = "Área"
    override def toString = {
      this.obj.dmap("Indefinido..")(_.name.get)
    }
    override def toForm: Box[Elem] = {
      Full(
        SHtml.selectElem(
          availableOptions,
          obj,
          "class" -> "select2 form-control",
          "data-placeholder" -> "Seleccione vategoria.."
        )(s => set(s.id.get))
      )
    }

    def availableOptions = Area.findAll
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

  object categorias extends TagField(this) {
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





}

object BlogPost extends BlogPost with RogueMetaRecord[BlogPost] {
  override def collectionName = "main.blog_posts"

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
        .where(_.categorias contains Tag.createRecord.tag(cat))
        .count
    case _ =>
      BlogPost
        .count
  }

  def findByCategoryPage(category: Box[String], limit: Int, page: Int): List[BlogPost] = category match {
    case Full(cat) =>
      BlogPost
        .where(_.categorias contains Tag.createRecord.tag(cat))
        .paginate(limit)
        .setPage(page)
        .fetch()
    case _ =>
      BlogPost
        .paginate(limit)
        .setPage(page)
        .fetch()
  }

  def findCategories: List[String] = {
    BlogPost.distinct(_.categorias.subfield(_.tag)).toList.asInstanceOf[List[String]]
  }
}
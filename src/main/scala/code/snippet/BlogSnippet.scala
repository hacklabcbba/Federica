package code.snippet

import code.config
import code.config.Site
import code.model.BlogPost
import net.liftweb.common.{Full, Box}
import net.liftweb.util.{PassThru, CssSel, Helpers}
import Helpers._

import scala.xml.NodeSeq

object BlogSnippet extends ListSnippet[BlogPost] {

  val meta = BlogPost

  val title = "Entradas del blog"

  val addUrl = Site.backendBlogAdd.calcHref(BlogPost.createRecord)

  def entityListUrl: String = Site.backendBlog.menu.loc.calcDefaultHref

  def itemEditUrl(inst: BlogPost): String = Site.backendBlogEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.author, meta.area, meta.date)

  private def previewCss(post: BlogPost): CssSel = {
    post.photo.get.fileId.get.trim match {
      case "" => "data-name=image" #> PassThru
      case other => "data-name=image" #> post.photo.previewFile
    }
  }

  private def imageCss(post: BlogPost): CssSel = {
    post.photo.get.fileId.get.trim match {
      case "" => "data-name=image" #> PassThru
      case other => "data-name=image" #> post.photo.viewFile
    }
  }

  def renderFrontEnd: CssSel = {
    "data-name=post" #> meta.findAll.map(post => {
      previewCss(post) &
      "data-name=title *" #> post.name.get &
      "data-name=area *" #> post.area.obj.dmap("")(_.name.get) &
      "data-name=author *" #> post.author.obj.dmap("")(_.name.get) &
      "data-name=date *" #> post.date.toString &
      "data-name=title [href]" #> Site.entradaBlog.calcHref(post) &
      "data-name=content *" #> post.content.asHtmlCutted(200)
    })
  }

  def renderViewFrontEnd: CssSel = {
    for {
      post <- Site.entradaBlog.currentValue ?~ "Entrada no encontrada"
    } yield {
      val next = BlogPost.findNext(post)
      val prev = BlogPost.findNext(post)
      val related = BlogPost.findRelated(post, 3)
      imageCss(post) &
      "data-name=title *" #> post.name.get &
        "data-name=area *" #> post.area.obj.dmap("")(_.name.get) &
      "data-name=author *" #> post.author.obj.dmap("")(_.name.get) &
      "data-name=date *" #> post.date.toString &
      "data-name=title [href]" #> Site.entradaBlog.calcHref(post) &
      "data-name=content *" #> post.content.asHtml &
      "data-name=tags" #> post.tags.get.map(tag => {
        "data-name=tag *" #> tag
      }) &
      prevItemCss(prev) &
      nextItemCss(next) &
      relatedCss(related)
    }
  }

  private def prevItemCss(post: Box[BlogPost]): CssSel = post match {
    case Full(post) =>
      "data-name=prev-item [href]" #> config.Site.entradaBlog.calcHref(post) &
      "data-name=prev-item" #> {
        "data-name=title *" #> post.name.get
      }
    case _ =>
      "data-name=prev-item" #> NodeSeq.Empty
  }

  private def nextItemCss(post: Box[BlogPost]): CssSel = post match {
    case Full(post) =>
      "data-name=prev-item [href]" #> config.Site.entradaBlog.calcHref(post) &
      "data-name=prev-item" #> {
          "data-name=title *" #> post.name.get
        }
    case _ =>
      "data-name=prev-item" #> NodeSeq.Empty
  }

  def relatedCss(posts: List[BlogPost]): CssSel = {
    "data-name=related" #> posts.map(post => {
      "data-name=url [href]" #> config.Site.entradaBlog.calcHref(post) &
      imageCss(post) &
      "data-name=title *" #> post.name.get
    })
  }

}

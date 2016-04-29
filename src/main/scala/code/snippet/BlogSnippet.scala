package code.snippet

import code.config
import code.config.Site
import code.lib.snippet.PaginatorSnippet
import code.model.{BlogPost, Value}
import net.liftweb.common.{Box, Full}
import net.liftweb.http.{RequestVar, S, SHtml}
import net.liftweb.util.{CssSel, Helpers, PassThru}
import Helpers._
import scala.xml.NodeSeq

object BlogSnippet extends ListSnippet[BlogPost] with PaginatorSnippet[BlogPost] {

  val meta = BlogPost

  val title = "Entradas del blog"

  val addUrl = Site.backendBlogAdd.calcHref(BlogPost.createRecord)

  override def itemsPerPage = 10

  override def count = meta.countPublishedByCategory(S.param("category"))
  
  override def page = meta.findPublishedByCategoryPage(S.param("category"), itemsPerPage, curPage)

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
    val category = S.param("category")
    "data-name=category *" #> category &
    "data-name=post" #> page.map(post => {
      previewCss(post) &
      "data-name=title *" #> post.name.get &
      (if (post.area.obj.isEmpty)
        "data-name=area" #> NodeSeq.Empty else
        "data-name=area-name *" #> post.area.obj.dmap("")(_.name.get)) &
      (if (post.transversalArea.obj.isEmpty)
        "data-name=transversalarea" #> NodeSeq.Empty
      else "data-name=transversalarea-name *" #> post.transversalArea.obj.dmap("")(_.name.get)) &
      "data-name=author *" #> post.author.obj.dmap("")(_.name.get) &
      "data-name=date *" #> post.date.toString &
      "data-name=title [href]" #> Site.entradaBlog.calcHref(post) &
      "data-name=content *" #> post.content.asHtmlCutted(200)
    }) &
    "data-name=all-categories" #> {
      "li [class+]" #> (if (category.isEmpty) "active" else "") &
      "a [href]" #> Site.blog.fullUrl
    } &
    "data-name=categories" #> meta.findCategories.map(cat => {
      "li [class+]" #> (if (category === cat) "active" else "") &
      "a [href]" #> s"${Site.blog.fullUrl}?category=$cat" &
      "a *" #> cat
    }) &
    paginate
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
        "data-name=tag *" #> tag.tag.get
      }) &
      prevItemCss(prev) &
      nextItemCss(next) &
      relatedCss(related)
    }
  }


  def renderLastThreePostByFilter(values: Box[Value]): CssSel = {
    BlogPost.findPublishedByFilters(values).size > 0 match {
      case true =>
        "data-name=posts" #> BlogPost.findPublishedByFilters(values).map(post => {
          "data-name=title *" #> post.name.get &
          "data-name=area *" #> post.area.obj.dmap("")(_.name.get) &
          {
            post.photo.valueBox match {
              case Full(image) =>
                val imageSrc = image.fileId.get
                "data-name=image [src]" #> s"/image/$imageSrc"
              case _ =>
                "data-name=image *" #> NodeSeq.Empty
            }
          } &
          "data-name=author *" #> post.author.obj.dmap("")(_.name.get) &
          "data-name=date *" #> post.date.toString &
          "data-name=description" #> post.content.asHtmlCutted(250)
        })
      case false =>
        "data-name=postsH" #> NodeSeq.Empty
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

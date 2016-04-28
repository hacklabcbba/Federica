package code.snippet

import code.config
import code.config.Site
import code.lib.snippet.PaginatorSnippet
import code.model.{ActionLine, BlogPost, Tag}
import net.liftweb.common.{Box, Full}
import net.liftweb.http.{S, SHtml}
import net.liftweb.util.{CssSel, Helpers, PassThru}
import Helpers._
import code.lib.request.request._
import code.model.event.Values
import net.liftweb.http.js.JsCmds.RedirectTo

import scala.collection.immutable.Stream.Empty
import scala.xml.NodeSeq

object BlogSnippet extends ListSnippet[BlogPost] with PaginatorSnippet[BlogPost] {

  val meta = BlogPost

  val title = "Entradas del blog"

  val addUrl = Site.backendBlogAdd.calcHref(BlogPost.createRecord)

  override def itemsPerPage = 10

  override def count = meta.countPublishedByCategory(S.param("category"))
  
  //override def page = meta.findPublishedByCategoryPage(S.param("category"), itemsPerPage, curPage)
  override def page = meta.findPublishedByFilters(itemsPerPage, curPage)

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
    val category = categoryBlogRequestVar.get
    "data-name=category *" #> category.map(_.tag.get) &
    "data-name=post" #> page.map(post => {
      previewCss(post) &
      "data-name=title *" #> post.name.get &
      renderTags(post)&
      "data-name=title [href]" #> Site.entradaBlog.calcHref(post) &
      "data-name=content *" #> post.content.asHtmlCutted(200)
    }) &
    "data-name=all-categories" #> {
      "li [class+]" #> (if (category.isEmpty) "active" else "") &
      "a [href]" #> Site.blog.fullUrl
    } &
    "data-name=categories" #> meta.findCategories.map(cat => {
      "li [class+]" #> (if (category.dmap("")(_.tag.get) == cat) "active" else "") &
      "a [onclick]" #> SHtml.ajaxInvoke(() => {
        RedirectTo(Site.blog.url, () => categoryBlogRequestVar.set(Full(Tag.createRecord.tag(cat))))
      }) &
      "a *" #> cat
    }) &
    paginate
  }

  def renderTags(post: BlogPost): CssSel = {
    (if (post.area.obj.isEmpty)
      "data-name=area" #> NodeSeq.Empty else {
      "data-name=area-name *" #> post.area.obj.dmap("")(_.name.get) &
      "data-name=area-name [onclick]" #> SHtml.ajaxInvoke(() => {
        RedirectTo(Site.blog.url, () => areaBlogRequestVar.set(post.area.obj))
      })
    }) &
    (if (post.transversalArea.obj.isEmpty)
      "data-name=transversalarea" #> NodeSeq.Empty
    else {
      "data-name=transversalarea-name *" #> post.transversalArea.obj.dmap("")(_.name.get) &
      "data-name=transversalarea-name [onclick]" #> SHtml.ajaxInvoke(() => {
        RedirectTo(Site.blog.url, () => areaTransversalBlogRequestVar.set(post.transversalArea.obj))
      })
    }) &
    (if (post.process.obj.isEmpty)
      "data-name=process" #> NodeSeq.Empty
    else {
      "data-name=process-name *" #> post.process.obj.dmap("")(_.name.get) &
      "data-name=process-name [onclick]" #> SHtml.ajaxInvoke(() => {
        RedirectTo(Site.blog.url, () => processBlogRequestVar.set(post.process.obj))
      })
    }) &
    (
      if(post.author.obj.isEmpty)
        "data-name=author" #> NodeSeq.Empty
      else {
        "data-name=author-name *" #> post.author.obj.dmap("")(_.name.get) &
        "data-name=author-name [onclick]" #> SHtml.ajaxInvoke(() => {
          RedirectTo(Site.blog.url, () => authorBlogRequestVar.set(post.author.obj))
        })
      }
     ) &
    (
      if(post.categories.get.isEmpty)
        "data-name=categories-tag" #> NodeSeq.Empty
      else {
        "data-name=categories-tag" #> post.categories.get.map(cat => {
          "data-name=category-tag *" #> cat.tag.get &
          "data-name=category-tag [onclick]" #> SHtml.ajaxInvoke(() => {
            RedirectTo(Site.blog.url, () => categoryBlogRequestVar.set(Full(Tag.createRecord.tag(cat.tag.get))))
          })
        })
      }
    ) &
    (
        if(post.tags.get.isEmpty)
          "data-name=tags" #> NodeSeq.Empty
        else {
          "data-name=tags" #> post.tags.get.map(cat => {
            "data-name=tag *" #> cat.tag.get &
              "data-name=tag [onclick]" #> SHtml.ajaxInvoke(() => {
                RedirectTo(Site.blog.url, () => tagBlogRequestVar.set(Full(Tag.createRecord.tag(cat.tag.get))))
              })
          })
        }
    ) &
    (
      if(post.program.obj.isEmpty)
        "data-name=programs" #> NodeSeq.Empty
      else {
        "data-name=program *" #> post.program.toString &
          "data-name=program [onclick]" #> SHtml.ajaxInvoke(() => {
            RedirectTo(Site.blog.url, () => programBlogRequestVar.set(post.program.obj))
          })
      }
    ) &
    (
      if(post.values.get.isEmpty)
        "data-name=values" #> NodeSeq.Empty
      else {
        "data-name=values *" #> post.values.get.map(value => {
          "data-name=value *" #> Values.find(value).dmap("")(_.name.get) &
            "data-name=value [onclick]" #> SHtml.ajaxInvoke(() => {
              RedirectTo(Site.blog.url, () => valuesBlogRequestVar.set(Values.find(value)))
            })
        })
      }
      ) &
    (
      if(post.actionLines.get.isEmpty)
        "data-name=actionLines" #> NodeSeq.Empty
      else {
        "data-name=actionLines *" #> post.actionLines.get.map(actionLine => {
          "data-name=actionLine *" #> ActionLine.find(actionLine).dmap("")(_.name.get) &
            "data-name=actionLine [onclick]" #> SHtml.ajaxInvoke(() => {
              RedirectTo(Site.blog.url, () => actionLineBlogRequestVar.set(ActionLine.find(actionLine)))
            })
        })
      }
      ) &
    "data-name=date *" #> post.date.toString
  }

  def renderViewFrontEnd: CssSel = {
    for {
      post <- Site.entradaBlog.currentValue ?~ "Entrada no encontrada"
    } yield {
      val next = BlogPost.findNext(post)
      val prev = BlogPost.findNext(post)
      val related = BlogPost.findRelated(post, 3)
      val category = categoryBlogRequestVar.get
      imageCss(post) &
      "data-name=title *" #> post.name.get &
      "data-name=title [href]" #> Site.entradaBlog.calcHref(post) &
      "data-name=content *" #> post.content.asHtml &
      "data-name=all-categories" #> {
        "li [class+]" #> (if (category.isEmpty) "active" else "") &
          "a [href]" #> Site.blog.fullUrl
      } &
      "data-name=categories" #> meta.findCategories.map(cat => {
        "li [class+]" #> (if (category.dmap("")(_.tag.get) == cat) "active" else "") &
          "a [onclick]" #> SHtml.ajaxInvoke(() => {
            RedirectTo(Site.blog.url, () => categoryBlogRequestVar.set(Full(Tag.createRecord.tag(cat))))
          }) &
          "a *" #> cat
      }) &
      renderTags(post) &
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

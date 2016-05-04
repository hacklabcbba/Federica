package code.snippet

import code.config
import code.config.Site
import code.lib.snippet.PaginatorSnippet
import code.model._
import net.liftweb.common.{Box, Full}
import net.liftweb.http.{S, SHtml, Templates}
import net.liftweb.util.{CssSel, Helpers, PassThru}
import Helpers._
import code.lib.request.request._
import net.liftweb.http.js.JsCmds.RedirectTo
import org.bson.types.ObjectId

import scala.xml.{NodeSeq, Text}

object BlogSnippet extends ListSnippet[BlogPost] with PaginatorSnippet[BlogPost] {

  val meta = BlogPost

  val title = "Entradas del blog"

  val addUrl = Site.backendBlogAdd.calcHref(BlogPost.createRecord)

  override def itemsPerPage = 10

  override def count = meta.countPublishedByFilters(getParameter)
  
  override def page = meta.findPostPublishedByFilters(getParameter, itemsPerPage, curPage)

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
    val parameter = getParameter.headOption.fold("")(_._1)
    val value = getParameter.headOption.fold("")(_._2)
    "data-name=category *" #> value &
    "data-name=post" #> page.map(post => {
      previewCss(post) &
      "data-name=title *" #> post.name.get &
      renderTags(post)&
      "data-name=title [href]" #> Site.entradaBlog.calcHref(post) &
      "data-name=content *" #> post.content.asHtmlCutted(200)
    }) &
    "data-name=all-categories" #> {
      "li [class+]" #> (if (((parameter == "category") && (value.isEmpty)) || (parameter != "category")) "active" else "") &
      "a [href]" #> Site.blog.fullUrl
    } &
    "data-name=categories" #> meta.findCategories.map(cat => {
      "li [class+]" #> (if ((parameter == "category") && (value == cat)) "active" else "") &
      "a [href]" #> s"${Site.blog.fullUrl}?category=$cat" &
      "a *" #> cat
    }) &
    paginate
  }

  def getParameter: List[(String, String)] = {
    val parameters : Map[String,List[String]] = S.request.toList.flatMap(_.params).toMap
    val key = parameters.keys.headOption
    val value = parameters.values.headOption.map(_.headOption).flatten
    (key, value) match {
      case (Some(k), Some(v)) =>
        List((k, v))
      case _ =>
        Nil
    }
  }

  def renderTags(post: BlogPost): CssSel = {
    (post.area.obj match {
      case Full(area) =>
        "data-name=area-name *" #> area.name.get &
        "data-name=area-name [href]" #> s"${Site.blog.fullUrl}?area=${area.name.get}"
      case _ =>
        "data-name=area" #> NodeSeq.Empty
      }
    ) &
    (post.transversalArea.obj match {
      case Full(area) =>
        "data-name=transversalarea-name *" #> area.name.get &
        "data-name=transversalarea-name [href]" #> s"${Site.blog.fullUrl}?areaT=${area.name.get}"
      case _ =>
        "data-name=transversalarea" #> NodeSeq.Empty
      }
    ) &
    (post.process.obj match {
      case Full(process) =>
        "data-name=process-name *" #> process.name.get &
        "data-name=process-name [href]" #> s"${Site.blog.fullUrl}?proceso=${process.name.get}"
      case _ =>
        "data-name=transversalarea" #> NodeSeq.Empty
      }
    ) &
    (post.author.obj match {
      case Full(author) =>
        "data-name=author-name *" #> author.name.get &
        "data-name=author-name [href]" #> s"${Site.blog.fullUrl}?autor=${author.name.get}"
      case _ =>
        "data-name=author" #> NodeSeq.Empty
      }
    ) &
    (post.categories.get match {
      case (tags: List[Tag]) =>
        "data-name=categories-tag" #> tags.map(cat => {
          "data-name=category-tag *" #> cat.tag.get &
          "data-name=category-tag [href]" #> s"${Site.blog.fullUrl}?categoria=${cat.tag.get}"
        })
      case Nil =>
        "data-name=categories-tag" #> NodeSeq.Empty &
        "data-name=tag-category-title" #> NodeSeq.Empty
      }
    ) &
    (post.tags.get match {
      case (tags: List[Tag]) =>
        "data-name=tags" #> tags.map(cat => {
          "data-name=tag *" #> cat.tag.get &
          "data-name=tag [href]" #> s"${Site.blog.fullUrl}?etiqueta=${cat.tag.get}"
        })
      case Nil =>
        "data-name=tags" #> NodeSeq.Empty &
        "data-name=tags-title" #> NodeSeq.Empty
      }
    ) &
    (post.program.obj match {
      case Full(program) =>
        "data-name=program *" #> program.name.get &
        "data-name=program [href]" #> s"${Site.blog.fullUrl}?programa=${program.name.get}"
      case _ =>
        "data-name=programs" #> NodeSeq.Empty
      }
    ) &
    (post.values.get.filter(v => Value.find(v).dmap("")(_.name.get).trim != "").isEmpty match {
      case false =>
        "data-name=values" #> post.values.get.map((va: ObjectId) => {
          "data-name=value *" #> Value.find(va).dmap("")(_.name.get) &
          "data-name=value [href]" #> s"${Site.blog.fullUrl}?principio=${Value.find(va).dmap("")(_.name.get)}"
        })
      case true =>
        "data-name=values-title" #> NodeSeq.Empty &
        "data-name=values" #> NodeSeq.Empty
      }
    ) &
    (post.actionLines.get.filter(v => ActionLine.find(v).dmap("")(_.name.get).trim != "") match {
      case (actionLine: List[ActionLine]) =>
        "data-name=actionLines" #> actionLine.map((al: ObjectId) => {
          "data-name=actionLine *" #> ActionLine.find(al).dmap("")(_.name.get) &
          "data-name=actionLine [href]" #> s"${Site.blog.fullUrl}?lineaAccion=${ActionLine.find(al).dmap("")(_.name.get)}"
        })
      case Nil =>
        "data-name=actionLines" #> NodeSeq.Empty &
        "data-name=actionLine-title" #> NodeSeq.Empty
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
      val parameter = getParameter.headOption.fold("")(_._1)
      val value = getParameter.headOption.fold("")(_._2)
      imageCss(post) &
      "data-name=title *" #> post.name.get &
      "data-name=title [href]" #> Site.entradaBlog.calcHref(post) &
      "data-name=content *" #> post.content.asHtml &
      "data-name=all-categories" #> {
        "li [class+]" #> (if (((parameter == "category") && (value.isEmpty)) || (parameter != "category")) "active" else "") &
          "a [href]" #> Site.blog.fullUrl
      } &
      "data-name=categories" #> meta.findCategories.map(cat => {
        "li [class+]" #> (if ((parameter == "category") && (value == cat)) "active" else "") &
          "a [href]" #> s"${Site.blog.fullUrl}?category=$cat" &
          "a *" #> cat
      }) &
      renderTags(post) &
      prevItemCss(prev) &
      nextItemCss(next) &
      relatedCss(related)
    }
  }

  def templateRelatedBlog =
    Templates("templates-hidden" :: "frontend" :: "_relatedPosts" :: Nil) openOr Text(S ? "No edit template found")

  def relatedPosts(title: String, values: Box[Value], program: Box[Program], area: Box[Area],
                   actionLine: Box[ActionLine], transversalArea: Box[TransversalArea],
                   transversalApproach: Box[TransversalApproach], process: Box[Process]): NodeSeq = {
    renderLastThreePostByFilter(title, values, program, area, actionLine, transversalArea, transversalApproach,
      process).apply(templateRelatedBlog)
  }

  def renderLastThreePostByFilter(title: String, values: Box[Value], program: Box[Program], area: Box[Area],
                                  actionLine: Box[ActionLine], transversalArea: Box[TransversalArea],
                                  transversalApproach: Box[TransversalApproach], process: Box[Process]): CssSel = {
    val listPosts = BlogPost.findPublishedByFilters(values, program, area, actionLine, transversalArea,
      transversalApproach, process)
    !listPosts.isEmpty match {
      case true =>
        "data-name=title-module" #> title &
        "data-name=posts" #> listPosts.map(post => {
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

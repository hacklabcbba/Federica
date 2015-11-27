package code.snippet

import code.config.Site
import code.model.BlogPost

object BlogSnippet extends ListSnippet[BlogPost] {

  val meta = BlogPost

  val title = "Entradas del blog"

  val addUrl = Site.backendBlogAdd.calcHref(BlogPost.createRecord)

  def entityListUrl: String = Site.backendBlog.menu.loc.calcDefaultHref

  def itemEditUrl(inst: BlogPost): String = Site.backendBlogEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.author, meta.area, meta.date)

}

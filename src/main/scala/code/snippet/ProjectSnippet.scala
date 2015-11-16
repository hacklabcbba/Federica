package code.snippet

import code.config.Site
import code.model.Project

object ProjectSnippet extends ListSnippet[Project] {

  val meta = Project

  val title = "Proyecto"

  val addUrl = Site.backendProjectAdd.calcHref(Project.createRecord)

  def entityListUrl: String = Site.backendProjects.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Project): String = Site.backendProjectEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.administrator, meta.area)

}

package code.snippet

import code.config.Site
import code.model.Area

object AreaSnippet extends ListSnippet[Area] {

  val meta = Area

  val addUrl = Site.backendAreaAdd.calcHref(Area.createRecord)

  def entityListUrl: String = Site.backendAreas.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Area): String = Site.backendAreaEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.responsible, meta.email)

}

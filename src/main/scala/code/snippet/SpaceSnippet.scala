package code.snippet

import code.config.Site
import code.model.Area
import code.model.network.Space

object SpaceSnippet extends ListSnippet[Space] {

  val meta = Space

  val addUrl = Site.backendSpaceAdd.calcHref(Space.createRecord)

  def entityListUrl: String = Site.backendSpaces.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Space): String = Site.backendSpaceEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.email)

}

package code.snippet

import code.config.Site
import code.model.User

object BackendUserSnippet extends ListSnippet[User] {

  val meta = User

  val title = "Usuarios"

  val addUrl = Site.backendUserAdd.calcHref(User.createRecord)

  def entityListUrl: String = Site.backendUsers.menu.loc.calcDefaultHref

  def itemEditUrl(inst: User): String = Site.backendUserEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.email, meta.roles)

}

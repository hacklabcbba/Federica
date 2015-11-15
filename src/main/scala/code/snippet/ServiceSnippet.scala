package code
package snippet

import code.config.Site
import code.model.Service

object ServiceSnippet extends ListSnippet[Service] {

  val meta = Service

  val addUrl = Site.backendServiceAdd.calcHref(Service.createRecord)

  def entityListUrl: String = Site.backendServices.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Service): String = Site.backendServiceEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.responsible, meta.email)

}

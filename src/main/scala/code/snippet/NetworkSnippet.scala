package code.snippet

import code.config.Site
import code.model.network.Network

object NetworkSnippet extends ListSnippet[Network] {

  val meta = Network

  val title = "Redes"

  val addUrl = Site.backendNetworkAdd.calcHref(Network.createRecord)

  def entityListUrl: String = Site.backendNetworks.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Network): String = Site.backendNetworkEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.networkType, meta.administrator)

}

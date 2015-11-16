package code.snippet

import code.config.Site
import code.model.Process

object ProcessSnippet extends ListSnippet[Process] {

  val meta = Process

  val title = "Procesos"

  val addUrl = Site.backendProcessAdd.calcHref(Process.createRecord)

  def entityListUrl: String = Site.backendProcess.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Process): String = Site.backendProcessEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.processType, meta.administrator)

}

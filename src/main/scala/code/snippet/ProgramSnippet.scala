package code.snippet

import code.config.Site
import code.model.Program

object ProgramSnippet extends ListSnippet[Program] {

  val meta = Program

  val addUrl = Site.backendProgramAdd.calcHref(Program.createRecord)

  def entityListUrl: String = Site.backendPrograms.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Program): String = Site.backendProgramEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.responsible, meta.email)

}

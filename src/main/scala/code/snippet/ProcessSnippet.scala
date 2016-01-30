package code.snippet

import code.config.Site
import code.model.Process
import com.foursquare.rogue.LiftRogue
import net.liftmodules.extras.SnippetHelper
import net.liftweb.http.js.JsCmd
import net.liftweb.json.JsonAST.JValue
import LiftRogue._
import net.liftweb.util.{CssSel, Helpers}
import Helpers._
import net.liftweb.http.js.JsCmds._

object ProcessSnippet extends SortableSnippet[Process] {

  val meta = Process

  val title = "Procesos"

  val addUrl = Site.backendProcessAdd.calcHref(Process.createRecord)

  def entityListUrl: String = Site.backendProcess.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Process): String = Site.backendProcessEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.administrator)

  def updateOrderValue(json: JValue): JsCmd = {
    implicit val formats = net.liftweb.json.DefaultFormats
    for {
      id <- tryo((json \ "id").extract[String])
      order <- tryo((json \ "order").extract[Long])
      item <- meta.find(id)
    } yield meta.where(_.id eqs item.id.get).modify(_.order setTo order).updateOne()
    Noop
  }
}

//ToDo fix null pointer
class ProcesoSnippet(proceso: Process) extends SnippetHelper {

  def renderViewFrontEnd: CssSel = {
    "data-name=name *" #> proceso.name.get &
    "data-name=name [href]" #> Site.proceso.calcHref(proceso) &
    "data-name=description *" #> proceso.description.asHtml
  }

}

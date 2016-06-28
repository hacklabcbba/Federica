package code.snippet

import code.config.Site
import code.model.Project
import com.foursquare.rogue.LiftRogue
import net.liftweb.http.js.JsCmd
import net.liftweb.json.JsonAST.JValue
import LiftRogue._
import net.liftweb.util.Helpers
import Helpers._
import net.liftweb.http.js.JsCmds._

object ProjectSnippet extends ListSnippet[Project] {

  val meta = Project

  val title = "Proyecto"

  val addUrl = Site.backendProjectAdd.calcHref(Project.createRecord)

  def entityListUrl: String = Site.backendProjects.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Project): String = Site.backendProjectEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.administrator, meta.area)

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

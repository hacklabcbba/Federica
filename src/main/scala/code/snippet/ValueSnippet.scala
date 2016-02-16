package code
package snippet

import code.config.Site
import code.model.Value
import com.foursquare.rogue.LiftRogue
import com.foursquare.rogue.LiftRogue._
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds._
import net.liftweb.json.JsonAST.JValue
import net.liftweb.util.Helpers._
import net.liftweb.util.{CssSel, Helpers}

object ValueSnippet extends SortableSnippet[Value] {

  val meta = Value

  val title = "Principios"

  val addUrl = Site.backendValueAdd.calcHref(Value.createRecord)

  def entityListUrl: String = Site.backendValues.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Value): String = Site.backendValueEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.url)

  def renderViewFrontEnd: CssSel = {
    for {
      value <- Site.principio.currentValue
    } yield {
      "data-name=name *" #> value.name.get &
      "data-name=name [href]" #> Site.principio.calcHref(value) &
      "data-name=description *" #> value.description.asHtml
    }
  }

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

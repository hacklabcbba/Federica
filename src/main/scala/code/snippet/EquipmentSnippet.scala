package code.snippet

import code.config.Site
import code.model.resource.Equipment
import com.foursquare.rogue.LiftRogue
import net.liftweb.http.js.JsCmd
import net.liftweb.json.JsonAST.JValue
import LiftRogue._
import net.liftweb.util.Helpers
import Helpers._
import net.liftweb.http.js.JsCmds._

object EquipmentSnippet extends SortableSnippet[Equipment] {

  val meta = Equipment

  override def items: List[Equipment] = meta.findAll.sortBy(_.name.get)

  val title = "Equipos"

  val addUrl = Site.backendEquipmentAdd.calcHref(Equipment.createRecord)

  def entityListUrl: String = Site.backendEquipments.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Equipment): String = Site.backendEquipmentEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.cost, meta.equipmentType)

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

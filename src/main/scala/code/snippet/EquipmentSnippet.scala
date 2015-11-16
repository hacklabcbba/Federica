package code.snippet

import code.config.Site
import code.model.resource.Equipment

object EquipmentSnippet extends ListSnippet[Equipment] {

  val meta = Equipment

  val title = "Equipos"

  val addUrl = Site.backendEquipmentAdd.calcHref(Equipment.createRecord)

  def entityListUrl: String = Site.backendEquipments.menu.loc.calcDefaultHref

  def itemEditUrl(inst: Equipment): String = Site.backendEquipmentEdit.toLoc.calcHref(inst)

  override def listFields = List(meta.name, meta.cost, meta.equipmentType)

}

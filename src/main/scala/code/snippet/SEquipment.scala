package code.snippet

import code.lib.menu.EquipmentMenu
import code.lib.request.request._
import code.model.resource.{Equipment, ClassType}
import net.liftmodules.extras.SnippetHelper
import net.liftweb.common.Full
import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds._
import net.liftweb.util.Helpers._
import net.liftweb.util._

object SEquipment extends SnippetHelper {

  val menu = EquipmentMenu

  def addForm: CssSel = {
    val item = Equipment.createRecord
    "data-name=name *" #> item.name.toForm &
    "data-name=description *" #> item.description.toForm &
    "data-name=nameGroup *" #> item.nameGroup.toForm &
    "type=submit" #> SHtml.ajaxOnSubmit(() => save(item))&
    "type=cancel" #> SHtml.ajaxButton("Cancelar", () => RedirectTo(menu.menuList.url), "class"->"btn btn-default" )
  }

  def editForm: CssSel = {
    for {
        item <- equipmentRequestVar.get ?~ "Equipo no definido"
    } yield {
      "data-name=name *" #> item.name.toForm &
      "data-name=description *" #> item.description.toForm &
      "data-name=nameGroup *" #> item.nameGroup.toForm &
      "type=submit" #> SHtml.ajaxOnSubmit(() => update(item)) &
      "type=cancel" #> SHtml.ajaxButton("Cancelar", () => RedirectTo(menu.menuList.url), "class"->"btn btn-default" )
    }: CssSel
  }

  def showAll = {
    "data-name=list" #> page.map(
      item => {
        "data-name=checkbox *" #> customCheckbox(item) &
        "data-name=name *"  #> item.name &
        "data-name=nameGroup *"  #> item.nameGroup.toString &
        "data-name=edit *" #> SHtml.ajaxButton(
          "Editar",
          () => RedirectTo(menu.menuEdit.url, () => equipmentRequestVar.set(Full(item))),
          "class" -> "btn btn-default"
        )
      }) &
    "data-name=add" #> SHtml.ajaxButton("Agregar equipo", () => RedirectTo(menu.menuAdd.url), "class"->"btn btn-primary" ) &
    "data-name=delete" #> SHtml.ajaxButton(
      "Eliminar",
      () => {
        val listToDelete = equipmentDeleteRequestVar.get
        RedirectTo(menu.menuList.url, () => delete(listToDelete))
      },
      "class"->"btn btn-danger")
  }

  def customCheckbox(item: Equipment) = {
    SHtml.ajaxCheckbox(false, b => updateDeleteList(b, item), "class" -> "checkbox-list")
  }

  def updateDeleteList(value: Boolean, e: Equipment): JsCmd = value match {
    case true =>
      equipmentDeleteRequestVar.set(e :: equipmentDeleteRequestVar.is)
      Noop
    case false =>
      equipmentDeleteRequestVar.set(equipmentDeleteRequestVar.is.filter(b => b.id != e.id))
      Noop
  }

  def page = Equipment.findAll.filter( r => r.classType.get == ClassType.EquipmentType)

  def save(item: Equipment) = {
    item.classType(ClassType.EquipmentType)
    item.save(true)
    redirectToHome
  }

  def update(item: Equipment) = {
    item.classType(ClassType.EquipmentType)
    item.update
    redirectToHome
  }

  def delete(items: List[Equipment]) = {
    items.map(e => {
      e.delete_!
    })
  }

  def redirectToHome = {
    RedirectTo(menu.menuList.url)
  }
}

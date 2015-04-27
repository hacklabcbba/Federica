package code.snippet

import code.lib.menu.{RoomMenu, EventMenu}
import code.lib.request.request._
import code.model.Setting
import code.model.event._
import code.model.resource.{ClassType, Room}
import net.liftmodules.extras.SnippetHelper
import net.liftweb.common.Full
import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds._
import net.liftweb.util.Helpers._
import net.liftweb.util._

object SRoom extends SnippetHelper {

  val menu = RoomMenu

  def addForm: CssSel = {
    val item = Room.createRecord
    "data-name=name *" #> item.name.toForm &
    "data-name=description *" #> item.description.toForm &
    "data-name=capacity *" #> item.capacity.toForm &
    "data-name=code *" #> item.code.toForm &
    "data-name=state *" #> item.state.toForm &
    "data-name=plane *" #> item.plane.toForm &
    "data-name=isReservable *" #> item.isReservable.toForm &
    "type=submit" #> SHtml.ajaxOnSubmit(() => save(item))&
    "type=cancel" #> SHtml.ajaxButton("Cancelar", () => RedirectTo(menu.menuList.url), "class"->"btn btn-default" )
  }

  def editForm: CssSel = {
    for {
        item <- roomRequestVar.get ?~ "Sala no definida"
    } yield {
      "data-name=name *" #> item.name.toForm &
      "data-name=description *" #> item.description.toForm &
      "data-name=capacity *" #> item.capacity.toForm &
      "data-name=code *" #> item.code.toForm &
      "data-name=state *" #> item.state.toForm &
      "data-name=plane *" #> item.plane.toEditForm &
      "data-name=isReservable *" #> item.isReservable.toForm &
      "type=submit" #> SHtml.ajaxOnSubmit(() => update(item)) &
      "type=cancel" #> SHtml.ajaxButton("Cancelar", () => RedirectTo(menu.menuList.url), "class"->"btn btn-default" )
    }:CssSel
  }

  def showAll = {
    "data-name=list" #> page.map(
      item => {
        "data-name=checkbox *" #> customCheckbox(item) &
        "data-name=name *"  #> item.name &
        "data-name=capacity *"  #> item.capacity.toString &
        "data-name=code *" #> item.code.toString &
        "data-name=state *" #> item.state.toString &
        "data-name=plane *" #> item.plane.toString &
        "data-name=isReservable *" #> item.isReservable.toString &
        "data-name=edit *" #> SHtml.ajaxButton(
          "Editar",
          () => RedirectTo(menu.menuEdit.url, () => roomRequestVar.set(Full(item))),
          "class" -> "btn btn-default"
        )
      }) &
    "data-name=add" #> SHtml.ajaxButton("Agregar sala", () => RedirectTo(menu.menuAdd.url), "class"->"btn btn-primary" ) &
    "data-name=delete" #> SHtml.ajaxButton(
      "Eliminar",
      () => {
        val listToDelete = roomDeleteRequestVar.get
        RedirectTo(menu.menuList.url, () => delete(listToDelete))
      },
      "class"->"btn btn-danger")
  }

  def customCheckbox(item: Room) = {
    SHtml.ajaxCheckbox(false, b => updateDeleteList(b, item), "class" -> "checkbox-list")
  }

  def updateDeleteList(value: Boolean, e: Room): JsCmd = value match {
    case true =>
      roomDeleteRequestVar.set(e :: roomDeleteRequestVar.is)
      Noop
    case false =>
      roomDeleteRequestVar.set(roomDeleteRequestVar.is.filter(b => b.id != e.id))
      Noop
  }

  def page = Room.findAll.filter( r => r.classType.get == ClassType.RoomType)

  def save(item: Room) = {
    item.classType(ClassType.RoomType)
    item.save(true)
    redirectToHome
  }

  def update(item: Room) = {
    item.classType(ClassType.RoomType)
    item.update
    redirectToHome
  }

  def delete(items: List[Room]) = {
    items.map(e => {
      e.delete_!
    })
  }

  def redirectToHome = {
    RedirectTo(menu.menuList.url)
  }

}

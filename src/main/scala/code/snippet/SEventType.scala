package code
package snippet

import net.liftweb.common.{Full, Failure, Empty}
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.{SHtml, PaginatorSnippet, StatefulSnippet}
import net.liftweb.util._
import Helpers._
import net.liftmodules.extras.SnippetHelper
import code.lib.request.request._
import code.model.event.EventType
import code.lib.menu.EventTypeMenu

object SEventType extends SnippetHelper {

  val menu = EventTypeMenu

  def addForm: CssSel = {
    val item = EventType.createRecord
    "data-name=name *" #> item.name.toForm &
    "type=submit" #> SHtml.ajaxOnSubmit(() => save(item))&
    "type=cancel" #> SHtml.ajaxButton("Cancelar", () => RedirectTo(menu.menuList.url), "class"->"btn btn-default" )
  }

  def editForm: CssSel = {
    for {
      item <- eventTypeRequestVar.get ?~ "Tipo de Evento no definida"
    } yield {
      "data-name=eventTypeName *" #> item.name &
      "data-name=name *" #> item.name.toForm &
      "type=submit" #> SHtml.ajaxOnSubmit(() => update(item)) &
      "type=cancel" #> SHtml.ajaxButton("Cancelar", () => RedirectTo(menu.menuList.url), "class"->"btn btn-default" )
    }:CssSel
  }

  def showAll = {
    "data-name=list" #> page.map(
      item => {
        "data-name=checkbox *" #> customCheckbox(item) &
        "data-name=name *"  #> item.name &
          "data-name=edit *" #> SHtml.ajaxButton(
            "Editar",
            () => RedirectTo(menu.menuEdit.url, () => eventTypeRequestVar.set(Full(item))),
            "class" -> "btn btn-default"
          )
      }) &
      "data-name=add" #> SHtml.ajaxButton("Agregar tipo de evento", () => RedirectTo(menu.menuAdd.url), "class"->"btn btn-primary" ) &
      "data-name=delete" #> SHtml.ajaxButton(
        "Eliminar",
        () => {
          val list = eventTypeDeleteRequestVar.get
          RedirectTo(menu.menuList.url, () => delete(list))
        },
        "class"->"btn btn-danger")
  }

  def customCheckbox(item: EventType) = {
    SHtml.ajaxCheckbox(false, b => updateDeleteList(b, item), "class" -> "checkbox-list")
  }

  def updateDeleteList(value: Boolean, item: EventType): JsCmd = value match {
    case true =>
      eventTypeDeleteRequestVar.set(item :: eventTypeDeleteRequestVar.is)
      Noop
    case false =>
      eventTypeDeleteRequestVar.set(eventTypeDeleteRequestVar.is.filter(b => b.id != item.id))
      Noop
  }

  def page = EventType.findAll

  def save(item: EventType) = {
    item.save(true)
    redirectToHome
  }

  def update(item: EventType) = {
    item.update
    redirectToHome
  }

  def delete(items: List[EventType]) = {
    items.map(e => {
      e.delete_!
    })
  }

  def redirectToHome = {
    RedirectTo(menu.menuList.url)
  }

}

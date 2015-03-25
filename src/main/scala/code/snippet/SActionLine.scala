package code.snippet

import code.lib.request.request._
import net.liftweb.common.{Full, Failure, Empty}
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.{SHtml, PaginatorSnippet, StatefulSnippet}
import code.model.event._
import net.liftweb.util._
import Helpers._
import net.liftmodules.extras.SnippetHelper
import code.lib.menu.ActionLineMenu
import code.model.proposal.ActionLine

object SActionLine extends SnippetHelper {

  val menu = ActionLineMenu

  def addForm: CssSel = {
    val item = ActionLine.createRecord
    "data-name=name *" #> item.name.toForm &
    "data-name=description *" #> item.description.toForm &
    "type=submit" #> SHtml.ajaxOnSubmit(() => save(item))&
    "type=cancel" #> SHtml.ajaxButton("Cancelar", () => RedirectTo(menu.menuList.url), "class"->"btn btn-default" )
  }

  def editForm: CssSel = {
    for {
      item <- actionLineRequestVar.get ?~ "Linea de acción no definido"
    } yield {
      "data-name=actionLineName *" #> item.name &
      "data-name=name *" #> item.name.toForm &
      "data-name=description *" #> item.description.toForm &
      "type=submit" #> SHtml.ajaxOnSubmit(() => update(item)) &
      "type=cancel" #> SHtml.ajaxButton("Cancelar", () => RedirectTo(menu.menuList.url), "class"->"btn btn-default" )
    }:CssSel
  }

  def showAll = {
    "data-name=list" #> page.map(
      item => {
        "data-name=checkbox *" #> customCheckbox(item) &
        "data-name=name *"  #> item.name &
        "data-name=description *"  #> item.description.toString &
        "data-name=edit *" #> SHtml.ajaxButton(
          "Editar",
          () => RedirectTo(menu.menuEdit.url, () => actionLineRequestVar.set(Full(item))),
          "class" -> "btn btn-default"
        )
      }) &
    "data-name=add" #> SHtml.ajaxButton("Agregar Linea de acción", () => RedirectTo(menu.menuAdd.url), "class"->"btn btn-primary" ) &
    "data-name=delete" #> SHtml.ajaxButton(
      "Eliminar",
      () => {
        val list = actionLinetDeleteRequestVar.get
        RedirectTo(menu.menuList.url, () => delete(list))
      },
      "class"->"btn btn-danger")
  }

  def customCheckbox(item: ActionLine) = {
    SHtml.ajaxCheckbox(false, b => updateDeleteList(b, item), "class" -> "checkbox-list")
  }

  def updateDeleteList(value: Boolean, item: ActionLine): JsCmd = value match {
    case true =>
      actionLinetDeleteRequestVar.set(item :: actionLinetDeleteRequestVar.is)
      Noop
    case false =>
      actionLinetDeleteRequestVar.set(actionLinetDeleteRequestVar.is.filter(b => b.id != item.id))
      Noop
  }

  def page = ActionLine.findAll

  def save(a: ActionLine) = {
    a.save(true)
    redirectToHome
  }

  def update(a: ActionLine) = {
    a.update
    redirectToHome
  }

  def delete(items: List[ActionLine]) = {
    items.map(e => {
      e.delete_!
    })
  }

  def redirectToHome = {
    RedirectTo(menu.menuList.url)
  }

}

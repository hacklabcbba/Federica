package code
package snippet

import net.liftweb.common.{Full, Failure, Empty}
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.{SHtml, PaginatorSnippet, StatefulSnippet}
import net.liftweb.util._
import Helpers._
import net.liftmodules.extras.SnippetHelper
import code.lib.menu.AreaMenu
import code.model.proposal.Area
import code.lib.request.request._

object SArea extends SnippetHelper {

  val menu = AreaMenu

  def addForm: CssSel = {
    val item = Area.createRecord
    "data-name=name *" #> item.name.toForm &
    "data-name=description *" #> item.description.toForm &
    "data-name=email *" #> item.email.toForm &
    "data-name=code *" #> item.code.toForm &
    "type=submit" #> SHtml.ajaxOnSubmit(() => save(item))&
    "type=cancel" #> SHtml.ajaxButton("Cancelar", () => RedirectTo(menu.menuList.url), "class"->"btn btn-default" )
  }

  def editForm: CssSel = {
    for {
      item <- areaRequestVar.get ?~ "Area no definida"
    } yield {
      "data-name=areaName *" #> item.name &
      "data-name=name *" #> item.name.toForm &
      "data-name=description *" #> item.description.toForm &
      "data-name=email *" #> item.email.toForm &
      "data-name=code *" #> item.code.toForm &
      "type=submit" #> SHtml.ajaxOnSubmit(() => update(item)) &
      "type=cancel" #> SHtml.ajaxButton("Cancelar", () => RedirectTo(menu.menuList.url), "class"->"btn btn-default" )
    }:CssSel
  }

  def showAll = {
    "data-name=list" #> page.map(
      item => {
        "data-name=checkbox *" #> customCheckbox(item) &
        "data-name=name *"  #> item.name &
        "data-name=email *"  #> item.email &
        "data-name=code *"  #> item.code &
          "data-name=edit *" #> SHtml.ajaxButton(
            "Editar",
            () => RedirectTo(menu.menuEdit.url, () => areaRequestVar.set(Full(item))),
            "class" -> "btn btn-default"
          )
      }) &
      "data-name=add" #> SHtml.ajaxButton("Agregar Area", () => RedirectTo(menu.menuAdd.url), "class"->"btn btn-primary" ) &
      "data-name=delete" #> SHtml.ajaxButton(
        "Eliminar",
        () => {
          val list = areaDeleteRequestVar.get
          RedirectTo(menu.menuList.url, () => delete(list))
        },
        "class"->"btn btn-danger")
  }

  def customCheckbox(item: Area) = {
    SHtml.ajaxCheckbox(false, b => updateDeleteList(b, item), "class" -> "checkbox-list")
  }

  def updateDeleteList(value: Boolean, item: Area): JsCmd = value match {
    case true =>
      areaDeleteRequestVar.set(item :: areaDeleteRequestVar.is)
      Noop
    case false =>
      areaDeleteRequestVar.set(areaDeleteRequestVar.is.filter(b => b.id != item.id))
      Noop
  }

  def page = Area.findAll

  def save(item: Area) = {
    item.save(true)
    redirectToHome
  }

  def update(item: Area) = {
    item.update
    redirectToHome
  }

  def delete(items: List[Area]) = {
    items.map(e => {
      e.delete_!
    })
  }

  def redirectToHome = {
    RedirectTo(menu.menuList.url)
  }

}

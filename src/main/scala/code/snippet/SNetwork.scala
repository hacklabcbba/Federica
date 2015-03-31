package code
package snippet

import code.lib.request.request._
import net.liftweb.common.{Full, Failure, Empty}
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.{SHtml, PaginatorSnippet, StatefulSnippet}
import code.model.event._
import net.liftweb.util._
import Helpers._
import net.liftmodules.extras.SnippetHelper
import code.lib.menu.{NetworkMenu, ActionLineMenu}
import code.model.proposal.ActionLine
import code.model.network.Network

object SNetwork extends SnippetHelper {

  val menu = NetworkMenu

  def addForm: CssSel = {
    val item = Network.createRecord
    "data-name=name *" #> item.name.toForm &
    "data-name=description *" #> item.description.toForm &
    "data-name=administrator *" #> item.administrator.toForm &
    "data-name=area *" #> item.area.toForm &
    "data-name=program *" #> item.program.toForm &
    "data-name=type *" #> item.networkType.toForm &
    "type=submit" #> SHtml.ajaxOnSubmit(() => save(item))&
    "type=cancel" #> SHtml.ajaxButton("Cancelar", () => RedirectTo(menu.menuList.url), "class"->"btn btn-default" )
  }

  def editForm: CssSel = {
    for {
      item <- networkRequestVar.get ?~ "Red no definida"
    } yield {
      "data-name=networkName *" #> item.name &
      "data-name=name *" #> item.name.toForm &
      "data-name=description *" #> item.description.toForm &
      "data-name=administrator *" #> item.administrator.toForm &
      "data-name=area *" #> item.area.toForm &
      "data-name=program *" #> item.program.toForm &
      "data-name=type *" #> item.networkType.toForm &
      "type=submit" #> SHtml.ajaxOnSubmit(() => update(item)) &
      "type=cancel" #> SHtml.ajaxButton("Cancelar", () => RedirectTo(menu.menuList.url), "class"->"btn btn-default" )
    }:CssSel
  }

  def showAll = {
    "data-name=list" #> page.map(
      item => {
        "data-name=checkbox *" #> customCheckbox(item) &
        "data-name=name *"  #> item.name &
        "data-name=type *"  #> item.networkType.toString &
        "data-name=area *"  #> item.area.toString &
        "data-name=program *"  #> item.program.toString &
        "data-name=administrator *"  #> item.administrator.toString &
        "data-name=edit *" #> SHtml.ajaxButton(
          "Editar",
          () => RedirectTo(menu.menuEdit.url, () => networkRequestVar.set(Full(item))),
          "class" -> "btn btn-default"
        )
      }) &
    "data-name=add" #> SHtml.ajaxButton("Agregar red", () => RedirectTo(menu.menuAdd.url), "class"->"btn btn-primary" ) &
    "data-name=delete" #> SHtml.ajaxButton(
      "Eliminar",
      () => {
        val list = networkDeleteRequestVar.get
        RedirectTo(menu.menuList.url, () => delete(list))
      },
      "class"->"btn btn-danger")
  }

  def customCheckbox(item: Network) = {
    SHtml.ajaxCheckbox(false, b => updateDeleteList(b, item), "class" -> "checkbox-list")
  }

  def updateDeleteList(value: Boolean, item: Network): JsCmd = value match {
    case true =>
      networkDeleteRequestVar.set(item :: networkDeleteRequestVar.is)
      Noop
    case false =>
      networkDeleteRequestVar.set(networkDeleteRequestVar.is.filter(b => b.id != item.id))
      Noop
  }

  def page = Network.findAll

  def save(a: Network) = {
    a.save(true)
    redirectToHome
  }

  def update(a: Network) = {
    a.update
    redirectToHome
  }

  def delete(items: List[Network]) = {
    items.map(e => {
      e.delete_!
    })
  }

  def redirectToHome = {
    RedirectTo(menu.menuList.url)
  }

}

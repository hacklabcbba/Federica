package code
package snippet

import code.lib.request.request._
import net.liftweb.common.{Full, Failure, Empty}
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.{SHtml, PaginatorSnippet, StatefulSnippet}
import net.liftweb.util._
import Helpers._
import net.liftmodules.extras.SnippetHelper
import code.lib.menu.{ProcessMenu, ActionLineMenu}
import code.model.process.Process

object SProcess extends SnippetHelper {

  val menu = ProcessMenu

  def addForm: CssSel = {
    val item = Process.createRecord
    "data-name=name *" #> item.name.toForm &
    "data-name=processType *" #> item.processType.toForm &
    "data-name=goal *" #> item.goal.toForm &
    "data-name=description *" #> item.description.toForm &
    "data-name=area *" #> item.area.toForm &
    "data-name=program *" #> item.program.toForm &
    "data-name=administrator *" #> item.administrator.toForm &
    "data-name=history *" #> item.history.toForm &
    "type=submit" #> SHtml.ajaxOnSubmit(() => save(item))&
    "type=cancel" #> SHtml.ajaxButton("Cancelar", () => RedirectTo(menu.menuList.url), "class"->"btn btn-default" )
  }

  def editForm: CssSel = {
    for {
      item <- processRequestVar.get ?~ "Proceso no definido"
    } yield {
      "data-name=processName *" #> item.name &
      "data-name=name *" #> item.name.toForm &
      "data-name=processType *" #> item.processType.toForm &
      "data-name=goal *" #> item.goal.toForm &
      "data-name=description *" #> item.description.toForm &
      "data-name=area *" #> item.area.toForm &
      "data-name=program *" #> item.program.toForm &
      "data-name=administrator *" #> item.administrator.toForm &
      "data-name=history *" #> item.history.toForm &
      "type=submit" #> SHtml.ajaxOnSubmit(() => update(item)) &
      "type=cancel" #> SHtml.ajaxButton("Cancelar", () => RedirectTo(menu.menuList.url), "class"->"btn btn-default" )
    }: CssSel
  }

  def showAll = {
    "data-name=list" #> page.map(
      item => {
        "data-name=checkbox *" #> customCheckbox(item) &
        "data-name=name *"  #> item.name &
        "data-name=processType *"  #> item.processType.toString &
        "data-name=area *" #> item.area.toString &
        "data-name=program *" #> item.program.toString &
        "data-name=administrator *" #> item.administrator.toString &
        "data-name=edit *" #> SHtml.ajaxButton(
          "Editar",
          () => RedirectTo(menu.menuEdit.url, () => processRequestVar.set(Full(item))),
          "class" -> "btn btn-default"
        )
      }) &
    "data-name=add" #> SHtml.ajaxButton("Agregar Proceso", () => RedirectTo(menu.menuAdd.url), "class"->"btn btn-primary" ) &
    "data-name=delete" #> SHtml.ajaxButton(
      "Eliminar",
      () => {
        val list = processDeleteRequestVar.get
        RedirectTo(menu.menuList.url, () => delete(list))
      },
      "class"->"btn btn-danger")
  }

  def customCheckbox(item: Process) = {
    SHtml.ajaxCheckbox(false, b => updateDeleteList(b, item), "class" -> "checkbox-list")
  }

  def updateDeleteList(value: Boolean, item: Process): JsCmd = value match {
    case true =>
      processDeleteRequestVar.set(item :: processDeleteRequestVar.is)
      Noop
    case false =>
      processDeleteRequestVar.set(processDeleteRequestVar.is.filter(b => b.id != item.id))
      Noop
  }

  def page = Process.findAll

  def save(a: Process) = {
    a.save(true)
    redirectToHome
  }

  def update(a: Process) = {
    a.update
    redirectToHome
  }

  def delete(items: List[Process]) = {
    items.map(e => {
      e.delete_!
    })
  }

  def redirectToHome = {
    RedirectTo(menu.menuList.url)
  }
}

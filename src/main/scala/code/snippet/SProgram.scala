package code.snippet

import net.liftweb.common.{Full, Failure, Empty}
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.{SHtml, PaginatorSnippet, StatefulSnippet}
import net.liftweb.util._
import Helpers._
import net.liftmodules.extras.SnippetHelper
import code.lib.menu._
import code.model.proposal.{Program, Area}
import code.lib.request.request._

object SProgram extends SnippetHelper {

  val menu = ProgramMenu

  def addForm: CssSel = {
    val item = Program.createRecord
    "data-name=name *" #> item.name.toForm &
    "data-name=description *" #> item.description.toForm &
    "type=submit" #> SHtml.ajaxOnSubmit(() => save(item))&
    "type=cancel" #> SHtml.ajaxButton("Cancelar", () => RedirectTo(menu.menuList.url), "class"->"btn btn-default" )
  }

  def editForm: CssSel = {
    for {
      item <- programRequestVar.get ?~ "Programa no definida"
    } yield {
      "data-name=programName *" #> item.name &
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
        "data-name=description *"  #> item.description &
        "data-name=edit *" #> SHtml.ajaxButton(
          "Editar",
          () => RedirectTo(menu.menuEdit.url, () => programRequestVar.set(Full(item))),
          "class" -> "btn btn-default"
        )
      }) &
      "data-name=add" #> SHtml.ajaxButton("Agregar programa", () => RedirectTo(menu.menuAdd.url), "class"->"btn btn-primary" ) &
      "data-name=delete" #> SHtml.ajaxButton(
        "Eliminar",
        () => {
          val list = programDeleteRequestVar.get
          RedirectTo(menu.menuList.url, () => delete(list))
        },
        "class"->"btn btn-danger"
      )
  }

  def customCheckbox(item: Program) = {
    SHtml.ajaxCheckbox(false, b => updateDeleteList(b, item), "class" -> "checkbox-list")
  }

  def updateDeleteList(value: Boolean, item: Program): JsCmd = value match {
    case true =>
      programDeleteRequestVar.set(item :: programDeleteRequestVar.is)
      Noop
    case false =>
      programDeleteRequestVar.set(programDeleteRequestVar.is.filter(b => b.id != item.id))
      Noop
  }

  def page = Program.findAll

  def save(item: Program) = {
    item.save(true)
    redirectToHome
  }

  def update(item: Program) = {
    item.update
    redirectToHome
  }

  def delete(items: List[Program]) = {
    items.map(e => {
      e.delete_!
    })
  }

  def redirectToHome = {
    RedirectTo(menu.menuList.url)
  }

}

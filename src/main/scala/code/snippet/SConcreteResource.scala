package code.snippet

import code.lib.menu.ConcreteResourceMenu
import code.lib.request.request._
import code.model.resource.ConcreteResource
import net.liftmodules.extras.SnippetHelper
import net.liftweb.common.Full
import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds._
import net.liftweb.util.Helpers._
import net.liftweb.util._

object SConcreteResource extends SnippetHelper {

  val menu = ConcreteResourceMenu

  def addForm: CssSel = {
    val item = ConcreteResource.createRecord
    "data-name=name *" #> item.name.toForm &
    "data-name=description *" #> item.description.toForm &
    "type=submit" #> SHtml.ajaxOnSubmit(() => save(item))&
    "type=cancel" #> SHtml.ajaxButton("Cancelar", () => RedirectTo(menu.menuList.url), "class"->"btn btn-default" )
  }

  def editForm: CssSel = {
    for {
      item <- concreteResourceRequestVar.get ?~ "Recurso no definida"
    } yield {
      "data-name=resourceName *" #> item.name &
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
        "data-name=edit *" #> SHtml.ajaxButton(
            "Editar",
            () => RedirectTo(menu.menuEdit.url, () => concreteResourceRequestVar.set(Full(item))),
            "class" -> "btn btn-default"
          )
      }) &
      "data-name=add" #> SHtml.ajaxButton("Agregar Recurso", () => RedirectTo(menu.menuAdd.url), "class"->"btn btn-primary" ) &
      "data-name=delete" #> SHtml.ajaxButton(
        "Eliminar",
        () => {
          val list = concreteResourceDeleteRequestVar.get
          RedirectTo(menu.menuList.url, () => delete(list))
        },
        "class"->"btn btn-danger")
  }

  def customCheckbox(item: ConcreteResource) = {
    SHtml.ajaxCheckbox(false, b => updateDeleteList(b, item), "class" -> "checkbox-list")
  }

  def updateDeleteList(value: Boolean, item: ConcreteResource): JsCmd = value match {
    case true =>
      concreteResourceDeleteRequestVar.set(item :: concreteResourceDeleteRequestVar.is)
      Noop
    case false =>
      areaDeleteRequestVar.set(areaDeleteRequestVar.is.filter(b => b.id != item.id))
      Noop
  }

  def page = ConcreteResource.findAll

  def save(item: ConcreteResource) = {
    item.save(true)
    redirectToHome
  }

  def update(item: ConcreteResource) = {
    item.update
    redirectToHome
  }

  def delete(items: List[ConcreteResource]) = {
    items.map(e => {
      e.delete_!
    })
  }

  def redirectToHome = {
    RedirectTo(menu.menuList.url)
  }
}
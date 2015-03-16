package code
package snippet

import code.lib.request.request._
import net.liftweb.common.Full
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.{SHtml, PaginatorSnippet, StatefulSnippet}
import code.model.productive.ProductiveUnit
import net.liftweb.util._
import Helpers._

object SProductiveUnit {


  def addForm = {
    var addProductiveUnit = ProductiveUnit.createRecord
    "data-name=name" #> addProductiveUnit.name.toForm &
    "data-name=description" #> addProductiveUnit.description.toForm &
    "data-name=administrator" #> addProductiveUnit.administrator.toForm &
    "data-name=area" #> addProductiveUnit.area.toForm &
    "data-name=program" #> addProductiveUnit.program.toForm &
    "data-name=productiveType" #> addProductiveUnit.productiveType.toForm &
    "data-name=add" #> SHtml.ajaxButton("Guardar" ,() => save(addProductiveUnit))
  }

  def editForm = {
    val editProductiveUnit: ProductiveUnit = productiveRequestVar.get.dmap(ProductiveUnit.createRecord)(p => p)
    "data-name=name" #> editProductiveUnit.name.toForm &
    "data-name=description" #> editProductiveUnit.description.toForm &
    "data-name=administrator" #> editProductiveUnit.administrator.toForm &
    "data-name=area" #> editProductiveUnit.area.toForm &
    "data-name=program" #> editProductiveUnit.program.toForm &
    "data-name=productiveType" #> editProductiveUnit.productiveType.toForm &
    "data-name=edit" #> SHtml.ajaxButton("Guardar" ,() => update(editProductiveUnit))
  }

  def showAll = {
    "data-name=list" #> page.map(productive => {
      "data-name=checkbox *" #> customCheckbox(productive) &
      "data-name=name *" #> productive.name &
      "data-name=administrator *" #> productive.administrator.toString &
      "data-name=area *" #> productive.area.toString &
      "data-name=program *" #> productive.program.toString &
      "data-name=type *" #> productive.productiveType.get.toString &
      "data-name=edit *" #> SHtml.ajaxButton("Editar", () => RedirectTo("/productive/edit", () => productiveRequestVar.set(Full(productive))))
    }) &
    "data-name=add" #> SHtml.ajaxButton("Agregar", () => RedirectTo("/productive/add")) &
    "data-name=delete" #> SHtml.ajaxButton("Eliminar", () => {
       val listToDelete = productiveDeleteRequestVar.get
       RedirectTo("/productive/productives", () => delete(listToDelete))
    })
  }

  def customCheckbox(item: ProductiveUnit) = {
    SHtml.ajaxCheckbox(false, b => {
      println("checke: " + b +  " item, " + item)
      updateDeleteList(b, item)
    }, "class" -> "checkbox-list")
  }

  def updateDeleteList(value: Boolean, productive: ProductiveUnit): JsCmd = value match {
    case true =>
      productiveDeleteRequestVar.set(productive :: productiveDeleteRequestVar.is)
      println("productivedelet : " + productiveDeleteRequestVar.get)
      Noop
    case false =>
      productiveDeleteRequestVar.set(productiveDeleteRequestVar.is.filter(b => b.id != productive.id))
      println("productiveNot delete : " + productiveDeleteRequestVar.get)
      Noop
  }

  def page = ProductiveUnit.findAll

  def save(addProductiveUnit: ProductiveUnit) = {
    addProductiveUnit.save(true)
    redirectToHome
  }

  def update(productiveUnit: ProductiveUnit) = {
    productiveUnit.update
    redirectToHome
  }

  def delete(items: List[ProductiveUnit]) = {
    println("delete list: " + items)
    items.map(productive => {
      productive.delete_!
    })
  }

  def redirectToHome={
    RedirectTo("/productive/productives")
  }
}

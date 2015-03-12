package code
package snippet

import com.mongodb.QueryBuilder
import net.liftweb.mongodb.{Skip, Limit}
import net.liftweb.http.{SHtml, PaginatorSnippet, StatefulSnippet}
import code.model.productive.ProductiveUnit
import net.liftweb.util._
import Helpers._
import net.liftweb.http.S._
import scala.xml.NodeSeq

object SProductiveUnit extends StatefulSnippet with PaginatorSnippet[ProductiveUnit] {

  var dispatch: DispatchIt = {
    case "showAll" => showAll _
    case "editForm" => editForm _
    case "paginate" => paginate _
  }

  var editingProductiveUnit = ProductiveUnit.createRecord

  def editForm(xhtml:NodeSeq): NodeSeq = {
    ( "#editfirstname" #> editingProductiveUnit.name.toForm &
      "#editlastname" #> editingProductiveUnit.description.toForm &
      "#editcompany" #> editingProductiveUnit.administrator.toForm &
      "#editposition" #> editingProductiveUnit.area.toForm &
      "#editposition1" #> editingProductiveUnit.program.toForm &
      "#editposition2" #> editingProductiveUnit.productiveType.toForm &
      "type=submit" #> SHtml.submit(?("Save") ,() => save )).apply(xhtml)
  }

  def showAll(xhtml:NodeSeq): NodeSeq = {
    page.flatMap(productive => {
      (".firstname *" #> productive.name &
        ".lastname *" #> productive.description &
        ".company *" #> productive.administrator &
        ".position *" #> productive.area ).apply(xhtml)
    })
  }

  override def itemsPerPage = 2

  override def count = ProductiveUnit.count

  override def page = ProductiveUnit.findAll(QueryBuilder.start().get(),Limit(itemsPerPage),Skip(curPage * itemsPerPage))

  def save={
    editingProductiveUnit.save(true)
    redirectToHome
  }

  def redirectToHome={
    editingProductiveUnit = ProductiveUnit.createRecord
    redirectTo("/neeluser")
  }
}

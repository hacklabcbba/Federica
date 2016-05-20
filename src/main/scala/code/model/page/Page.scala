package code
package model

import code.config.Site
import code.lib.field._
import code.lib.js.Bootstrap
import code.lib._
import net.liftweb.common.Box
import net.liftweb.http.js.JsCmds.RedirectTo
import net.liftweb.http.{IdMemoizeTransform, S, SHtml}
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdPk, ObjectIdRefListField}
import net.liftweb.util.{CssSel, Helpers}
import net.liftweb.json.JsonDSL._

import scala.xml.NodeSeq
import Helpers._

class Page private () extends MongoRecord[Page] with ObjectIdPk[Page] with BaseModel[Page] with WithUrl[Page] {

  override def meta = Page

  def title = "Página"

  def entityListUrl = Site.backendPages.menu.loc.calcDefaultHref

  object name extends BsStringField(this, 500) {
    override def displayName = "Nombre"
    override def toString = get
  }

  object body extends BsCkUnsecureTextareaField(this, 1000) {
    override def displayName = "Contenido"
  }

  object widgets extends ObjectIdRefListField(this, Widget) {
    override def shouldDisplay_? = false
  }

  override def template: NodeSeq = S.runTemplate(List("backend" , "record", "page-form")).openOr(<div>Template not found</div>)

  override def formCssSel: CssSel = {
    legendCssSel &
    "data-name=fields *" #> toFieldset(fields) &
    "data-name=save" #> SHtml.ajaxOnSubmit(saveFunc _) &
    "#cancel-btn" #> SHtml.ajaxButton("Cancelar", () => RedirectTo(entityListUrl)) &
    "data-name=widgets" #> SHtml.idMemoize(body => {
      "data-name=add-widget [onclick]" #> SHtml.ajaxInvoke(() => Bootstrap.Modal(modalBody(body))) &
      "data-name=widget" #> widgets.objs.map(widget => {
        "data-name=content" #> widget.body.asHtml &
        "data-name=remove-widget [onclick]" #> SHtml.ajaxInvoke(() => {
          widgets.set(widgets.get.filter(s => s != widget.id.get))
          body.setHtml()
        })
      })
    })
  }

  def modalBody(body: IdMemoizeTransform): NodeSeq = {
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
          <h4 class="modal-title">Agregar widget</h4>
        </div>
        <div class="modal-body">
          <ul>
            {
            Widget.findAll.foldLeft(NodeSeq.Empty){ case (node, widget) => {
              node ++
              <li data-name="widget">
                <a href="#" onclick={SHtml.ajaxInvoke(() => {
                  widgets.set(widgets.get ++ List(widget.id.get))
                  Bootstrap.Modal.close() &
                  body.setHtml()
                })._2.toJsCmd}>{widget.name.get}</a>
              </li>
            }}}
          </ul>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  }

  def updateElasticSearch(page: Page) = {
    ElasticSearch.mongoindexSave(
      ElasticSearch.elasticSearchPath ++ List(s"page_${page.id.get}"),
      ("url" -> Site.pagina.calcHref(page)) ~
      ("name" -> page.name.get) ~
      ("content" -> page.body.asHtml.text)
    )
  }

  def urlString: String = Site.pagina.calcHref(this)

  override def toString = name.get
}

object Page extends Page with RogueMetaRecord[Page] {
  override def collectionName = "page.pages"
  override def fieldOrder = List(name, body)

  def findByUrl(url: String): Box[Page] = {
    Page.where(_.url eqs url).fetch().headOption
  }

}

package code
package snippet

import code.lib.js.Bootstrap
import code.lib.{BaseModel, SortableModel}
import code.model.User
import net.liftmodules.extras.SnippetHelper
import net.liftweb.common.{Box, Empty, EmptyBox, Failure, Full}
import net.liftweb.http.S.{SFuncHolder, _}
import net.liftweb.http.js.JE.{JsRaw, JsVar}
import net.liftweb.http.js.{JE, JsCmd}
import net.liftweb.http.js.JsCmds._
import net.liftweb.http._
import net.liftweb.json.JsonAST.JValue
import net.liftweb.mongodb.{Limit, Skip}
import net.liftweb.mongodb.record.{MongoMetaRecord, MongoRecord}
import net.liftweb.record.Field
import net.liftweb.util.{CssSel, Helpers}
import net.liftweb.util.Helpers._
import _root_.net.liftweb.json.JsonDSL._
import _root_.net.liftweb.json._
import code.config.MongoConfig
import com.mongodb.{BasicDBList, BasicDBObject, DBCursor, DBObject}

import scala.xml.{NodeSeq, Text}

trait ListSnippet[BaseRecord <: MongoRecord[BaseRecord]] extends SnippetHelper {

  val meta: MongoMetaRecord[BaseRecord]
  val addUrl: String
  def entityListUrl: String

  object deletedItems extends RequestVar[List[BaseRecord]](Nil)

  def itemEditUrl(inst: BaseRecord): String

  def template: NodeSeq = Templates(List("templates-hidden", "backend", "listing-table")) openOr Text("Missing template")

  def templateTable: NodeSeq = Templates(List("templates-hidden", "backend", "data-table")) openOr Text("Missing template")

  def listFields: List[Field[_, BaseRecord]] = meta.fieldOrder

  val title: String

  def items: List[BaseRecord] = {
    S.param("sSearch") match {
      case Full(p) =>
        val orClause: BasicDBList = new BasicDBList
        val condition: DBObject = new BasicDBObject("$regex", p)
        for{
          field <- listFields
        } yield {
          val newDBObject: DBObject = new BasicDBObject(field.name, condition)
          orClause.add(newDBObject)
        }
        val query: DBObject = new BasicDBObject("$or", orClause)
        sortOrder match {
          case Some(sort) =>
            meta.findAll(query, sort, Skip(S.param("iDisplayStart").dmap(0)(_.toInt)), Limit(10))
          case _ =>
            meta.findAll(query, Skip(S.param("iDisplayStart").dmap(0)(_.toInt)), Limit(10))
        }

      case Empty =>
        meta.findAll
      case Failure(msg ,_ , _) =>
        S.error(msg)
        Nil

    }
  }

  def sortOrder: Option[DBObject] = {
    (S.param("iSortCol_0"), S.param("sSortDir_0")) match {
      case (Full(column), Full(order))=>
        listFields.lift(column.toInt) match {
          case Some(field) if(order.trim != "") =>
            val sort = {
              if(order == "asc")
                1
              else
                -1
            }
            Full(new BasicDBObject(field.name, sort))
          case _ =>
            Empty
        }
      case _ =>
        Empty
    }
  }

  def allItemsAfterSearch: List[BaseRecord] = {
    S.param("sSearch") match {
      case Full(p) =>
        val orClause: BasicDBList = new BasicDBList
        val condition: DBObject = new BasicDBObject("$regex", p)
        for{
          field <- listFields
        } yield {
          val newDBObject: DBObject = new BasicDBObject(field.name, condition)
          orClause.add(newDBObject)
        }
        val query: DBObject = new BasicDBObject("$or", orClause)
        meta.findAll(query)
      case Empty =>
        meta.findAll
      case Failure(msg ,_ , _) =>
        S.error(msg)
        Nil
    }
  }

  def columns: List[String] = listFields.map(_.displayName)

  def fun = (params: DataTableParams) => {
    val rows: List[List[(String, String)]] = for {
      item <- items
    } yield {
      var indice = -1
      var result: List[(String, String)] = listFields.zipWithIndex.collect{
        case (x, i) => (i.toString, item.fieldByName(x.name).dmap("")(_.toString))
      }
      val id = item.fieldByName("_id").dmap("")(_.toString).replace(" ", "_")
      result = result ::: List(("DT_RowId", id)) ::: Nil
      result
    }

    new DataTableObjectSource(allItemsAfterSearch.size, allItemsAfterSearch.size, rows)

  }

  def jsonOptions: List[(String, String)] = List(("iDisplayLength", "10"), ("bLengthChange", "false"))

  def idOpt = Some(Helpers.nextFuncName)

  def render = {

    "*" #>
      {
        "data-name=title *" #> title &
        "data-name=add-item [href]" #> addUrl &
        "data-name=edit [onclick]" #> SHtml.jsonCall(JE.Call("getSelected"), (res: JValue) => {
          getListOfItemsSelected(res).headOption match {
            case Some(item) =>
              RedirectTo(itemEditUrl(item))
            case _ =>
              Noop
          }
        }) &
        "data-name=delete [onclick]" #> SHtml.jsonCall(JE.Call("getSelected"), (res: JValue) => {
          deletedItems.remove()
          deletedItems.set(getListOfItemsSelected(res))
          deleteItemsJsCmd
        }) &
        "#datas" #> {

          val f = (ignore: String) => {
            val columns = S.param("iColumns").dmap(0)(_.toInt)
            val a = (1 to columns).map(i => S.param("bSearchable_" + i).dmap(true)(_.toBoolean)).toList

            val params = new DataTableParams(
              S.param("iDisplayStart").dmap(0)(_.toInt),
              S.param("iDisplayLength").dmap(0)(_.toInt),
              columns,
              S.param("sSearch").dmap("")(_.toString),
              S.param("bRegex").dmap(false)(_.toBoolean),
              (1 to columns).map(i => true).toList,
              (1 to columns).map(i => S.param("sSearch_" + i).dmap("")(_.toString)).toList,
              (1 to columns).map(i => S.param("bRegex_" + i).dmap(false)(_.toBoolean)).toList,
              (1 to columns).map(i => true).toList,
              S.param("iSortingCols").dmap(0)(_.toInt),
              (1 to columns).map(i => {
                S.param("iSortCol_" + i).dmap(0)(_.toInt)
              }).toList,
              (1 to columns).map(i => {
                S.param("sSortDir_" + i).dmap("")(_.toString)
              }).toList,
              (1 to columns).map(i => {
                S.param("mDataProp_" + i).dmap("")(_.toString)
              }).toList)

            val source = fun(params)

            val json = ("iTotalRecords" -> source.totalRecords) ~
              ("iTotalDisplayRecords" -> source.totalDisplayRecords) ~
              ("sEcho" -> S.param("sEcho").dmap(0)(_.toInt)) ~
              ("aaData" -> source.jsonData)

            JsonResponse(json)
          }

          fmapFunc(SFuncHolder(f)) { func =>
            val where: String = encodeURL(S.contextPath + "/" + LiftRules.ajaxPath + "?" + func + "=foo")

            val id = idOpt getOrElse Helpers.nextFuncName

            val jqOptions = ("bProcessing", "true") ::
              ("bServerSide", "true") ::
              ("sAjaxSource", where.encJs) ::
              Nil ::: jsonOptions

            val json = jqOptions.map(t => t._1 + ":" + t._2).mkString("{", ",", ", rowCallback: " +
              " function( row, data ) { " +
              "if ( $.inArray(data.DT_RowId, selected) !== -1 ) {" +
              "$(row).addClass('selected');}}" +
              "}")
            val datatableOptions = JsRaw(json)

            val onLoad = JsRaw("""
              var selected = [];

              function getSelected(){
                return selected;
              }

              $(document).ready(function() {
              $("#""" + id + """").dataTable(""" + datatableOptions.toJsCmd + """);
              $("#""" + id + """ tbody").on('click', 'tr', function () {

                var id = this.id;
                var index = $.inArray(id, selected);
                if ( index === -1 ) {
                    selected.push( id );
                } else {
                    selected.splice( index, 1 );
                }

                $(this).toggleClass('selected');
                console.log("selected: ", selected.length);
                if(selected.length > 0){
                  console.log("entro");
                  if(selected.length == 1){
                    $("#edit").attr("disabled", false);
                    $("#delete").attr("disabled", false);
                    console.log("selected " , selected[0]);
                  } else if(selected.length > 1) {
                    $("#delete").attr("disabled", false);
                    $("#edit").attr("disabled", true);
                  } else {
                    $("#edit").attr("disabled", true);
                    $("#delete").attr("disabled", true);
                  }
                }
              } );
            });""")

            <table id={ id }>
              <head_merge>
                { Script(onLoad) }
              </head_merge>
              <thead>
                <tr>
                  { columns.map(c => <th>{ c }</th>) }
                </tr>
              </thead>
              <tbody>
                <tr>
                  { columns.map(_ => <td></td>) }
                </tr>
              </tbody>
            </table>
          }
        }
      }.apply(template)
  }

  def getListOfItemsSelected(jArray: JValue): List[BaseRecord] = {
    jArray.children.map(obj => obj match {
      case (objJson: JString) =>
        meta.find(objJson.values)
      case _ =>
        Empty
    }).flatten
  }

  def facebookHeaders(in: NodeSeq) = {
      <meta property="og:title" content="Example content" /> ++
        <meta property="og:description" content="Example description" /> ++
        <meta property="og:type" content="article" />
  }

  def selectItem(selected: Boolean, item: BaseRecord): JsCmd = {
    if (selected)
      deletedItems.set(deletedItems.get ++ List(item))
    else
      deletedItems.set(deletedItems.get.filter(_ != item))
    Noop
  }

  def deleteItemsJsCmd: JsCmd = {
    tryo(deletedItems.get.foreach(meta.delete_!(_))) match {
      case Full(_) =>
        RedirectTo(entityListUrl, () => S.notice("Items eliminados"))
      case Failure(msg, _, _) =>
        S.error(msg)
      case _ =>
        S.error("Error desconocido")
    }
  }

  def deleteItemModalJsCmd(inst: BaseRecord): JsCmd = {
    Bootstrap.Modal(
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title">Eliminar</h4>
          </div>
          <div class="modal-body">
            ¿Estas seguro de elimar este item?
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-danger" onclick={SHtml.ajaxInvoke(() => {
              deleteItemJsCmd(inst)
            })._2.toJsCmd}>Si</button>
            <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
          </div>
        </div>
      </div>
    )
  }

  def deleteItemJsCmd(item: BaseRecord): JsCmd = {
    tryo(meta.delete_!(item)) match {
      case Full(_) =>
        RedirectTo(entityListUrl, () => S.notice("Item eliminado"))
      case Failure(msg, _, _) =>
        S.error(msg)
      case _ =>
        S.error("Error desconocido")
    }
  }
}

trait SortableSnippet[BaseRecord <: MongoRecord[BaseRecord] with SortableModel[BaseRecord]] extends ListSnippet[BaseRecord] {
  override def items: List[BaseRecord] = meta.findAll.sortBy(_.order.get)
  override def render = {
    val callbacks = Function(
      "updateOrderValue",
      List("json"),
      SHtml.jsonCall(
        JsVar("json"),
        updateOrderValue _).exp.cmd
    )
    val sorteableScript = Run(
      """
        |$("#items").sortable({}).on('sortupdate', function(e, obj) {
        |  console.log(obj);
        |  console.log(obj.item[0].id);
        |  updateOrderValue({ id: obj.item[0].id, order: obj.index});
        |});
      """.stripMargin)
    S.appendJs(callbacks)
    S.appendJs(sorteableScript)

    "*" #>
      {
        "data-name=title *" #> title &
        "data-name=column-name *" #> listFields.map(field => field.displayName) &
        "data-name=add-item [href]" #> addUrl &
        "data-name=remove-items [onclick]" #> SHtml.ajaxInvoke(deleteItemsJsCmd _) &
        "data-name=items" #> items.map(item => {
          "data-name=items [id]" #> item.id.toString &
          "type=checkbox" #> SHtml.ajaxCheckbox(false, s => selectItem(s, item)) &
          "data-name=column-data *" #> listFields.map(field => item.fieldByName(field.name).dmap("")(_.toString)) &
          "data-name=edit-item [href]" #> itemEditUrl(item) &
          "data-name=remove-item [onclick]" #> SHtml.ajaxInvoke(() => deleteItemModalJsCmd(item))
        }) &
        "data-name=pagination" #> NodeSeq.Empty
      }.apply(template)
  }

  def updateOrderValue(json: JValue): JsCmd
}

object CrudSnippet extends SnippetHelper {
  private def serve(snip: BaseModel[_] => NodeSeq): NodeSeq =
    (for {
      loc <- S.location ?~ "no value"
      value <- loc.currentValue ?~ "no value"
      bm <- Box.asA[BaseModel[_]](value) ?~ "value was the wrong type"
    } yield {
      snip(bm)
    })

  def edit(in: NodeSeq): NodeSeq = serve { baseModel => baseModel.toForm }

}
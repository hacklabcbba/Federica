package code.snippet

import code.lib.{ElasticSearch, Helper}
import com.twitter.util.{Await, Duration, Future}
import net.liftmodules.extras.NgJE._
import net.liftmodules.extras.NgJsCmds._
import net.liftmodules.extras.{JsExtras, SnippetHelper}
import net.liftweb.common._
import net.liftweb.http.{S, SHtml}
import net.liftweb.http.js.JE.{AnonFunc, JsObj}
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds.{JsReturn, Run}
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import net.liftweb.json.{NoTypeHints, Serialization}
import net.liftweb.util.Props

import scala.xml.NodeSeq
import net.liftweb.util.Helpers._
import java.nio.charset.Charset

object SearchPageSnippet extends SnippetHelper with Logger {

  def items(in: NodeSeq): NodeSeq = {
    implicit val formats = Serialization.formats(NoTypeHints)

    def fetchItems(json: JValue): JsCmd = {
      info("json: " + pretty(JsonAST.render(json)))
      for {
        searchText <- tryo((json \ "search").extract[String])
      } yield {

        val jsonQuery: JValue =
          ("query" ->
            ("filtered" ->
              (
                "query" ->
                ("query_string" ->
                  ("query" -> searchText) ~
                  ("fields" -> ("name^2" :: "content" :: Nil))
                )
              )
            )
          )

        val httpResponse = ElasticSearch.mongoindexSearch(ElasticSearch.elasticSearchPath, jsonQuery)
        val future = httpResponse.map(
          response => {
            val json = parse( response.getContent.toString(Charset.defaultCharset()))
            val cnt = tryo(json \ "hits" \ "total") match {
              case Full(JInt(total)) => total.toLong
              case _ => 0L
            }
            val items = for {
              JArray(rows) <- tryo(json \ "hits" \ "hits")
            } yield {
              rows.map(_ \ "_source").map(s => s.transform {
                case JField("content", JString(content)) =>
                  JField("content", JString(content.take(300) + "..."))
              })
            }

            val ret = if(cnt > 0) {
              Full(("count" -> cnt) ~ ("items" -> items.openOr(Nil)))
            } else {
              Full(("count" -> 0) ~ ("message" -> "¡Ups! Parece que no hay ningún resultado con tu búsqueda, sigue intentándolo."))
            }

            NgBroadcast("search", "after-fetch-items", Full(ret))
          }
        )
        Await.result(Future.collect(List(future))).headOption.
          getOrElse(NgBroadcast("search", "after-fetch-items", Empty))
      }
    }

    val funcs = JsObj(
      "fetchItems" -> JsExtras.JsonCallbackAnonFunc(fetchItems)
    )

    val params: JValue = ("host" -> Props.get("default.host", "127.0.0.1")) ~
      ("search" -> S.param("search").openOr(""))

    val onload =
      NgModule("SearchServer", Nil) ~>
      NgConstant("ServerParams", params) ~>
      NgFactory("ServerFuncs", AnonFunc(JsReturn(funcs)))

    S.appendGlobalJs(JsExtras.IIFE(onload.cmd))

    in
  }

}

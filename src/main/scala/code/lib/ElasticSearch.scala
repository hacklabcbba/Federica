package code.lib

import code.model._
import code.model.event.Event
import code.model.network.Network
import net.liftweb.common.{Loggable, Full, Box, Logger}
import com.twitter.finagle.ServiceFactory
import org.jboss.netty.handler.codec.http._
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.http.Http
import com.twitter.conversions.time._
import net.liftweb.util.Props
import util.JsonEnumeration
import org.jboss.netty.buffer.ChannelBuffers
import net.liftweb.json._
import org.jboss.netty.util.CharsetUtil._
import org.jboss.netty.handler.codec.http.HttpHeaders.Names._
import org.jboss.netty.util.CharsetUtil
import net.liftweb.http.NamedCometListener
import xml.Text
import com.twitter.util.Future

object ElasticSearch extends Logger {

  lazy val elasticSearchPath = List("martadero", "content")
  val host= Props.get("elasticsearch.host")
  val port= Props.get("elasticsearch.port")
  val hostAndPort =   "%s:%s".format(host.openOr("localhost"), port.openOr("9200"))
  info("host port is %s" format hostAndPort)

  /**
    * You init a clientFactory only once and use it several times across your application
    */
  val clientFactory: ServiceFactory[HttpRequest, HttpResponse] = ClientBuilder()
    .codec(Http())
    .hosts(hostAndPort)
    .tcpConnectTimeout(1.second)
    .hostConnectionLimit(1)
    .buildFactory()


  /**
    * The path to the elastic search table (index) and the json to send
    */
  def mongoindexSave(path: List[String], json: JValue) ={
    info("json is %s" format json)
    val req = requestBuilderPut(path, json)
    sendToElastic(req)
  }

  /**
    * The path to the elastic search table (index) and the json to delete
    */
  def mongoindexDelete(path: List[String], id: String) ={
    info(s"Deleteting $id in $path")
    val req = requestBuilderDelete(path ++ List(id.toString))
    sendToElastic(req)
  }

  /**
    * Generate a request to send to ElasticSearch
    * @param path the path to your document, as a list
    * @param json ths JValue representing the payload, i.e. ("id" -> "1") ~ ("part_number" -> "02k7011")
    * @return a request object
    */
  def requestBuilderPut(path: List[String], json: JValue): DefaultHttpRequest = {
    val payload = ChannelBuffers.copiedBuffer( compact(render(json))  , UTF_8)
    val _path = path.mkString("/","/","")
    val request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.PUT, _path)
    request.setHeader("User-Agent", "Finagle 6.5.2 - Liftweb")
    request.setHeader("Host", host.openOr("localhost")) // the ~(host) can be replace for host.openOr("default value here")
    request.setHeader(CONTENT_TYPE, "application/json")
    request.setHeader(CONNECTION, "keep-alive")
    request.setHeader(CONTENT_LENGTH, String.valueOf(payload.readableBytes()));
    request.setContent(payload)
    //info("\nSending request:\n%s".format(request))
    //info("\nSending body:\n%s".format(request.getContent.toString(CharsetUtil.UTF_8)))
    request
  }

  def mongoindexSearch(path: List[String], json: JValue): Future[HttpResponse] ={
    val req = requestBuilderGet(path ++ List("_search"), json)
    sendToElastic(req)
  }

  /**
    * Generate a request to search the Elastic Search instance
    * @param path the path to your document, as a list
    * @param json ths JValue representing the payload, i.e. ("id" -> "1") ~ ("part_number" -> "02k7011")
    * @return a request object
    */
  def requestBuilderGet(path: List[String], json: JValue): DefaultHttpRequest = {
    val payload = ChannelBuffers.copiedBuffer( compact(render(json))  , UTF_8)
    val _path = path.mkString("/", "/" ,"")
    val request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, _path)
    request.setHeader("User-Agent", "Finagle 4.0.2 - Liftweb")
    request.setHeader("Host", host.openOr("localhost"))
    request.setHeader(CONTENT_TYPE, "application/x-www-form-urlencoded")
    request.setHeader(CONTENT_LENGTH, String.valueOf(payload.readableBytes()));
    request.setContent(payload)
    //info("Sending request:\n%s".format(request))
    //info("Sending body:\n%s".format(request.getContent.toString(CharsetUtil.UTF_8)))
    request
  }

  /**
    * Generate a request to delete data
    * @param path the path to your document, as a list
    * @return a request object
    */
  def requestBuilderDelete(path: List[String]): DefaultHttpRequest = {
    val _path = path.mkString("/","/","")
    val request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.DELETE, _path)
    request.setHeader("User-Agent", "Finagle 4.0.2 - Liftweb")
    request.setHeader("Host", host.openOr("localhost"))
    //info("Sending request:\n%s".format(request))
    //info("Sending body:\n%s".format(request.getContent.toString(CharsetUtil.UTF_8)))
    request
  }

  /**
    * Take a request ans send it
    * @param request The request
    * @return
    */
  def sendToElastic(request: DefaultHttpRequest): Future[HttpResponse] ={
    val client = clientFactory.apply()()
    info("Request to send is %s" format request)
    val httpResponse = client(request)

    httpResponse.onSuccess{
      response =>
        info("Received response: " + response)
        client.close()
        Future.Done
    }.onFailure{err =>
      error(err)
      client.close()
      Future.Done
    }
  }


  /**
    * Deletes all the indeces from elastic search
    * @return
    */
  def unsafeDeleteAllIndeces() ={
    val req = requestBuilderDelete(List())
    sendToElastic(req)
  }

  def updateAllIndeces() = {

    Event.findAll.foreach(e => {
      Event.updateElasticSearch(e)
    })

    Network.findAll.foreach(n => {
      Network.updateElasticSearch(n)
    })

    Page.findAll.foreach(p => {
      Page.updateElasticSearch(p)
    })

    ActionLine.findAll.foreach(a => {
      ActionLine.updateElasticSearch(a)
    })

    Area.findAll.foreach(a => {
      Area.updateElasticSearch(a)
    })

    BlogPost.findAll.foreach(b => {
      BlogPost.updateElasticSearch(b)
    })

    Call.findAll.foreach(c => {
      Call.updateElasticSearch(c)
    })

    Process.findAll.foreach(p => {
      Process.updateElasticSearch(p)
    })

    Service.findAll.foreach(s => {
      Service.updateElasticSearch(s)
    })

    TransversalApproach.findAll.foreach(t => {
      TransversalApproach.updateElasticSearch(t)
    })

    TransversalArea.findAll.foreach(t => {
      TransversalArea.updateElasticSearch(t)
    })

    Value.findAll.foreach(v => {
      Value.updateElasticSearch(v)
    })
  }
}
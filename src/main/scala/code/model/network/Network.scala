package code
package model
package network

import code.config.Site
import code.lib.field.{BsCkTextareaField, BsCkUnsecureTextareaField, BsStringField}
import code.lib._
import net.liftweb.common.{Box, Full}
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.{ObjectIdPk, ObjectIdRefField, ObjectIdRefListField}
import net.liftweb.record.field.EnumNameField
import net.liftweb.json.JsonDSL._

class Network private () extends MongoRecord[Network] with ObjectIdPk[Network] with BaseModel[Network] with SortableModel[Network] with WithUrl[Network] {

  override def meta = Network

  def title = "Red"

  def entityListUrl = Site.backendNetworks.menu.loc.calcDefaultHref

  object name extends BsStringField(this, 500) {
    override def displayName = "Nombre"
  }

  object description extends BsCkUnsecureTextareaField(this, 500){
    override def displayName = "Descripción"
  }

  object spaces extends ObjectIdRefListField(this, Space) {
    override def displayName = "Espacios conectados"
    val spaces = Space.findAll
    override def toForm = {
      Full(SHtml.multiSelectElem(
        spaces,
        objs,
        "class" -> "select2 form-control",
        "data-placeholder" -> "Seleccione proceso.."
      )(p => set(p.map(_.id.get))))
    }
  }

  object scope extends EnumNameField(this, Scope) {
    override def displayName = "Alcance"
  }

  def urlString: String = Site.red.calcHref(this)

}

object Network extends Network with RogueMetaRecord[Network] {
  override def collectionName = "main.networks"
  override def fieldOrder =
    List(name, description, spaces)

  override def find(s: String) = {
    println("KEY:"+s)
    val res = super.find(s)
    println("RES:" + res)
    res
  }

  def findByUrl(url: String): Box[Network] = {
    Network.where(_.url eqs url).fetch(1).headOption
  }

  def updateElasticSearch(network: Network) = {
    ElasticSearch.mongoindexSave(
      ElasticSearch.elasticSearchPath ++ List(s"network_${network.id.get}"),
      ("url" -> Site.red.calcHref(network)) ~
      ("name" -> network.name.get) ~
      ("content" -> network.description.asHtml.text)
    )
  }
}

object NetworkType extends Enumeration {
  type NetworkType = Value
  val Intern = Value("Interno")
  val Associate = Value("Asociado")
}

object Scope extends Enumeration {
  type StateType = Value
  val Neighborhood = Value(0, "Barrial")
  val Local = Value(1, "Local")
  val National = Value(2, "Nacional")
  val Regional = Value(3, "Regional")
  val Global = Value(4, "Internacional")
}
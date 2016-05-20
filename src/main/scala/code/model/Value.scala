package code.model

import code.config.Site
import code.lib.field.{BsCkUnsecureTextareaField, BsStringField, BsTextareaField, FileField}
import code.lib.{ContentSearchType, _}
import code.model.event.Event.image._
import net.liftweb.common.{Box, Full}
import net.liftweb.http.SHtml
import net.liftweb.mongodb.record.{BsonMetaRecord, BsonRecord, MongoRecord}
import net.liftweb.mongodb.record.field.{BsonRecordListField, ObjectIdPk, ObjectIdRefField}
import net.liftweb.record.LifecycleCallbacks
import net.liftweb.record.field.{StringField, TextareaField}
import net.liftweb.json.JsonDSL._
import scala.xml.NodeSeq


class Value private () extends MongoRecord[Value] with ObjectIdPk[Value] with BaseModel[Value] with SortableModel[Value] with WithUrl[Value]{

  override def meta = Value

  def title = "Principio"

  def entityListUrl = Site.backendValues.menu.loc.calcDefaultHref

  object name extends StringField(this, 500) {
    override def displayName = "Nombre"
    override def toString = get
    override def toForm = Full(SHtml.text(
      value,
      (s: String) => set(s),
      "class" -> "form-control", "data-placeholder" -> "Ingrese nombre.."))
  }

  object description extends BsCkUnsecureTextareaField(this, 1000) {
    override def displayName = "Descripción"

  }

  //ToDo mapa de area -> descripcion de como interactura, programa -> descripcion, area de apoyo -> descripcion

  object areasDefinitions extends BsonRecordListField(this, Definition) with LifecycleCallbacks {
    override def displayName = "Definición por Area"
    private var newDefs: List[Definition] = Nil
    override def toForm: Box[NodeSeq] = Full {
      Area.findAll.foldLeft(NodeSeq.Empty){ case (node, area) => {
        val definition = this.value.find(d => d.area.get == area.id.get).getOrElse{
          val c = Definition.createRecord.area(area.id.get)
          newDefs = newDefs ++ List(c)
          c
        }
        node ++
          <div class="row">
            <div class="col-md-12 col-lg-12"><h5>{area.name.get}</h5></div>
          </div> ++
          <div class="row">
            <div class="form-group margin-bottom-xs">
              <label data-name="label" class="control-label col-md-6" for="email_id">Definición</label>
              <div class="col-md-12 col-lg-12">
                {definition.description.toForm openOr NodeSeq.Empty}
              </div>
            </div>
          </div>
      }}
    }

    override def beforeSave: Unit = {
      this.set(this.get ++ newDefs)
    }

    def getListOfNonEmptyDescription: List[Definition] = {
      this.get.filter(d => d.description.get.trim != "")
    }
  }

  object programsDefinitions extends BsonRecordListField(this, Definition) with LifecycleCallbacks {
    override def displayName = "Definición por programa"
    private var newDefs: List[Definition] = Nil
    override def toForm: Box[NodeSeq] = Full {
      Program.findAll.foldLeft(NodeSeq.Empty){ case (node, program) => {
        val definition = this.value.find(d => d.program.get == program.id.get).getOrElse{
          val c = Definition.createRecord.program(program.id.get)
          newDefs = newDefs ++ List(c)
          c
        }
        node ++
          <div class="row">
            <div class="col-md-12 col-lg-12"><h5>{program.name.get}</h5></div>
          </div> ++
          <div class="row">
            <div class="form-group margin-bottom-xs">
              <label data-name="label" class="control-label col-md-6" for="email_id">Definición</label>
              <div class="col-md-12 col-lg-12">
                {definition.description.toForm openOr NodeSeq.Empty}
              </div>
            </div>
          </div> ++ <br/> ++ <br/>
      }}
    }

    override def beforeSave: Unit = {
      this.set(this.get ++ newDefs)
    }

    def getListOfNonEmptyDescription: List[Definition] = {
      this.get.filter(d => d.description.get.trim != "")
    }
  }

  object transvesalAreasDefinitions extends BsonRecordListField(this, Definition) with LifecycleCallbacks {
    override def displayName = "Definición por área transversal"
    private var newDefs: List[Definition] = Nil
    override def toForm: Box[NodeSeq] = Full {
      TransversalArea.findAll.foldLeft(NodeSeq.Empty){ case (node, at) => {
        val definition = this.value.find(d => d.transversalArea.get == at.id.get).getOrElse{
          val c = Definition.createRecord.transversalArea(at.id.get)
          newDefs = newDefs ++ List(c)
          c
        }
        node ++
          <div class="row">
            <div class="col-md-12 col-lg-12"><h5>{at.name.get}</h5></div>
          </div> ++
          <div class="row">
            <div class="form-group margin-bottom-xs">
              <label data-name="label" class="control-label col-md-6" for="email_id">Definición</label>
              <div class="col-md-12 col-lg-12">
                {definition.description.toForm openOr NodeSeq.Empty}
              </div>
            </div>
          </div>
      }}
    }

    override def beforeSave: Unit = {
      this.set(this.get ++ newDefs)
    }

    def getListOfNonEmptyDescription: List[Definition] = {
      this.get.filter(d => d.description.get.trim != "")
    }
  }

  object image extends FileField(this) {
    override def displayName = "Imágen"
    override def toString = {
      value.fileName.get
    }
  }

  def urlString: String = Site.principio.calcHref(this)

  override def toString = name.get
}

object Value extends Value with RogueMetaRecord[Value] {

  override def collectionName = "main.values"

  override def fieldOrder = List(
    name, description, image, url, order, areasDefinitions, programsDefinitions, transvesalAreasDefinitions
  )

  def findByUrl(url: String): Box[Value] = {
    Value.where(_.url eqs url).fetch(1).headOption
  }

  def updateElasticSearch(value: Value) = {
    ElasticSearch.mongoindexSave(
      ElasticSearch.elasticSearchPath ++ List(s"value_${value.id.get}"),
      ("url" -> Site.principio.calcHref(value)) ~
      ("name" -> value.name.get) ~
      ("content" -> value.description.asHtml.text) ~
      ("type" -> ContentSearchType.Process.id)
    )
  }
}

class Definition extends BsonRecord[Definition ] {

  def meta = Definition

  object area extends ObjectIdRefField(this, Area) {
    override def shouldDisplay_?  = false
    override def optional_? = true
  }

  object program extends ObjectIdRefField(this, Program) {
    override def shouldDisplay_?  = false
    override def optional_? = true
  }

  object transversalArea extends ObjectIdRefField(this, TransversalArea) {
    override def shouldDisplay_?  = false
    override def optional_? = true
  }

  object description extends BsCkUnsecureTextareaField(this, 1000) {
    override def displayName = "Definición"
  }
}

object Definition extends Definition with BsonMetaRecord[Definition]

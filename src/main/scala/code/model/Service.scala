package code
package model

import code.config.Site
import code.lib.{BaseModel, RogueMetaRecord}
import code.lib.field.{FileField, BsEmailField, BsStringField, BsCkTextareaField}
import net.liftweb.common.{Box, Full}
import net.liftweb.http.js.{HtmlFixer, JsCmd}
import net.liftweb.http.js.JsCmds.Run
import net.liftweb.http.{S, IdMemoizeTransform, SHtml}
import net.liftweb.mongodb.record.{BsonMetaRecord, BsonRecord, MongoRecord}
import net.liftweb.mongodb.record.field.{BsonRecordListField, ObjectIdRefListField, ObjectIdRefField, ObjectIdPk}
import net.liftweb.record.field.{TextareaField, EnumNameField, BooleanField, StringField}
import net.liftweb.util.Helpers

import scala.xml.{Text, Elem}
import Helpers._

class Service private () extends MongoRecord[Service] with ObjectIdPk[Service] with BaseModel[Service] {

  override def meta = Service

  def title = "Servicio"

  def entityListUrl = Site.backendServices.menu.loc.calcDefaultHref

  object name extends BsStringField(this, 500) {
    override def displayName = "Nombre"
  }

  object description extends BsCkTextareaField(this, 500) {
    override def displayName = "Descripción"
  }

  object responsible extends ObjectIdRefField(this, User) {
    override def displayName = "Responsable"
    override def toString = {
      this.obj.dmap("Indefinido..")(_.name.get)
    }
    override def toForm: Box[Elem] = {
      Full(
        SHtml.selectElem(
          availableOptions,
          obj,
          "class" -> "select2 form-control",
          "data-placeholder" -> "Seleccione responsable.."
        )(s => set(s.id.get))
      )
    }

    def availableOptions = User.findAll
  }

  object email extends BsEmailField(this, 100) {
    override def displayName = "Correo eléctronico"
  }

  object photo extends FileField(this) {
    override def displayName = "Foto"
    override def toString = {
      value.fileName.get
    }
  }

  object previousWork extends BsonRecordListField(this, PreviousWork) with HtmlFixer {
    override def displayName = "Trabajos previos"
    def deleteJsCmd(body: IdMemoizeTransform, service: Service, previousWork: PreviousWork): JsCmd = {
      service.previousWork.set(service.previousWork.get.filter(p => p != previousWork))
      body.setHtml()
    }

    private def template =
      <div class="table-responsive">
        <table class="table table-striped" data-name="items">
          <thead>
            <tr>
              <th>#</th>
              <th>Nombre</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr data-name="item">
              <th scope="row" data-name="number">1</th>
              <td data-name="name">Nombre</td>
              <td>
                <div aria-label="..." role="group" class="btn-group">
                  <a data-name="edit" class="btn btn-default btn-warning " type="button" href="#!"><i class="fa fa-pencil"></i></a>
                  <a data-name="delete" class="btn btn-default btn-danger" type="button" href="#!"><i class="fa fa-remove"></i></a>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div> ++
      <div>
        <a data-name="add" data-target="#modal-previous-work" data-toggle="modal" class="btn btn-default btn-success" type="button" href="#">
          <i class="fa fa-plus-circle"></i> Agregar trabajo previo
        </a>
      </div>

    private def elem = {
      ("data-name=items" #> SHtml.idMemoize(body => {
        "data-name=item" #> owner.previousWork.get.zipWithIndex.map{ case (pw, index) => {
          "data-name=number *" #> (index + 1) &
          "data-name=name *" #> pw.name.get &
          "data-name=edit [onclick]" #> SHtml.ajaxInvoke(() => owner.previousWork.dialogHtml(body, owner, pw, false)) &
          "data-name=delete [onclick]" #> SHtml.ajaxInvoke(() => owner.previousWork.deleteJsCmd(body, owner, pw))
        }} &
        "data-name=add [onclick]" #> SHtml.ajaxInvoke(() => owner.previousWork.dialogHtml(body, owner))
      })).apply(template)
    }

    override def toForm = Full(elem)

    def dialogHtml(body: IdMemoizeTransform, service: Service, previousWork: PreviousWork = PreviousWork.createRecord, isNew: Boolean = true): JsCmd = {
      val modalId = nextFuncName
      def closeDiaglog = {
        Run("$('#" + modalId + "').modal('hide')")
      }
      val addJsCmd = () => {
        if (isNew) service.previousWork.set(service.previousWork.get ++ List(previousWork))
        body.setHtml() &
          closeDiaglog
      }

      val action = (if (isNew) "Agregar trabajo previo" else "Actualizar trabajo previo")

      val template = S.runTemplate(List("templates-hidden", "_previous-work-modal")) openOr Text("template not found")
      val html = {
        "data-name=title *" #> action &
          "data-name=modal [id]" #> modalId &
          "data-name=name" #> previousWork.name.toForm &
          "data-name=description" #> previousWork.description.toForm &
          "data-name=hidden" #> SHtml.hidden(addJsCmd) &
          "data-name=close [onclick]" #> SHtml.ajaxInvoke(() => closeDiaglog) &
          "type=submit *" #> action
      }.apply(template)
      val (xml, js) = fixHtmlAndJs("modal", html)
      Run("$(" + xml + ").modal();")
    }
  }

  override def toString = name.get
}

object Service extends Service with RogueMetaRecord[Service] {
  override def fieldOrder = List(name, description, responsible, photo, email)
}


class PreviousWork extends BsonRecord[PreviousWork] {
  def meta = PreviousWork
  object name extends BsStringField(this, 500) {
    override def displayName = "Nombre"
  }

  object description extends BsCkTextareaField(this, 500) {
    override def displayName = "Descripción"
  }
}

object PreviousWork extends PreviousWork with BsonMetaRecord[PreviousWork]
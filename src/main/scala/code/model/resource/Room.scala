package code
package model
package resource

import code.config.Site
import code.lib.RogueMetaRecord
import code.lib.field.{BsDoubleField, BsStringField, FileField}
import net.liftweb.common.{Full, Box}
import net.liftweb.mongodb.record.field.BsonRecordListField
import net.liftweb.mongodb.record.{BsonMetaRecord, BsonRecord}
import net.liftweb.record.LifecycleCallbacks
import net.liftweb.record.field.{EnumField, BooleanField}

import scala.xml.NodeSeq

class Room private() extends Resource[Room] {

  override def meta = Room

  def title = "Sala"

  def entityListUrl = Site.backendRooms.menu.loc.calcDefaultHref

  object capacity extends BsStringField(this, 500) {
    override def displayName = "Capacidad"
  }

  object code  extends BsStringField(this, 50) {
    override def displayName = "Código"
  }

  object plane extends FileField(this) {
    override def displayName = "Plano"
    override def toString = {
      value.fileName.get
    }
  }

  object isBookable extends BooleanField(this, false) {
    override def displayName = "Reservable"
  }

  object isBookableShift extends BooleanField(this, false) {
    override def displayName = "Reservable por turnos"
  }

  object photo1 extends FileField(this) {
    override def displayName = "Foto"
    override def toString = {
      value.fileName.get
    }
  }

  object photo2 extends FileField(this) {
    override def displayName = "Foto"
    override def toString = {
      value.fileName.get
    }
  }

  object location extends FileField(this) {
    override def displayName = "Ubicación"
    override def toString = {
      value.fileName.get
    }
  }

  object areasCost extends BsonRecordListField(this, Cost) with LifecycleCallbacks {
    override def displayName = "Costos por area"
    private var newCosts: List[Cost] = Nil
    override def toForm: Box[NodeSeq] = Full {
      Area.findAll.foldLeft(NodeSeq.Empty){ case (node, area) => {
        val cost = this.value.find(cost => cost.area.get == area.id.get).getOrElse{
          val c = Cost.createRecord.area(area.id.get)
          newCosts = newCosts ++ List(c)
          c
        }
        node ++
          <div class="row">
            <div class="col-md-6"><h5>{area.name.get}</h5></div>
            <div class="col-md-3">C.S.</div>
            <div class="col-md-3">S.S.</div>
          </div> ++
          <div class="row">
            <div class="form-group margin-bottom-xs">
              <label data-name="label" class="control-label col-md-6" for="email_id">Privado</label>
              <div class="col-md-3">
                {cost.costCSPrivate.toForm openOr NodeSeq.Empty}
                <span class="input-group-addon">
                  Bs.
                </span>
              </div>
              <div class="col-md-3">
                {cost.costSSPrivate.toForm openOr NodeSeq.Empty}
                <span class="input-group-addon">
                  Bs.
                </span>
              </div>
            </div>
          </div> ++
          <div class="row">
            <div class="form-group margin-bottom-xs">
              <label data-name="label" class="control-label col-md-6" for="email_id">Coordinador</label>
              <div class="col-md-3">
                {cost.costCSCoord.toForm openOr NodeSeq.Empty}
                <span class="input-group-addon">
                  Bs.
                </span>
              </div>
              <div class="col-md-3">
                {cost.costSSCoord.toForm openOr NodeSeq.Empty}
                <span class="input-group-addon">
                  Bs.
                </span>
              </div>
            </div>
          </div> ++
          <div class="row">
            <div class="form-group margin-bottom-xs">
              <label data-name="label" class="control-label col-md-6" for="email_id">Asociado</label>
              <div class="col-md-3">
                {cost.costCSAssociate.toForm openOr NodeSeq.Empty}
                <span class="input-group-addon">
                  Bs.
                </span>
              </div>
              <div class="col-md-3">
                {cost.costSSAssociate.toForm openOr NodeSeq.Empty}
                <span class="input-group-addon">
                  Bs.
                </span>
              </div>
            </div>
          </div> ++
          <div class="row">
            <div class="form-group margin-bottom-xs">
              <label data-name="label" class="control-label col-md-6" for="email_id">Artista</label>
              <div class="col-md-3">
                {cost.costCSArtist.toForm openOr NodeSeq.Empty}
                <span class="input-group-addon">
                  Bs.
                </span>
              </div>
              <div class="col-md-3">
                {cost.costSSArtist.toForm openOr NodeSeq.Empty}
                <span class="input-group-addon">
                  Bs.
                </span>
              </div>
            </div>
          </div> ++
          <div class="row">
            <div class="form-group margin-bottom-xs">
              <label data-name="label" class="control-label col-md-6" for="email_id">Institución cultural con fines de lucro</label>
              <div class="col-md-3">
                {cost.costCSInst1.toForm openOr NodeSeq.Empty}
                <span class="input-group-addon">
                  Bs.
                </span>
              </div>
              <div class="col-md-3">
                {cost.costSSInst1.toForm openOr NodeSeq.Empty}
                <span class="input-group-addon">
                  Bs.
                </span>
              </div>
            </div>
          </div> ++
          <div class="row">
            <div class="form-group margin-bottom-xs">
              <label data-name="label" class="control-label col-md-6" for="email_id">Institución cultural sin fines de lucro</label>
              <div class="col-md-3">
                {cost.costCSInst2.toForm openOr NodeSeq.Empty}
                <span class="input-group-addon">
                  Bs.
                </span>
              </div>
              <div class="col-md-3">
                {cost.costSSInst2.toForm openOr NodeSeq.Empty}
                <span class="input-group-addon">
                  Bs.
                </span>
              </div>
            </div>
          </div>
      }}
    }

    override def beforeSave: Unit = {
      this.set(this.get ++ newCosts)
    }
  }

  object programsCost extends BsonRecordListField(this, Cost) with LifecycleCallbacks {
    override def displayName = "Costos por programa"
    private var newCosts: List[Cost] = Nil
    override def toForm: Box[NodeSeq] = Full {
      Program.findAll.foldLeft(NodeSeq.Empty){ case (node, program) => {
        val cost = this.value.find(cost => cost.program.get == program.id.get).getOrElse{
          val c = Cost.createRecord.program(program.id.get)
          newCosts = newCosts ++ List(c)
          c
        }
        node ++
          <div class="row">
            <div class="col-md-6"><h5>{program.name.get}</h5></div>
            <div class="col-md-3">C.S.</div>
            <div class="col-md-3">S.S.</div>
          </div> ++
          <div class="row">
            <div class="form-group margin-bottom-xs">
              <label data-name="label" class="control-label col-md-6" for="email_id">Privado</label>
              <div class="col-md-3">
                {cost.costCSPrivate.toForm openOr NodeSeq.Empty}
                <span class="input-group-addon">
                  Bs.
                </span>
              </div>
              <div class="col-md-3">
                {cost.costSSPrivate.toForm openOr NodeSeq.Empty}
                <span class="input-group-addon">
                  Bs.
                </span>
              </div>
            </div>
          </div> ++
          <div class="row">
            <div class="form-group margin-bottom-xs">
              <label data-name="label" class="control-label col-md-6" for="email_id">Coordinador</label>
              <div class="col-md-3">
                {cost.costCSCoord.toForm openOr NodeSeq.Empty}
                <span class="input-group-addon">
                  Bs.
                </span>
              </div>
              <div class="col-md-3">
                {cost.costSSCoord.toForm openOr NodeSeq.Empty}
                <span class="input-group-addon">
                  Bs.
                </span>
              </div>
            </div>
          </div> ++
          <div class="row">
            <div class="form-group margin-bottom-xs">
              <label data-name="label" class="control-label col-md-6" for="email_id">Asociado</label>
              <div class="col-md-3">
                {cost.costCSAssociate.toForm openOr NodeSeq.Empty}
                <span class="input-group-addon">
                  Bs.
                </span>
              </div>
              <div class="col-md-3">
                {cost.costSSAssociate.toForm openOr NodeSeq.Empty}
                <span class="input-group-addon">
                  Bs.
                </span>
              </div>
            </div>
          </div> ++
          <div class="row">
            <div class="form-group margin-bottom-xs">
              <label data-name="label" class="control-label col-md-6" for="email_id">Artista</label>
              <div class="col-md-3">
                {cost.costCSArtist.toForm openOr NodeSeq.Empty}
                <span class="input-group-addon">
                  Bs.
                </span>
              </div>
              <div class="col-md-3">
                {cost.costSSArtist.toForm openOr NodeSeq.Empty}
                <span class="input-group-addon">
                  Bs.
                </span>
              </div>
            </div>
          </div> ++
          <div class="row">
            <div class="form-group margin-bottom-xs">
              <label data-name="label" class="control-label col-md-6" for="email_id">Institución cultural con fines de lucro</label>
              <div class="col-md-3">
                {cost.costCSInst1.toForm openOr NodeSeq.Empty}
                <span class="input-group-addon">
                  Bs.
                </span>
              </div>
              <div class="col-md-3">
                {cost.costSSInst1.toForm openOr NodeSeq.Empty}
                <span class="input-group-addon">
                  Bs.
                </span>
              </div>
            </div>
          </div> ++
          <div class="row">
            <div class="form-group margin-bottom-xs">
              <label data-name="label" class="control-label col-md-6" for="email_id">Institución cultural sin fines de lucro</label>
              <div class="col-md-3">
                {cost.costCSInst2.toForm openOr NodeSeq.Empty}
                <span class="input-group-addon">
                  Bs.
                </span>
              </div>
              <div class="col-md-3">
                {cost.costSSInst2.toForm openOr NodeSeq.Empty}
                <span class="input-group-addon">
                  Bs.
                </span>
              </div>
            </div>
          </div>
      }}
    }

    override def beforeSave: Unit = {
      this.set(this.get ++ newCosts)
    }
  }

}

object Room extends Room with RogueMetaRecord[Room] {
  override def collectionName = "resource.resources"

  override def fieldOrder = List(
    code, name, photo1, photo2, isBookable,
    isBookableShift, capacity, order, plane,
    location, description, areasCost, programsCost)

  def findAllBookeableEnabled: List[Room] = {
    Room
      .where(_.isBookable eqs true)
      .and(_.classType eqs ClassType.RoomType)
      .fetch()
  }

  override def findAll: List[Room] = {
    Room.where(_.classType eqs ClassType.RoomType).fetch()
  }
}


package code
package model

import code.lib.field.BsDoubleField
import code.model.resource.CostType._
import net.liftweb.mongodb.record.field.ObjectIdRefField
import net.liftweb.mongodb.record.{BsonMetaRecord, BsonRecord}
import net.liftweb.record.field.EnumField

class Cost extends BsonRecord[Cost] {

  def meta = Cost

  object area extends ObjectIdRefField(this, Area) {
    override def shouldDisplay_?  = false
    override def optional_? = true
  }

  object program extends ObjectIdRefField(this, Program) {
    override def shouldDisplay_?  = false
    override def optional_? = true
  }

  object costCSAreaCoord extends BsDoubleField(this, 0.0) {
    override def displayName = "C.S"
  }

  object costSSAreaCoord extends BsDoubleField(this, 0.0) {
    override def displayName = "S.S"
  }

  object costCSCoord extends BsDoubleField(this, 0.0) {
    override def displayName = "C.S"
  }

  object costSSCoord extends BsDoubleField(this, 0.0) {
    override def displayName = "S.S"
  }

  object costCSAssociate extends BsDoubleField(this, 0.0) {
    override def displayName = "C.S"
  }

  object costSSAssociate extends BsDoubleField(this, 0.0) {
    override def displayName = "S.S"
  }

  object costCSNewArtist extends BsDoubleField(this, 0.0) {
    override def displayName = "C.S"
  }

  object costSSNewArtist extends BsDoubleField(this, 0.0) {
    override def displayName = "S.S"
  }

  object costCSArtist extends BsDoubleField(this, 0.0) {
    override def displayName = "C.S"
  }

  object costSSArtist extends BsDoubleField(this, 0.0) {
    override def displayName = "S.S"
  }

  object costCSInst1 extends BsDoubleField(this, 0.0) {
    override def displayName = "C.S"
  }

  object costSSInst1 extends BsDoubleField(this, 0.0) {
    override def displayName = "S.S"
  }

  object costCSInst2 extends BsDoubleField(this, 0.0) {
    override def displayName = "C.S"
  }

  object costSSInst2 extends BsDoubleField(this, 0.0) {
    override def displayName = "S.S"
  }

  object costCSInst3 extends BsDoubleField(this, 0.0) {
    override def displayName = "C.S"
  }

  object costSSInst3 extends BsDoubleField(this, 0.0) {
    override def displayName = "S.S"
  }

}

object Cost extends Cost with BsonMetaRecord[Cost]

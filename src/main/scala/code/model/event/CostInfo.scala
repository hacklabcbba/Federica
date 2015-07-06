package code
package model
package event

import code.model.ProductiveUnit
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field._
import net.liftweb.record.field.{LongField, DecimalField, StringField}
import code.model.activity.Activity
import code.lib.RogueMetaRecord

class CostInfo private() extends MongoRecord[CostInfo] with ObjectIdPk[CostInfo]{

  override def meta = CostInfo
  object cost extends DecimalField(this, 0)
  object currency extends StringField(this, "")
  object description extends StringField(this, "")
}

object CostInfo extends CostInfo with RogueMetaRecord[CostInfo]
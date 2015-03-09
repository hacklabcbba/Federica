package code.model.event

import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.record.field.{StringField, BooleanField}
import net.liftweb.mongodb.record.field.{ObjectIdPk, ObjectIdRefListField}
import code.lib.RogueMetaRecord


class CostContributionByUse private() extends MongoRecord[CostContributionByUse] with ObjectIdPk[CostContributionByUse]{

  override def meta = CostContributionByUse

  object title extends StringField(this, "")
  //object itemList extends ObjectIdRefListField(this, CostSelection)
}
object CostContributionByUse extends CostContributionByUse with RogueMetaRecord[CostContributionByUse]{
}









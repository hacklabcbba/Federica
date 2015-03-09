package code
package model
package event

import code.lib.RogueMetaRecord
import code.model.project._
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field._
import net.liftweb.record.field.{LongField, DecimalField, StringField}
import code.model.proposal.{ActionLine, Area, Program}
import code.model.activity.{Activity, ActivityType}
import code.model.productive.ProductiveUnit

class Event private() extends MongoRecord[Event] with ObjectIdPk[Event]{

  override def meta = Event

  object eventNumber extends LongField(this)
  object name extends StringField(this, 200)
  object schedule extends ObjectIdRefField(this, DateInfoList)
  object costInfo extends ObjectIdRefField(this, CostInfo)
  object eventTypes extends ObjectIdRefListField(this, EventType)
  object area extends ObjectIdRefField(this, Area)
  object program extends ObjectIdRefField(this, Program)
  object productiveUnit extends ObjectIdRefField(this, ProductiveUnit){
    override def optional_? = true
  }
  object city extends ObjectIdRefField(this, City)
  object country extends ObjectIdRefField(this, Country)
  object place extends StringField(this, "")
  object shortDescription extends StringField(this, 500)
  object activities extends ObjectIdRefListField(this, Activity)
  object description extends StringField(this, 1000)
  object requirementsList extends ObjectIdRefListField(this, EventRequirement)

  object expositor extends ObjectIdRefListField(this, User)
  object organizer extends ObjectIdRefListField(this, User)
  object handler extends ObjectIdRefListField(this, User)
  object sponsor extends ObjectIdRefListField(this, User)
  object support extends ObjectIdRefListField(this, User)
  object collaborator extends ObjectIdRefListField(this, User)
  object pressRoom extends ObjectIdRefListField(this, Gallery)

  object goals extends StringField(this, "")
  object quote extends StringField(this, "")
  object tools extends StringField(this, "")
  object supplies extends StringField(this, "")
  object registration extends StringField(this, "")

  object costContributionByUse extends ObjectIdRefField(this, CostContributionByUse)
  //object schedule extends MongoListField[Event, Schedule](this)
  object process extends ObjectIdRefField(this, Process)
  object actionLines extends ObjectIdRefListField(this, ActionLine)
}

object Event extends Event with RogueMetaRecord[Event]
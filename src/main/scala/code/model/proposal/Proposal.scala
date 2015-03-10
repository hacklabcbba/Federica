package code
package model
package proposal

import code.lib.RogueMetaRecord
import code.model.activity.ActivityType
import code.model.productive.ProductiveUnit
import code.model.project.{Country, City}
import code.model.resource.Resource
import net.liftweb.mongodb.record.field.{MongoListField, ObjectIdPk, ObjectIdRefField}
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.record.field.{EnumNameField, DateTimeField, BooleanField, StringField}

class Proposal private () extends MongoRecord[Proposal] with ObjectIdPk[Proposal] {
  override def meta = Proposal

  object name extends StringField(this, 500)
  object description extends StringField(this, 500)
  object concept extends StringField(this, 500)
  object review extends StringField(this, 500)
  object state extends StringField(this, 100)
  object city extends ObjectIdRefField(this, City)
  object country extends ObjectIdRefField(this, Country)
  object area extends ObjectIdRefField(this, Area)
  object productiveUnit extends ObjectIdRefField(this, ProductiveUnit)
  object isAproved extends BooleanField(this, false)
  object currentDate extends DateTimeField(this)
  object activityType extends ObjectIdRefField(this, ActivityType)
  object administrator extends ObjectIdRefField(this, User)
  object applicant extends ObjectIdRefField(this, User)
  object dateInit extends DateTimeField(this)
  object dateEnd extends DateTimeField(this)
  object reserveType extends EnumNameField(this, ReserveType)
}

object Proposal extends Proposal with RogueMetaRecord[Proposal]

object ReserveType extends Enumeration {
  type ReserveType = Value
  val Continuous, Discontinuous, Loose = Value
}

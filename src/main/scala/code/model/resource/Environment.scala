package code
package model
package resource

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.ObjectIdPk

class Environment private() extends MongoRecord[Environment] with ObjectIdPk[Environment]{

  override def meta = Environment

}

object Environment extends Environment with RogueMetaRecord[Environment]
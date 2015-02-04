package code
package model
package resource

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.ObjectIdPk

class Resource private() extends MongoRecord[Resource] with ObjectIdPk[Resource]{

  override def meta = Resource

}

object Resource extends Resource with RogueMetaRecord[Resource]
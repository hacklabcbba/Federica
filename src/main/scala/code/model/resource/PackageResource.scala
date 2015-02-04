package code
package model
package resource

import code.lib.RogueMetaRecord
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.ObjectIdPk

class PackageResource private() extends MongoRecord[PackageResource] with ObjectIdPk[PackageResource]{

  override def meta = PackageResource

}

object PackageResource extends PackageResource with RogueMetaRecord[PackageResource]
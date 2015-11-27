package code
package model

import code.lib.RogueMetaRecord
import code.lib.field.BsStringField
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.mongodb.record.field.ObjectIdPk

class BlogPost private() extends MongoRecord[BlogPost] with ObjectIdPk[BlogPost]{

  override def meta = BlogPost

  object name extends BsStringField(this, 200)
}

object BlogPost extends BlogPost with RogueMetaRecord[BlogPost] {
  override def collectionName = "main.blog_posts"
}
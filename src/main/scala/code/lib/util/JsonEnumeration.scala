package code.lib.util

import net.liftweb.json.JsonAST.{JField, JObject, JValue, JString}
import net.liftweb.common._
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import net.liftweb.http.S

trait JsonEnumeration extends Enumeration {

  def fromJson(in: JValue): Box[this.Value] = in match {
    case JInt(s) => values.find(_.id == s)
    case _ => Empty
  }

  def valuesAsJValue: JValue = {
    implicit val formats = DefaultFormats
    values.map(s => JObject(List(JField("id", s.id), JField("name", S ? s.toString))))
  }

  def fromId(in: Int): Box[this.Value] = values.find(_.id  == in)

}

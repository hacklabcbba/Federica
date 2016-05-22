package code.lib

import net.liftweb.http.S

object Helper {

  def getParameter: List[(String, String)] = {
    val parameters : Map[String,List[String]] = S.request.toList.flatMap(_.params).toMap
    val key = parameters.keys.headOption
    val value = parameters.values.headOption.map(_.headOption).flatten
    (key, value) match {
      case (Some(k), Some(v)) =>
        List((k, v))
      case _ =>
        Nil
    }
  }
}

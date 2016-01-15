package code
package lib
package js

import xml.NodeSeq
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds._

object Bootstrap {

  implicit def pairToBootstrapOption(pair: (String, Any)) = BootstrapOption(pair._1, pair._2)

  implicit def pairsToBootstrapOptions(pairs: List[(String, Any)]) = pairs.map(pair => pairToBootstrapOption(pair))

  object Modal {

    val defaultId = "modal"

    def apply(body: NodeSeq, options: (String, Any)*): JsCmd = Modal(body, defaultId, options.toList)

    def apply(body: NodeSeq, id: String, options: (String, Any)*): JsCmd = Modal(body, id, options.toList)

    def close(id: String) = {
      Run("""$('#""" + id + """').modal('hide')""") &
        Run("""$('#""" + id + """').remove()""")
    }

    def close() = {
      Run("""$('#""" + defaultId + """').modal('hide')""") &
        Run("""$('#""" + defaultId + """').remove()""")
    }
  }

  case class Modal(data: NodeSeq, id: String, options: List[BootstrapOption]) extends JsCmd {

    val (modal,_) = fixHtmlAndJs("inline", <div id={id} class="modal">{data}</div>)

    lazy val js = """$(""" + modal + """).modal(""" + optionsToString + """)
                     .on('hidden.bs.modal', function (e) {
                       $('#""" + id + """').remove();
                     });"""

    lazy val toJsCmd = js

    def optionsToString() = options match {
      case Nil =>
        ""
      case head :: Nil =>
        head
      case list =>
        list.foldLeft(""){case (string,option) => string + option}
    }

    def toggle() = Run("""$('""" + id + """').modal('toggle')""")

    def show() = Run("""$('""" + id + """').modal('show')""")

    def hide() = Run("""$('""" + id + """').modal('hide')""")

  }
}

case class BootstrapOption(name: String, value: Any) {
  override def toString() = name + ": " + (value match {
    case (int: Int) =>
      int.toString
    case (string: String) =>
      "'" + string + "'"
    case (boolean: Boolean) =>
      boolean.toString
    case _ =>
      value.toString
  })
}
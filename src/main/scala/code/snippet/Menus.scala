package code
package snippet

import net.liftmodules.extras.snippet._
import net.liftweb.common.Full
import net.liftweb.http.S
import net.liftweb.util.{Helpers, CssSel}
import Helpers._

object Menus extends BsMenu {
  def title: CssSel = {
    val r =
      for {
        request <- S.request
        loc <- request.location
      } yield {
        loc.currentValue match {
          case Full(value) if value.toString != "()"=> value.toString + " | "
          case _ => if (loc.title.toString == "Inicio") "" else loc.title + " | "
        }
      }

    "title *" #> s"${r.openOr("")} mARTadero | Arte y Cultura | Cochabamba Bolivia "
  }
}

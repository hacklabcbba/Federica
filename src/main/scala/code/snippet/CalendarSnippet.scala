package code.snippet

import code.model.User
import net.liftmodules.extras.SnippetHelper
import net.liftweb.util.CssSel
import net.liftweb.util.Helpers._

import scala.xml.NodeSeq

/**
  * Created by Nataly on 20/04/2016.
  */
object CalendarSnippet extends SnippetHelper {

  def render: CssSel = {
    if(!User.hasRole("Usuario")){
      "data-name=title *" #> "Calendario"
    } else {
      "*" #> NodeSeq.Empty
    }
  }
}

package code.snippet

import code.config.Site
import code.model.User
import code.model.event.Event
import net.liftmodules.extras.SnippetHelper
import net.liftweb.http.js.JsCmds
import net.liftweb.http.{S, SHtml}
import net.liftweb.http.js.JsCmds.{RedirectTo, Run}
import net.liftweb.util.CssSel
import net.liftweb.util.Helpers._

object DashboardSnippet extends SnippetHelper {

  def render: CssSel = {
    for {
      user <- User.currentUser ?~ "Para ver esta sección debe iniciar una sesión"
    } yield {
      "data-name=checkbox" #> SHtml.ajaxCheckbox(false, a => {
        S.appendJs(Run(
          """
            var sendbtn = document.getElementById('createEvent');
            sendbtn.disabled = """ + (!a).toString + """;
          """.stripMargin))
      }) &
      "data-name=createEvent [onclick]" #> SHtml.ajaxInvoke(() => RedirectTo(Site.backendEventAdd.calcHref(Event.createRecord)))
    }
  }
}

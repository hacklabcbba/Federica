package code
package snippet

import code.config.Site
import code.lib.ReCaptcha
import code.model.{LoginCredentials, User}
import net.liftmodules.extras.{Gravatar, SnippetHelper}
import net.liftmodules.mongoauth.LoginRedirect
import net.liftmodules.mongoauth.field.PasswordField
import net.liftmodules.mongoauth.model.ExtSession
import net.liftweb.common._
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.{S, SHtml}
import net.liftweb.util.Helpers._
import net.liftweb.util.Mailer.{From, Subject, To}
import net.liftweb.util._

import scala.xml._

sealed trait UserSnippet extends SnippetHelper with Loggable {

  protected def user: Box[User]

  protected def serve(snip: User => NodeSeq): NodeSeq =
    (for {
      u <- user ?~ "User not found"
    } yield {
      snip(u)
    }): NodeSeq

  protected def serve(html: NodeSeq)(snip: User => CssSel): NodeSeq =
    (for {
      u <- user ?~ "User not found"
    } yield {
      snip(u)(html)
    }): NodeSeq

  def header(xhtml: NodeSeq): NodeSeq = serve { user =>
    name(xhtml)
  }

  def bio(xhtml: NodeSeq): NodeSeq =  serve {
    user => user.bio.asHtml
  }

  def gravatar(xhtml: NodeSeq): NodeSeq = {
    val size = S.attr("size").map(toInt) openOr Gravatar.defaultSize.vend

    serve { user =>
      Gravatar.imgTag(user.email.get, size)
    }
  }

  def socialNetworks: CssSel = {
    for {
      user <- user
    } yield {
      (user.facebook.valueBox match {
        case Full(f) if f.trim.nonEmpty =>
          "data-name=facebook-url [href]" #> s"https://www.facebook.com/$f" &
          "data-name=facebook-username" #> f
        case _ =>
          "data-name=facebook" #> NodeSeq.Empty
      }) &
      (user.twitter.valueBox match {
        case Full(f) if f.trim.nonEmpty =>
          "data-name=twitter-url [href]" #> s"https://twitter.com/$f" &
          "data-name=twitter-username" #> f
        case _ =>
          "data-name=twitter" #> NodeSeq.Empty
      }) &
      (user.youtube.valueBox match {
        case Full(f) if f.trim.nonEmpty =>
          "data-name=youtube-url [href]" #> s"https://www.youtube.com/user/$f" &
          "data-name=youtube-username" #> f
        case _ =>
          "data-name=youtube" #> NodeSeq.Empty
      }) &
      (user.googleplus.valueBox match {
        case Full(f) if f.trim.nonEmpty =>
          "data-name=googleplus-url [href]" #> s"https://plus.google.com/+$f" &
          "data-name=googleplus-username" #> f
        case _ =>
          "data-name=googleplus" #> NodeSeq.Empty
      }) &
      (user.instagram.valueBox match {
        case Full(f) if f.trim.nonEmpty =>
          "data-name=instagram-url [href]" #> s"https://www.instagram.com/$f" &
           "data-name=instagram-username" #> f
        case _ =>
          "data-name=instagram" #> NodeSeq.Empty
      })
    }
  }

  def username(xhtml: NodeSeq): NodeSeq = serve { user =>
    Text(user.username.get)
  }

  def name(xhtml: NodeSeq): NodeSeq = serve { user =>
    if (user.name.get.length > 0)
      Text("%s (%s)".format(user.name.get, user.username.get))
    else
      Text(user.username.get)
  }

  def title(xhtml: NodeSeq): NodeSeq = serve { user =>
    <lift:head>
      <title lift="Menu.title">{"Federica: %*% - "+user.username.get}</title>
    </lift:head>
  }
}

object CurrentUser extends UserSnippet {
  protected def user = User.currentUser
  def render: CssSel = {
    for {
      user <- user
    } yield {
      val size = S.attr("size").map(toInt) openOr Gravatar.defaultSize.vend
      "data-name=user-name *" #> user.name.get &
      "data-name=user-avatar" #> Gravatar.imgTag(user.email.get, size) &
      "data-name=user-status" #> user.isOnline.asHtml
    }: CssSel
  }
}

object ProfileLocUser extends UserSnippet {

  protected def user = Site.profileLoc.currentValue

  import java.text.SimpleDateFormat

  val df = new SimpleDateFormat("MMM d, yyyy")

  def profile(html: NodeSeq): NodeSeq = serve(html) { user =>
    val editLink: NodeSeq =
      if (User.currentUser.filter(_.id.get == user.id.get).isDefined)
        <a href={Site.editProfile.url} class="btn btn-info"><i class="icon-edit icon-white"></i> Edit Your Profile</a>
      else
        NodeSeq.Empty

    "#id_avatar *" #> Gravatar.imgTag(user.email.get) &
    "#id_name *" #> <h3>{user.name.get}</h3> &
    "#id_location *" #> user.location.get &
    "#id_whencreated" #> df.format(user.whenCreated.toDate).toString &
    "#id_bio *" #> user.bio.get &
    "#id_editlink *" #> editLink
  }
}

object UserConfirmation extends SnippetHelper {
  def render: CssSel = for {
    user <- Site.emailConfirmation.currentValue ?~ "Ocurrio un error. Porfavor intente nuevamente."
  } yield {
    val userValidated = user.verified(true)
    User.update(userValidated)
    val message = "Felicidades su cuenta ha sido creada con exito. Ahora usted puede ingresar al sistema."

    "data-name=message" #> message
  }
}

object UserResetPassword extends SnippetHelper {

  def doSubmit(pwd: String, repeatPwd: String): JsCmd = {

    (for {
      user <- Site.passwordRecovery.currentValue
    } yield {
      if((pwd == repeatPwd) && !pwd.trim.isEmpty) {
        user.password(pwd, true).update
        RedirectTo("/", () => S.notice("Su contraseña ha sido cambiado con exito."))
      } else {
        S.error("recovery_err", "Las contraseñas no coinciden")
        Noop
      }
    }) openOr Noop
  }

  def render: CssSel = {
    var repeatPwd = ""
    var pwd = ""
    "data-name=password" #> SHtml.ajaxText("", s => {
      pwd = s
      Noop
    }, "type" -> "password") &
      "data-name=repeatPwd" #> SHtml.ajaxText("", s => {
        repeatPwd = s
        Noop
      }, "type" -> "password") &
    "data-name=submit" #> SHtml.ajaxOnSubmit(() => doSubmit(pwd, repeatPwd))
  }
}

object UserRegister extends ReCaptcha with SnippetHelper {
  def render: CssSel = {
    //form vars
    var newUser = User.createRecord
    var repeatPwd = ""
    var pwd = ""

    def doSubmit(): JsCmd = {
      newUser.username(newUser.email.get)
      if(validateCaptcha.isDefined){
        // invalid captcha error message
        S.error("captchaError", "Invalid captcha")
        reloadCaptcha
      } else {
        newUser.validate match {
          case Nil =>
            if((pwd == repeatPwd) && !pwd.trim.isEmpty) {
              newUser.verified(false)
              newUser.password(pwd, true).save(true)
              User.sendEmailConfirmation(newUser)
              RedirectTo("/", () => S.notice("Un correo electronico ha sido enviado a su cuenta con instrucciones para acceder."))
            } else {
              S.error("register_err", "Las contraseñas no coinciden")
              Noop
            }
          case error: List[FieldError] =>
            S.error(error)
            Noop
        }
      }
    }

    "data-name=name" #> newUser.name.toForm &
    "data-name=lastName" #> newUser.lastName.toForm &
    "data-name=email" #> newUser.email.toForm &
    "data-name=password" #> SHtml.ajaxText("", s => {
      pwd = s
      Noop
    }, "type" -> "password") &
    "data-name=repeatPwd" #> SHtml.ajaxText("", s => {
      repeatPwd = s
      Noop
    }, "type" -> "password") &
    "data-name=captcha" #> captchaXhtml &
    "data-name=submit" #> SHtml.ajaxSubmit("Enviar", doSubmit)
  }
}

object UserLogin extends Loggable with SnippetHelper {

  def render: CssSel = {
    // form vars
    var password = ""
    var remember = User.loginCredentials.is.isRememberMe

    def doSubmit(): JsCmd = {
      val referer = S.referer.openOr("/dashboard")
      S.param("email").map(e => {
        val email = e.toLowerCase.trim
        // save the email and remember entered in the session var
        User.loginCredentials(LoginCredentials(email, remember))

        if (email.length > 0 && password.length > 0) {
          User.findByEmail(email) match {
            case Full(user) if (user.password.isMatch(password)) =>
              logger.debug("pwd matched")
              if(user.verified.get == true) {
                User.logUserIn(user, true)
                if (remember) User.createExtSession(user.id.get)
                else ExtSession.deleteExtCookie()
                RedirectTo("/")
              } else {
                S.error("login_err", "Aun falta validar esta cuenta. Porfavor revise su correo electronico para terminar el proceso.")
                Noop
              }
            case _ =>
              S.error("login_err", "El email o el password son incorrectos")
              Noop
          }
        }
        else if (email.length <= 0 && password.length > 0) {
          S.error("login_err", "El email o el password son incorrectos")
          Noop
        }
        else if (password.length <= 0 && email.length > 0) {
          S.error("login_err", "El email o el password son incorrectos")
          Noop
        }
        else if (email.length > 0) {
          // see if email exists in the database
          User.findByEmail(email) match {
            case Full(user) =>
              User.sendLoginToken(user)
              User.loginCredentials.remove()
              S.notice("An email has been sent to you with instructions for accessing your account")
              Noop
            case _ =>
              RedirectTo(Site.register.url)
          }
        }
        else {
          S.error("login_err", "El email o el password son incorrectos")
          Noop
        }
      }) openOr {
        S.error("login_err", "El email o el password son incorrectos")
        Noop
      }
    }

    "#id_email [value]" #> User.loginCredentials.is.email &
    "#id_password" #> SHtml.password(password, password = _) &
    "name=remember" #> SHtml.checkbox(remember, remember = _) &
    "data-name=recoverPwd [onclick]" #> SHtml.ajaxInvoke(() => {
      RedirectTo("/recovery/email")
    }) &
    "#id_submit" #> SHtml.hidden(doSubmit)
  }
}

object EmailRecovery extends SnippetHelper {
  var email = ""
  def render: CssSel = {
    "data-name=email" #> SHtml.ajaxText("", s => {
      email = s
    }) &
    "data-name=save" #> SHtml.ajaxButton("Recuperar contraseña", () => {
      email.trim.isEmpty match {
        case false =>
          User.findByEmail(email) match {
            case Full(user) =>
              User.sendPasswordRecovery(user)
              S.notice("recovery_not", "Se envio un email a su cuenta con instrucciones para acceder a su cuenta de mARTadero.")
            case _ =>
              S.error("recovery_error", "No encontramos una cuenta con ese correo electronico. Verifique y vuelva a intentar")
          }
        case true =>
          S.error("recovery_error", "Ingrese un correo electronico.")
      }
      Noop
    })
  }
}

object UserTopbar {
  def render = {
    User.currentUser match {
      case Full(user) =>
        <div data-lift="embed?what=/templates-hidden/parts/user-block"></div>
      case _ =>
        <div data-lift="embed?what=/templates-hidden/parts/login-block"></div>
    }
  }
}

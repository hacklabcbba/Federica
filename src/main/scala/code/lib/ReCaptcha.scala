package code.lib

import net.liftweb.common.{ Box, Empty, Full, Failure }
import net.liftweb.util.{ FieldError, FieldIdentifier }
import net.liftweb.http.S
import net.tanesha.recaptcha.ReCaptchaFactory
import net.liftweb.json.JsonDSL._
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JE.JsFunc._
import net.liftweb.http.js.JE.JsFunc
import scala.xml.Unparsed
import net.liftweb.json.JsonAST._
import net.liftweb.json.Printer._
import net.liftweb.util.Props

trait ReCaptcha {

  val reCaptchaPublicKey: String = Props.get("recaptcha.publicKey", "")
  val reCaptchaPrivateKey: String = Props.get("recaptcha.privateKey", "")

  protected def reCaptchaOptions = ("theme" -> "white") ~ ("lang" -> S.containerRequest.flatMap(_.locale).map(_.getLanguage).getOrElse("en"))

  private lazy val reCaptcha = ReCaptchaFactory.newReCaptcha(reCaptchaPublicKey, reCaptchaPrivateKey, false)

  protected def captchaXhtml() = {
    val RecaptchaOptions = compact(render(reCaptchaOptions))

    <xml:group>
      <script>
        var RecaptchaOptions ={ Unparsed(RecaptchaOptions) }
        ;
      </script>
      <script type="text/javascript" src={ "http://api.recaptcha.net/challenge?k=" + reCaptchaPublicKey }></script>
      <noscript>
        <iframe src={ "http://www.google.com/recaptcha/api/noscript?k=" + reCaptchaPublicKey } height="300" width="500" frameborder="0"></iframe><br/>
        <textarea name="recaptcha_challenge_field" rows="3" cols="40">
        </textarea>
        <input type="hidden" name="recaptcha_response_field" value="manual_challenge"/>
      </noscript>
    </xml:group>
  }

  protected def validateCaptcha(): Box[String] = {
    def checkAnswer(remoteAddr: String, challenge: String, uresponse: String): Box[String] = {
      val reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse)
      reCaptchaResponse.isValid() match {
        case true =>
          println("es valido")
          Empty
        case false =>
          println("no es valido")
          Full(S.?("reCaptcha." + reCaptchaResponse.getErrorMessage))
      }
    }
    val res = for (
      remoteAddr <- S.containerRequest.map(_.remoteAddress);
      challenge <- S.param("recaptcha_challenge_field");
      uresponse <- S.param("recaptcha_response_field");
      b <- checkAnswer(remoteAddr, challenge, uresponse)
    ) yield b

    res match {
      case Failure(msg, _, _) => Full(msg)
      case Full(msg) => Full(msg)
      case Empty => Empty
    }
  }

  /**
    * to load a new Captcha with ajax
    * @return JsCmd
    */
  protected def reloadCaptcha(): JsCmd = JsFunc("Recaptcha.reload").cmd

  case class FakeFieldIdentifier(override val uniqueFieldId: Box[String]) extends FieldIdentifier

}
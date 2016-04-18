package code
package model

import code.config.{Permissions, DefaultRoles, Site}
import code.lib.field.{BsCkTextareaField, BsEmailField, BsStringField}
import code.lib.{BaseModel, RogueMetaRecord}
import net.liftmodules.mongoauth._
import net.liftmodules.mongoauth.field._
import net.liftmodules.mongoauth.model._
import net.liftweb.common._
import net.liftweb.http.{BooleanField => _, S, SHtml, StringField => _, _}
import net.liftweb.mongodb.record.field._
import net.liftweb.record.field.{PasswordField => _, _}
import net.liftweb.sitemap.Loc.If
import net.liftweb.util.Helpers._
import net.liftweb.util.{FieldContainer, FieldError}
import org.bson.types.ObjectId
import org.joda.time.DateTime

import scala.xml._

class User private () extends MongoAuthUser[User] with ObjectIdPk[User] with BaseModel[User] {
  def meta = User

  def userIdAsString: String = id.toString

  def title = "Usuario"

  def entityListUrl = Site.backendUsers.menu.loc.calcDefaultHref

  object username extends BsStringField(this, 32) {
    override def displayName = "Nombre de usuario"
    override def setFilter = trim _ :: super.setFilter

    private def valUnique(msg: => String)(value: String): List[FieldError] = {
      if (value.length > 0)
        meta.findAll(name, value).filterNot(_.id.get == owner.id.get).map(u =>
          FieldError(this, Text(msg))
        )
      else
        Nil
    }

    override def validations =
      valUnique(S ? "liftmodule-monogoauth.monogoAuthUser.username.validation.unique") _ ::
        valMinLen(3, S ? "liftmodule-monogoauth.monogoAuthUser.username.validation.min.length") _ ::
        valMaxLen(32, S ? "liftmodule-monogoauth.monogoAuthUser.username.validation.max.length") _ ::
        super.validations
  }

  /*
  * http://www.dominicsayers.com/isemail/
  */
  object email extends BsEmailField(this, 254) {
    override def displayName = "Correo electrónico"
    override def setFilter = trim _ :: toLower _ :: super.setFilter

    private def valUnique(msg: => String)(value: String): List[FieldError] = {
      owner.meta.findAll(name, value).filter(_.id.get != owner.id.get).map(u =>
        FieldError(this, Text(msg))
      )
    }

    override def validations =
      valUnique("That email address is already registered with us") _  ::
        valMaxLen(254, "Email must be 254 characters or less") _ ::
        super.validations
  }
  // email address has been verified by clicking on a LoginToken link
  object verified extends BooleanField(this) {
    override def displayName = "Verificado"
  }
  object password extends PasswordField(this, 6, 32) {
    override def displayName = "Contraseña"
    override def elem = S.fmapFunc(S.SFuncHolder(s => {
      if (s != this.get) {
        this.setBox(PasswordField.hashpw(s))
      }
    })) {
      funcName => <input type="password" maxlength={maxLength.toString}
                         name={funcName}
                         value={valueBox openOr ""}
                         tabindex={tabIndex toString} class="form-control"/>}
  }
  object permissions extends PermissionListField(this) {
    override def displayName = "Permisos"
    override def options = Permissions.list.map(p => p -> p.toString())
    private def elem: Elem = SHtml.multiSelectObj[Permission](options, this.value, list => {
      set(list)
    }, "class" -> "form-control")
    override def toForm: Box[NodeSeq] = Full(elem)
    override def toString = this.value.mkString(", ")
  }

  object roles extends StringRefListField(this, Role) {
    override def displayName = "Roles"
    def permissions: List[Permission] = objs.flatMap(_.permissions.get)
    def names: List[String] = objs.map(_.id.get)
    override def options: List[(String, String)] = DefaultRoles.list.map(s => s.id.get -> s.id.get)
    super.options
    private def elem: Elem = SHtml.multiSelect(options, names, list => {
      set(list)
    }, "class" -> "form-control")
    override def toForm: Box[NodeSeq] = Full(elem)
    override def toString = names.mkString(", ")
  }

  lazy val authPermissions: Set[Permission] = (permissions.get ::: roles.permissions).toSet
  lazy val authRoles: Set[String] = roles.names.toSet

  lazy val fancyEmail = AuthUtil.fancyEmail(username.get, email.get)

  object locale extends LocaleField(this) {
    override def displayName = "Idioma"
    override def defaultValue = "es_BO"

    private def elem = SHtml.select(buildDisplayList, Full(valueBox.map(_.toString) openOr ""),
      locale => setBox(Full(locale)), "tabindex" -> tabIndex.toString, "class" -> "form-control")

    override def toForm: Box[NodeSeq] =
      uniqueFieldId match {
        case Full(id) => Full(elem % ("id" -> id))
        case _ => Full(elem)
      }
  }
  object timezone extends TimeZoneField(this) {
    override def displayName = "Zona horaria"
    override def defaultValue = "America/La_Paz"
    private def elem = SHtml.select(buildDisplayList, Full(valueBox openOr ""),
      timezone => setBox(Full(timezone))) % ("tabindex" -> tabIndex.toString) % ("class" -> "form-control")

    override def toForm: Box[NodeSeq] =
      uniqueFieldId match {
        case Full(id) => Full(elem % ("id" -> id))
        case _ => Full(elem)
      }
  }

  object name extends BsStringField(this, 64) {
    override def displayName = "Nombre"

    override def validations =
      valMaxLen(64, "Name must be 64 characters or less") _ ::
      super.validations
  }

  object lastName extends BsStringField(this, 64) {
    override def displayName = "Apellidos"

    override def validations =
      valMaxLen(64, "Name must be 64 characters or less") _ ::
        super.validations
  }

  object location extends BsStringField(this, 64) {
    override def displayName = "Lugar"

    override def validations =
      valMaxLen(64, "Location must be 64 characters or less") _ ::
      super.validations
  }
  object bio extends BsCkTextareaField(this, 5000) {
    override def displayName = "Bio"

    override def validations =
      valMaxLen(5000, "Bio must be 500 characters or less") _ ::
      super.validations
  }

  object facebook extends BsStringField(this, 128) {
    override def displayName = "Nombre de usuario en Facebook"
    override def optional_? = true
    override def validations =
      valMaxLen(128, "Facebook must be 128 characters or less") _ ::
        super.validations
  }

  object twitter extends BsStringField(this, 128) {
    override def displayName = "Nombre de usuario en Twitter"
    override def optional_? = true
    override def validations =
      valMaxLen(128, "Twitter must be 128 characters or less") _ ::
        super.validations
  }

  object youtube extends BsStringField(this, 128) {
    override def displayName = "Nombre de usuario en Youtube"
    override def optional_? = true
    override def validations =
      valMaxLen(128, "Youtube must be 128 characters or less") _ ::
        super.validations
  }

  object googleplus extends BsStringField(this, 128) {
    override def displayName = "Nombre de usuario en Googleplus"
    override def optional_? = true
    override def validations =
      valMaxLen(128, "Googleplus must be 128 characters or less") _ ::
        super.validations
  }

  object instagram extends BsStringField(this, 128) {
    override def displayName = "Nombre de usuario en Instagram"
    override def optional_? = true
    override def validations =
      valMaxLen(128, "Instagram must be 128 characters or less") _ ::
        super.validations
  }

  object gnusocial extends BsStringField(this, 128) {
    override def displayName = "Nombre de usuario en gnusocial"
    override def optional_? = true
    override def validations =
      valMaxLen(128, "gnusocial must be 128 characters or less") _ ::
        super.validations
  }

  object flickr extends BsStringField(this, 128) {
    override def displayName = "Nombre de usuario en Flickr"
    override def optional_? = true
    override def validations =
      valMaxLen(128, "gnusocial must be 128 characters or less") _ ::
        super.validations
  }

  object isOnline extends BooleanField(this, false) {
    override def shouldDisplay_? = false
    override def asHtml =
      if (this.value) <a href="#"><i class="fa fa-circle text-success"></i> Online</a>
      else <a href="#"><i class="fa fa-circle text-danger"></i> Offline</a>
  }

  /*
   * FieldContainers for various LiftScreeens.
   */
  def accountScreenFields = new FieldContainer {
    def allFields = List(username, email, locale, timezone)
  }

  def profileScreenFields = new FieldContainer {
    def allFields = List(name, lastName, email, timezone, locale, location, bio, gnusocial,
      facebook, twitter, youtube, instagram,
      flickr)
  }

  def registerScreenFields = new FieldContainer {
    def allFields = List(username, email)
  }

  def whenCreated: DateTime = new DateTime(id.get.getDate)

  override def toString = name.get
}

object User extends User with ProtoAuthUserMeta[User] with RogueMetaRecord[User] with Loggable {
  import net.liftweb.mongodb.BsonDSL._

  override def collectionName = "user.users"

  override def fieldOrder = List(
    name, lastName, email, timezone,
    locale, permissions, bio, gnusocial,
    facebook, twitter, youtube, instagram,
    flickr, verified, password)

  createIndex((email.name -> 1), true)
  createIndex((username.name -> 1), true)

  def findByEmail(in: String): Box[User] = find(email.name, in)
  def findByUsername(in: String): Box[User] = find(username.name, in)

  def findByStringId(id: String): Box[User] =
    if (ObjectId.isValid(id)) find(new ObjectId(id))
    else Empty

  override def onLogIn: List[User => Unit] = List(user => User.loginCredentials.remove())
  override def onLogOut: List[Box[User] => Unit] = List(
    x => logger.debug("User.onLogOut called."),
    boxedUser => boxedUser.foreach { u =>
      ExtSession.deleteExtCookie()
    }
  )

  /*
   * MongoAuth vars
   */
  private lazy val siteName = MongoAuth.siteName.vend
  private lazy val sysUsername = MongoAuth.systemUsername.vend
  private lazy val indexUrl = MongoAuth.indexUrl.vend
  private lazy val registerUrl = MongoAuth.registerUrl.vend
  private lazy val loginTokenAfterUrl = MongoAuth.loginTokenAfterUrl.vend

  /*
   * LoginToken
   */
  override def handleLoginToken: Box[LiftResponse] = {
    val resp = S.param("token").flatMap(LoginToken.findByStringId) match {
      case Full(at) if (at.expires.isExpired) => {
        at.delete_!
        RedirectWithState(indexUrl, RedirectState(() => { S.error("Login token has expired") }))
      }
      case Full(at) => find(at.userId.get).map(user => {
        if (user.validate.length == 0) {
          user.verified(true)
          user.update
          logUserIn(user)
          at.delete_!
          RedirectResponse(loginTokenAfterUrl)
        }
        else {
          at.delete_!
          regUser(user)
          RedirectWithState(registerUrl, RedirectState(() => { S.notice("Please complete the registration form") }))
        }
      }).openOr(RedirectWithState(indexUrl, RedirectState(() => { S.error("User not found") })))
      case _ => RedirectWithState(indexUrl, RedirectState(() => { S.warning("Login token not provided") }))
    }

    Full(resp)
  }

  // send an email to the user with a link for logging in
  def sendLoginToken(user: User): Unit = {
    import net.liftweb.util.Mailer._

    LoginToken.createForUserIdBox(user.id.get).foreach { token =>

      val msgTxt =
        """
          |Someone requested a link to change your password on the %s website.
          |
          |If you did not request this, you can safely ignore it. It will expire 48 hours from the time this message was sent.
          |
          |Follow the link below or copy and paste it into your internet browser.
          |
          |%s
          |
          |Thanks,
          |%s
        """.format(siteName, token.url, sysUsername).stripMargin

      sendMail(
        From(MongoAuth.systemFancyEmail),
        Subject("%s Password Help".format(siteName)),
        To(user.fancyEmail),
        PlainMailBodyType(msgTxt)
      )
    }
  }

  def sendEmailConfirmation(user: User): Unit = {
    import net.liftweb.util.Mailer._

    val tokenUrl = S.hostAndPath + "/confirmation/" + user.id
    val msgTxt =
      """
        |Tu cuenta en nuestro sitio web %s ha sido creado.
        |
        |Para confirmar y terminar el proceso haga click en el siguiente enlace o copie y pegue en su navegador web
        |
        |%s
        |
        |Gracias,
        |%s
      """.format(siteName, tokenUrl, user.name.get).stripMargin

    println(msgTxt)

    sendMail(
      From(MongoAuth.systemFancyEmail),
      Subject("%s Confirmacion de creacion de cuenta".format(siteName)),
      To(user.fancyEmail),
      PlainMailBodyType(msgTxt)
    )
  }

  def sendPasswordRecovery(user: User): Unit = {
    import net.liftweb.util.Mailer._

    val tokenUrl = S.hostAndPath + "/recovery/password/" + user.id

    val msgTxt =
      """"
        |Haz olvidado tu contraseña?
        |Para recuperar tu contraseña debes hacer click en el siguiente enlace o copiar y pegar en tu navegador web.
        |%s
      """.format(tokenUrl).stripMargin

    println(msgTxt)

    sendMail(
      From(MongoAuth.systemFancyEmail),
      Subject("%s Recuperar contraseña".format(siteName)),
      To(user.fancyEmail),
      PlainMailBodyType(msgTxt)
    )
  }

  /*
   * ExtSession
   */
  def createExtSession(uid: ObjectId): Box[Unit] = ExtSession.createExtSessionBox(uid)

  /*
  * Test for active ExtSession.
  */
  def testForExtSession: Box[Req] => Unit = {
    ignoredReq => {
      if (currentUserId.isEmpty) {
        ExtSession.handleExtSession match {
          case Full(es) => find(es.userId.get).foreach { user => logUserIn(user, false) }
          case Failure(msg, _, _) =>
            logger.warn("Error logging user in with ExtSession: %s".format(msg))
          case Empty =>
        }
      }
    }
  }

  // used during login process
  object loginCredentials extends SessionVar[LoginCredentials](LoginCredentials(""))
  object regUser extends SessionVar[User](createRecord.email(loginCredentials.is.email))

  def HasRoleOrPermission(role: Role, permission: Permission) =
    If(() => User.hasRole(role.id.get) || User.hasPermission(permission),
      DisplayError("liftmodule-monogoauth.locs.hasRole", "liftmodule-monogoauth.locs.hasPermission"))

  protected def DisplayError(message: String*) = () => {
    val msg = message.map(S ? _).mkString(",")
    RedirectWithState(indexUrl, RedirectState(() => S.error(msg)))
  }
}

case class LoginCredentials(email: String, isRememberMe: Boolean = false)

object SystemUser {
  private val username = "admin"
  private val email = "admin@martadero.org"

  lazy val user: User = User.findByUsername(username) openOr {
    User.createRecord
      .name("Administración Espacio")
      .username(username)
      .email(email)
      .locale("es_BO")
      .timezone("America/La_Paz")
      .verified(true)
      .password("asdf1234", true) // TODO: set me
      .roles(List(DefaultRoles.SuperAdmin.id.get))
      .save(true)
  }

  def resetPassword = {
    user.password("asdf1234", true).save(true)
  }
}


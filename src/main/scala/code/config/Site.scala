package code
package config

import code.model.Widget
import code.model.event.Event
import code.model.network._
import code.model.Page
import code.model.resource.{Equipment, Room}
import code.model._
import net.liftmodules.mongoauth.Locs
import net.liftweb.common.Full
import net.liftweb.http.{S, Templates}
import net.liftweb.sitemap.Loc.{Value => _, _}
import net.liftweb.sitemap.{MenuItem => _, _}
import DefaultRoles._
import Permissions._


object MenuGroups {
  val SettingsGroup = LocGroup("settings")
  val TopBarGroup = LocGroup("topbar")
  val LeftMenuGroup = LocGroup("left")
  val AdminGroup = LocGroup("admin")
  val RightMenuGroup = LocGroup("right")
}



/*
 * Wrapper for Menu locations
 */
case class MenuLoc(menu: Menu) {
  lazy val url: String = S.contextPath+menu.loc.calcDefaultHref
  lazy val fullUrl: String = S.hostAndPath+menu.loc.calcDefaultHref
}

object Site extends Locs {
  import code.config.MenuGroups._

  /* Menu superior */
  // locations (menu entries)
  val home = MenuLoc(Menu.i("Inicio") / "index" >> TopBarGroup)

  val dashboard = MenuLoc(Menu.i("Panel de inicio") / "dashboard" >> TopBarGroup >> RequireLoggedIn >> LeftMenuGroup)

  /* Quienes somos menu */
  // Quienes somos
  val who = MenuLoc(Menu.i("Quienes somos") / "quienes-somos" >> TopBarGroup submenus(
    // Sobre el mARTadero
    Menu.i("Sobre mARTadero") / "sobre-martadero" >> TopBarGroup,
    Menu.i("Equipo Humano") / "equipo-humano" >> TopBarGroup
  ))

  /* Menu Izquierdo */
  /* Agenda */
  // Agenda
  val agenda = MenuLoc(Menu.i("Agenda") / "agenda" submenus(
    // Agenda
    Menu.i("Calendario de Actividades") / "calendario",
    // Google Calendar
    Menu.i("Google Calendar") / "google-calendar"
    ))

  /* Participa */
  // Participa
  val participa = MenuLoc(Menu.i("Participa") / "participa")

  /* Espacio */
  // Espacio
  val espacios = MenuLoc(Menu.i("Espacio") / "espacios" submenus(
    // Conoce mARTadero
    Menu.i("Conoce mARTadero") / "conoce-martadero" submenus(
      // Salas y equipamiento
      Menu.i("Salas y Equpamiento") / "salas-y-equipamiento"
      ),
    // Aporte por uso
    Menu.i("Aporte por uso") / "aporte-por-uso"  submenus(
      // Cuadro de estimaciones
      Menu.i("Cuadro de estimaciones") / "cuadro-de-estimaciones"
      ),
    // Solicita un espacio
    Menu.i("Solicita un espacio") / "solicita-un-espacio"  submenus(
      // Formulario de solicitud
      Menu.i("Formulario de solicitud") / "formulario-de-solicitud"
      )
    ))

  /* Media */
  // Media
  val media = MenuLoc(Menu.i("Media") / "media")

  /* Blog */
  // Blog
  val entradaBlog = Menu.param[BlogPost](
    "Ver entrada del blog", "Ver entrada del blog",
    BlogPost.find,
    s => s.id.get.toString) / "blog" / "post" / * >>
    TemplateBox(() => Templates("blog" :: "post" :: Nil)) >>
    Hidden

  val blog = MenuLoc(Menu.i("Blog") / "blog" / "index" submenus(entradaBlog))

  val servicio =  Menu.param[Service](
    "Ver Servicio", "Ver Servicio",
    Service.find,
    s => s.id.get.toString) / "servicio" / * >>
    TemplateBox(() => Templates("servicio" :: Nil)) >>
    Hidden

  val servicios = MenuLoc(Menu.i("Servicios ") / "servicios" submenus(servicio))

  val area =  Menu.param[Area](
    "Ver Area", "Ver Area",
    Area.findByUrl,
    s => s.url.get.toString) / "area" / * >>
    TemplateBox(() => Templates("area" :: Nil)) >>
    Hidden

  val areas = MenuLoc(Menu.i("Areas ") / "areas" submenus(area))

  val principio =  Menu.param[Value](
    "Ver Principio", "Ver Principio",
    Value.findByUrl,
    s => s.url.get.toString) / "principio" / * >>
    TemplateBox(() => Templates("principio" :: Nil)) >>
    Hidden

  val principios = MenuLoc(Menu.i("Principios ") / "principios" submenus(principio))

  val areaTransversal =  Menu.param[TransversalArea](
    "Ver Area transversal", "Ver Area transversal",
    TransversalArea.findByUrl,
    s => s.url.get) / "areatransversal" / * >>
    TemplateBox(() => Templates("areatransversal" :: Nil)) >>
    Hidden

  val areasTransversales = MenuLoc(Menu.i("Areas transversal ") / "areastransversales" submenus(areaTransversal))

  val proceso =  Menu.param[Process](
    "Ver Proceso", "Ver Proceso",
    Process.findByUrl,
    s => s.url.get) / "proceso" / * >>
    TemplateBox(() => Templates("proceso" :: Nil)) >>
    Hidden

  val procesos = MenuLoc(Menu.i("Procesos ") / "procesos" submenus(proceso) >> Hidden)

  val lineaDeAccion =  Menu.param[ActionLine](
    "Ver Linea de Acción", "Ver Linea de Acción",
    ActionLine.findByUrl,
    s => s.url.get.toString) / "linea-de-accion" / * >>
    TemplateBox(() => Templates("linea-de-accion" :: Nil)) >>
    Hidden

  val lineasDeAccion = MenuLoc(Menu.i("Lineas de acción ") / "lineas-de-accion" submenus(lineaDeAccion) >> Hidden)

  val enfoqueTransversal =  Menu.param[TransversalApproach](
    "Ver Enfoque Transversal", "Ver Enfoque Transversal",
    TransversalApproach.findByUrl,
    s => s.url.get.toString) / "enfoque-transversal" / * >>
    TemplateBox(() => Templates("enfoque-transversal" :: Nil)) >>
    Hidden

  val enfoquesTransversales = MenuLoc(Menu.i("Enfoques transversales ") / "enfoques-transversales" submenus(enfoqueTransversal) >> Hidden)

  val programa =  Menu.param[Program](
    "Ver Programa", "Ver Programa",
    Program.findByUrl,
    s => s.url.get.toString) / "programa" / * >>
    TemplateBox(() => Templates("programa" :: Nil)) >>
    Hidden

  val programas = MenuLoc(Menu.i("Programas ") / "programas" submenus(programa))

  val red =  Menu.param[Network](
    "Ver red", "Ver red",
    Network.findByUrl,
    s => s.url.get.toString) / "red" / * >>
    TemplateBox(() => Templates("red" :: Nil)) >>
    Hidden

  val redes = MenuLoc(Menu.i("Redes ") / "redes" submenus(red) >> Hidden)

  /* Convocatorias */
  // Convocatorias
  val convocatoria =  Menu.param[Call](
    "Ver Convocatoria", "Ver Convocatoria",
    Call.find,
    s => s.id.get.toString) / "convocatoria" / * >>
    TemplateBox(() => Templates("convocatoria" :: Nil)) >>
    Hidden
  val convocatorias = MenuLoc(Menu.i("Convocatorias") / "convocatorias" submenus(convocatoria))

  val pagina =  Menu.param[Page](
    "Ver Pagina", "Ver Pagina",
    Page.findByUrl,
    s => s.url.get) / "pagina" / * >>
    TemplateBox(() => Templates("pagina" :: Nil)) >>
    Hidden

  /* Contacto */
  // Contacto
  val contacto = MenuLoc(Menu.i("Contacto") / "contacto" submenus(
    // Contacto
    Menu.i("Contactacte con mARTadero ") / "formulario-de-contacto",
    // Mapa
    Menu.i("Mapa") / "mapa",
    // Como llegar
    Menu.i("Como llegar") / "como-llegar"
    ))

  /* Entenados */
  // Entenados
  val entenados = MenuLoc(Menu.i("Entenados") / "entenados" >> RightMenuGroup submenus(
    // Proyectos asociados
    Menu.i("Proyectos Asociados") / "proyectos-asociados" >> RightMenuGroup submenus(
      // La Ubre Amarga
      Menu.i("La Ubre Amarga2") / "la-ubre-amarga2" >> RightMenuGroup,
      // Agrupacion Artesanal Kuska
      Menu.i("Agrupacion Artesanal Kuska") / "agrupacion-artesanal-kuska" >> RightMenuGroup,
      // Breaking the Floor
      Menu.i("Breaking The Floor - Escuela de Break Dance") / "breaking-the-floor" >> RightMenuGroup,
      // Pi Producciones
      Menu.i("Pi Producciones") / "pi-producciones" >> RightMenuGroup
      )
  ))





  val loginToken = MenuLoc(buildLoginTokenMenu)
  val logout = MenuLoc(buildLogoutMenu)
  private val profileParamMenu = Menu.param[User]("User", "Perfil",
    User.findByUsername _,
    _.username.get
  ) / "user" >> Loc.CalcValue(() => User.currentUser) >> LeftMenuGroup
  lazy val profileLoc = profileParamMenu.toLoc

  val password = MenuLoc(Menu.i("Password") / "settings" / "password" >> RequireLoggedIn >> SettingsGroup)
  val account = MenuLoc(Menu.i("Account") / "settings" / "account" >> SettingsGroup >> RequireLoggedIn)
  val editProfile = MenuLoc(Menu("EditProfile", "Profile") / "settings" / "profile" >> SettingsGroup >> RequireLoggedIn)
  val register = MenuLoc(Menu.i("Register") / "register" >> RequireNotLoggedIn)

  //Backend menu

  val backendMessages = MenuLoc(Menu.i("Mensajes") / "backend" / "messages" >>
    TemplateBox(() => Templates("templates-hidden" :: "backend" :: "listing-table" :: Nil)) >>
    User.HasRoleOrPermission(SuperAdmin, Mensajes) >> LeftMenuGroup)

  val backendPendingEvents = MenuLoc(Menu.i("Solicitudes") / "backend" / "events" / "pendingevents" >>
    User.HasRoleOrPermission(SuperAdmin, Solicitudes))

  val backendApprovedEvents = MenuLoc(Menu.i("Eventos aprobados") / "backend" / "events" / "index" >>
    User.HasRoleOrPermission(SuperAdmin, Eventos))

  val backendEventAdd = Menu.param[Event](
    "Agregar evento", "Agregar evento",
    s => Full(Event.createRecord),
    s => "new") / "backend" / "events" / "add" / * >>
    RequireLoggedIn >>
    TemplateBox(() => Templates("backend" :: "record" :: "event-form-page" :: Nil)) >>
    Hidden

  val backendEventEdit = Menu.param[Event](
    "Editar evento", "Editar evento",
    Event.find,
    s => s.id.get.toString) / "backend" / "events" / "edit" / * >>
    User.HasRoleOrPermission(SuperAdmin, Convocatorias) >>
    TemplateBox(() => Templates("backend" :: "record" :: "event-form-page" :: Nil)) >>
    Hidden

  val backendEvents = MenuLoc(Menu.i("Eventos") / "backend" / "events" >> RequireLoggedIn >>
     LeftMenuGroup submenus(
      backendPendingEvents.menu,
      backendApprovedEvents.menu,
      backendEventAdd,
      backendEventEdit))

  val frontendEvents = MenuLoc(Menu.i("Lista de Eventos") / "events" >> TemplateBox(() => Templates("events" :: Nil)))

  val backendCallAdd = Menu.param[Call](
    "Agregar convocatoria", "Agregar convocatoria",
    s => Full(Call.createRecord),
    s => "new") / "backend" / "calls" / "add" / * >>
    User.HasRoleOrPermission(SuperAdmin, Convocatorias) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendCallEdit = Menu.param[Call](
    "Editar convocatoria", "Editar convocatoria",
    Call.find,
    s => s.id.get.toString) / "backend" / "calls" / "edit" / * >>
    User.HasRoleOrPermission(SuperAdmin, Convocatorias) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendCalls = MenuLoc(Menu.i("Convocatorias ") / "backend" / "calls" >>
    TemplateBox(() => Templates("backend" :: "calls" :: "index" :: Nil)) >>
    User.HasRoleOrPermission(SuperAdmin, Convocatorias) >> LeftMenuGroup submenus(
      backendCallAdd, backendCallEdit))

  val backendWidgetAdd = Menu.param[Widget](
    "Agregar widget", "Agregar widget",
    s => Full(Widget.createRecord),
    s => "new") / "backend" / "widgets" / "add" / * >>
    User.HasRoleOrPermission(SuperAdmin, Widgets) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendWidgetEdit = Menu.param[Widget](
    "Editar widget", "Editar widget",
    Widget.find,
    s => s.id.get.toString) / "backend" / "widgets" / "edit" / * >>
    User.HasRoleOrPermission(SuperAdmin, Widgets) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendWidgets = MenuLoc(Menu.i("Widgets") / "backend" / "widgets" >>
    TemplateBox(() => Templates("backend" :: "widgets" :: "index" :: Nil)) >>
    User.HasRoleOrPermission(SuperAdmin, Widgets) submenus(
    backendWidgetAdd, backendWidgetEdit))


  val backendMenus = MenuLoc(Menu.i("Menus") / "backend" / "menus" >>
    TemplateBox(() => Templates("backend" :: "menus" :: Nil)) >>
    User.HasRoleOrPermission(SuperAdmin, Menus))

  val backendPageAdd = Menu.param[Page](
    "Agregar página", "Agregar página",
    s => Full(Page.createRecord),
    s => "new") / "backend" / "pages" / "add" / * >>
    User.HasRoleOrPermission(SuperAdmin, Paginas) >>
    TemplateBox(() => Templates("backend" :: "record" :: "pages-form-page" :: Nil)) >>
    Hidden

  val backendPageEdit = Menu.param[Page](
    "Editar página", "Editar página",
    Page.find,
    s => s.id.get.toString) / "backend" / "pages" / "edit" / * >>
    User.HasRoleOrPermission(SuperAdmin, Widgets) >>
    TemplateBox(() => Templates("backend" :: "record" :: "pages-form-page" :: Nil)) >>
    Hidden

  val backendPages = MenuLoc(Menu.i("Paginas") / "backend" / "pages" >>
    TemplateBox(() => Templates("backend" :: "pages" :: "index" :: Nil)) >>
    User.HasRoleOrPermission(SuperAdmin, Widgets) >> LeftMenuGroup submenus(
    backendPageAdd, backendPageEdit))

  val backendRoomAdd = Menu.param[Room](
    "Agregar sala", "Agregar sala",
    s => Full(Room.createRecord),
    s => "new") / "backend" / "rooms" / "add" / * >>
    User.HasRoleOrPermission(SuperAdmin, Salas) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendRoomEdit = Menu.param[Room](
    "Editar sala", "Editar sala",
    Room.find,
    s => s.id.get.toString) / "backend" / "rooms" / "edit" / * >>
    User.HasRoleOrPermission(SuperAdmin, Salas) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendRooms = MenuLoc(Menu.i("Salas") / "backend" / "rooms" >> User.HasRoleOrPermission(SuperAdmin, Salas) >>
    TemplateBox(() => Templates("backend" :: "rooms" :: "index" :: Nil)) submenus(
      backendRoomAdd, backendRoomEdit))

  val backendEquipmentAdd = Menu.param[Equipment](
    "Agregar equipo", "Agregar equipo",
    s => Full(Equipment.createRecord),
    s => "new") / "backend" / "equipments" / "add" / * >>
    User.HasRoleOrPermission(SuperAdmin, Accesorios) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendEquipmentEdit = Menu.param[Equipment](
    "Editar equipo", "Editar equipo",
    Equipment.find,
    s => s.id.get.toString) / "backend" / "equipments" / "edit" / * >>
    User.HasRoleOrPermission(SuperAdmin, Accesorios) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendEquipments = MenuLoc(Menu.i("Equipos") / "backend" / "equipments" >>
    User.HasRoleOrPermission(SuperAdmin, Accesorios) >>
    TemplateBox(() => Templates("backend" :: "equipments" :: "index" :: Nil)) submenus(
    backendEquipmentAdd, backendEquipmentEdit))

  val backendAreaAdd = Menu.param[Area](
    "Agregar área", "Agregar área",
    s => Full(Area.createRecord),
    s => "new") / "backend" / "areas" / "add" / * >>
    User.HasRoleOrPermission(SuperAdmin, Areas) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendAreaEdit = Menu.param[Area](
    "Editar área", "Editar área",
    Area.find,
    s => s.id.get.toString) / "backend" / "areas" / "edit" / * >>
    User.HasRoleOrPermission(SuperAdmin, Areas) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendAreas = MenuLoc(Menu.i("Áreas") / "backend" / "areas" >> User.HasRoleOrPermission(SuperAdmin, Areas) >>
    TemplateBox(() => Templates("backend" :: "areas" :: "index" :: Nil)) submenus(
    backendAreaAdd, backendAreaEdit))

  val backendTransversableAreaAdd = Menu.param[TransversalArea](
    "Agregar área transversal", "Agregar área transaversal",
    s => Full(TransversalArea.createRecord),
    s => "new") / "backend" / "transversableareas" / "add" / * >>
    User.HasRoleOrPermission(SuperAdmin, AreasTransversales) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendTransversableAreaEdit = Menu.param[TransversalArea](
    "Editar área transversal", "Editar área transversal",
    TransversalArea.find,
    s => s.id.get.toString) / "backend" / "transversableareas" / "edit" / * >>
    User.HasRoleOrPermission(SuperAdmin, AreasTransversales) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendTransversableAreas = MenuLoc(Menu.i("Áreas transversales") / "backend" / "transversableareas" >> User.HasRoleOrPermission(SuperAdmin, AreasTransversales) >>
    TemplateBox(() => Templates("backend" :: "transversableareas" :: "index" :: Nil)) submenus(
    backendTransversableAreaAdd, backendTransversableAreaEdit))

  val backendProgramAdd = Menu.param[Program](
    "Agregar programa", "Agregar programa",
    s => Full(Program.createRecord),
    s => "new") / "backend" / "program" / "add" / * >>
    User.HasRoleOrPermission(SuperAdmin, Programas) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendProgramEdit = Menu.param[Program](
    "Editar programa", "Editar programa",
    Program.find,
    s => s.id.get.toString) / "backend" / "programs" / "edit" / * >>
    User.HasRoleOrPermission(SuperAdmin, Programas) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendPrograms = MenuLoc(Menu.i("Programas") / "backend" / "programs" >>
    User.HasRoleOrPermission(SuperAdmin, Programas) >>
    TemplateBox(() => Templates("backend" :: "programs" :: "index" :: Nil)) submenus(
    backendProgramAdd, backendProgramEdit))

  val backendProcessAdd = Menu.param[Process](
    "Agregar proceso", "Agregar proceso",
    s => Full(Process.createRecord),
    s => "new") / "backend" / "process" / "add" / * >>
    User.HasRoleOrPermission(SuperAdmin, Procesos) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendProcessEdit = Menu.param[Process](
    "Editar proceso", "Editar proceso",
    Process.find,
    s => s.id.get.toString) / "backend" / "process" / "edit" / * >>
    User.HasRoleOrPermission(SuperAdmin, Procesos) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendProcess = MenuLoc(Menu.i("Procesos") / "backend" / "process" >>
    User.HasRoleOrPermission(SuperAdmin, Procesos) >>
    TemplateBox(() => Templates("backend" :: "process" :: "index" :: Nil)) submenus(
    backendProcessAdd, backendProcessEdit))

  val backendActionLineAdd = Menu.param[ActionLine](
    "Agregar linea de acción", "Agregar linea de acción",
    s => Full(ActionLine.createRecord),
    s => "new") / "backend" / "actionlines" / "add" / * >>
    User.HasRoleOrPermission(SuperAdmin, LineasDeAccion) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendActionLineEdit = Menu.param[ActionLine](
    "Editar linea de acción", "Editar linea de acción",
    ActionLine.find,
    s => s.id.get.toString) / "backend" / "actionlines" / "edit" / * >>
    User.HasRoleOrPermission(SuperAdmin, LineasDeAccion) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendActionLines = MenuLoc(Menu.i("Lineas de acción") / "backend" / "actionlines" >>
    User.HasRoleOrPermission(SuperAdmin, LineasDeAccion) >>
    TemplateBox(() => Templates("backend" :: "actionlines" :: "index" :: Nil)) submenus(
    backendActionLineAdd, backendActionLineEdit))

  val backendTransversalApproachAdd = Menu.param[TransversalApproach](
    "Agregar enfoque transversal", "Agregar enfoque transversal",
    s => Full(TransversalApproach.createRecord),
    s => "new") / "backend" / "transversalapproaches" / "add" / * >>
    User.HasRoleOrPermission(SuperAdmin, EnfoquesTransversales) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendTransversalApproachEdit = Menu.param[TransversalApproach](
    "Editar enfoque transversal", "Editar enfoque transversal",
    TransversalApproach.find,
    s => s.id.get.toString) / "backend" / "transversalapproaches" / "edit" / * >>
    User.HasRoleOrPermission(SuperAdmin, EnfoquesTransversales) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendTransversalApproaches = MenuLoc(Menu.i("Enfoques Transversales") / "backend" / "transversalapproaches" >>
    User.HasRoleOrPermission(SuperAdmin, EnfoquesTransversales) >>
    TemplateBox(() => Templates("backend" :: "transversalapproaches" :: "index" :: Nil)) submenus(
    backendTransversalApproachAdd, backendTransversalApproachEdit))

  val backendValueAdd = Menu.param[Value](
    "Agregar principio", "Agregar principio",
    s => Full(Value.createRecord),
    s => "new") / "backend" / "values" / "add" / * >>
    User.HasRoleOrPermission(SuperAdmin, Principios) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendValueEdit = Menu.param[Value](
    "Editar principio", "Editar principio",
    Value.find,
    s => s.id.get.toString) / "backend" / "values" / "edit" / * >>
    User.HasRoleOrPermission(SuperAdmin, Principios) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendValues = MenuLoc(Menu.i("Principios ") / "backend" / "values" >>
    User.HasRoleOrPermission(SuperAdmin, LineasDeAccion) >>
    TemplateBox(() => Templates("backend" :: "values" :: "index" :: Nil)) submenus(
    backendValueAdd, backendValueEdit))

  val backendSpaceAdd = Menu.param[Space](
    "Agregar espacio", "Agregar espacio",
    s => Full(Space.createRecord),
    s => "new") / "backend" / "spaces" / "add" / * >>
    User.HasRoleOrPermission(SuperAdmin, Espacios) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendSpaceEdit = Menu.param[Space](
    "Editar espacio", "Editar espacio",
    Space.find,
    s => s.id.get.toString) / "backend" / "spaces" / "edit" / * >>
    User.HasRoleOrPermission(SuperAdmin, Espacios) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendSpaces = MenuLoc(Menu.i("Espacios") / "backend" / "spaces" >>
    User.HasRoleOrPermission(SuperAdmin, Espacios) >>
    TemplateBox(() => Templates("backend" :: "spaces" :: "index" :: Nil)) submenus(
    backendSpaceAdd, backendSpaceEdit))

  val backendNetworkAdd = Menu.param[Network](
    "Agregar red", "Agregar red",
    s => Full(Network.createRecord),
    s => "new") / "backend" / "networks" / "add" / * >>
    User.HasRoleOrPermission(SuperAdmin, Redes) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendNetworkEdit = Menu.param[Network](
    "Editar red", "Editar red",
    Network.find,
    s => s.id.get.toString) / "backend" / "networks" / "edit" / * >>
    User.HasRoleOrPermission(SuperAdmin, Redes) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendNetworks = MenuLoc(Menu.i("Redes") / "backend" / "networks" >>
    User.HasRoleOrPermission(SuperAdmin, Redes) >>
    TemplateBox(() => Templates("backend" :: "networks" :: "index" :: Nil)) submenus(
    backendNetworkAdd, backendNetworkEdit))

  val backendProjectAdd = Menu.param[Project](
    "Agregar proyecto", "Agregar proyecto",
    s => Full(Project.createRecord),
    s => "new") / "backend" / "projects" / "add" / * >>
    User.HasRoleOrPermission(SuperAdmin, Proyectos) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendProjectEdit = Menu.param[Project](
    "Editar proyecto", "Editar proyecto",
    Project.find,
    s => s.id.get.toString) / "backend" / "projects" / "edit" / * >>
    User.HasRoleOrPermission(SuperAdmin, Proyectos) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendProjects= MenuLoc(Menu.i("Proyectos") / "backend" / "projects" >>
    User.HasRoleOrPermission(SuperAdmin, Proyectos) >>
    TemplateBox(() => Templates("backend" :: "projects" :: "index" :: Nil)) submenus(
    backendProjectAdd, backendProjectEdit))

  val backendUserAdd = Menu.param[User](
    "Agregar usuario", "Agregar usuario",
    s => Full(User.createRecord),
    s => "new") / "backend" / "users" / "add" / * >>
    User.HasRoleOrPermission(SuperAdmin, Usuarios) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendUserEdit = Menu.param[User](
    "Editar usuario", "Editar usuario",
    User.find,
    s => s.id.get.toString) / "backend" / "users" / "edit" / * >>
    User.HasRoleOrPermission(SuperAdmin, Usuarios) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendUsers = MenuLoc(Menu.i("Usuarios") / "backend" / "users" >>
    User.HasRoleOrPermission(SuperAdmin, Usuarios) >> LeftMenuGroup >>
    TemplateBox(() => Templates("backend" :: "users" :: "index" :: Nil)) submenus(
    backendUserAdd, backendUserEdit))

  val backendServiceAdd = Menu.param[Service](
    "Agregar servicio", "Agregar servicio",
    s => Full(Service.createRecord),
    s => "new") / "backend" / "services" / "add" / * >>
    User.HasRoleOrPermission(SuperAdmin, Servicios) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendServiceEdit = Menu.param[Service](
    "Editar servicio", "Editar servicio",
    Service.find,
    s => s.id.get.toString) / "backend" / "services" / "edit" / * >>
    User.HasRoleOrPermission(SuperAdmin, Servicios) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendServices = MenuLoc(Menu.i("Servicios") / "backend" / "services" >>
    User.HasRoleOrPermission(SuperAdmin, Servicios) >>
    TemplateBox(() => Templates("backend" :: "services" :: "index" :: Nil)) submenus(
    backendServiceAdd, backendServiceEdit))


  //Submenus equipos, accesorios y servicios


  val backendBlogAdd = Menu.param[BlogPost](
    "Agregar entrada al blog", "Agregar entrada al blog",
    s => Full(BlogPost.createRecord),
    s => "new") / "backend" / "blog" / "add" / * >>
    User.HasRoleOrPermission(SuperAdmin, Blog) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendBlogEdit = Menu.param[BlogPost](
    "Editar entrada del blog", "Editar entrada del blog",
    BlogPost.find,
    s => s.id.get.toString) / "backend" / "blog" / "edit" / * >>
    User.HasRoleOrPermission(SuperAdmin, Blog) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendBlog = MenuLoc(Menu.i("Blog ") / "backend" / "blog" >>
    User.HasRoleOrPermission(SuperAdmin, Blog) >> LeftMenuGroup >>
    TemplateBox(() => Templates("backend" :: "blog" :: "index" :: Nil)) submenus(
    backendBlogAdd, backendBlogEdit))

  val backendOrganizacionModule = MenuLoc(Menu.i("Organización") / "backend" / "organizacion" >>
    User.HasRoleOrPermission(SuperAdmin, Organizacion) >>
    LeftMenuGroup >>
    PlaceHolder submenus(
      backendAreas.menu,
      backendPrograms.menu,
      backendTransversableAreas.menu,
      backendProcess.menu,
      backendActionLines.menu,
      backendTransversalApproaches.menu,
      backendValues.menu,
      backendServices.menu))

  val backendAmbientesModule = MenuLoc(Menu.i("Ambientes") / "backend" / "ambientes" >>
    User.HasRoleOrPermission(SuperAdmin, Ambientes) >>
    LeftMenuGroup >>
    PlaceHolder submenus(
      backendRooms.menu,
      backendEquipments.menu))

  val backendRedesModule = MenuLoc(Menu.i("Redes  ") / "backend" / "redes" >>
    User.HasRoleOrPermission(SuperAdmin, Redes) >>
    LeftMenuGroup >>
    PlaceHolder submenus(
      backendNetworks.menu,
      backendSpaces.menu))

  val backendAparienciaModule = MenuLoc(Menu.i("Apariencia") / "backend" / "ui" >>
    User.HasRoleOrPermission(SuperAdmin, Apariencia) >>
    LeftMenuGroup >>
    PlaceHolder submenus(
    backendWidgets.menu,
    backendMenus.menu))

  // Salas

  // Lineas de accion

  // Procesos

  // Tags y cada tag debe poder tener asociada una descripcion

  // Modulos Cotizaciones con posibilidad de modificar por admins y descuentos a nivel de item

  // Programas




  private def menus = List(
    home.menu,
    dashboard.menu,
    Menu.i("Login") / "login" >> RequireNotLoggedIn,
    register.menu,
    loginToken.menu,
    logout.menu,
    profileParamMenu,
    account.menu,
    password.menu,
    editProfile.menu,
    backendMessages.menu,
    backendUsers.menu,
    backendEvents.menu,
    backendOrganizacionModule.menu,
    backendAmbientesModule.menu,
    backendRedesModule.menu,
    backendAparienciaModule.menu,
    backendPages.menu,
    backendCalls.menu,
    frontendEvents.menu,
    Menu.i("Calendario") / "backend" / "calendario" >> User.HasRoleOrPermission(SuperAdmin, Calendario) >> LeftMenuGroup,
    backendBlog.menu,
    Menu.i("Error") / "error" >> Hidden,
    Menu.i("404") / "404" >> Hidden,
    Menu.i("Throw") / "throw"  >> EarlyResponse(() => throw new Exception("This is only a test.")),
    who.menu,
    areas.menu,
    agenda.menu,
    participa.menu,
    espacios.menu,
    media.menu,
    blog.menu,
    convocatorias.menu,
    pagina,
    servicios.menu,
    procesos.menu,
    programas.menu,
    redes.menu,
    areasTransversales.menu,
    lineasDeAccion.menu,
    enfoquesTransversales.menu,
    contacto.menu)

  /*
   * Return a SiteMap needed for Lift
   */
  def siteMap: SiteMap = SiteMap(menus:_*)
}

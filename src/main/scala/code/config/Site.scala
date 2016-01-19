package code
package config

import code.model.Widget
import code.model.event.Event
import code.model.network._
import code.model.Page
import code.model.resource.{Equipment, Room}
import code.model.{User, _}
import net.liftmodules.mongoauth.Locs
import net.liftweb.common.Full
import net.liftweb.http.{S, Templates}
import net.liftweb.sitemap.Loc._
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

  val dashboard = MenuLoc(Menu.i("Dashboard") / "dashboard" >> TopBarGroup >> RequireLoggedIn >> LeftMenuGroup)

  /* Quienes somos menu */
  // Quienes somos
  val who = MenuLoc(Menu.i("Quienes somos") / "quienes-somos" >> TopBarGroup submenus(
    // Sobre el mARTadero
    Menu.i("Sobre mARTadero") / "sobre-martadero" >> TopBarGroup,
    Menu.i("Equipo Humano") / "equipo-humano" >> TopBarGroup
    ))

  /* Programas */
  // Programas
  val programs = MenuLoc(Menu.i("Ver Programs") / "programs" >> TopBarGroup submenus(
    // formARTe
    Menu.i("formARTe") / "formarte" >> TopBarGroup,
    // Taller de creatividad infantil
    Menu.i("Taller de Creatividad Infantil") / "tallerinfantil" >> TopBarGroup,
    // Taller de creatividad infantil
    Menu.i("Acción Urbana") / "accion-urbana" >> TopBarGroup submenus(
      // Plataforma Vecinal
      Menu.i("Plataforma Vecinal") / "plataforma-vecinal" >> TopBarGroup,
      // Paseo de las Artes
      Menu.i("Paseo de las Artes") / "paseo-de-las-artes" >> TopBarGroup
      ),
    // Vivo y Verde
    Menu.i("Vivo y Verde") / "vivo-y-verde" >> TopBarGroup,
    // Vivo y Verde
    Menu.i("Vivero de Emprendimientos Artístico Culturales") / "vivero-de-emprendimientos-artistico-culturales" >> TopBarGroup,
    // Políticas Culturales y Redes
    Menu.i("Políticas Culturales y Redes") / "politicas-culturales-y-redes" >> TopBarGroup,
    // Residencias Artísticas PRANA
    Menu.i("Residencias Artísticas PRANA") / "prana" >> TopBarGroup
    ))

  /* Areas de apoyo */
  // Areas de apoyo
  val supportAreas = MenuLoc(Menu.i("Áreas de apoyo") / "areas-de-apoyo" >> TopBarGroup submenus(
    // Direccion
    Menu.i("Dirección") / "direccion" >> TopBarGroup,
    // Gestion de Proyectos
    Menu.i("Gestión de Proyectos") / "gestion-de-proyectos" >> TopBarGroup,
    // Contabilidad
    Menu.i("Contabilidad") / "contabilidad" >> TopBarGroup,
    // Administracion de espacios
    Menu.i("Administración de espacios") / "administracion-de-espacios" >> TopBarGroup,
    // Programación
    Menu.i("Programación") / "programacion" >> TopBarGroup,
    // Comunicación
    Menu.i("Comunicación") / "comunicacion" >> TopBarGroup submenus(
      // Hacklab
      Menu.i("Hacklab") / "hacklab" >> TopBarGroup,
      // Comun&ca
      Menu.i("Comun&ca") / "comunica" >> TopBarGroup
      )
    ))

  /* Principios */
  // Principios
  val principios = MenuLoc(Menu.i("Principios") / "principios" >> TopBarGroup submenus(
    // Principios
    Menu.i("Innovación") / "innovacion" >> TopBarGroup,
    // Investigacion
    Menu.i("Investigación") / "investigacion" >> TopBarGroup,
    // Experimentacion
    Menu.i("Experimentación") / "experimentacion" >> TopBarGroup,
    // Rigor Conceptual y Formal
    Menu.i("Rigor Conceptual y Formal") / "rigor-conceptual-formal" >> TopBarGroup,
    // Integracion
    Menu.i("Integración") / "integracion" >> TopBarGroup,
    // Intercambio de conocimiento y experiencias
    Menu.i("Intercambio de Conocimiento y Experiencias") / "intercambio-de-conocimiento-y-experciencias" >> TopBarGroup,
    // Interculturalidad
    Menu.i("Interculturalidad") / "interculturalidad" >> TopBarGroup
    ))

  /* FONDART */
  // FONDART
  val fondArt = MenuLoc(Menu.i("FONDART") / "fondart" >> TopBarGroup submenus(
    //Solicitud FONDART
    Menu.i("Solicitud FONDART") / "solicitud-fondart" >> TopBarGroup
    ))

  /* Apoya */
  // apoya
  val apoya = MenuLoc(Menu.i("Apoya") / "apoya" >> TopBarGroup submenus(
    // Hivos
    Menu.i("Hivos") / "hivos" >> TopBarGroup,
    // Conexion
    Menu.i("Conexion") / "conexion" >> TopBarGroup,
    // Solidar AOS Suiza
    Menu.i("Solidar AOS Suiza") / "solidar-aos-suiza" >> TopBarGroup,
    // CAF
    Menu.i("CAF") / "caf" >> TopBarGroup,
    // Cooperación Italiana
    Menu.i("Cooperación Italiana") / "cooperacion-italiana" >> TopBarGroup,
    // Nos ayudan a enriquecer el espacio
    Menu.i("Nos ayudan a enriquecer el espacio") / "nos-ayudan-a-enriquecer-el-espacio" >> TopBarGroup,
    // Nos apoyaron
    Menu.i("Nos apoyaron") / "nos-apoyaron" >> TopBarGroup,
    // Nos apoyan corresponsablemente
    Menu.i("Nos apoyan corresponsablemente") / "nos-apoyan-corresponsablemente" >> TopBarGroup,
    // Nos apoyan voluntarios de
    Menu.i("Nos apoyan voluntarios de") / "nos-apoyan-voluntarios-de" >> TopBarGroup
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
    Area.find,
    s => s.id.get.toString) / "area" / * >>
    TemplateBox(() => Templates("area" :: Nil)) >>
    Hidden

  val areas = MenuLoc(Menu.i("Areas ") / "areas" submenus(area))

  val proceso =  Menu.param[Process](
    "Ver Proceso", "Ver Proceso",
    Process.find,
    s => s.id.get.toString) / "proceso" / * >>
    TemplateBox(() => Templates("proceso" :: Nil)) >>
    Hidden

  val procesos = MenuLoc(Menu.i("Procesos ") / "procesos" submenus(proceso) >> Hidden)

  val programa =  Menu.param[Program](
    "Ver Programa", "Ver Programa",
    Program.find,
    s => s.id.get.toString) / "programa" / * >>
    TemplateBox(() => Templates("programa" :: Nil)) >>
    Hidden

  val programas = MenuLoc(Menu.i("Programas ") / "programas" submenus(programa))

  val red =  Menu.param[Network](
    "Ver Red", "Ver Red",
    Network.find,
    s => s.id.get.toString) / "red" / * >>
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
    s => s.id.get.toString) / "pagina" / * >>
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

  /* Lineas de accion*/
  // Lineas de accion
  val lineasDeAccion = MenuLoc(Menu.i("Líneas de Acción") / "lineas-de-accion" >> RightMenuGroup submenus(
    // Linea de accion
    Menu.param[Network]("Linea de acción", "Linea de acción",
      Network.find _,
      _.name.get
    ) / "red" / * >> TemplateBox(() => Templates("red" :: Nil)) >> RightMenuGroup
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

  val backendApprovedEventsWorkshops = MenuLoc(Menu.i("Talleres") / "backend" / "events" / "workshops" >>
    User.HasRoleOrPermission(SuperAdmin, Eventos))

  val backendResidencies = MenuLoc(Menu.i("Residencias") / "backend" / "events" / "residencies" >>
    User.HasRoleOrPermission(SuperAdmin, Residencias))

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
      backendResidencies.menu,
      backendApprovedEventsWorkshops.menu,
      backendEventAdd,
      backendEventEdit))

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
    User.HasRoleOrPermission(SuperAdmin, Widgets) >> LeftMenuGroup submenus(
    backendWidgetAdd, backendWidgetEdit))

  val backendMenuItemAdd = Menu.param[MenuItem](
    "Agregar menú", "Agregar menú",
    s => Full(MenuItem.createRecord),
    s => "new") / "backend" / "menus" / "add" / * >>
    User.HasRoleOrPermission(SuperAdmin, MenuItems) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendMenuItemEdit = Menu.param[MenuItem](
    "Editar menú", "Editar menú",
    MenuItem.find,
    s => s.id.get.toString) / "backend" / "menus" / "edit" / * >>
    User.HasRoleOrPermission(SuperAdmin, MenuItems) >>
    TemplateBox(() => Templates("backend" :: "record" :: "form-page" :: Nil)) >>
    Hidden

  val backendMenuItems = MenuLoc(Menu.i("Widgets") / "backend" / "menus" >>
    TemplateBox(() => Templates("backend" :: "menus" :: "index" :: Nil)) >>
    User.HasRoleOrPermission(SuperAdmin, MenuItems) >> LeftMenuGroup submenus(
    backendMenuItemAdd, backendMenuItemEdit))

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


  val backendFiles = MenuLoc(Menu.i("Archivos") / "backend" / "files" >>
    User.HasRoleOrPermission(SuperAdmin, Archivos) >> LeftMenuGroup)


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
      backendProcess.menu,
      backendActionLines.menu,
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
    backendEvents.menu,
    backendOrganizacionModule.menu,
    backendAmbientesModule.menu,
    backendRedesModule.menu,
    backendCalls.menu,
    backendPages.menu,
    backendWidgets.menu,
    backendUsers.menu,
    backendFiles.menu,
    backendBlog.menu,
    Menu.i("Error") / "error" >> Hidden,
    Menu.i("404") / "404" >> Hidden,
    Menu.i("Throw") / "throw"  >> EarlyResponse(() => throw new Exception("This is only a test.")),
    who.menu,
    areas.menu,
    programs.menu,
    supportAreas.menu,
    principios.menu,
    fondArt.menu,
    apoya.menu,
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
    contacto.menu)

  /*
   * Return a SiteMap needed for Lift
   */
  def siteMap: SiteMap = SiteMap(menus:_*)
}

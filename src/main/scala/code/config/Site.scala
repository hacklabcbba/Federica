package code
package config

import model.User

import net.liftweb._
import net.liftweb.http.{Templates, S}
import code.lib.menu.{EventMenu, ProductiveUnitMenu}
import net.liftweb._
import util._
import sitemap._
import Loc._

import net.liftmodules.mongoauth.Locs
import code.model._
import process._
import network._


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
  import MenuGroups._

  /* Menu superior */
  // locations (menu entries)
  val home = MenuLoc(Menu.i("Inicio") / "index" >> TopBarGroup)

  /* Quienes somos menu */
  // Quienes somos
  val who = MenuLoc(Menu.i("Quienes somos") / "quienes-somos" >> TopBarGroup submenus(
    // Sobre el mARTadero
    Menu.i("Sobre mARTadero") / "sobre-martadero" >> TopBarGroup,
    Menu.i("Equipo Humano") / "equipo-humano" >> TopBarGroup
    ))

  /* Áreas menu*/
  // Áreas
  val areas = MenuLoc(Menu.i("Áreas") / "areas" >> TopBarGroup submenus(
    // Áreas
    Menu.i("Artes Escénicas") / "artes-escenicas" >> TopBarGroup submenus(
      // Elenco mARTadero
      Menu.i("Elenco mARTadero") / "elenco-martadero" >> TopBarGroup
      ),
    // Artes visuales
    Menu.i("Artes Visuales") / "artes-visuales" >> TopBarGroup submenus(
      // Bienal arte urbano
      Menu.i("Bienal arte urbano") / "bienal-arte-urbano" >> TopBarGroup,
      // CONART
      Menu.i("Conart") / "conart" >> TopBarGroup
      ),
    // Musica
    Menu.i("Música") / "musica" >> TopBarGroup,
    // Arquitectura y Diseño Gráfico
    Menu.i("Arquitectura y Diseño Gráfico") / "arquitectura-y-diseño-gráfico" >> TopBarGroup,
    // Audiovisual
    Menu.i("Audiovisual") / "audiovisual" >> TopBarGroup,
    // Letras
    Menu.i("Letras") / "letras" >> TopBarGroup submenus(
      // La ubre amarga
      Menu.i("La Ubre Amarga") / "la-ubre-amarga" >> TopBarGroup
      ),
    // Interaccion Social
    Menu.i("Interacción Social") / "interaccion-social" >> TopBarGroup submenus(
      // Jovenes Artivistas
      Menu.i("Jóvenes artivistas") / "jovenes-artivistas" >> TopBarGroup,
      // Enredarte con NADA
      Menu.i("Enredarte con NADA") / "enredarte-con-nada" >> TopBarGroup
      )
    ))

  /* Programas */
  // Programas
  val programs = MenuLoc(Menu.i("Programs") / "programs" >> TopBarGroup submenus(
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
  val agenda = MenuLoc(Menu.i("Agenda") / "agenda" >> LeftMenuGroup submenus(
    // Agenda
    Menu.i("Calendario de Actividades") / "calendario" >> LeftMenuGroup,
    // Google Calendar
    Menu.i("Google Calendar") / "google-calendar" >> LeftMenuGroup
    ))

  /* Participa */
  // Participa
  val participa = MenuLoc(Menu.i("Participa") / "participa" >> LeftMenuGroup submenus(
    // Como artista
    Menu.i("Como artista") / "como-artista" >> LeftMenuGroup,
    // Como organizacion
    Menu.i("Como organización") / "como-organizacion" >> LeftMenuGroup,
    // Como auspiciador
    Menu.i("Como auspiciador") / "como-auspiciador" >> LeftMenuGroup,
    // Como voluntario
    Menu.i("Como voluntario") / "como-voluntario" >> LeftMenuGroup
    ))

  /* Espacio */
  // Espacio
  val espacio = MenuLoc(Menu.i("Espacio") / "espacio" >> LeftMenuGroup submenus(
    // Conoce mARTadero
    Menu.i("Conoce mARTadero") / "conoce-martadero" >> LeftMenuGroup submenus(
      // Salas y equipamiento
      Menu.i("Salas y Equpamiento") / "salas-y-equipamiento" >> LeftMenuGroup
      ),
    // Aporte por uso
    Menu.i("Aporte por uso") / "aporte-por-uso" >> LeftMenuGroup submenus(
      // Cuadro de estimaciones
      Menu.i("Cuadro de estimaciones") / "cuadro-de-estimaciones" >> LeftMenuGroup
      ),
    // Solicita un espacio
    Menu.i("Solicita un espacio") / "solicita-un-espacio" >> LeftMenuGroup submenus(
      // Formulario de solicitud
      Menu.i("Formulario de solicitud") / "formulario-de-solicitud" >> LeftMenuGroup
      )
    ))

  /* Media */
  // Media
  val media = MenuLoc(Menu.i("Media") / "media" >> LeftMenuGroup submenus(
    // Biblioteca libre
    Menu.i("Biblioteca libre") / "biblioteca-libre" >> LeftMenuGroup,
    // Descargas (logos, fuentes, libros, catálogos)
    Menu.i("Descargas (logos, fuentes, libros, catálogos)") / "descargas" >> LeftMenuGroup,
    // Contextopedia
    Menu.i("Contextopedia") / "contextopedia" >> LeftMenuGroup,
    // Redes Sociales
    Menu.i("Redes Sociales") / "redes-sociales" >> LeftMenuGroup,
    // Fotos mARTadero (Flickr mARTadero)
    Menu.i("Fotos mARTadero (Flickr mARTadero)") / "fotos" >> LeftMenuGroup,
    // Videos mARTadero (Canal de YouTube)
    Menu.i("Videos mARTadero (Canal de YouTube)") / "videos" >> LeftMenuGroup,
    // Rss Newsfeed (general/áreas)
    Menu.i("Rss Newsfeed (general/áreas)") / "rss" >> LeftMenuGroup
    ))

  /* Blog */
  // Blog
  val blog = MenuLoc(Menu.i("Blog") / "blog" >> LeftMenuGroup)

  /* Convocatorias */
  // Convocatorias
  val convocatorias = MenuLoc(Menu.i("Convocatorias") / "convocatorias" >> LeftMenuGroup)

  /* Contacto */
  // Contacto
  val contacto = MenuLoc(Menu.i("Contacto") / "contacto" >> LeftMenuGroup submenus(
    // Contacto
    Menu.i("Contactacte con mARTadero ") / "formulario-de-contacto" >> LeftMenuGroup,
    // Mapa
    Menu.i("Mapa") / "mapa" >> LeftMenuGroup,
    // Como llegar
    Menu.i("Como llegar") / "como-llegar" >> LeftMenuGroup
    ))

  /* Administracion */
  // Administracion
  val administracion = MenuLoc(Menu.i("Administración") / "dashboard" >> LeftMenuGroup)

  /* Menu Derecha */
  /* Procesos */
  // Procesos
  val procesos = MenuLoc(Menu.i("Procesos") / "procesos" >> RightMenuGroup submenus(
    // Procesos
    Menu.param[Process]("Proceso", "Proceso",
      Process.find _,
      _.name.get
    ) / "proceso" / * >> TemplateBox(() => Templates("proceso" :: Nil)) >> RightMenuGroup
    ))


  /* Redes */
  // Redes
  val redes = MenuLoc(Menu.i("Redes") / "redes" >> RightMenuGroup submenus(
    // Red
    Menu.param[Network]("Red", "Red",
      Network.find _,
      _.name.get
    ) / "red" / * >> TemplateBox(() => Templates("red" :: Nil)) >> RightMenuGroup
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
      ),
    // Servicios
    Menu.i("Servicios") / "servicios" >> RightMenuGroup submenus(
      // Gestión de eventos culturales
      Menu.i("Gestión de Eventos Culturales") / "gestion-de-eventos-culturales" >> RightMenuGroup,
      // Residencias Prana
      Menu.i("Residencias PrAna") / "residencias-prana" >> RightMenuGroup,
      // Comun&ca
      Menu.i("Comun&ca 2") / "comunica2" >> RightMenuGroup,
      // La Mosquita
      Menu.i("La Mosquita Muerta") / "la-mosquita-muerta" >> RightMenuGroup
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
  private val profileParamMenu = Menu.param[User]("User", "Profile",
    User.findByUsername _,
    _.username.get
  ) / "user" >> Loc.CalcValue(() => User.currentUser)
  lazy val profileLoc = profileParamMenu.toLoc

  val password = MenuLoc(Menu.i("Password") / "settings" / "password" >> RequireLoggedIn >> SettingsGroup)
  val account = MenuLoc(Menu.i("Account") / "settings" / "account" >> SettingsGroup >> RequireLoggedIn)
  val editProfile = MenuLoc(Menu("EditProfile", "Profile") / "settings" / "profile" >> SettingsGroup >> RequireLoggedIn)
  val register = MenuLoc(Menu.i("Register") / "register" >> RequireNotLoggedIn)

  private def menus = List(
    home.menu,
    Menu.i("Login") / "login" >> RequireNotLoggedIn,
    register.menu,
    loginToken.menu,
    logout.menu,
    profileParamMenu,
    account.menu,
    password.menu,
    editProfile.menu,
    ProductiveUnitMenu.menuAdd.menu,
    ProductiveUnitMenu.menuEdit.menu,
    ProductiveUnitMenu.menuList.menu,
    EventMenu.menuAdd.menu,
    EventMenu.menuEdit.menu,
    EventMenu.menuList.menu,
    AreaMenu.menuAdd.menu,
    AreaMenu.menuEdit.menu,
    AreaMenu.menuList.menu,
    ActionLineMenu.menuAdd.menu,
    ActionLineMenu.menuEdit.menu,
    ActionLineMenu.menuList.menu,
    ProgramMenu.menuAdd.menu,
    ProgramMenu.menuEdit.menu,
    ProgramMenu.menuList.menu,
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
    espacio.menu,
    media.menu,
    blog.menu,
    convocatorias.menu,
    contacto.menu,
    administracion.menu
  )

  /*
   * Return a SiteMap needed for Lift
   */
  def siteMap: SiteMap = SiteMap(menus:_*)
}

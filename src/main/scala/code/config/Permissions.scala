package code
package config

import net.liftmodules.mongoauth.Permission

object Permissions {
  val Mensajes = Permission("Mensajes")
  val Administracion = Permission("Administración")
  val Solicitudes = Permission("Solicitudes")
  val Eventos = Permission("Eventos")
  val Calendario = Permission("Calendario")
  val Convocatorias = Permission("Convocatorias")
  val Residencias = Permission("Residencias")
  val Salas = Permission("Salas")
  val Espacios = Permission("Espacios")
  val Areas = Permission("Áreas")
  val Programas = Permission("Programas")
  val Procesos = Permission("Procesos")
  val Proyectos = Permission("Proyectos")
  val Archivos = Permission("Archivos")
  val Accesorios = Permission("Accessorios (Equipos y Herramientas)")
  val Servicios = Permission("Servicios")
  val Usuarios = Permission("Usuarios")
  val Blog = Permission("Blog")
  val Roles = Permission("Roles")
  val Redes = Permission("Redes")
  val Cotizaciones = Permission("Cotizaciones")
  val Estadisticas = Permission("Estadísticas")
  //ToDO widgets no es soportado todavia
  val Widgets = Permission("Widgets")
  //ToDO paginas no es soportado todavia
  val Paginas = Permission("Paginas")

  def list = List(
    Accesorios, Administracion, Archivos, Areas, Blog, Calendario, Convocatorias, Cotizaciones, Espacios, Estadisticas, Eventos,
    Mensajes, Paginas, Procesos, Programas, Proyectos, Redes, Residencias, Roles, Salas, Servicios, Solicitudes,
    Usuarios, Widgets)

}

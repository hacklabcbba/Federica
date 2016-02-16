package code
package config

import net.liftmodules.mongoauth.Permission

object Permissions {
  val Mensajes = Permission("Mensajes")
  val Organizacion = Permission("Organizacion")
  val Ambientes = Permission("Ambientes")
  val Solicitudes = Permission("Solicitudes")
  val Principios = Permission("Principios")
  val Eventos = Permission("Eventos")
  val Calendario = Permission("Calendario")
  val Convocatorias = Permission("Convocatorias")
  val Paginas  = Permission("Paginas")
  val Widgets = Permission("Widgets")
  val Menus = Permission("Menus")
  val Residencias = Permission("Residencias")
  val Salas = Permission("Salas")
  val Espacios = Permission("Espacios")
  val Areas = Permission("Áreas")
  val AreasTransversales = Permission("Áreas transversales")
  val Programas = Permission("Programas")
  val Procesos = Permission("Procesos")
  val LineasDeAccion = Permission("Lineas de acción")
  val EnfoquesTransversales = Permission("Enfoques transversales")
  val Proyectos = Permission("Proyectos")
  val Archivos = Permission("Archivos")
  val Accesorios = Permission("Accessorios (Equipos y Herramientas)")
  val Servicios = Permission("Servicios")
  val Usuarios = Permission("Usuarios")
  val Blog = Permission("Blog")
  val Roles = Permission("Roles")
  val Redes = Permission("Redes")
  val Apariencia = Permission("Apariencia")
  val Cotizaciones = Permission("Cotizaciones")
  val Estadisticas = Permission("Estadísticas")

  def list = List(
    Accesorios, Apariencia, Organizacion, Ambientes, Archivos, Areas, AreasTransversales, Blog, Calendario,
    Convocatorias, Cotizaciones, Espacios, Estadisticas, Eventos, Mensajes,
    Menus, Paginas, Principios, Procesos, Programas, Proyectos, Redes, Residencias,
    Roles, Salas, Servicios, Solicitudes, Usuarios, Widgets)

}

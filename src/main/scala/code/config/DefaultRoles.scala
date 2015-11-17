package code
package config

import net.liftmodules.mongoauth.model.Role
import Permissions._

object DefaultRoles {

  private val SUPER_ADMINISTRATOR = "Super Admin"

  def SuperAdmin = Role.find(SUPER_ADMINISTRATOR) openOr
    Role
      .createRecord
      .id(SUPER_ADMINISTRATOR)
      .save(true)

  private val ADMINISTRATOR = "Admin"

  def Admin = Role.find(ADMINISTRATOR) openOr
    Role
      .createRecord
      .id(ADMINISTRATOR)
      .permissions(
        List(
          Mensajes, Solicitudes, Eventos, Calendario, Proyectos,
          Convocatorias, Residencias, Salas, Areas, Programas,
          Archivos, Accesorios, Servicios, Cotizaciones, Procesos, Paginas,
          Estadisticas, Usuarios, Roles, Blog, Widgets, Espacios, Redes))
      .save(true)

  private val COORD_GENERAL = "Coordinador General"

  def CoordGeneral = Role.find(COORD_GENERAL) openOr
    Role
      .createRecord
      .id(COORD_GENERAL)
      .permissions(
        List(
          Mensajes, Solicitudes, Eventos, Calendario,
          Convocatorias, Residencias, Accesorios,
          Cotizaciones, Estadisticas, Blog))
      .save(true)

  private val COORD = "Coordinador de Áreas y Programas"

  def Coord = Role.find(COORD) openOr
    Role
      .createRecord
      .id(COORD)
      .permissions(
        List(
          Mensajes, Solicitudes, Eventos, Calendario,
          Convocatorias, Cotizaciones, Blog))
      .save(true)

  private val USR = "Usuario"

  def Usuario = Role.find(USR) openOr
    Role
      .createRecord
      .id(USR)
      .permissions(
        List(
          Mensajes, Solicitudes, Eventos, Calendario))
      .save(true)



}

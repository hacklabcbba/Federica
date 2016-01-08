package code
package snippet

import code.config.Site
import code.model.network.Network
import code.model.{Program, Area, Process}
import net.liftmodules.extras.SnippetHelper
import net.liftweb.sitemap.SiteMap
import net.liftweb.util.Helpers
import Helpers._
import scala.xml.NodeSeq

object MenuGenerator extends SnippetHelper {

  def areas = {
    val menus: NodeSeq = { Area.findAllPublished.foldLeft(NodeSeq.Empty){ case (node, area) => {
      node ++ {
        val process = Process.findByArea(area)
        if (process.isEmpty) {
          <li><a href={Site.area.calcHref(area)}>{area.name.get}</a></li>
        } else {
          val dropdown: NodeSeq = process.map { proceso =>
            <li><a href={Site.proceso.calcHref(proceso)}>{proceso.name.get}</a></li>
          }

          <li class="dropdown">
            <a href={Site.area.calcHref(area)} class="dropdown-toggle" data-toggle="dropdown">{area.name.get} <b class="caret"></b></a>
            <ul class="dropdown-menu">{ dropdown }</ul>
          </li>
        }
      }}}: NodeSeq
    }

    "* *" #> menus
  }

  def programs = {
    val menus: NodeSeq = { Program.findAllPublished.foldLeft(NodeSeq.Empty){ case (node, program) => {
      node ++ {
        val process = Process.findByProgram(program)
        if (process.isEmpty) {
          <li><a href={Site.programa.calcHref(program)}>{program.name.get}</a></li>
        } else {
          val dropdown: NodeSeq = process.map { proceso =>
            <li><a href={Site.proceso.calcHref(proceso)}>{proceso.name.get}</a></li>
          }

          <li class="dropdown">
            <a href={Site.programa.calcHref(program)} class="dropdown-toggle" data-toggle="dropdown">{program.name.get} <b class="caret"></b></a>
            <ul class="dropdown-menu">{ dropdown }</ul>
          </li>
        }
      }}}: NodeSeq
    }

    "* *" #> menus
  }

  def networks = {
    val menus: NodeSeq = { Network.findAll.foldLeft(NodeSeq.Empty){ case (node, network) => {
      node ++ {
        <li><a href={Site.red.calcHref(network)}>{network.name.get}</a></li>
      }}}: NodeSeq
    }

    "* *" #> menus
  }

}

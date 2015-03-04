package code
package model
package proposal

import code.model.project._
import net.liftweb.record.field.StringField
import net.liftweb.mongodb.record.field.ObjectIdRefField

class ProposalSpec extends BaseMongoSessionWordSpec {

  "Proposal" should {
    "create, validate, save, and retrieve properly" in {

      val city = City.createRecord
        .name("Cochabamba")

      val errsCity = city.validate
      if (errsCity.length > 1) {
        fail("Validation error: " + errsCity.mkString(", "))
      }

      city.validate.length should equal (0)
      city.save(false)

      val country = Country.createRecord
        .name("Bolivia")

      val errsCountry = country.validate
      if (errsCountry.length > 1) {
        fail("Validation error: " + errsCountry.mkString(", "))
      }

      country.validate.length should equal (0)
      country.save(false)


      /* Action line test
      */
      val actionLine = ActionLine.createRecord
        .name("Action Line: Desarrollo Barrial")
        .description("descripcion Desarrollo Barrial")

      val errsActionLine = actionLine.validate
      if (errsActionLine.length > 1) {
        fail("Validation error: " + errsActionLine.mkString(", "))
      }
      actionLine.validate.length should equal (0)
      actionLine.save(false)

      /* Program test */

      val program = Program.createRecord
        .name("Programa nro1")
        .description("Descripcion del programa")

      val errsProgram = program.validate
      if (errsProgram.length > 1) {
        fail("Validation error: " + errsProgram.mkString(", "))
      }
      program.validate.length should equal (0)
      program.save(false)


      /* Area test */
      val area = Area.createRecord
        .name(" Visual crafts")
        .description("Martadero Areas")
        .email("visual@gmail.com")
        .code("ARV")

      val errsArea = area.validate
      if (errsArea.length > 1) {
        fail("Validation error: " + errsArea.mkString(", "))
      }
      area.validate.length should equal (0)
      area.save(false)


      val proposal = Proposal.createRecord
        .description("Include information about recent international progress in the field of the research, and the " +
        "relationship of this proposal to work in the field generally")
        .review("Link phases of the research plan/approach with the anticipated timeline")
        .concept("The Joint Centre for History and Economics is delighted to announce the History Project, an initiative" +
        " supported by the Institute for New Economic Thinking, with the object of encouraging a new generation of " +
        "historians of the economy and economic life. At a time when the need for historical understanding of large-scale" +
        " economic changes is extraordinarily great, the Project seeks to provide support for young historians who are " +
        "interested in economic history, the history of economic thought, and political and cultural histories of economic life. ")
        .name("Big History Project")
        .state("In review")
        .city(city.id.get)
        .country(country.id.get)
        .area(area.id.get)

      val errsProposal = proposal.validate
      if (errsProposal.length > 1) {
        fail("Validation error: " + errsProposal.mkString(", "))
      }

      proposal.validate.length should equal (0)

      proposal.save(false)

    }
  }

  def createProcess:Process = {

    val responsible = createResponsible

    val process = Process.createRecord
      .name("Proceso nro1")
      .description("Descripcion del proceso 1")
      .goal("Objetivo")
      .responsible(responsible.id.get)
      .history("Historia del proceso")

    val errsList = process.validate
    if (errsList.length > 1) {
      fail("Validation error: " + errsList.mkString(", "))
    }
    process.validate.length should equal (0)
    process.save(false)
    process
  }

  def createResponsible:Organizer = {
    val organizer = Organizer.createRecord
      .name("Juan")
      .lastName("Perez")

    val errsList = organizer.validate
    if (errsList.length > 1) {
      fail("Validation error: " + errsList.mkString(", "))
    }
    organizer.validate.length should equal (0)
    organizer.save(false)
    organizer
  }
}
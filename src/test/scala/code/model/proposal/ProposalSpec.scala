package code
package model
package proposal

import code.model.activity.ActivityType
import code.model.project._
import code.model.event.RangeType
import org.joda.time.DateTime

class ProposalSpec extends BaseMongoSessionWordSpec {

  "Proposal" should {
    "create, validate, save, and retrieve properly" in {

      val city = createCity
      val country = createCountry
      val area = createArea
      val activity = createActivityType
      val actionLine = createActionLine
      val program = createProgram

      val date1: DateTime = new DateTime(2015, 3, 10, 15, 0, 0, 0)
      val date2: DateTime = new DateTime(2015, 3, 11, 18, 0, 0, 0)
      val date3: DateTime = new DateTime(2015, 3, 12, 18, 0, 0, 0)
      val date4: DateTime = new DateTime(2015, 3, 18, 21, 0, 0, 0)

      val proposal = Proposal
        .createRecord
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
        .isAproved(false)
        .activityType(activity.id.get)
        .city(city.id.get)
        .country(country.id.get)
        .area(area.id.get)
        .dateRange(date1 :: date2 :: date3 :: date4 :: Nil)
        .rangeType(RangeType.ContinuousInterval)

      val errsProposal = proposal.validate
      if (errsProposal.length > 1) {
        fail("Validation error: " + errsProposal.mkString(", "))
      }

      proposal.validate.length should equal (0)

      proposal.save(false)

    }
  }

  def createProgram = {
    /* Program test */

    val program = Program
      .createRecord
      .name("Programa nro1")
      .description("Descripcion del programa")

    val errsProgram = program.validate
    if (errsProgram.length > 1) {
      fail("Validation error: " + errsProgram.mkString(", "))
    }
    program.validate.length should equal (0)
    program.save(false)

  }

  def createCity = {
    val city = City
      .createRecord
      .name("Cochabamba")

    val errsCity = city.validate
    if (errsCity.length > 1) {
      fail("Validation error: " + errsCity.mkString(", "))
    }

    city.validate.length should equal (0)
    city.save(false)
    city
  }

  def createCountry = {
    val country = Country
      .createRecord
      .name("Bolivia")

    val errsCountry = country.validate
    if (errsCountry.length > 1) {
      fail("Validation error: " + errsCountry.mkString(", "))
    }

    country.validate.length should equal (0)
    country.save(false)
  }

  def createActivityType = {
    val activityType = ActivityType
      .createRecord
      .name("Proyection")

    val errsActivity = activityType.validate
    if (errsActivity.length > 1) {
      fail("Validation error: " + errsActivity.mkString(", "))
    }

    activityType.validate.length should equal (0)
    activityType.save(false)
  }

  def createArea = {
    val area = Area
      .createRecord
      .name(" Visual crafts")
      .description("Martadero Areas")
      .email("visual@gmail.com")
      .code("ARV")

    val errsArea = area.validate
    if (errsArea.length > 1) {
      fail("Validation error: " + errsArea.mkString(", "))
    }

    area.validate.length should equal(0)
    area.save(false)
  }

  def createResponsible:Organizer = {
    val organizer = Organizer
      .createRecord
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

  def createActionLine = {
    /* Action line test
        */
    val actionLine = ActionLine
      .createRecord
      .name("Action Line: Desarrollo Barrial")
      .description("descripcion Desarrollo Barrial")

    val errsActionLine = actionLine.validate
    if (errsActionLine.length > 1) {
      fail("Validation error: " + errsActionLine.mkString(", "))
    }
    actionLine.validate.length should equal (0)
    actionLine.save(false)
  }
}
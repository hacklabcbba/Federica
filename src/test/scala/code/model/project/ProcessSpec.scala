package code
package model
package project

import java.util.Calendar
import net.liftweb.common.Box
import net.liftweb.util.Helpers._
import org.joda.time
import org.joda.time.DateTime

class ProcessSpec extends BaseMongoSessionWordSpec {

  "Process" should {
    "create, validate, save, and retrieve properly" in {

      val city = City
        .createRecord
        .name("Cochabamba")

      val errsCity = city.validate
      if (errsCity.length > 1) {
        fail("Validation error: " + errsCity.mkString(", "))
      }

      city.validate.length should equal (0)

      city.save(false)

      val country = Country
        .createRecord
        .name("Bolivia")

      val errsCountry = country.validate
      if (errsCountry.length > 1) {
        fail("Validation error: " + errsCountry.mkString(", "))
      }

      country.validate.length should equal (0)

      country.save(false)

      val date: DateTime = new DateTime(2001, 5, 20, 0, 0, 0, 0)

      val schedule = Schedule
        .createRecord
        .begins(date.toDate)
        .ends(date.toDate)
        .city(city.id.get)
        .country(country.id.get)
        .description("Inauguration")

      val errsSchedule = country.validate
      if (errsSchedule.length > 1) {
        fail("Validation error: " + errsSchedule.mkString(", "))
      }

      schedule.validate.length should equal (0)

      schedule.save(false)

      val organizer = Organizer
        .createRecord
        .name("Jonh")
        .lastName("Smith")

      val errsOrganizer = organizer.validate
      if (errsOrganizer.length > 1) {
        fail("Validation error: " + errsOrganizer.mkString(", "))
      }

      organizer.validate.length should equal (0)

      organizer.save(false)

      val process = Process
        .createRecord
        .description("Include information about recent international progress in the field of the research, and the " +
          "relationship of this proposal to work in the field generally")
        .goal("Link phases of the research plan/approach with the anticipated timeline")
        .history("The Joint Centre for History and Economics is delighted to announce the History Project, an initiative" +
          "supported by the Institute for New Economic Thinking, with the object of encouraging a new generation of " +
          "historians of the economy and economic life. At a time when the need for historical understanding of large-scale" +
          "economic changes is extraordinarily great, the Project seeks to provide support for young historians who are " +
          "interested in economic history, the history of economic thought, and political and cultural histories of economic life. ")
        .name("Big History Project")
        .responsible(organizer.id.get)

      val errsProcess = process.validate
      if (errsProcess.length > 1) {
        fail("Validation error: " + errsProcess.mkString(", "))
      }

      process.validate.length should equal (0)

      process.save(false)

    }
  }
}

package code
package model
package event

import java.util.Calendar

import code.model.project._
import net.liftweb.common.Box
import net.liftweb.util.Helpers._

class EventSpec extends BaseMongoSessionWordSpec {

  "Event" should {
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

      val dateFormat : java.text.DateFormat = new java.text.SimpleDateFormat("dd-MM-yyyy")
      val date : Box[java.util.Date] = tryo {
        val calendar = Calendar.getInstance()
        calendar.setTime(dateFormat.parse("20-05-2001"))
        calendar.getTime
      }

      val schedule = Schedule.createRecord
        .startDate(date)
        .endDate(date)
        .city(city.id.get)
        .country(country.id.get)
        .description("Inauguration")

      val errsSchedule = country.validate
      if (errsSchedule.length > 1) {
        fail("Validation error: " + errsSchedule.mkString(", "))
      }

      schedule.validate.length should equal (0)

      schedule.save(false)

      val organizer = Organizer.createRecord
        .name("Jonh")
        .lastName("Smith")

      val errsOrganizer = organizer.validate
      if (errsOrganizer.length > 1) {
        fail("Validation error: " + errsOrganizer.mkString(", "))
      }

      organizer.validate.length should equal (0)

      organizer.save(false)

      val event = Event.createRecord
        .description("Include information about recent international progress in the field of the research, and the " +
        "relationship of this proposal to work in the field generally")
        .name("Big History Project")
        .responsible(organizer.id.get)
        .schedule(schedule :: Nil)

      val errsEvent = event.validate
      if (errsEvent.length > 1) {
        fail("Validation error: " + errsEvent.mkString(", "))
      }

      event.validate.length should equal (0)

      event.save(false)

    }
  }
}
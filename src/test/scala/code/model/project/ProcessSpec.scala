package code
package model
package project

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

      val schedule = createSchedule

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

  def createSchedule: Schedule = {

    val date1: DateTime = new DateTime(2015, 3, 10, 15, 0, 0, 0)
    val date2: DateTime = new DateTime(2015, 3, 11, 18, 0, 0, 0)
    val date3: DateTime = new DateTime(2015, 3, 12, 18, 0, 0, 0)
    val date4: DateTime = new DateTime(2015, 3, 18, 21, 0, 0, 0)

    val schedule = Schedule
      .createRecord
      .isAtSameHour(true)
      .dateRange(date1 :: date2 :: date3 :: date4 :: Nil)
      .rangeType(RangeType.ContinuousInterval)


    val errsList = schedule.validate
    if (errsList.length > 1) {
      fail("Validation error: " + errsList.mkString(", "))
    }
    schedule.validate.length should equal (0)
    schedule.save(false)
    schedule
  }

  def createCity(name: String) = {
    val city = City
      .createRecord
      .name(name)

    val errsCity = city.validate
    if (errsCity.length > 1) {
      fail("Validation error: " + errsCity.mkString(", "))
    }

    city.validate.length should equal (0)
    city.save(false)
    city
  }

  def createCountry(name: String) = {
    val country = Country
      .createRecord
      .name(name)

    val errsCountry = country.validate
    if (errsCountry.length > 1) {
      fail("Validation error: " + errsCountry.mkString(", "))
    }

    country.validate.length should equal (0)
    country.save(false)
  }
}

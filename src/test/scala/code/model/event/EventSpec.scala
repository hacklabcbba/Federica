package code
package model
package event

import java.util.{Date, Calendar}

import code.model.project._
import net.liftweb.common.Box
import net.liftweb.util.Helpers._
import org.joda.time.DateTime

class EventSpec extends BaseMongoSessionWordSpec {

  "Event" should {
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
        .startDate(date.toDate)
        .endDate(date.toDate)
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

      val eType1 =  createEventType("festival")
      val eType2 =  createEventType("concierto")

      val dateInfoList = createDateInfoList

      val event = Event
        .createRecord
        .eventNumber(1000)
        .name("Big History Project")
        .description("Include information about recent international progress in the field of the research, and the " +
          "relationship of this proposal to work in the field generally")
        .schedule()

      val errsEvent = event.validate
      if (errsEvent.length > 1) {
        fail("Validation error: " + errsEvent.mkString(", "))
      }

      event.validate.length should equal (0)

      event.save(false)

    }
  }

  def createEventType(name:String): EventType = {
    val eventType = EventType
      .createRecord
      .name(name)

    val errsList = eventType.validate
    if (errsList.length > 1) {
      fail("Validation error: " + errsList.mkString(", "))
    }
    eventType.validate.length should equal (0)
    eventType.save(false)
    eventType
  }

  def createDateInfoList: DateInfoList = {

    val date1: DateTime = new DateTime(2015, 3, 10, 15, 0, 0, 0)
    val date2: DateTime = new DateTime(2015, 3, 10, 18, 0, 0, 0)
    val date3: DateTime = new DateTime(2015, 3, 11, 18, 0, 0, 0)
    val date4: DateTime = new DateTime(2015, 3, 11, 21, 0, 0, 0)

    val itemList1 = createDateInfo(date1, date2)
    val itemList2 = createDateInfo(date3, date4)

    val dateInfoList = DateInfoList
      .createRecord
      .isCorrelative(true)
      itemList(itemList1 :: itemList2 :: Nil)
      .isAtSameHour(true)

    val eventType = EventType
      .createRecord
      .name(name)

    val errsList = eventType.validate
    if (errsList.length > 1) {
      fail("Validation error: " + errsList.mkString(", "))
    }
    eventType.validate.length should equal (0)
    eventType.save(false)
    eventType
  }

  def createDateInfo(startDate: DateTime, endDate: DateTime): DateInfo = {

    val dateInfo = DateInfo
      .createRecord
      .startDate(startDate.toDate)
      .endDate(startDate.toDate)
      .description("Description de la fecha")

    val errsList = dateInfo.validate
    if (errsList.length > 1) {
      fail("Validation error: " + errsList.mkString(", "))
    }
    dateInfo.validate.length should equal (0)
    dateInfo.save(false)
    dateInfo
  }
}
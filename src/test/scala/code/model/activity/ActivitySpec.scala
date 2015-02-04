package code
package model
package activity

import java.util.Calendar

import code.model.project.{Country, City, Schedule}
import code.model.resource.{PackageResource, Resource, Environment}
import net.liftweb.common.Box
import net.liftweb.util.Helpers._
import code.model.activity.ActivityType._

class ActivitySpec extends BaseMongoSessionWordSpec {

  "Activity" should {
    "create, validate, save, and retrieve properly" in {

      val resource = Resource.createRecord

      val environment = Environment.createRecord

      val packageRes = PackageResource.createRecord

      val dateFormat : java.text.DateFormat = new java.text.SimpleDateFormat("dd-MM-yyyy")
      val date : Box[java.util.Date] = tryo {
        val calendar = Calendar.getInstance()
        calendar.setTime(dateFormat.parse("20-05-2001"))
        calendar.getTime
      }

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

      val activity = Activity.createRecord
        .activityType(Other)
        .cost(25.22)
        .description("Children's Day")
        .environment(environment :: Nil)
        .packages(packageRes :: Nil)
        .resources(resource :: Nil)
        .name("Children's Day")

      val errsActivity= activity.validate
      if (errsActivity.length > 1) {
        fail("Validation error: " + errsActivity.mkString(", "))
      }

      activity.validate.length should equal (0)

      activity.save(false)

    }
  }
}

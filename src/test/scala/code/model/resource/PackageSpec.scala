package code
package model
package resource

import code.model.resource.ClassType._
import code.model.resource.CostType._

class PackageSpec extends BaseMongoSessionWordSpec {

  "Package" should {
    "create, validate, save, and retrieve properly" in {

      val consumer = Consumer.createRecord
        .name("PROGRAM Formarte mARTadero")
        .description("")

      consumer.validate.length should equal (0)
      consumer.save(false)

      val quotePackage = RoomQuote.createRecord
        .costType(PackageCost)
        .cost(280.00)
        .characteristics("Weekly (10% OFF)")
        .parameter("10 hrs. week")
        .consumer(consumer.id.get)

      quotePackage.validate.length should equal (0)
      quotePackage.save(false)

      val packageResourse = ResourcePackage.createRecord
        .name("DIGITAL ROOM LAB + 10 PCs + DATA DISPLAY")
        .description("The contributions for use discount is only valid if payment of at least 40% of the contribution " +
        "is made, before the workshop")
        .comboName("PACKAGE FOR WORKSHOPS IN THE DIGITAL LAB")
        .classType(PackageType)
        .cost(quotePackage.id.get)

      val errsEnvironment = packageResourse.validate
      if (errsEnvironment.length > 1) {
        fail("Validation error: " + errsEnvironment.mkString(", "))
      }

      packageResourse.validate.length should equal (0)

      packageResourse.save(false)

    }
  }
}

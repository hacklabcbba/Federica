package code
package model
package resource

import code.model.proposal.Area
import code.model.resource.ClassType._
import code.model.resource.CostType._

class RoomSpec extends BaseMongoSessionWordSpec {

  "Room" should {
    "create, validate, save, and retrieve properly" in {

      val area1 = Area.createRecord
        .code("ARV")
        .description("")
        .isWorkshop(true)
        .name("Visual and graphic design")

      area1.validate.length should equal (0)
      area1.save(false)

      val area2 = Area.createRecord
        .code("LET")
        .description("")
        .isWorkshop(true)
        .name("Arts and Literature")

      area2.validate.length should equal (0)
      area2.save(false)

      val consumer = Consumer.createRecord
        .areas(area1 :: area2 :: Nil)
        .name("associated")
        .description("EMERGING NATIONAL - INTERNATIONAL Autogestionados (40% OFF) *")

      consumer.validate.length should equal (0)
      consumer.save(false)

      val quoteRoom = RoomQuote.createRecord
        .costType(RoomCost)
        .cost(280)
        .characteristics("Weekly (10% OFF)")
        .parameter("10 hrs. week")
        .consumer(consumer.id.get)

      quoteRoom.validate.length should equal (0)
      quoteRoom.save(false)

      val room = Room.createRecord
        .description("Include information about recent international progress in the field of the research, and the " +
        "relationship of this proposal to work in the field generally")
        .capacity(300)
        .state("Available")
        .name("Trozadero")
        .isReservable(true)
        .plane("plane.jpg")
        .classType(RoomType)
        .cost(quoteRoom.id.get)

      val errsEnvironment = room.validate
      if (errsEnvironment.length > 1) {
        fail("Validation error: " + errsEnvironment.mkString(", "))
      }

      room.validate.length should equal (0)

      room.save(false)

    }
  }
}
package code
package model
package resource

import code.model.project.{Country, City}
import code.model.proposal.{Proposal, Area}

class RoomSpec extends BaseMongoSessionWordSpec {

  "Room" should {
    "create, validate, save, and retrieve properly" in {

      val room = Room.createRecord
        .description("Include information about recent international progress in the field of the research, and the " +
        "relationship of this proposal to work in the field generally")
        .capacity(300)
        .state("Available")
        .name("Trozadero")
        .isReservable(true)
        .plane("plane.jpg")

      val errsEnvironment = room.validate
      if (errsEnvironment.length > 1) {
        fail("Validation error: " + errsEnvironment.mkString(", "))
      }

      room.validate.length should equal (0)

      room.save(false)

    }
  }
}
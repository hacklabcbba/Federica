package code
package model
package resource

import code.model.project.{Country, City}
import code.model.proposal.{Proposal, Area}

class EnvironmentSpec extends BaseMongoSessionWordSpec {

  "Environment" should {
    "create, validate, save, and retrieve properly" in {

      val environment = Environment.createRecord
        .description("Include information about recent international progress in the field of the research, and the " +
        "relationship of this proposal to work in the field generally")
        .capacity(300)
        .state("Available")
        .name("Trozadero")
        .isReservable(true)
        .plane("plane.jpg")

      val errsEnvironment = environment.validate
      if (errsEnvironment.length > 1) {
        fail("Validation error: " + errsEnvironment.mkString(", "))
      }

      environment.validate.length should equal (0)

      environment.save(false)

    }
  }
}
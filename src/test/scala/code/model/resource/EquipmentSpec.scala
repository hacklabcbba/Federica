package code
package model
package resource

import code.model.resource.CostType._
import code.model.resource.ClassType._

class EquipmentSpec extends BaseMongoSessionWordSpec {

  "Package" should {
    "create, validate, save, and retrieve properly" in {

      val consumer = Consumer
        .createRecord
        .name("Artist")
        .description("EMERGING NATIONAL - INTERNATIONAL Autogestionados (40% OFF) *")

      consumer.validate.length should equal (0)
      consumer.save(false)

      val quoteEquipment = EquipmentQuote
        .createRecord
        .costType(CostType.Day)
        .cost(50.00)
        .characteristics("15")
        .parameter("By day")
        .consumer(consumer.id.get)

      quoteEquipment.validate.length should equal (0)
      quoteEquipment.save(false)

      val equipment = Equipment
        .createRecord
        .name("SET OF 500W HALOGEN (Units)")
        .description("NOTE: GUARANTEED MONEY WILL BE TOGETHER WITH IDENTIFICATION CARD. THE AMOUNT OF Bs. 150.- TO THE" +
        " EQUIPMENT TO BE REQUESTED. ALL OUT AND RETURN OF EQUIPMENT IS")
        .classType(ClassType.EquipmentType)

      val errsEnvironment = equipment.validate
      if (errsEnvironment.length > 1) {
        fail("Validation error: " + errsEnvironment.mkString(", "))
      }

      equipment.validate should equal (Nil)
      equipment.save(false)

    }
  }
}

package code
package model
package productive

class ProductiveUnitSpec extends BaseMongoSessionWordSpec {

  "Productive Unit" should {
    "create, validate, save, and retrieve properly" in {

      val productive = ProductiveUnit.createRecord
        .name("Mosquita muerta")
        .description("Una emprendimiento creativo multifuncional y autosostenible, " +
        "que tiene como objetivo promover actividades y productos ligados al entorno " +
        "creativo del Proyecto mARTadero, en colaboración con los coordinadores de las " +
        "áreas artísticas y los creadores vinculados al espacio.")

      val errsProductive = productive.validate
      if (errsProductive.length > 1) {
        fail("Validation error: " + errsProductive.mkString(", "))
      }

      productive.validate.length should equal (0)

      productive.save(false)

    }
  }
}
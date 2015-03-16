package code
package lib
package request

import code.model.productive.ProductiveUnit
import net.liftweb.common.{Empty, Box}
import net.liftweb.http.RequestVar

package object request {
  object productiveRequestVar extends RequestVar[Box[ProductiveUnit]](Empty)
  object productiveDeleteRequestVar extends RequestVar[List[ProductiveUnit]](Nil)
}

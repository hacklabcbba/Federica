package code
package lib
package request

import code.model.productive.ProductiveUnit
import code.model.event._
import net.liftweb.common.{Empty, Box}
import net.liftweb.http.RequestVar
import code.model.proposal.Area
import code.model.proposal.ActionLine

package object request {
  object productiveRequestVar extends RequestVar[Box[ProductiveUnit]](Empty)
  object productiveDeleteRequestVar extends RequestVar[List[ProductiveUnit]](Nil)
  object eventRequestVar extends RequestVar[Box[Event]](Empty)
  object eventDeleteRequestVar extends RequestVar[List[Event]](Nil)
  object areaRequestVar extends RequestVar[Box[Area]](Empty)
  object areaDeleteRequestVar extends RequestVar[List[Area]](Nil)
  object actionLineRequestVar extends RequestVar[Box[ActionLine]](Empty)
  object actionLinetDeleteRequestVar extends RequestVar[List[ActionLine]](Nil)
}

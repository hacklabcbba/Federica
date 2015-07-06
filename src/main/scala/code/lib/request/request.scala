package code
package lib
package request

import java.lang.Process

import code.model
import code.model._
import code.model.event._
import net.liftweb.common.{Empty, Box}
import net.liftweb.http.RequestVar
import code.model.network.Network
import code.model.resource.{Equipment, Room, ConcreteResource}

package object request {
  object productiveRequestVar extends RequestVar[Box[ProductiveUnit]](Empty)
  object productiveDeleteRequestVar extends RequestVar[List[ProductiveUnit]](Nil)
  object eventRequestVar extends RequestVar[Box[Event]](Empty)
  object eventDeleteRequestVar extends RequestVar[List[Event]](Nil)
  object areaRequestVar extends RequestVar[Box[Area]](Empty)
  object areaDeleteRequestVar extends RequestVar[List[Area]](Nil)
  object actionLineRequestVar extends RequestVar[Box[ActionLine]](Empty)
  object actionLineDeleteRequestVar extends RequestVar[List[ActionLine]](Nil)
  object programRequestVar extends RequestVar[Box[Program]](Empty)
  object programDeleteRequestVar extends RequestVar[List[Program]](Nil)
  object eventTypeRequestVar extends RequestVar[Box[EventType]](Empty)
  object eventTypeDeleteRequestVar extends RequestVar[List[EventType]](Nil)
  object networkRequestVar extends RequestVar[Box[Network]](Empty)
  object networkDeleteRequestVar extends RequestVar[List[Network]](Nil)
  object processRequestVar extends RequestVar[Box[model.Process]](Empty)
  object processDeleteRequestVar extends RequestVar[List[model.Process]](Nil)
  object concreteResourceRequestVar extends RequestVar[Box[ConcreteResource]](Empty)
  object concreteResourceDeleteRequestVar extends RequestVar[List[ConcreteResource]](Nil)
  object roomRequestVar extends RequestVar[Box[Room]](Empty)
  object roomDeleteRequestVar extends RequestVar[List[Room]](Nil)
  object equipmentRequestVar extends RequestVar[Box[Equipment]](Empty)
  object equipmentDeleteRequestVar extends RequestVar[List[Equipment]](Nil)
}

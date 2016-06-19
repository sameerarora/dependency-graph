package com.stackstate.event

import com.stackstate.graph.Node

case class FailureEvent(severity: Int)

object Minor extends FailureEvent(1)

object Warning extends FailureEvent(2)

object Major extends FailureEvent(3)

object Critical extends FailureEvent(4)

object Fatal extends FailureEvent(5)

object SeverityScore

object DetectCyclesEvent

case class HighMemoryUsageEvent(node: Node)

case class DiskOutOfSpaceEvent(node: Node)

case class HighCpuConsumption(node: Node)

object HealthCheck

case class Alert(severity: Int, message: Option[String] = None)

case class Ok(message: Option[String])

object FailureAlert extends Alert(5, Option("Node is in critical condition, please take remedial actions"))

case class PropagationAlert(node: Node, event: FailureEvent)

object ResetEvent

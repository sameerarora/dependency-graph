package com.stackstate.actor

import akka.actor.Actor
import com.stackstate.graph.Node


class NodeActor(var node: Node) extends Actor {

  override def receive: Receive = {
    case m: FailureEvent =>
      node = Node(node.label, node.alerts :+ m)
      sender ! "Acknowledged"
    case HealthCheck => if (node.severityScore >= 10) {
      sender ! Alert(5, Option("Node is in critical condition, please take remedial actions"))
    }
  }
}

case class FailureEvent(severity: Int)

object Major extends FailureEvent(3)

object Critical extends FailureEvent(4)

object Fatal extends FailureEvent(5)
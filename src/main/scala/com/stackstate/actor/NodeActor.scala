package com.stackstate.actor

import akka.actor.Actor
import com.stackstate.event._
import com.stackstate.graph.Node


class NodeActor(node: Node) extends Actor {

  var severity: Int = 0

  override def receive: Receive = {
    case m: FailureEvent =>
      severity += m.severity
      if (severity >= node.threshold) {
        sender ! PropagationAlert(node, Warning)
      }
    case HealthCheck => if (severity >= node.threshold) sender ! FailureAlert else sender ! Ok

    case ResetEvent => severity = 0
  }
}


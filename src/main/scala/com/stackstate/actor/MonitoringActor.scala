package com.stackstate.actor

import akka.actor.{Actor, ActorLogging}
import com.stackstate.graph.{Graph, Node}

case class MonitoringActor(graph: Graph) extends Actor with ActorLogging {

  override def receive: Receive = {
    case DetectCyclesEvent =>
      graph.hasCycle match {
        case true => sender ! Alert(3, Option("Alert : Cycle(s) have been detected in the graph please review your dependencies"))
        case _ => sender ! Ok
      }
    case HighMemoryUsageEvent(node: Node) =>
      if (graph.dependenciesOf(graph.centralNode).contains(node))
        sender ! Alert(4)
      else sender ! Ok
    case _ => throw new IllegalArgumentException("I don't know how to respond to this message.")
  }
}


object DetectCyclesEvent

case class HighMemoryUsageEvent(node: Node)

object HealthCheck

case class Alert(severity: Int, message: Option[String] = None)

case class Ok(message: Option[String])
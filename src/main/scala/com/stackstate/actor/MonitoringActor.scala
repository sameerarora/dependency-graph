package com.stackstate.actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.stackstate.config.CommonSettings._
import com.stackstate.event._
import com.stackstate.graph.{Graph, Node}

case class MonitoringActor(dependencyGraph: Graph) extends Actor with ActorLogging {

  val nodeActors: Set[(Node, ActorRef)] = dependencyGraph.vertices().map(v => v -> context.system.actorOf(Props(classOf[NodeActor], v), v.label))

  var severityScore: Int = 0

  override def receive: Receive = {
    case DetectCyclesEvent =>
      dependencyGraph.hasCycle match {
        case true => sender ! Alert(3, Option("Alert : Cycle(s) have been detected in the graph please review your dependencies"))
        case _ => sender ! Ok
      }
    case HighMemoryUsageEvent(node: Node) => respondToFailure(node, Major)

    case DiskOutOfSpaceEvent(node: Node) => respondToFailure(node, Critical)

    case HighCpuConsumption(node: Node) => respondToFailure(node, Fatal)

    case PropagationAlert(node: Node, event: FailureEvent) =>
      severityScore += event.severity * dependencyGraph.dependentsOf(node).size
      nodeActors.filter(p => dependencyGraph.dependentsOf(node).contains(p._1)).foreach(n => n._2 ! event)
      sender ! Alert(4, Option(s"Node ${node.label} has gone into a failure state"))

    case HealthCheck =>
      if (severityScore > graphThreshold)
        sender ! Alert(5, Option("System is found in a critical state, please take remedial action!!"))
      else sender ! Ok

    case ResetEvent => nodeActors.foreach(x => x._2 ! ResetEvent)

    case _ => throw new IllegalArgumentException("I don't know how to respond to this message.")

  }

  private def respondToFailure(node: Node, failureEvent: FailureEvent): Unit = {
    severityScore += failureEvent.severity
    nodeActors.find(p => p._1.label.equals(node.label)) match {
      case Some(actor) => {
        actor._2 ! failureEvent
        sender ! "Acknowledged"
      }
      case None => throw new scala.IllegalArgumentException("No Node found with label " + node.label)
    }
  }
}



package com.stackstate.actor

import akka.actor.{ActorRef, ActorSystem, Props}
import com.stackstate.event._
import com.stackstate.graph.{Edge, Graph, Node}
import com.typesafe.config.ConfigFactory

class MonitoringActorTest extends ActorTestBase(ActorSystem("monitoring-graph", ConfigFactory.load("test.conf"))) {

  private final val acknowledged = "Acknowledged"

  describe("A Monitoring Actor") {
    it("should find out if monitored graph has any cycles in it") {
      val monitoringActor: ActorRef = system.actorOf(Props(classOf[MonitoringActor], Graph(Set[Edge](Edge("1", "2"), Edge("1", "4"), Edge("2", "3"), Edge("3", "4"), Edge("4", "2")))))
      monitoringActor ! DetectCyclesEvent
      expectMsg(Alert(3, Some("Alert : Cycle(s) have been detected in the graph please review your dependencies")))
    }

    val monitoringActor: ActorRef = system.actorOf(Props(classOf[MonitoringActor], Graph(Set[Edge](Edge("A", "B"), Edge("A", "D"), Edge("B", "C"), Edge("C", "D")))))
    it("responds to high memory usage event with high severity if node is an immediate dependency of central node ") {

      monitoringActor ! HighMemoryUsageEvent(Node("C"))
      expectMsg(acknowledged)
      monitoringActor ! HealthCheck
      expectMsg(Ok)
    }

    it("should retrieve the overall system state when health check event is called") {
      monitoringActor ! HealthCheck
      expectMsg(Ok)
    }

    it("should propagate failures to adjacent nodes and report system's health") {
      val node: Node = Node("C")
      monitoringActor ! ResetEvent
      monitoringActor ! HighMemoryUsageEvent(node)
      expectMsg(acknowledged)
      monitoringActor ! DiskOutOfSpaceEvent(node)
      expectMsg(acknowledged)
      monitoringActor ! HealthCheck
      expectMsg(Ok)
      monitoringActor ! HighCpuConsumption(node)
      expectMsg(acknowledged)
      monitoringActor ! HealthCheck
      expectMsg(Alert(5, Some("System is found in a critical state, please take remedial action!!")))
    }
  }

}

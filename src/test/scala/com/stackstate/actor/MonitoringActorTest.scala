package com.stackstate.actor

import akka.actor.{ActorRef, ActorSystem, Props}
import com.stackstate.graph.{Edge, Graph, Node}
import com.typesafe.config.ConfigFactory

class MonitoringActorTest extends ActorTestBase(ActorSystem("test-tasker", ConfigFactory.load("test.conf"))) {

  private val graph: Graph = Graph(Set[Edge](Edge("A", "B"), Edge("A", "D"), Edge("B", "C"), Edge("C", "D"), Edge("D", "B")))

  describe("A Monitoring Actor") {
    it("should find out if monitored graph has any cycles in it") {
      val monitoringActor: ActorRef = system.actorOf(Props(classOf[MonitoringActor], graph))
      monitoringActor ! DetectCyclesEvent
      expectMsg(Alert(3, Some("Alert : Cycle(s) have been detected in the graph please review your dependencies")))
    }
  }

  describe("A Monitoring Actor") {
    it("responds to high memory usage event with high severity if node is an immediate dependency of central node ") {
      val monitoringActor: ActorRef = system.actorOf(Props(classOf[MonitoringActor], Graph(Set[Edge](Edge("A", "B"), Edge("A", "D"), Edge("B", "C"), Edge("C", "D")))))
      monitoringActor ! HighMemoryUsageEvent(Node("C"))
      expectMsg(Alert(4))
    }
  }

}

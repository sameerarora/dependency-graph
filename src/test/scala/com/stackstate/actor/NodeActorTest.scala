package com.stackstate.actor

import akka.actor.{ActorRef, ActorSystem, Props}
import com.stackstate.graph.Node
import com.typesafe.config.ConfigFactory

class NodeActorTest extends ActorTestBase(ActorSystem("test-tasker", ConfigFactory.load("test.conf"))) {

  private val nodeActor: ActorRef = system.actorOf(Props(classOf[NodeActor], Node("A")))

  describe("A Node Actor") {
    it("should respond to failure events") {
      nodeActor ! Fatal
      expectMsg("Acknowledged")
      nodeActor ! Fatal
      expectMsg("Acknowledged")
      nodeActor ! HealthCheck
      expectMsg(Alert(5, Option("Node is in critical condition, please take remedial actions")))
    }
  }


}

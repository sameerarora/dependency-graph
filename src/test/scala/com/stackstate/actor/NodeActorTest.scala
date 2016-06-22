package com.stackstate.actor

import akka.actor.{ActorRef, ActorSystem, Props}
import com.stackstate.event._
import com.stackstate.graph.Node
import com.typesafe.config.ConfigFactory

class NodeActorTest extends ActorTestBase(ActorSystem("test-tasker", ConfigFactory.load("application.conf"))) {

  private val node: Node = Node("A")

  describe("A Node Actor") {
    it("should propagate failure to parent if thresold is breached") {
      val nodeActor: ActorRef = system.actorOf(Props(classOf[NodeActor], node))
      nodeActor ! Major
      expectNoMsg
      nodeActor ! Critical
      expectNoMsg
      nodeActor ! Fatal
      expectMsg(PropagationAlert(node, Warning))
    }

    it("should acknowledge its state when health check event is received"){
      val nodeActor: ActorRef = system.actorOf(Props(classOf[NodeActor], node))
      nodeActor ! Major
      expectNoMsg
      nodeActor ! HealthCheck
      expectMsg(Ok)
    }

    it("should send a failure alert if severity is more than threshold when health check event is received"){
      val nodeActor: ActorRef = system.actorOf(Props(classOf[NodeActor], node))
      nodeActor ! Major
      expectNoMsg
      nodeActor ! Critical
      expectNoMsg
      nodeActor ! Fatal
      expectMsg(PropagationAlert(node, Warning))
      nodeActor ! HealthCheck
      expectMsg(Alert(5,Some("Node is in critical condition, please take remedial actions")))
    }
  }


}

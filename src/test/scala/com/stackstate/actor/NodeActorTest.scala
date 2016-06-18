package com.stackstate.actor

import akka.actor.{ActorRef, ActorSystem, Props}
import com.stackstate.graph.Node
import com.typesafe.config.ConfigFactory

class NodeActorTest extends ActorTestBase(ActorSystem("test-tasker", ConfigFactory.load("test.conf"))) {

  private val nodeActor: ActorRef = system.actorOf(Props(classOf[NodeActor], Node("A")))

  describe("A Node Actor") {
    it("should respond to events") {
      nodeActor ! "Ping"
      expectMsg("Hello")
    }
  }


}

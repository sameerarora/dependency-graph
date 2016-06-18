package com.stackstate.actor

import akka.actor.Actor
import com.stackstate.graph.Node


class NodeActor(node: Node) extends Actor {
  override def receive: Receive = {
    case _ => sender ! "Hello"
  }
}

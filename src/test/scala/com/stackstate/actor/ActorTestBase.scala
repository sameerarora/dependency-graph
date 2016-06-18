package com.stackstate.actor

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
abstract class ActorTestBase(_system: ActorSystem) extends TestKit(ActorSystem("graph-monitor", ConfigFactory.load("test.conf"))) with ImplicitSender with UnitTestBase {
  def this() = this(ActorSystem())

  override def afterAll() {
    TestKit.shutdownActorSystem(system)
  }
}


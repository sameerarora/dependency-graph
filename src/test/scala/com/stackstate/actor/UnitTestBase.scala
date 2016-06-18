package com.stackstate.actor

import org.scalatest._

trait UnitTestBase extends FunSpecLike with BeforeAndAfterAll with BeforeAndAfterEach with Matchers with GivenWhenThen {

}

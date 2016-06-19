package com.stackstate.config

import com.typesafe.config.{Config, ConfigFactory}


object CommonSettings {

  private val config: Config = ConfigFactory.load("application.conf")

  lazy val graphThreshold = config.getInt("graph.severity.threshold")

  lazy val defaultNodeThreshold = config.getInt("node.severity.threshold")

}

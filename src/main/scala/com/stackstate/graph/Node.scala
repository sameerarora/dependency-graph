package com.stackstate.graph

import com.stackstate.config.CommonSettings

case class Node(label: String, threshold: Int = CommonSettings.defaultNodeThreshold) {
  override def equals(obj: scala.Any): Boolean = obj.asInstanceOf[Node].label.equals(label)

}

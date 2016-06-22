package com.stackstate.graph

import com.stackstate.config.CommonSettings
import com.stackstate.event.Alert

case class Node(label: String, threshold: Int = CommonSettings.defaultNodeThreshold, alerts: List[Alert] = List[Alert]()) {
  override def equals(obj: scala.Any): Boolean = {
    val thatNode: Node = obj.asInstanceOf[Node]
    thatNode.label.equals(label) && alerts.size == thatNode.alerts.size
  }

  def update(threshold: Int, alerts: List[Alert]): Node = Node(label, threshold, this.alerts ++ alerts)

}

package com.stackstate.graph

import com.stackstate.actor.FailureEvent

case class Node(label: String, alerts: List[FailureEvent] = List[FailureEvent]()) {
  override def equals(obj: scala.Any): Boolean = obj.asInstanceOf[Node].label.equals(label)

  def severityScore: Int = alerts.foldRight(0)((a, b) => a.severity + b)

}

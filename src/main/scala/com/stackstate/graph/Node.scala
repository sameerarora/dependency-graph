package com.stackstate.graph

case class Node(label: String, threshold: Int = 10) {
  override def equals(obj: scala.Any): Boolean = obj.asInstanceOf[Node].label.equals(label)

}

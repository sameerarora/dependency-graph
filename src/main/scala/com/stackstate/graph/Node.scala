package com.stackstate.graph

case class Node(label: String) {
  override def equals(obj: scala.Any): Boolean = obj.asInstanceOf[Node].label.equals(label)

}

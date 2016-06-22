package com.stackstate.graph


case class Edge(source: Node, destination: Node) {
  def update(updatedNode: Node): Edge = {
    updatedNode.label match {
      case this.source.label => Edge(updatedNode, destination)
      case this.destination.label => Edge(source, updatedNode)
    }
  }

  def contains(node: Node): Boolean = source.label == node.label || destination.label == node.label
}

object Edge {
  def apply(source: String, dest: String): Edge = Edge(Node(source), Node(dest))
}

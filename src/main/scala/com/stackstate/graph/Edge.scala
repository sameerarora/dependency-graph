package com.stackstate.graph


case class Edge(source: Node, destination: Node) {

}

object Edge {
  def apply(source: String, dest: String):Edge = Edge(Node(source), Node(dest))
}

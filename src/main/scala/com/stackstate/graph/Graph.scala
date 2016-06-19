package com.stackstate.graph

case class Graph(edges: Set[Edge] = Set[Edge](), root: Option[Node] = None) {

  def hasCycle: Boolean = detectCycles()

  //Node that has all incoming dependencies and no outgoing dependencies
  lazy val centralNode: Node = vertices().filter(v => dependentsOf(v).isEmpty).head

  def dependenciesOf(node: Node): Set[Node] = {
    edges.filter(x => x.destination.equals(node)).map(e => e.source)
  }

  def dependentsOf(node: Node): Set[Node] = {
    edges.filter(x => x.source.equals(node)).map(e => e.destination)
  }

  private def empty: Set[Node] = Set[Node]()

  def vertices(nodes: Set[Node] = empty, edges: List[Edge] = edges.toList): Set[Node] = {
    edges.toList match {
      case Nil => root match {
        case Some(x) => Set[Node](x)
        case None => nodes
      }
      case x :: xs => vertices(nodes ++ Set(x.source, x.destination), xs)
    }
  }

  def detectCycles(whiteSet: Set[Node] = vertices(), graySet: Set[Node] = empty, blackSet: Set[Node] = empty): Boolean = {
    whiteSet.toList match {
      case Nil => false
      case head :: tail =>
        dependentsOf(head).foreach(x => {
          if ((graySet + head).contains(x)) return true
          if (detectCycles(tail.toSet, graySet + head, blackSet)) return true
        })
        false
    }
  }

  def addEdge(edge: Edge) = Graph(edges + edge)

  def display() {
    vertices().toList.foreach(v => {
      println(v.label + " ->| ")
      dependentsOf(v).foreach(d => println("    |--> " + d.label + " "))
      println()
    })

  }

}

object Graph {
  def apply() = new Graph()

  def apply(edges: Set[Edge]) = new Graph(edges)

  def apply(root: Node) = new Graph(Set[Edge](), Some(root))
}
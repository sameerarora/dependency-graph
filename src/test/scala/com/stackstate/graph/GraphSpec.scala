package com.stackstate.graph

import com.stackstate.event.Alert
import org.scalatest._

class GraphSpec extends FlatSpec with Matchers {

  val graph: Graph = Graph(Set[Edge](Edge("A", "B"), Edge("A", "D"), Edge("B", "C"), Edge("C", "D"), Edge("D", "B")))

  "A Graph " should "be able to add Edges" in {
    val updatedGraph: Graph = Graph().addEdge(Edge(Node("A"), Node("B")))
    updatedGraph.edges.size shouldEqual 1
  }

  "It " should "be possible to create a Graph with just one Node" in {
    val graph: Graph = Graph(Node("A"))
    graph.vertices().size shouldEqual 1
    graph.vertices().contains(Node("A")) shouldBe true
    graph.edges.size shouldBe 0
  }

  "A Graph initialized with no edges " should " expand itself when edges are introduced" in {
    val graph: Graph = Graph(Node("A"))
    graph.vertices().size shouldEqual 1
    val updatedGraph: Graph = graph.addEdge(Edge("B", "A"))
    updatedGraph.vertices().size shouldEqual 2
    updatedGraph.edges.size shouldEqual 1
  }

  "A Graph " should "be able to return all its vertices" in {
    val vertices: Set[Node] = graph.vertices()
    vertices.size shouldEqual 4
    vertices.contains(Node("A")) shouldBe true
    vertices.contains(Node("B")) shouldBe true
    vertices.contains(Node("C")) shouldBe true
    vertices.contains(Node("D")) shouldBe true
  }

  "A Graph " should "List all dependencies of a node" in {
    val dependencies: Set[Node] = graph.dependenciesOf(Node("D"))
    dependencies.size shouldEqual 2
    dependencies.contains(Node("A")) shouldBe true
    dependencies.contains(Node("C")) shouldBe true
  }

  "A Graph " should "List all dependent nodes of a node" in {
    val dependents: Set[Node] = graph.dependentsOf(Node("D"))
    dependents.size shouldEqual 1
    dependents.contains(Node("B")) shouldBe true
  }

  "A graph " should "be able to detect cycles" in {
    graph.hasCycle shouldBe true
  }

  "A Graph" should "be able update existing Node" in {
    val node: Node = Node("A", 10, List[Alert](Alert(4)))
    val updatedGraph: Graph = graph.updateNode(node)
    val vertices: Set[Node] = updatedGraph.vertices()
    vertices.size shouldEqual graph.vertices().size
    graph.edges.size shouldEqual updatedGraph.edges.size
    vertices.find(p => p.label.equals(node.label)) match {
      case Some(x) => x.alerts.size shouldEqual 1
      case None => throw new AssertionError(s"Expected Node ${node.label} found None")
    }
  }

  "A Graph " should "be able to detect that there are no cycles" in {
    val graph1: Graph = Graph(Set[Edge](Edge("A", "B"), Edge("A", "D"), Edge("B", "C")))
    graph1.hasCycle shouldBe false
  }

  "A Graph " should "be able to determine the root node" in {
    val dependencyGraph: Graph = Graph(Set[Edge](Edge("A", "B"), Edge("A", "D"), Edge("B", "C"), Edge("C", "E"), Edge("F", "D"), Edge("E", "D")))
    dependencyGraph.centralNode shouldEqual Node("D")
  }

}

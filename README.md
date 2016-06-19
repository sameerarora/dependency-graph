Graph-Based-Monitoring-System
=============================

# Implementation

The application defines a deployment topology in the form of a directed graph. The deployment graph consists of various nodes that
may or may not be dependent on each other.
Dependency of node A on node B is defined as an Incoming edge from node B to node A.

The dependency graph may end up with cycles , therefore the graph implementation provides a way to determine if there are any cycles
in the graph and alert the monitoring system to review the deployment topology.

Every node in the graph describes a logical representation of a component in the deployment model. 

Monitoring system is an event based asynchronous system that is capable of handling or forwarding events to each node that is encapsulated
in a Node Actor which receives and responds to the messages.

Each Node defines a severity threshold and each incoming failure event has its own severity which adds up with each incoming failure event.
Once the threshold of Node is breached it starts propagating the events to all the nodes that are dependent on current node. Each propagation adds
a milder severity to the dependent node and too many incoming failure events would keep on adding to the severity state of dependent nodes
and eventually force them to propagate the event to their dependencies if the threshold is breached.

Each time a failure event is handled, the severity state of the overall dependency graph is updated, graph can be configured with an overall
severity threshold and may start sending alerts in the form of email or text message to the concerned team. 

# Technologies

The application is built in Scala and uses Akka framework to build the monitoring system. Build tool used is sbt.
Unit testing is done using specs2 and scala test libraries.
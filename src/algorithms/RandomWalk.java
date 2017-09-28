package algorithms;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import edu.uci.ics.jung.graph.Graph;
import elements.Link;
import elements.NIC;
import elements.Node;

public class RandomWalk {
	private Graph<Node, Link> graph;
	// private int searchCost;
	private int numWalkers;
	private List<Node> resultList;

	private String failReason = null;

	public RandomWalk(Graph<Node, Link> g) {
		this.graph = g;
		this.numWalkers = 1;
		// this.searchCost = 0;

		this.resultList = new ArrayList<Node>();
		// indxr = Indexer.newIndexer(graph, 0);
	}

	public RandomWalk(Graph<Node, Link> g, int numWalkers) {
		this.graph = g;
		this.numWalkers = numWalkers;
		// this.searchCost = 0;
		this.resultList = new ArrayList<Node>();
	}

	public List<Node> searchNetwork(Node fromID, Node toID, int numWalkers) {
		List<Node> resList;
		if (graph == null) {
			failReason = "Network not found !";
			//return false;
		}
		if (numWalkers <= 0) {
			failReason = "Invalid number of walkers !";
			//return false;
		}
		this.numWalkers = numWalkers;
		resList = searchNetwork(fromID, toID);
//		if (isDone) {
//			//return true;
//		} else {
//			//return false;
//		}
		return resList;
	}

	public List<Node> searchNetwork(Node fromID, Node toID) {
		if (this.graph == null) {
			failReason = "Network not found !";
			//return false;
		}
		if (numWalkers <= 0) {
			failReason = "Invalid number of walkers !";
			//return false;
		}
		// graph.
		if (!graph.containsVertex(fromID) || !graph.containsVertex(toID)) {
			System.err.println("graph doesnt contain source or destination vertex!");
			failReason = "graph doesnt contain source or destination vertex!";
			//return false;
		}

		// keeps track of the last node visited by each walker
		Node lastNode[] = new Node[numWalkers];

		// keep track of the current position of each walker
		Node currentPosition[] = new Node[numWalkers];

		// Global knowledge to all the walkers if still searching or not.
		boolean looking = true;
		boolean roundDone;
		// int past, next, currentIndex;
		int next;
		Node currentNode, currentIndex, pastNode;
		// Iterator neighIter;
		Map<Node, NIC> connections;
		// searchCost = 0;
		Random rand = new Random();
		List<List<Node>> pathsList = new ArrayList<List<Node>>();
		// this.resultList.add(fromID);
		// System.out.println("f: " + fromID + " t: " + toID + " numWalkers: " +
		// numWalkers );

		// Start from the source node.
		for (int i = 0; i < numWalkers; i++) {
			lastNode[i] = fromID;
			currentPosition[i] = fromID;
			pathsList.add(new ArrayList<Node>());
		}

		// No need to search if already at the target node.
		for (int i = 0; i < numWalkers; i++) {
			if (currentPosition[i] == toID) {
				looking = false;
				resultList = pathsList.get(0); // set resultList as empty if
												// srcNode = destNode
			}
		}

		// All walkers keep searching until looking=false
		while (looking) {
			// Process each walker
			for (int i = 0; i < numWalkers; i++) {
				roundDone = false;
				currentNode = currentPosition[i];
				pathsList.get(i).add(currentPosition[i]); // set Node for each
															// isolated

				// Process only if a node has more than one neighbor
				// if ( currentNode.numNeighbors() > 1 )
				if (this.graph.getNeighborCount(currentNode) > 1) {
					// chose random number between 0 and neighborCount - 1
					// and skip previous node.

					// next = rand.nextInt( currentNode.numNeighbors() - 1 );
					next = rand.nextInt(this.graph.getNeighborCount(currentNode) - 1);

					connections = currentNode.getConnections();

					// cast this to list
					// Collection<Node> kek =
					// this.graph.getNeighbors(currentNode);
					for (Map.Entry<Node, NIC> entry : connections.entrySet()) {
						// currentIndex = indxr.get(entry.getKey());
						currentIndex = entry.getKey();
						if (next == 0 && currentIndex != lastNode[i] && !roundDone) {
							roundDone = true;
							pastNode = currentPosition[i];
							currentPosition[i] = currentIndex;
							lastNode[i] = pastNode;
							next--;
						} else if (currentIndex != lastNode[i] && !roundDone) {
							next--;
						}
					}
				}
				// else a node has only 1 neighbor
				else if (currentNode.numNeighbors() == 1) {
					// just return to previous node
					connections = currentNode.getConnections();
					for (Map.Entry<Node, NIC> entry : connections.entrySet()) {
						currentIndex = entry.getKey();
						if (currentIndex != currentPosition[i] && !roundDone) {
							pastNode = currentPosition[i];
							currentPosition[i] = currentIndex;
							lastNode[i] = pastNode;
							roundDone = true;
						} else if (currentIndex == currentPosition[i]) {
							failReason = "isolated node in search!!";
							//return false;
						}
					}
				}
				// Node has no neighbors.. Isolated in space !
				else {
					failReason = "isolated node in search!!";
					//return false;
				}
			}

			// check for success and update counter
			for (int i = 0; i < numWalkers; i++) {
				if (currentPosition[i] == toID) {
					pathsList.get(i).add(currentPosition[i]); // set Node for
																// each list in
																// pathsList
					looking = false;
					resultList = pathsList.get(i); // set resultList with the
													// found path
				}
				// searchCost++;
			}

		} // end of while (looking)
//		for (Node x : resultList) {
//			System.out.println(x.getToolTip());
//		}
		return resultList;
	} // end of function searchNetwork

	public List<Link> searchNetwork() {
		// get nodes and edges from graph
		
		//List<Node> x = new ArrayList<Node>();
		
		List<Link> completedAllSccEdges = new ArrayList<Link>();
		
		List<Set<Node>> componentCollector = (List<Set<Node>>) new StronglyConnectedComponents().strongComponentsAsSets(this.graph);
		
		
		for(Set<Node> x : componentCollector){
			System.out.println("SCC");
			for(Node y : x){
				System.out.println(y.getToolTip());
			}
		}
		
		Iterator<Set<Node>> sccItearator = componentCollector.iterator();
		while (sccItearator.hasNext()) {
			Set<Node> nodeComponent = sccItearator.next();
			if(nodeComponent.size() != 1){
				
		//List<Link> graphLinks = new ArrayList<Link>(this.graph.getEdges());
		//List<Node> graphNodes = new ArrayList<Node>(this.graph.getVertices());
		List<Node> graphNodes = new ArrayList<Node>(nodeComponent);
//		List<Node> unconnectedNodes = getUnVertex(graphNodes,this.graph);
//		for (Node x : unconnectedNodes) {
//			//System.out.println("unconnectedNodes: "+x.getToolTip());
//			graphNodes.remove(x);
//			//g.removeVertex(x);
//		}
		Random randx = new Random();
		int randNum = randx.nextInt(graphNodes.size() - 1);
		Node sourceNode = graphNodes.get(randNum);
		
		
		// Lists to track compelted edges and nodes
		List<Node> completedNodes = new ArrayList<Node>();
		List<Link> completedEdges = new ArrayList<Link>();
		completedNodes.add(sourceNode);
		// boolean to check if we finished or not
		boolean looking = true;
		boolean roundDone;
		int next;
		Node currentNode, currentIndex, pastNode, lastNode, currentPosition;
		Map<Node, NIC> connections;
		Random rand = new Random();

		lastNode = sourceNode;
		currentPosition = sourceNode;
		
		while (looking) {
			roundDone = false;
			currentNode = currentPosition;
			// Process only if a node has more than one neighbor
			if (this.graph.getNeighborCount(currentNode) > 1) {
				next = rand.nextInt(this.graph.getNeighborCount(currentNode) - 1);
				connections = currentNode.getConnections();
				for (Map.Entry<Node, NIC> entry : connections.entrySet()) {
					currentIndex = entry.getKey();
					if (next == 0 && currentIndex != lastNode && !roundDone) {
						roundDone = true;
						pastNode = currentPosition;
						currentPosition = currentIndex;
						lastNode = pastNode;
//						System.out.println("pastNode: "+pastNode.getToolTip());
//						System.out.println("currentIndex: "+currentPosition.getToolTip());
//						
						//Link currentRevEdge = this.graph.findEdge(currentPosition, pastNode);
						//System.out.println("edge: "+currentEdge);
						//System.out.println("Edge: "+ "("+currentEdge.getNode_left().getToolTip()+", "+currentEdge.getNode_right().getToolTip()+")");
						//Link currentEdge = this.graph.findEdge(pastNode, currentPosition);
						if (!completedNodes.contains(currentPosition)){
							Link currentEdge = this.graph.findEdge(pastNode, currentPosition);
							// check if right is currentPosition if not create a new reverse link
							if(currentEdge.getNode_right() == currentPosition){
								completedEdges.add(currentEdge);
								completedNodes.add(currentPosition);

//								System.out.println("currentPosition: "+currentPosition.getToolTip());
//								System.out.println("Edge: "+ "("+currentEdge.getNode_left().getToolTip()+", "+currentEdge.getNode_right().getToolTip()+")");

							} else { // create new reversed link
								Link tmp = new Link();
								tmp.setNode_left(pastNode);
								tmp.setNode_right(currentPosition);
								completedEdges.add(tmp);
								completedNodes.add(currentPosition);

//								System.out.println("currentPosition: "+currentPosition.getToolTip());
//								System.out.println("Edge: "+ "("+tmp.getNode_left().getToolTip()+", "+tmp.getNode_right().getToolTip()+")");

							}
//							System.out.println("currentPosition: "+currentPosition.getToolTip());
//							System.out.println("Edge: "+ "("+currentEdge.getNode_left().getToolTip()+", "+currentEdge.getNode_right().getToolTip()+")");

						}
//						if (!completedNodes.contains(currentIndex))
//							completedNodes.add(currentIndex);
						next--;
					} else if (currentIndex != lastNode && !roundDone) {
						next--;
					}
				}
			}
			// else a node has only 1 neighbor
			else if (currentNode.numNeighbors() == 1) {
				//System.out.println("CurrentNode: "+ currentNode.getToolTip());
				// just return to previous node
				connections = currentNode.getConnections();
				for (Map.Entry<Node, NIC> entry : connections.entrySet()) {
					currentIndex = entry.getKey();
					if (currentIndex != currentPosition && !roundDone) {
						pastNode = currentPosition;
						currentPosition = currentIndex;
						lastNode = pastNode;
						roundDone = true;
						//System.out.println("currentPosition: "+currentPosition.getToolTip());
						//System.out.println("currentIndex: "+currentIndex.getToolTip());
						//System.out.println("lastNode: "+lastNode.getToolTip());
						//System.out.println("pastNode: "+pastNode.getToolTip());
						//Link currentEdge = this.graph.findEdge(pastNode, currentPosition);
						//System.out.println(currentEdge);
						//System.out.println("Edge: "+ "("+currentEdge.getNode_left().getToolTip()+", "+currentEdge.getNode_right().getToolTip()+")");
//						if (!completedNodes.contains(currentPosition)){
//							Link currentEdge = this.graph.findEdge(pastNode, currentPosition);
//							completedEdges.add(currentEdge);
//							completedNodes.add(currentPosition);
//							System.out.println("currentPosition: "+currentPosition.getToolTip());
//							System.out.println("Edge: "+ "("+currentEdge.getNode_left().getToolTip()+", "+currentEdge.getNode_right().getToolTip()+")");
//
//						}
						
						if (!completedNodes.contains(currentPosition)){
							//System.out.println("No curPos in compNodes");
							Link currentEdge = this.graph.findEdge(pastNode, currentPosition);
							//System.out.println("Edge: "+ "("+currentEdge.getNode_left().getToolTip()+", "+currentEdge.getNode_right().getToolTip()+")");
							// check if right is currentPosition if not create a new reverse link
							if(currentEdge.getNode_right() == currentPosition){
								completedEdges.add(currentEdge);
								completedNodes.add(currentPosition);
//
//								System.out.println("currentPosition: "+currentPosition.getToolTip());
//								System.out.println("Edge: "+ "("+currentEdge.getNode_left().getToolTip()+", "+currentEdge.getNode_right().getToolTip()+")");

							} else { // create new reversed link
								Link tmp = new Link();
								tmp.setNode_left(pastNode);
								tmp.setNode_right(currentPosition);
								completedEdges.add(tmp);
								completedNodes.add(currentPosition);

//								System.out.println("currentPosition: "+currentPosition.getToolTip());
//								System.out.println("Edge: "+ "("+tmp.getNode_left().getToolTip()+", "+tmp.getNode_right().getToolTip()+")");

							}
					} else if (currentIndex == currentPosition) {
						failReason = "isolated node in search!!";
						//completedNodes.add(currentIndex);
						//looking = false;
//						System.out.println(failReason);
						// return false;
					}
				}
				}// end for
			}
			// Node has no neighbors.. Isolated in space !
			else {
				failReason = "isolated node in search!!";
				//completedNodes.add(currentNode);
//				System.out.println(failReason);
				// return false;
			}
			
			// check for success and update counter
//			System.out.print("graphNodes:");
//			for (Node x : graphNodes) {
//				System.out.print(" " +x.getToolTip());
//			}
//			System.out.println();
//			System.out.print("completedNodes");
//			for (Node x : completedNodes) {
//				System.out.print(" " +x.getToolTip());
//			
//			}
//			System.out.println();
//			System.out.print("completedNodes");
//			for (Link x : completedEdges) {
//				System.out.println("Edge: "+ "("+x.getNode_left().getToolTip()+", "+x.getNode_right().getToolTip()+")");
//			
//			}
//			System.out.println();
//			System.out.println("Edge: "+ "("+currentEdge.getNode_left().getToolTip()+", "+currentEdge.getNode_right().getToolTip()+")");
			System.out.print("completedNodes");
			for (Node x : completedNodes) {
				System.out.print(" " +x.getToolTip());
			
			}
			System.out.println();
			System.out.print("graphNodes");
			for (Node x : graphNodes) {
				System.out.print(" " +x.getToolTip());
			
			}
			System.out.println();
			if (listEqualsNoOrder(graphNodes, completedNodes)) {
				looking = false;
			}
//			System.out.println("kKEK");
		}
		for(Link x : completedEdges){
			completedAllSccEdges.add(x);
		}
		}
		}
		return completedAllSccEdges;
	}

	private <T> boolean listEqualsNoOrder(List<T> l1, List<T> l2) {
		final Set<T> s1 = new HashSet<>(l1);
		final Set<T> s2 = new HashSet<>(l2);

		return s1.equals(s2);
	}

	/**
	 * Get the number of walkers used for search
	 * 
	 * @return numWalkers
	 */
	public int getNumWalkers() {
		return numWalkers;
	}

	/**
	 * Get the reason because of which modelig failed
	 * 
	 * @return reason
	 */
	public String getFailReason() {
		return failReason;
	}
	
	private List<Node> getUnVertex(List<Node> nodesList, Graph<Node,Link> g){
		List<Node> nodes = new ArrayList<Node>();
		for(Node x : nodesList){
			if(g.inDegree(x) == 0)
				nodes.add(x);
		}
		
		
		
		return nodes;
	}
}

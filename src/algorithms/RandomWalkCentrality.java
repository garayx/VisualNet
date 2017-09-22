package algorithms;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.uci.ics.jung.graph.Graph;
import elements.Link;
import elements.Node;

public class RandomWalkCentrality {
	private Map<Node, Double> nodesRWCC = new HashMap<Node, Double>();
	private Node highestCCnode = null;
	private double highestCCscore = 0.0;
	public RandomWalkCentrality(Graph<Node,Link> g){
		//Map<Node, Double> nodesRWCC = new HashMap<Node, Double>();
		//Graph<Node,Link> copyGraph = g;
		Iterator<Node> nodeItearator = g.getVertices().iterator();
		// For each node in g we create randomWalks to all other nodes
		 while(nodeItearator.hasNext()){
			 double srcNodeWeight = 0.0;
			 Node sourceNode = nodeItearator.next();
			 Iterator<Node> nodeDestItearator = g.getVertices().iterator();
			 // For each node in g which != destNode we get a path: RandomWalk(sourceNode,destNode)
			 while(nodeDestItearator.hasNext()){
				 Node destNode = nodeDestItearator.next();
				 if(destNode != sourceNode){
					 List<Node> nodesList;
					 nodesList = new RandomWalk(g).searchNetwork(sourceNode, destNode, 5);
					 // each path we send to func that calcs weight of that path
					 double weight = getWeightFromPath(g,nodesList);
					 // make sum of all pathes weights
					 srcNodeWeight += weight;
				 }
			 }
			 double rwcc = (g.getVertexCount() - 1) / srcNodeWeight;
			 if(rwcc > highestCCscore){
				 highestCCscore = rwcc;
				 highestCCnode = sourceNode;
			 }
			 nodesRWCC.put(sourceNode, rwcc);
		 }
		 
		 
		 
		 
	}
	/*
	 * gets sum of weights of the edges in path
	 */
	private double getWeightFromPath(Graph<Node,Link> g, List<Node> nodeList){
		double weight=0.0;
		
//		for(Node x : nodeList){
//			System.out.print(x.getToolTip() + " ");
//		}
		//System.out.println();
		for(int i=0; i<nodeList.size()-1;i++){
			Node leftNode = nodeList.get(i);
			Node rightNode = nodeList.get(i+1);
			//System.out.print(g.findEdge(leftNode, rightNode).toString() + " ");
			weight += g.findEdge(leftNode, rightNode).getCapacity();
		}
		//System.out.println("Total Weight: "+ weight);
		return weight;
	}
	
	
	public Map<Node, Double> getNodeCC(){ return this.nodesRWCC; }
	public Node getNodeHighestCC(){ return this.highestCCnode; }
	public double getScoreHighestCC(){ return this.highestCCscore; }
}

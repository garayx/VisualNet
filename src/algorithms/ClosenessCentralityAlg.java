package algorithms;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import edu.uci.ics.jung.graph.Graph;
import elements.Link;
import elements.Node;


// Capacity given as weight
public class ClosenessCentralityAlg {
	private Map<Node, Double> nodeCC = new HashMap<Node, Double>();
	private Node highestCCnode = null;
	private double highestCCscore = 0.0;
	protected static Transformer<Link, Double> weights = new Transformer<Link, Double>() {
		public Double transform(Link link) {
			return (double) link.getCapacity();
		}
	};
	public ClosenessCentralityAlg(Graph<Node, Link> g){
		//weighted
		ClosenessCentrality<Node, Link> cc = new ClosenessCentrality<Node, Link>(g, weights);
		//unweighted
		//ClosenessCentrality<Node, Link> cc = new ClosenessCentrality<Node, Link>(g);
		 Iterator<Node> nodeItearator = g.getVertices().iterator();
		 
		 //double highestCCscore = 0.0;
		 //Node highestCCnode = null;
		 
		 while(nodeItearator.hasNext()){
			 Node node = nodeItearator.next();
			 if(cc.getVertexScore(node) > highestCCscore){
				 highestCCscore = cc.getVertexScore(node);
				 highestCCnode = node;
			 }
			 nodeCC.put(node, cc.getVertexScore(node));
			 node.setCCScore(cc.getVertexScore(node));
		 }
//		 System.out.println("Node ClosenessCentrality:");
//		 for (Map.Entry<Node, Double> entry : nodeCC.entrySet()) {
//			    String key = entry.getKey().getToolTip();
//			    double value = entry.getValue();
//			    System.out.println(key + "\t" + "CC:" +"\t" + value);
//			}
//		 System.out.println("Highest Closeness Centrality Node:");
//		 System.out.println(highestCCnode.getToolTip() + "\tCC Score:\t"+ highestCCscore);
		
	}
	
	// public methods
	public Map<Node, Double> getNodeCC(){ return this.nodeCC; }
	public Node getNodeHighestCC(){ return this.highestCCnode; }
	public double getScoreHighestCC(){ return this.highestCCscore; }
	
}

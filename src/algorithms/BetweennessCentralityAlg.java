package algorithms;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
import edu.uci.ics.jung.graph.Graph;
import elements.Link;
import elements.Node;

public class BetweennessCentralityAlg {
	private Map<Node, Double> nodeBC = null;
	private Map<Link, Double> linkBC = null;
	private double nodeAvgBC;
	private double linkAvgBC;

	//public BetweennessCentralityAlg(Graph<Node, Link> g, boolean withWeights) {
	public BetweennessCentralityAlg(Graph<Node, Link> g) {
		nodeBC = new HashMap<Node, Double>();
		linkBC = new HashMap<Link, Double>();
		nodeAvgBC = 0.0;
		linkAvgBC = 0.0;
		
		BetweennessCentrality<Node, Link> ranker = new BetweennessCentrality<Node, Link>(g);
		ranker.setRemoveRankScoresOnFinalize(false);
//		if(withWeights){
//			System.out.println("WITH WEIGHTS");
//			ranker.setEdgeWeights(getWeights(g));
//		}
		ranker.evaluate();

		Iterator<Node> nodeItearator = g.getVertices().iterator();
		while (nodeItearator.hasNext()) {
			Node tmpnode = nodeItearator.next();
			nodeAvgBC += ranker.getVertexRankScore(tmpnode);
			nodeBC.put(tmpnode, ranker.getVertexRankScore(tmpnode));
		}
		Iterator<Link> linkItearator = g.getEdges().iterator();
		while (linkItearator.hasNext()) {
			Link link = linkItearator.next();
			linkAvgBC += ranker.getEdgeRankScore(link);
			linkBC.put(link, ranker.getEdgeRankScore(link));
		}

		nodeAvgBC /= g.getVertexCount();
		linkAvgBC /= g.getEdgeCount();
//		System.out.println("Node betweencentrality:");
//		for (Map.Entry<Node, Double> entry : nodeBC.entrySet()) {
//			String key = entry.getKey().getToolTip();
//			double value = entry.getValue();
//			System.out.println(key + "\t" + "BC:" + "\t" + value);
//		}
//		System.out.println("Edge betweencentrality:");
//		for (Map.Entry<Link, Double> entry : linkBC.entrySet()) {
//			String key = entry.getKey().toString();
//			double value = entry.getValue();
//			System.out.println(key + "\t" + "BC:" + "\t" + value);
//		}
//		System.out.println("Nodes average betweencentrality: " + nodeAvgBC);
//		System.out.println("Edges average betweencentrality: " + linkAvgBC);
	}
	// public methods
	public Map<Node, Double> getNodeBC(){ return this.nodeBC; }
	public Map<Link, Double> getEdgeBC(){ return this.linkBC; }
	public double getNodeAvgBC(){ return this.nodeAvgBC; }
	public double getEdgeAvgBC(){ return this.linkAvgBC; }
}

package algorithms;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
import edu.uci.ics.jung.graph.Graph;
import elements.Link;
import elements.Node;

public class BetweennessCentralityAlg {
	private Map<Node, Double> nodeBC = new HashMap<Node, Double>();
	private Map<Link, Double> linkBC = new HashMap<Link, Double>();


	//public BetweennessCentralityAlg(Graph<Node, Link> g, boolean withWeights) {
	public BetweennessCentralityAlg(Graph<Node, Link> g) {
		BetweennessCentrality<Node, Link> ranker = new BetweennessCentrality<Node, Link>(g);
		ranker.setRemoveRankScoresOnFinalize(false);
//		if(withWeights){
//			System.out.println("WITH WEIGHTS");
//			ranker.setEdgeWeights(getWeights(g));
//		}
		ranker.evaluate();
		// TODO create popup window with information
		double nodeAvgBC = 0.0;
		double linkAvgBC = 0.0;
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
		System.out.println("Node betweencentrality:");
		for (Map.Entry<Node, Double> entry : nodeBC.entrySet()) {
			String key = entry.getKey().getToolTip();
			double value = entry.getValue();
			System.out.println(key + "\t" + "BC:" + "\t" + value);
		}
		System.out.println("Edge betweencentrality:");
		for (Map.Entry<Link, Double> entry : linkBC.entrySet()) {
			String key = entry.getKey().toString();
			double value = entry.getValue();
			System.out.println(key + "\t" + "BC:" + "\t" + value);
		}
		System.out.println("Nodes average betweencentrality: " + nodeAvgBC);
		System.out.println("Edges average betweencentrality: " + linkAvgBC);
	}

//	private Map<Link, Number> getWeights(Graph<Node, Link> g) {
//		// g.getEdges().
//		Map<Link, Number> resultsMap = new HashMap<Link, Number>();
//		Iterator<Link> linkItearator = g.getEdges().iterator();
//		while (linkItearator.hasNext()) {
//			Link edge = linkItearator.next();
//			resultsMap.put(edge, edge.getCapacity());
//		}
//		return resultsMap;
//	}
}

package algorithms;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.scoring.EigenvectorCentrality;
import edu.uci.ics.jung.graph.Graph;
import elements.Link;
import elements.Node;

public class EigenvectorCentralityAlg {
	private Map<Node, Double> nodeEC = new HashMap<Node, Double>();
	protected static Transformer<Link, Double> weights = new Transformer<Link, Double>() {
		public Double transform(Link link) {
			return (double) link.getCapacity();
		}	
		};
	public EigenvectorCentralityAlg(Graph<Node, Link> g, boolean isWeighted) {
		EigenvectorCentrality<Node, Link> ec = new EigenvectorCentrality<Node, Link>(g);
		if(isWeighted){
		ec.setEdgeWeights(weights);
		}
		//ec.acceptDisconnectedGraph(true);
		ec.evaluate();
		Iterator<Node> nodeItearator = g.getVertices().iterator();
		while (nodeItearator.hasNext()) {
			Node tmpnode = nodeItearator.next();
			//nodeAvgEC += ec.getVertexRankScore(tmpnode);
			nodeEC.put(tmpnode, ec.getVertexScore(tmpnode));
		}
		System.out.println("isWeighted: "+isWeighted);
		System.out.println("Node Eigenvector Centrality:");
		
		for (Map.Entry<Node, Double> entry : nodeEC.entrySet()) {
			String key = entry.getKey().getToolTip();
			double value = entry.getValue();
			System.out.println(key + "\t" + "EC:" + "\t" + value);
		}
	}
}

package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.Graph;
import elements.Link;
import elements.Node;

public class ShortestPath {
	public ShortestPath() {
	}

	public static List<Node> withoutWeights(Graph<Node, Link> g,
			Node src, Node dest) {
		DijkstraShortestPath<Node, Link> alg = new DijkstraShortestPath<Node, Link>(g);
		List<Link> l_edges = alg.getPath(src, dest);
//		// for test
//		Map<Node, Number> distMap;
//		distMap = alg.getDistanceMap(src);
//		System.out.println("(unweighted) Distance Map from source node");
//		for (Map.Entry<Node, Number> entry : distMap.entrySet()) {
//			System.out.println("Node: " + entry.getKey().getToolTip() + "\tValue: " + entry.getValue().toString());
//		}
//		
		
		
		
		
		
		if (l_edges.size() == 0)
			return null;
		
		List<Node> l_nodes = new ArrayList<Node>();
		Node v_prev, v;
		l_nodes.add(src);
		v = src;
		// int i=1;
		for (Link edge_num : l_edges) {
			v_prev = v;
			v = g.getOpposite(v_prev, edge_num);
			l_nodes.add(v);
		}
		return l_nodes;
	}

	public static List<Node> withWeights(Graph<Node, Link> g,
			Node src,
			Node dest){
		// use length of segments to weight
	       Transformer<Link, Number> capacityTransformer = new Transformer<Link,Number>() {
	       	public Number transform(Link link) {
	       	return link.getCapacity();
	       	}
	       	};
        DijkstraShortestPath<Node, Link> alg = new DijkstraShortestPath<Node, Link>(g, capacityTransformer);
        List<Link> l_edges = alg.getPath(src, dest); 
        
//		// for test
//		Map<Node, Number> distMap;
//		distMap = alg.getDistanceMap(src);
//		System.out.println("(weighted) Distance Map from source node");
//		for (Map.Entry<Node, Number> entry : distMap.entrySet()) {
//			System.out.println("Node: " + entry.getKey().getToolTip() + "\tValue: " + entry.getValue().toString());
//		}
        
        
        
        if(l_edges.size() == 0) 
            return null;
        List<Node> l_nodes = new ArrayList<Node>();
        Node v_prev, v; 
        l_nodes.add(src);
        v = src; 
        for(Link edge_num : l_edges) { 
            v_prev = v; 
            v = g.getOpposite(v_prev, edge_num); 
            l_nodes.add(v); 
        } 
        return l_nodes;
	}
}

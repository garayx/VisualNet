package algorithms;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.Graph;
import elements.Link;
import elements.Node;

public class ShortestPath {
	// private final static Node[] NULL_NODE_ARRAY = new Node[0];

	public ShortestPath() {
	}

	public static List<Node> withoutWeights(Graph<Node, Link> g,
			// DijkstraShortestPath<Node, Link> alg,
			Node src, Node dest) {

		// if(!g.containsVertex(src)) {
		// System.out.println("Warning: (PathSearcher.getShortestPath) graph has
		// no src vertex=" + src);
		// return NULL_NODE_ARRAY;
		// }
		// if(!g.containsVertex(dest)) {
		// System.out.println("Warning: (PathSearcher.getShortestPath) graph has
		// no src vertex=" + dest);
		// return NULL_NODE_ARRAY;
		// }
		DijkstraShortestPath<Node, Link> alg = new DijkstraShortestPath<Node, Link>(g);
		List<Link> l_edges = alg.getPath(src, dest);
		if (l_edges.size() == 0)
			return null;

		// Node[] l_nodes = new Node[l_edges.size() + 1];
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
			//DijkstraShortestPath<Node, Link> alg, 
			Node src,
			Node dest){
		//Transformer<Link, Number> weights = 
//		g.getEdges().stream().forEach(edge -> {
//			edge.getCapacity();
//		});;
		// use length of segments to weight
	       Transformer<Link, Number> capacityTransformer = new Transformer<Link,Number>() {
	       	public Number transform(Link link) {
	       	return link.getCapacity();
	       	}
	       	};
        DijkstraShortestPath<Node, Link> alg = new DijkstraShortestPath<Node, Link>(g, capacityTransformer);
        List<Link> l_edges = alg.getPath(src, dest); 
        if(l_edges.size() == 0) 
            return null;
        
        
        //Node[] l_nodes = new Node[l_edges.size() + 1];
        List<Node> l_nodes = new ArrayList<Node>();
        Node v_prev, v; 
        l_nodes.add(src);
        v = src; 
        //int i=1; 
        for(Link edge_num : l_edges) { 
            v_prev = v; 
            v = g.getOpposite(v_prev, edge_num); 
            l_nodes.add(v); 
        } 
        return l_nodes;
	}
}

package algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import elements.Node;
import elements.Link;

public final class StronglyConnectedComponents { 
    
    /**
     * Computes strongly connected components. 
     *  
     * @param g a graph 
     * @param <V> vertex type 
     * @param <E> edge type 
     * @return a collection of strongly connected components 
     */ 
	public static boolean isStronglyConnected = false;
    public static Collection<Set<Node>> strongComponentsAsSets(Graph<Node, Link> g) { 
        AtomicInteger index = new AtomicInteger(0); 
        Stack<Node> vertexStack = new Stack<Node>(); 
        Map<Node, Integer> vIndex =  new HashMap<Node, Integer>();
        Map<Node, Integer> vLowlink =  new HashMap<Node, Integer>();
        List<Set<Node>> componentCollector = new ArrayList<Set<Node>>(); 
        for (Node vertex : g.getVertices()) { 
            if (!vIndex.containsKey(vertex)) { 
                tarjan(vertex, g, index, vertexStack, vIndex, vLowlink, componentCollector); 
            } 
        } 
        if(componentCollector.size() == 1){
        	isStronglyConnected = true;
        } else {
        	isStronglyConnected = false;
        }
        return componentCollector; 
    } 
     
    private static void tarjan(Node currentVertex, Graph<Node, Link> g, AtomicInteger index, Stack<Node> vertexStack, 
            Map<Node, Integer> vIndex, Map<Node, Integer> vLowlink, List<Set<Node>> componentCollector) { 
        vIndex.put(currentVertex, index.get()); 
        vLowlink.put(currentVertex, index.get()); 
        index.incrementAndGet(); 
        vertexStack.push(currentVertex); 
        for (Node successor : g.getSuccessors(currentVertex)) { 
            processSuccessor(currentVertex, g, index, vertexStack, vIndex, vLowlink, componentCollector, successor); 
        } 
        if (vLowlink.get(currentVertex).equals(vIndex.get(currentVertex))) { 
            componentCollector.add(extractNewComponent(currentVertex, vertexStack)); 
        } 
    } 
 
    private static void processSuccessor(Node currentVertex, Graph<Node, Link> g, AtomicInteger index, 
            Stack<Node> vertexStack, Map<Node, Integer> vIndex, Map<Node, Integer> vLowlink, List<Set<Node>> componentCollector, 
            Node successor) { 
        if (!vIndex.containsKey(successor)) { 
            tarjan(successor, g, index, vertexStack, vIndex, vLowlink, componentCollector); 
            vLowlink.put(currentVertex, Math.min(vLowlink.get(currentVertex), vLowlink.get(successor))); 
        } else if (vertexStack.contains(successor)) { 
            vLowlink.put(currentVertex, Math.min(vLowlink.get(currentVertex), vIndex.get(successor))); 
        } 
    } 
 
    private static Set<Node> extractNewComponent(Node currentVertex, Stack<Node> vertexStack) { 
        Set<Node> component = new HashSet<Node>(); 
        Node vertex; 
        do { 
            vertex = vertexStack.pop(); 
            component.add(vertex); 
        } while (currentVertex != vertex); 
        return component; 
    } 
     
    public StronglyConnectedComponents() { 
        // Don't instantiate 
    } 
}
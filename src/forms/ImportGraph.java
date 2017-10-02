package forms;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import elements.Controller;
import elements.Host;
import elements.Link;
import elements.NIC;
import elements.Node;
import elements.Switch;

public class ImportGraph {
	public ImportGraph(Graph<Node, Link> g, File topologyFile){
		ObjectMapper mapper = new ObjectMapper();

		HashMap<String, ArrayList> topology = new HashMap<>();
		
		ArrayList<HashMap> hosts = new ArrayList<>();
		ArrayList<HashMap> switches = new ArrayList<>();
		ArrayList<HashMap> controllers = new ArrayList<>();
		ArrayList<HashMap> linksList = new ArrayList<>();
		ArrayList<HashMap> connectionsList = new ArrayList<>();
		
		try { 
			topology = mapper.readValue(topologyFile, new TypeReference<HashMap<String, ArrayList>>(){});
		} catch (Exception e) {}
		
    	for (Map.Entry<String, ArrayList> entry : topology.entrySet()) {

    		if(entry.getKey() == "controllers"){
    			Iterator<HashMap> controllerIterator = entry.getValue().iterator();
    			while (controllerIterator.hasNext()) {
    				HashMap<String, Object> controllerEntry = controllerIterator.next();
    				Controller c = new Controller();
    				common.CommonData.controllerCount += 1;
    				c.setSid("c" + controllerEntry.get("id"));
    				c.setID((int) controllerEntry.get("id"));
    				((Node) c).setIP((String) controllerEntry.get("ip"));
    				((Node) c).setMAC((String) controllerEntry.get("mac"));
    				g.addVertex(c);
    			}
    		}
    		if(entry.getKey() == "switches"){

    			Iterator<HashMap> switchIterator = entry.getValue().iterator();
    			while (switchIterator.hasNext()) {
    				HashMap<String, Object> switchEntry = switchIterator.next();

    				Switch s = new Switch();
    				common.CommonData.switchCount += 1;
    				s.setSid("s" + switchEntry.get("id"));
    				s.setID((int) switchEntry.get("id"));
    				((Node) s).setIP((String) switchEntry.get("ip"));
    				((Node) s).setMAC((String) switchEntry.get("mac"));
    				g.addVertex(s);
    				
    				
    			}
    		}
    		if(entry.getKey() == "hosts"){

    			Iterator<HashMap> hostIterator = entry.getValue().iterator();
    			while (hostIterator.hasNext()) {
    				HashMap<String, Object> hostEntry = hostIterator.next();

    				
    				
    				Host h = new Host();
    				common.CommonData.hostsCount += 1;
    				h.setSid("h" + hostEntry.get("id"));
    				h.setID((int) hostEntry.get("id"));
    				((Node) h).setIP((String) hostEntry.get("ip"));
    				((Node) h).setMAC((String) hostEntry.get("mac"));
    				g.addVertex(h);
    				
    			}
    		}
    	}
    	// graph vertex collection
    	Collection<Node> vertexCol = g.getVertices();
    	// i run it twice to make sure that ALL nodes are created before adding links and connections
    	for (Map.Entry<String, ArrayList> entry : topology.entrySet()) {
    		if(entry.getKey() == "nodes_connections"){
    			Iterator<HashMap> connectionsIterator = entry.getValue().iterator();
    			while (connectionsIterator.hasNext()) {
    				HashMap<String, Object> connectionsEntry = connectionsIterator.next();
    				
    				String conSrc = (String) connectionsEntry.get("src");
    				String conDest = (String)connectionsEntry.get("dest");
    				int conPort = (Integer) connectionsEntry.get("nicPort");
    				for (Node src : vertexCol) {
    					if(src.getSid().equals(conSrc)){
    						for(Node dest : vertexCol) {
    							if(dest.getSid().equals(conDest)){
    								src.addConnection(dest, conPort);
    							}
    						}
    					}
    				}
    				
    			}
    		}
    		if(entry.getKey() == "links"){
    			Iterator<HashMap> linksIterator = entry.getValue().iterator();
    			while (linksIterator.hasNext()) {
    				HashMap<String, Object> linksEntry = linksIterator.next();
    				String linkLeftNode = (String)linksEntry.get("nodeLeft");
    				String linkRightNode = (String)linksEntry.get("nodeRight");
    				int linkLeftPort = (Integer) linksEntry.get("portLeft");
    				int linkRightPort = (Integer) linksEntry.get("portRight");
    				int linkCapacity = (Integer) linksEntry.get("capacity");
    				
    				for (Node src : vertexCol) {
    					if(src.getSid().equals(linkLeftNode)){
    						for (Node dest : vertexCol) {
    							if(dest.getSid().equals(linkRightNode)){
    								Link l = new Link();
    								l.setNode_left(src);
    								l.setNode_right(dest);
    								l.setPort_left(linkLeftPort);
    								l.setPort_right(linkRightPort);
    								l.setCapacity(linkCapacity);
    								if(src instanceof Controller || dest instanceof Controller){
    									l.setArrowType("controllerEdge");
    								}
    								g.addEdge(l, src, dest, EdgeType.UNDIRECTED);
    							}
    						}
    						
    					}
    				}
    				
    			}
    			
    			
    			
    		}
    	}
		
	}

}

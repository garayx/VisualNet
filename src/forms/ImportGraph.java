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
    		//System.out.println(entry.getKey());
    		//System.out.println(entry.getValue());
    		if(entry.getKey() == "controllers"){
    			//System.out.println();
    			//System.out.println("controllers");
    			//System.out.println(entry.getValue());
    			//System.out.println(entry.getValue().size());
    			Iterator<HashMap> controllerIterator = entry.getValue().iterator();
    			while (controllerIterator.hasNext()) {
    				HashMap<String, Object> controllerEntry = controllerIterator.next();
    				//HashMap kek = controllerIterator.next();
//    				System.out.println("id "+controllerEntry.get("id"));
//    				System.out.println("ip "+controllerEntry.get("ip"));
//    				System.out.println("mac "+controllerEntry.get("mac"));
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
//    			System.out.println();
//    			System.out.println("switches");
    			Iterator<HashMap> switchIterator = entry.getValue().iterator();
    			while (switchIterator.hasNext()) {
    				HashMap<String, Object> switchEntry = switchIterator.next();
//    				System.out.println("id "+switchEntry.get("id"));
//    				System.out.println("ip "+switchEntry.get("ip"));
//    				System.out.println("mac "+switchEntry.get("mac"));
    				
    				
    				Switch s = new Switch();
    				common.CommonData.switchCount += 1;
    				s.setSid("c" + switchEntry.get("id"));
    				s.setID((int) switchEntry.get("id"));
    				((Node) s).setIP((String) switchEntry.get("ip"));
    				((Node) s).setMAC((String) switchEntry.get("mac"));
    				g.addVertex(s);
    				
    				
    			}
    		}
    		if(entry.getKey() == "hosts"){
//    			System.out.println();
//    			System.out.println("hosts");
    			//entry.getValue().sort(c);
    			Iterator<HashMap> hostIterator = entry.getValue().iterator();
    			while (hostIterator.hasNext()) {
    				HashMap<String, Object> hostEntry = hostIterator.next();
//    				System.out.println("id "+hostEntry.get("id"));
//    				System.out.println("ip "+hostEntry.get("ip"));
//    				System.out.println("mac "+hostEntry.get("mac"));
    				
    				
    				Host h = new Host();
    				common.CommonData.hostsCount += 1;
    				h.setSid("c" + hostEntry.get("id"));
    				h.setID((int) hostEntry.get("id"));
    				((Node) h).setIP((String) hostEntry.get("ip"));
    				((Node) h).setMAC((String) hostEntry.get("mac"));
    				g.addVertex(h);
    				
    			}
    		}
    	}
    	
    	//List nodesList;
    	Collection<Node> vertexCol = g.getVertices();
    	System.out.println(vertexCol.size());
    	
    	// i run it twice to make sure that ALL nodes are created before adding links and connections
    	for (Map.Entry<String, ArrayList> entry : topology.entrySet()) {
    		if(entry.getKey() == "nodes_connections"){
    			Iterator<HashMap> connectionsIterator = entry.getValue().iterator();
    			while (connectionsIterator.hasNext()) {
    				HashMap<String, Object> connectionsEntry = connectionsIterator.next();
//    				System.out.println("src "+connectionsEntry.get("src"));
//    				System.out.println("dest "+connectionsEntry.get("dest"));
//    				System.out.println("nicPort "+connectionsEntry.get("nicPort"));
//    				System.out.println("nicMac "+connectionsEntry.get("nicMac"));
//    				System.out.println("nicIp "+connectionsEntry.get("nicIp"));
    				//hostEntry.get("src");
    				for (Node src : vertexCol) {
    					System.out.println(src.getToolTip());
    					if(src instanceof Switch){
    						System.out.println("Switch");
    						Switch srcNew = (Switch) src;
    						//System.out.println(srcNew.getSid()+" == " + connectionsEntry.get("src"));
	    					if(srcNew.getSid() == connectionsEntry.get("src")){
	    						for(Node dest : vertexCol) {
	    							if(dest instanceof Switch){
	    								Switch destNew = (Switch) dest;
		    							if(destNew.getSid() == connectionsEntry.get("dest")){
		    								srcNew.addConnection(destNew, (int) connectionsEntry.get("nicPort"));
		    								System.out.println("addConnection: "+ srcNew + " " + destNew + " Port " + connectionsEntry.get("nicPort"));
		    							}
	    							} else if (dest instanceof Host){
	    								Host destNew = (Host) dest;
		    							if(destNew.getSid() == connectionsEntry.get("dest")){
		    								srcNew.addConnection(destNew, (int) connectionsEntry.get("nicPort"));
		    								System.out.println("addConnection: "+ srcNew + " " + destNew + " Port " + connectionsEntry.get("nicPort"));
		    							}
	    							} else if (dest instanceof Controller){
	    								Controller destNew = (Controller) dest;
		    							if(destNew.getSid() == connectionsEntry.get("dest")){
		    								srcNew.addConnection(destNew, (int) connectionsEntry.get("nicPort"));
		    								System.out.println("addConnection: "+ srcNew + " " + destNew + " Port " + connectionsEntry.get("nicPort"));
		    							}
	    							} else {
	    								System.out.println("WTF");
	    							}
	    						}
	    					}
    					} else if (src instanceof Host){
    						Host srcNew = (Host) src;
    						if(srcNew.getSid() == connectionsEntry.get("src")){
	    						for (Node dest : vertexCol) {
	    							if(dest instanceof Switch){
	    								Switch destNew = (Switch) dest;
		    							if(destNew.getSid() == connectionsEntry.get("dest")){
		    								srcNew.addConnection(destNew, (int) connectionsEntry.get("nicPort"));
		    								System.out.println("addConnection: "+ srcNew + " " + destNew + " Port " + connectionsEntry.get("nicPort"));
		    							}
	    							} else if (dest instanceof Host){
	    								Host destNew = (Host) dest;
		    							if(destNew.getSid() == connectionsEntry.get("dest")){
		    								srcNew.addConnection(destNew, (int) connectionsEntry.get("nicPort"));
		    								System.out.println("addConnection: "+ srcNew + " " + destNew + " Port " + connectionsEntry.get("nicPort"));
		    							}
	    							} else if (dest instanceof Controller){
	    								Controller destNew = (Controller) dest;
		    							if(destNew.getSid() == connectionsEntry.get("dest")){
		    								srcNew.addConnection(destNew, (int) connectionsEntry.get("nicPort"));
		    								System.out.println("addConnection: "+ srcNew + " " + destNew + " Port " + connectionsEntry.get("nicPort"));
		    							}
	    							} else {
	    								System.out.println("WTF");
	    							}
	    						}
	    					}
    					} else if (src instanceof Controller){
    						Controller srcNew = (Controller) src;
    						if(srcNew.getSid() == connectionsEntry.get("src")){
	    						for (Node dest : vertexCol) {
	    							if(dest instanceof Switch){
	    								Switch destNew = (Switch) dest;
		    							if(destNew.getSid() == connectionsEntry.get("dest")){
		    								srcNew.addConnection(destNew, (int) connectionsEntry.get("nicPort"));
		    								System.out.println("addConnection: "+ srcNew + " " + destNew + " Port " + connectionsEntry.get("nicPort"));
		    							}
	    							} else if (dest instanceof Host){
	    								Host destNew = (Host) dest;
		    							if(destNew.getSid() == connectionsEntry.get("dest")){
		    								srcNew.addConnection(destNew, (int) connectionsEntry.get("nicPort"));
		    								System.out.println("addConnection: "+ srcNew + " " + destNew + " Port " + connectionsEntry.get("nicPort"));
		    							}
	    							} else if (dest instanceof Controller){
	    								Controller destNew = (Controller) dest;
		    							if(destNew.getSid() == connectionsEntry.get("dest")){
		    								srcNew.addConnection(destNew, (int) connectionsEntry.get("nicPort"));
		    								System.out.println("addConnection: "+ srcNew + " " + destNew + " Port " + connectionsEntry.get("nicPort"));
		    							}
	    							} else {
	    								System.out.println("WTF");
	    							}
	    						}
	    					}
    					} else {
    						System.out.println("WTF");
    					}
    				}
    				
    			}
    		}
    		if(entry.getKey() == "links"){
    			//System.out.println(entry.getValue());
    			//System.out.println(entry.getValue().size());
    			Iterator<HashMap> linksIterator = entry.getValue().iterator();
    			while (linksIterator.hasNext()) {
    				HashMap<String, Object> linksEntry = linksIterator.next();
    				//hostEntry.get("src");
    				for (Node src : vertexCol) {
    					if(src.getSid() == linksEntry.get("nodeLeft")){
    						for (Node dest : vertexCol) {
    							if(dest.getSid() == linksEntry.get("nodeRight")){
    								Link l = new Link();
    								l.setNode_left(src);
    								l.setNode_right(dest);
    								l.setPort_left((int) linksEntry.get("portLeft"));
    								l.setPort_right((int) linksEntry.get("portRight"));
    								l.setCapacity((int) linksEntry.get("capacity"));
    								g.addEdge(l, src, dest, EdgeType.DIRECTED);
    							}
    						}
    						
    					}
    				}
    				
    			}
    			
    			
    			
    		}
    	}
		
	}

}

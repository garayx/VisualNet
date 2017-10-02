package forms;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.uci.ics.jung.graph.Graph;
import elements.Controller;
import elements.Host;
import elements.Link;
import elements.NIC;
import elements.Node;
import elements.Switch;

public class ExportGraph {
	public ExportGraph(Graph<Node,Link> g){
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, ArrayList> topology = new HashMap<>();
		
		ArrayList<HashMap> hosts = new ArrayList<>();
		ArrayList<HashMap> switches = new ArrayList<>();
		ArrayList<HashMap> controllers = new ArrayList<>();
		ArrayList<HashMap> linksList = new ArrayList<>();
		ArrayList<HashMap> connectionsList = new ArrayList<>();
		
		for (Node node : g.getVertices())
        {
            if (node instanceof Host)
            {
                Host host = (Host) node;

                HashMap<String, Object> hostStruct = new HashMap<>();
                hostStruct.put("id", host.getID());
                hostStruct.put("ip", host.getIP());
                hostStruct.put("mac", host.getMAC());
                
                ArrayList<HashMap> host_connections = new ArrayList<>();
                if (host.getConnections().size() > 0)
                {
                	//HashMap<String, Object> connections_entry = new HashMap<>();
	           		for (Map.Entry<Node, NIC> entry : host.getConnections().entrySet()) {
	           			HashMap<String, Object> connections_entry = new HashMap<>();
	        			//String key = entry.getKey().getToolTip();
	        			NIC nic = entry.getValue();
	        			connections_entry.put("src", host.getSid());
	        			connections_entry.put("dest", entry.getKey().getSid());
	        			connections_entry.put("nicIp", nic.getIp());
	        			connections_entry.put("nicMac", nic.getMac());
	        			connections_entry.put("nicPort", nic.getPort());
	        			host_connections.add(connections_entry);
	        		}
                }

                hosts.add(hostStruct);
                connectionsList.addAll(host_connections);
            }
            if (node instanceof Switch)
            {
                Switch s = (Switch) node;

                HashMap<String, Object> switchStruct = new HashMap<>();
                switchStruct.put("id", s.getID());
                switchStruct.put("ip", s.getIP());
                switchStruct.put("mac", s.getMAC());
                
                ArrayList<HashMap> switch_connections = new ArrayList<>();
                if (s.getConnections().size() > 0)
                {
                	//HashMap<String, Object> connections_entry = new HashMap<>();
	           		for (Map.Entry<Node, NIC> entry : s.getConnections().entrySet()) {
	           			HashMap<String, Object> connections_entry = new HashMap<>();
	        			//String key = entry.getKey().getToolTip();
	        			NIC nic = entry.getValue();
	        			connections_entry.put("src", s.getSid());
	        			connections_entry.put("dest", entry.getKey().getSid());
	        			connections_entry.put("nicIp", nic.getIp());
	        			connections_entry.put("nicMac", nic.getMac());
	        			connections_entry.put("nicPort", nic.getPort());
	        			switch_connections.add(connections_entry);
	        		}
                }

                switches.add(switchStruct);
                connectionsList.addAll(switch_connections);
            }
            if (node instanceof Controller)
            {
            	Controller c = (Controller) node;

                HashMap<String, Object> controllerStruct = new HashMap<>();
                controllerStruct.put("id", c.getID());
                controllerStruct.put("ip", c.getIP());
                controllerStruct.put("mac", c.getMAC());
                
                ArrayList<HashMap> controller_connections = new ArrayList<>();
                if (c.getConnections().size() > 0)
                {
                	//HashMap<String, Object> connections_entry = new HashMap<>();
	           		for (Map.Entry<Node, NIC> entry : c.getConnections().entrySet()) {
	           			HashMap<String, Object> connections_entry = new HashMap<>();
	        			//String key = entry.getKey().getToolTip();
	        			NIC nic = entry.getValue();
	        			connections_entry.put("src", c.getSid());
	        			connections_entry.put("dest", entry.getKey().getSid());
	        			connections_entry.put("nicIp", nic.getIp());
	        			connections_entry.put("nicMac", nic.getMac());
	        			connections_entry.put("nicPort", nic.getPort());
	        			controller_connections.add(connections_entry);
	        		}
                }

                controllers.add(controllerStruct);
                connectionsList.addAll(controller_connections);
            }
        } // end nodes for
		
        for (Link l : g.getEdges())
        {
        	HashMap<String, Object> linkMap = new HashMap<>();
        	linkMap.put("nodeLeft", l.getNode_left().getSid());
        	linkMap.put("nodeRight", l.getNode_right().getSid());
        	linkMap.put("portLeft", l.getPort_left());
        	linkMap.put("portRight", l.getPort_right());
        	linkMap.put("capacity", l.getCapacity());
        	
        	
        	linksList.add(linkMap);
        }
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		topology.put("controllers", controllers);
		topology.put("switches", switches);
		topology.put("hosts", hosts);
		topology.put("nodes_connections", connectionsList);
		topology.put("links", linksList);
		
		
        try
        {
//        	//System.out.println(json);
//        	for (Map.Entry<String, ArrayList> entry : topology.entrySet()) {
//        	//topology.
//        		System.out.println(entry.toString());
//        		System.out.println(entry.getValue());
//        	}
        	
            //String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(topology);
            //System.out.println(json);

            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("topology.json"), topology);
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
		
		
	}
}

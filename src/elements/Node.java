package elements;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import common.CommonData;

public abstract class Node {
	private int id;
	public static Color Color;
	private String mac;
	private String ip;
	private String sid;
	// TODO
	private String type = "Node";
	private Map<Node,NIC> Connections = new HashMap<Node, NIC>();
	private String routeType = "";
	private final int port = 0 + (int)(Math.random() * 65535);
	//private NIC nNic;
	
    public Node()
    {
    	//nNic = new NIC(port);
        this.mac = common.Utils.generateMAC();
        this.ip = common.Utils.getUniqueIP();
    }
	
	public NIC addConnection(Node node) {
		NIC nic = new NIC(node.getMAC(), node.getIP());
		Connections.put(node, nic);
		return nic;
	}
	// TODO
	public void removeConnection(Node x){
//		for(Map.Entry<Node, NIC> entry: Connections.entrySet()){
//			if(entry.getKey().equals(x)){
//				//Connections.remove(entry);
//				boolean kek = Connections.remove(x, entry);
//				System.out.println("removed: "+x.getToolTip()+" "+entry.getValue()+ " " + kek);
//			}
//		}
	    for(Iterator<Map.Entry<Node, NIC>> it = Connections.entrySet().iterator(); it.hasNext(); ) {
	        Map.Entry<Node, NIC> entry = it.next();
	        if(entry.getKey().equals(x)) {
	          it.remove();
	        }
	}
	}
	// TODO
	public Map<Node, NIC> getConnections(){
		return Connections;
	}
	// TODO
	public int numNeighbors(){
		return this.Connections.size();
	}
	
	
	public int getID() {
		// TODO Auto-generated method stub
		return id;
	}
	
	public String getToolTip() {
		// TODO Auto-generated method stub
		String tmp = "";
//		if(this instanceof Switch){
//			tmp = ((Switch)this).getType() + " " + getID();
//		} else
			tmp = this.getType() + " " + getID();
		return tmp;
	}
	public void setColor(Color x){
		this.Color = x;
	}

	public Color getColor(){
		return this.Color;
	}
	public void removeNode(){
		//System.out.println("TODO: RemoveNode");
	}
	// TODO
	public String getType() {
		// TODO Auto-generated method stub
		return this.type;
	}
    public String getSid()
    {
        return sid;
    }
    /**
     * @param sid the sid to set
     */
    public void setSid(String sid)
    {
        this.sid = sid;
    }
    /**
     * method sets route type
     * @param x
     * set routeType
     */
    public void setRouteType(String x)
    {
        this.routeType = x;
    }
    /**
     * method returns routeType
     * @return routeType
     */
    public String getRouteType()
    {
        return routeType;
    }
    public String getIP(){
    	return this.ip;
    }
    public String getMAC(){
    	return this.mac;
    }
    
}

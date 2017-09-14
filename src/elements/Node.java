package elements;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import common.CommonData;

public abstract class Node {
	private int id;
	public static Color Color;
	private String MAC;
	private String sid;
	// TODO
	private String type = "Node";
	private Map<Node,NIC> Connections = new HashMap<Node, NIC>();
	
	
    public Node()
    {
    	
    }
	
	public NIC addConnection(Node node) {
		NIC nic = new NIC("MAC", "IP");
		Connections.put(node, nic);
		return nic;
	}
	// TODO
	public void removeConnection(Node x){
		for(Map.Entry<Node, NIC> entry: Connections.entrySet()){
			if(entry.equals(x))
				Connections.remove(entry);
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
		System.out.println("TODO: RemoveNode");
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
}

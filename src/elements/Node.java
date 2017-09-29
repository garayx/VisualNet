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
	
	private Number bcScore = null;
	private Number ccScore = null;
	private Number rwccScore = null;
	private Number evcScore = null;
	private Number evcwScore = null;
	
	
    public Node()
    {
    	//nNic = new NIC(port);
        this.mac = common.Utils.generateMAC();
        this.ip = common.Utils.getUniqueIP();
    }
	
	public NIC addConnection(Node node) {
		NIC nic = new NIC(node.getMAC(), node.getIP(), Connections.size()+1);
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
    
    
    /**
     * get alg score for each alg method
     * @return
     */
    public double getBCScore(){
    	return (double) this.bcScore;
    }
    public double getCCScore(){
    	return (double) this.ccScore;
    }
    public double getRWCCScore(){
    	return (double) this.rwccScore;
    }
    public double getEVCScore(){
    	return (double) this.evcScore;
    }
    public double getEVCWScore(){
    	return (double) this.evcwScore;
    }
    /**
     * set alg score methods
     * @param num
     */
    public void setBCScore(double num){
    	this.bcScore = num;
    }
    public void setCCScore(double num){
    	this.ccScore = num;
    }
    public void setRWCCScore(double num){
    	this.rwccScore = num;
    }
    public void setEVCScore(double num){
    	this.evcScore = num;
    }
    public void setEVCWScore(double num){
    	this.evcwScore = num;
    }
}

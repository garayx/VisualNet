package elements;

import java.awt.Color;

public class Link {
	private Node nodeLeft;
	private Node nodeRight;
	private int portLeft;
	private int portRight;
	private int capacity=1 + (int)(Math.random() * 10);
	private String routeType = "";
	private String arrowType = "";
	private Number bcScore = null;
	private String orderNum = "";
	
	public void setNode_left(Node x){
		this.nodeLeft = x;
	}
	public void setNode_right(Node x){
		this.nodeRight = x;
	}
	public Node getNode_left(){
		return this.nodeLeft;
	}
	public Node getNode_right(){
		return this.nodeRight;
	}
	public void setPort_left(int x){
		this.portLeft = x;
	}
	public void setPort_right(int x){
		this.portRight = x;
	}
	public int getPort_left(){
		return this.portLeft;
	}
	public int getPort_right(){
		return this.portRight;
	}
	
	public void setOrderNum(String x){
		this.orderNum = x;
	}
	public String getOrderNum(){
		return this.orderNum;
	}
	
	
	
	public void setCapacity(int x){
		this.capacity = x;
	}
	public int getCapacity(){
		return this.capacity;
	}
	public String toString(){
		
		return "("+nodeLeft.getSid()+", " + nodeRight.getSid()+")";
	}
	
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

	
	
	
    public void setArrowType(String x)
    {
        this.arrowType = x;
    }
    public String getArrowType()
    {
        return arrowType;
    }
	
	
	
    public double getBCScore(){
    	return (double) this.bcScore;
    }
    /**
     * set alg score methods
     * @param num
     */
    public void setBCScore(double num){
    	this.bcScore = num;
    }
	
	
	
	
	
}

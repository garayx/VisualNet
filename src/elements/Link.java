package elements;

public class Link {
	private Node nodeLeft;
	private Node nodeRight;
	private int portLeft;
	private int portRight;
	private int capacity=1 + (int)(Math.random() * 10);
	
	
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
	public void setCapacity(int x){
		this.capacity = x;
	}
	public int getCapacity(){
		return this.capacity;
	}
	public String toString(){
		
		return "("+nodeLeft.getSid()+", " + nodeRight.getSid()+")";
	}
}

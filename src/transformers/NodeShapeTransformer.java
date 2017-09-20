package transformers;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import elements.Controller;
import elements.Host;
import elements.Link;
import elements.Switch;

//public class NodeShapeTransformer<Node> implements Transformer< Context<Graph<Node, Link>, Node>,Shape > {
public class NodeShapeTransformer<Node> implements Transformer< Node,Shape > {	
	
	private Shape controllerShape = null;
	private Shape switchShape = null;
	private Shape hostShape = null;
	private Shape defaultShape = null;
	
	public static Ellipse2D.Double sphericalVertex_ = getSphericalVertex(10);
	public static Rectangle squareVertex_ = getRectangleVertex(20, 20);
	
	public NodeShapeTransformer(){
		controllerShape = sphericalVertex_;
		switchShape = sphericalVertex_;
		hostShape = squareVertex_;
		defaultShape = sphericalVertex_;
	}
	
	
	
	
	
	
    public Shape transform(Node context) {
    	//System.out.println("Arrow_Shape_Transform");
		Node v = (Node) context;

    	if (v instanceof Host) {
    		return hostShape;
    	}
    	else if (v instanceof Switch){
    		return switchShape;
    	}
    	else if (v instanceof Controller){
    		return controllerShape;
    	}
		return defaultShape;
    }
	
    
	public static Ellipse2D.Double getSphericalVertex(float radius) {
    	Ellipse2D.Double shape = new Ellipse2D.Double();
    	shape.x = -radius;
    	shape.y = -radius;
    	shape.height = 2*radius;
    	shape.width = 2*radius;
        return shape;
    }
	
	public static Rectangle getRectangleVertex(int w, int h) {
		Rectangle shape = new Rectangle();
		shape.x = -w/2;
		shape.y = -h/2;
		shape.height = h;
		shape.width = w;
    	return shape;
    }
}

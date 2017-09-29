
package transformers;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.Map;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import elements.Controller;
import elements.Host;
import elements.Link;
import elements.Node;
import elements.Switch;

//public class NodeShapeTransformer<Node> implements Transformer< Context<Graph<Node, Link>, Node>,Shape > {
public class NodeAlgShapeTransformer<Node> implements Transformer< Node,Shape > {	
	
//	private Shape controllerShape = null;
//	private Shape switchShape = null;
//	private Shape hostShape = null;
	private Shape defaultShape = null;
	
	public static Ellipse2D.Double sphericalVertex_ = getSphericalVertex(10);
	public static Rectangle squareVertex_ = getRectangleVertex(20, 20);
	
	private double algAvgScore = 0.0;
	private Map<Node, Double> nodeMapAlg = null;
	
	public NodeAlgShapeTransformer(Map<Node, Double> algMap){
		defaultShape = sphericalVertex_;
		nodeMapAlg = algMap;
		
		
		init();
		
	}
	private void init(){
		for (Map.Entry<Node, Double> entry : nodeMapAlg.entrySet()) {
			//String key = entry.getKey().getToolTip();
			double value = entry.getValue();
			// avg
			algAvgScore += value;
		}
		algAvgScore = algAvgScore / nodeMapAlg.size();
	}
	
	
	
	
	
    public Shape transform(Node context) {
    	//System.out.println("Arrow_Shape_Transform");
		Node v = (Node) context;
		double nodeScore = nodeMapAlg.get(v);
		if(nodeScore > 0)
			nodeScore = nodeScore / algAvgScore;
		else
			nodeScore = 1;
    	if (v instanceof Host) {
    		return getRectangleVertex((int) (20*nodeScore), (int) (20*nodeScore));
    	}
    	else if (v instanceof Switch){
    		return getRectangleVertex((int) (20*nodeScore), (int) (20*nodeScore));
    	}
    	else if (v instanceof Controller){
    		return getSphericalVertex((float) (10 * nodeScore));
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

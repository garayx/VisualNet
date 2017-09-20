package transformers;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.util.ArrowFactory;
import elements.Node;
import elements.Link;


public class ArrowShapeTransformer<Node, Link> implements Transformer< Context<Graph<Node, Link>,Link>,Shape > {
	/** Enhancer connections type */
	private String enhancerType_ = null;
	/** Inhibitor connections type */
	private String inhibitorType_ = null;
	/** Dual connections type */
	private String dualType_ = null;
	/** Unknown connections type */
	private String unknownType_ = null;
	private String controllerType_ = null;
	
	
	
	/** Notched arrow */
	public static Shape notchedArrow_ = ArrowFactory.getNotchedArrow(8, 9, 2);
	/** Circular arrow */
	public static Ellipse2D.Double sphericalArraw_ = getSphericalArrow(4);
	/** Square arrow */
	public static Rectangle squareArrow_ = getRectangularArrow(6, 6);
	/** Rectangular arrow */
	public static Rectangle rectangularArrow_ = getRectangularArrow(3, 14);
	
	
	
	/** Arrow of the enhancer connections */
	private Shape enhancerArrow_ = null;
	/** Arrow of the inhibitor connections */
	private Shape inhibitoryArrow_ = null;
	/** Arrow of the dual connections */
	private Shape dualArrow_ = null;
	/** Arrow of the unknown connections */
	private Shape unknownArrow_ = null;
	
	private Shape controllerArrow_ =null;
	
	
	/**
	 * Initialization
	 */
	private void initialize() {
		controllerType_ = "controllerEdge";
		inhibitorType_ = "inhibitorType";
		dualType_ = "dualType";
		unknownType_ = "";
	}
	
	public ArrowShapeTransformer() {
		initialize();
		distinguishConnectionByArrowHead(true);
	}
	
	public ArrowShapeTransformer(boolean distinguishSignedConnections) {
		//System.out.println("Arrow_Shape_Transform");
		initialize();
		distinguishConnectionByArrowHead(distinguishSignedConnections);
	}
	
	public ArrowShapeTransformer(Shape enhancerArrow, Shape inhibitorArrow, Shape dualArrow, Shape unknownArrow, Shape controllerArrow) {
		initialize();
		enhancerArrow_ = enhancerArrow;
		inhibitoryArrow_ = inhibitorArrow;
		dualArrow_ = dualArrow;
		unknownArrow_ = unknownArrow;
		controllerArrow_ = controllerArrow;
	}
	
	/**
	 * Create a circular arrow.
	 * @param radius
	 * @return The circular arrow
	 */
	public static Ellipse2D.Double getSphericalArrow(float radius) {
    	Ellipse2D.Double arrow = new Ellipse2D.Double();
    	arrow.x = -2*radius;
    	arrow.y = -radius;
    	arrow.height = 2*radius;
    	arrow.width = 2*radius;
        return arrow;
    }
	
	/**
	 * Create a rectangular arrow.
	 * @param w
	 * @param h
	 * @return The rectangular arrow
	 */
    public static Rectangle getRectangularArrow(int w, int h) {
    	Rectangle arrow = new Rectangle();
    	arrow.x = -w;
    	arrow.y = -h/2;
    	arrow.height = h;
    	arrow.width = w;
    	return arrow;
    }
    /**
     * Return the arrow that should be used for each edge of the network.
     */
    public Shape transform(Context<Graph<Node, Link>, Link> context) {
    	//System.out.println("Arrow_Shape_Transform");
		Link e = (Link) context.element;
		String type = ((elements.Link) e).getArrowType();

		if (type == controllerType_)
			return controllerArrow_;
		else if (type == enhancerType_)
			return enhancerArrow_;
		else if (type.equals(inhibitorType_))
			return inhibitoryArrow_;
		else if (type.equals(dualType_))
			return dualArrow_;
		else if (type.equals(unknownType_))
			return unknownArrow_;
		else
			return unknownArrow_;
	}
	/**
	 * Set if the transformer should distinguish each signed connection by its arrow head.
	 * @param yes
	 */
	public void distinguishConnectionByArrowHead(boolean yes) {
		if (yes) {
			enhancerArrow_ = notchedArrow_;
			inhibitoryArrow_ = rectangularArrow_;
			dualArrow_ = squareArrow_;
			unknownArrow_ = sphericalArraw_;
			controllerArrow_ = rectangularArrow_;
		}
		else {
			enhancerArrow_ = notchedArrow_;
			inhibitoryArrow_ = notchedArrow_;
			dualArrow_ = notchedArrow_;
			unknownArrow_ = notchedArrow_;
			controllerArrow_ = notchedArrow_;
		}
	}
	
	public void setEnhancerArrow(Shape shape) { enhancerArrow_ = shape; }
	public void setInhibitoryArrow(Shape shape) { inhibitoryArrow_ = shape; }
	public void setDualArrow(Shape shape) { dualArrow_ = shape; }
	public void setUnknownArrow(Shape shape) { unknownArrow_ = shape; }
	
	public Shape getEnhancerArrow() { return enhancerArrow_; }
	public Shape getInhibitoryArrow() { return inhibitoryArrow_; }
	public Shape getDualArrow() { return dualArrow_; }
	public Shape getUnknownArrow() { return unknownArrow_; }
}

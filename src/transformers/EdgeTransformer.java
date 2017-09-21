package transformers;

import java.awt.BasicStroke;
import java.awt.Stroke;
import elements.Node;
import elements.Link;

import org.apache.commons.collections15.Transformer;

public class EdgeTransformer<Link> implements Transformer<Link,Stroke> {
	
	/** Controller connections type */
	private String controllerType_ = null;
	/** Inhibitor connections type */
	private String inhibitorType_ = null;
	/** Dual connections type */
	private String shortpathType_ = null;
	/** Unknown connections type */
	private String unknownType_ = null;
	
	/** Arrow of the enhancer connections */
	private Stroke enhancerStroke_ = null;
	/** Arrow of the inhibitor connections */
	private Stroke inhibitoryStroke_ = null;
	/** Arrow of the dual connections */
	private Stroke shortpathStroke_ = null;
	/** Arrow of the unknown connections */
	private Stroke unknownStroke_ = null;
	// controler edge stroke
	private Stroke controllerStroke_ = null;

    
	
	/**ArrowColorTransformer
	 * Initialization
	 */
	private void initialize() {
		controllerType_ = "controllerEdge";
		inhibitorType_ = "inhibitorType";
		shortpathType_ = "SP";
		unknownType_ = "";
	}
	
	/**
	 * Constructor
	 */
	public EdgeTransformer() {
		initialize();
		distinguishConnectionByEdge(true);
	}
	
	/**
	 * This constructor allows to distinguish or not the signed connections by their
	 * edge/arrow color.
	 * @param distinguishSignedConnections
	 */
	public EdgeTransformer(boolean distinguishSignedConnections) {
		initialize();
		distinguishConnectionByEdge(distinguishSignedConnections);
	}
	
	/**
	 * Constructor with edge/arrow color specified.
	 * @param enhancerStroke Stroke of the enhancer interactions
	 * @param inhibitorStroke Stroke of the inhibitory interactions
	 * @param dualColor Stroke of the dual interactions
	 * @param unknownStroke Stroke of the unknown interactions
	 */
	public EdgeTransformer(Stroke enhancerStroke, Stroke inhibitorStroke, Stroke shortpathStroke, Stroke unknownStroke, Stroke controllerStroke) {
		initialize();
		enhancerStroke_ = enhancerStroke;
		inhibitoryStroke_ = inhibitorStroke;
		shortpathStroke_ = shortpathStroke;
		unknownStroke_ = unknownStroke;
		controllerStroke_ = controllerStroke;
	}

    /**
     * Return the color that should be used for each edge/arrow of the network.
     */
	public Stroke transform(Link e) {
	
		//Link edge = (Link) e;
		String type = ((elements.Link)e).getArrowType();
		//System.out.println(type);
		if (type == controllerType_)
			return controllerStroke_;
		else if (type.equals(inhibitorType_))
			return inhibitoryStroke_;
		else if (type.equals(shortpathType_))
			return shortpathStroke_;
		else if (type.equals(unknownType_))
			return unknownStroke_;
		else
			return getBasicStroke(10.0f);
	}
	
	/**
	 * Set if the transformer should distinguish each signed connection by a color.
	 * @param yes
	 */
	public void distinguishConnectionByEdge(boolean yes) {

		if (yes) {
			enhancerStroke_ = null;
			inhibitoryStroke_ = null;
			shortpathStroke_ = null;
			float[] dash = {21, 9, 3, 9};
			unknownStroke_ = getBasicStroke(dash);
//			unknownStroke_ = 
		}
		else {
			float[] dash = {10.0f};
			controllerStroke_ =  getBasicStroke(dash);
			enhancerStroke_ = null;
			inhibitoryStroke_ = null;
			//float[] dash2 = {21, 9, 3, 9};
			shortpathStroke_ = null;
			//shortpathStroke_ = getBasicStroke(dash2);
			unknownStroke_ = null;
//			float[] dash2 = {21, 9, 3, 9};
//			unknownStroke_ = getBasicStroke(dash2);
		}
	}
	
	public Stroke getBasicStroke(final float d) {
		float[] dash = {d};
		return new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
	}
	
	public Stroke getBasicStroke(final float[] dash) {
		return new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
	}
	
	public void setEnhancerArrow(Stroke stroke) { enhancerStroke_ = stroke; }
	public void setInhibitoryArrow(Stroke stroke) { inhibitoryStroke_ = stroke; }
	public void setshortpathArrow(Stroke stroke) { shortpathStroke_ = stroke; }
	public void setUnknownArrow(Stroke stroke) { unknownStroke_ = stroke; }
	
	public Stroke getEnhancerArrow() { return enhancerStroke_; }
	public Stroke getInhibitoryArrow() { return inhibitoryStroke_; }
	public Stroke getshortpathArrow() { return shortpathStroke_; }
	public Stroke getUnknownArrow() { return unknownStroke_; }
}
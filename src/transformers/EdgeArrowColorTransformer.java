package transformers;

import java.awt.Color;
import java.awt.Paint;

import org.apache.commons.collections15.Transformer;

public class EdgeArrowColorTransformer<Link> implements Transformer<Link,Paint> {
	/** Enhancer connections type */
	private String enhancerType_ = null;
	/** Inhibitor connections type */
	private String inhibitorType_ = null;
	/** Dual connections type */
	private String showpathType_ = null;
	/** Unknown connections type */
	private String unknownType_ = null;
	/** Controller connections type */
	private String controllerType_ = null;
	
	/** Arrow of the enhancer connections */
	private Color enhancerColor_ = null;
	/** Arrow of the inhibitor connections */
	private Color inhibitoryColor_ = null;
	/** Arrow of the dual connections */
	private Color showpathColor_ = null;
	/** Arrow of the unknown connections */
	private Color unknownColor_ = null;
	
	/** Arrow of the controller connections */
	private Color controllerColor_ = null;
	
	
	private void initialize() {
		controllerType_ = "controllerEdge";
		inhibitorType_ = "inhibitorType";
		showpathType_ = "SP";
		unknownType_ = "";
	}
	
	/**
	 * Constructor
	 */
	public EdgeArrowColorTransformer() {
		initialize();
		distinguishConnectionByColor(true);
	}
	
	/**
	 * This constructor allows to distinguish or not the signed connections by their
	 * edge/arrow color.
	 * @param distinguishSignedConnections
	 */
	public EdgeArrowColorTransformer(boolean distinguishSignedConnections) {
		initialize();
		distinguishConnectionByColor(distinguishSignedConnections);
	}
	
	/**
	 * Constructor with edge/arrow color specified.
	 * @param enhancerColor Color of the enhancer interactions
	 * @param inhibitorColor Color of the inhibitory interactions
	 * @param unknownColor Color of the unknown interactions
	 */
	public EdgeArrowColorTransformer(Color enhancerColor, Color inhibitorColor, Color showpathColor, Color unknownColor, Color controllerColor) {
		initialize();
		enhancerColor_ = enhancerColor;
		inhibitoryColor_ = inhibitorColor;
		showpathColor_ = showpathColor;
		unknownColor_ = unknownColor;
		controllerColor_ = controllerColor;
	}
	
    /**
     * Return the color that should be used for each edge/arrow of the network.
     */
	public Paint transform(Link e) {
	
		//Link l = (Link) e;
		String type = ((elements.Link)e).getArrowType();
		//System.out.println("Edge_Arrow_Color_Transform");
		if(type == controllerType_)
			return controllerColor_;
		else if (type == enhancerType_)
			return enhancerColor_;
		else if (type.equals(inhibitorType_))
			return inhibitoryColor_;
		else if (type.equals(showpathType_))
			return showpathColor_;
		else if (type.equals(unknownType_))
			return unknownColor_;
		else
			return Color.BLACK;
	}
	
	/**
	 * Set if the transformer should distinguish each signed connection by a color.
	 * @param yes
	 */
	public void distinguishConnectionByColor(boolean yes) {
		if (yes) {
			enhancerColor_ = new Color(0, 0, 130); //Color.BLACK;
			inhibitoryColor_ = Color.RED;
			showpathColor_ = Color.MAGENTA;
			unknownColor_ = Color.LIGHT_GRAY; //new Color(120,120,120);
		}
		else {
			controllerColor_ = Color.GRAY;
			enhancerColor_ = Color.BLACK;
			inhibitoryColor_ = Color.BLACK;
			showpathColor_ = Color.RED;
			unknownColor_ = Color.BLACK;
		}
	}
	
	public void setEnhancerArrow(Color color) { enhancerColor_ = color; }
	public void setInhibitoryArrow(Color color) { inhibitoryColor_ = color; }
	public void setshowpathArrow(Color color) { showpathColor_ = color; }
	public void setUnknownArrow(Color color) { unknownColor_ = color; }
	
	public Color getEnhancerArrow() { return enhancerColor_; }
	public Color getInhibitoryArrow() { return inhibitoryColor_; }
	public Color getshowpathArrow() { return showpathColor_; }
	public Color getUnknownArrow() { return unknownColor_; }
}

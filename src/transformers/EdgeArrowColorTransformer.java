package transformers;

import java.awt.Color;
import java.awt.Paint;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.visualization.picking.PickedInfo;

public class EdgeArrowColorTransformer<Link> implements Transformer<Link,Paint> {
	
	/** Pick info */
	private PickedInfo<Link> pi_ = null;
	
	/** Showpath connections type */
	private String showpathType_ = null;
	/** Unknown connections type */
	private String unknownType_ = null;
	/** Controller connections type */
	private String controllerType_ = null;
	
	
	private Color pickedColor_ = null;
	/** Arrow of the dual connections */
	private Color showpathColor_ = null;
	/** Arrow of the unknown connections */
	private Color unknownColor_ = null;
	
	/** Arrow of the controller connections */
	private Color controllerColor_ = null;
	
	
	private void initialize() {
		pickedColor_ = Color.BLUE;
		controllerColor_ = Color.GRAY;
		showpathColor_ = Color.RED;
		unknownColor_ = Color.BLACK;
	}


	public EdgeArrowColorTransformer(PickedInfo<Link> pi) {
		this.pi_ = pi;
		
		controllerType_ = "controllerEdge";
		showpathType_ = "SP";
		unknownType_ = "";
		
		initialize();
	}
	

    /**
     * Return the color that should be used for each edge/arrow of the network.
     */
	public Paint transform(Link e) {
		String type = ((elements.Link)e).getArrowType();
		
    	if (pi_.isPicked(e))
    		return pickedColor_;
    	else if(type == controllerType_)
			return controllerColor_;
		else if (type.equals(showpathType_))
			return showpathColor_;
		else if (type.equals(unknownType_))
			return unknownColor_;
		else
			return Color.BLACK;
	}
	
	
	public void setshowpathArrow(Color color) { showpathColor_ = color; }
	public void setUnknownArrow(Color color) { unknownColor_ = color; }
	
	public Color getshowpathArrow() { return showpathColor_; }
	public Color getUnknownArrow() { return unknownColor_; }
}

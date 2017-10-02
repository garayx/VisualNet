package transformers;

import java.awt.Color;
import java.awt.Paint;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.visualization.picking.PickedInfo;
import elements.Controller;
import elements.Host;
import elements.Switch;

public class NodeFillColorTransformer<Node> implements Transformer<Node,Paint> {
		/** Pick info */
		private PickedInfo<Node> pi_ = null;
		/** Normal color of the graph controller. */
	    private Color defaultControllerColor = null;
		/** Normal color of the graph controller. */
	    private Color defaultSwitchColor = null;
		/** Normal color of the graph controller. */
	    private Color defaultHostColor = null;
	    /** Color of the picked vertex. */
	    private Color pickedColor_ = null;
	    /** Color of the SP vertex. */
	    private Color spColor_ = null;
	    /** Color of the SP vertex. */
	    private Color defaultNodeColor  = null;
	    /** Color of the SP vertex. */
	    private Color srcNodeColor  = null;
	    /** Color of the SP vertex. */
	    private Color destNodeColor  = null;
	    
	    
	    
	    
	    public NodeFillColorTransformer(PickedInfo<Node> pi) {
	        pi_ = pi;
	        // set default colors
	        defaultNodeColor = new Color(113, 153, 255); 		// Blue light
	        defaultControllerColor = new Color(0, 127, 127);
	        defaultSwitchColor = Color.ORANGE;
	        defaultHostColor = new Color(127, 127, 0);
	        pickedColor_ = Color.YELLOW;
	        spColor_ = Color.RED;
	        //srcNodeColor = new Color(255, 102, 102);
	        srcNodeColor = new Color(204, 0, 0);
	        destNodeColor = new Color(204, 0, 0);;
	    }
	    public Paint transform(Node v) {
	    	if (pi_.isPicked(v))
	    		return pickedColor_;
	    	else if (v instanceof Host) {
//	    		Host n = (Host) v;
	    		if(((Host) v).getRouteType().equals("")){
	            	return defaultHostColor;
	            } else if(((Host) v).getRouteType().equals("SP")){
	            	return spColor_;
	            } else if(((Host) v).getRouteType().equals("src")){
	            	return srcNodeColor;
	            } else if(((Host) v).getRouteType().equals("dest")){
	            	return destNodeColor;
	            }
	    	}
	    	else if (v instanceof Switch){
	            if(((Switch)v).getRouteType().equals("")){
	            	return defaultSwitchColor;
	            } else if(((Switch)v).getRouteType().equals("SP")){
	            	return spColor_;
	            } else if(((Switch)v).getRouteType().equals("src")){
	            	return srcNodeColor;
	            } else if(((Switch)v).getRouteType().equals("dest")){
	            	return destNodeColor;
	            }
	    	}
	    	else if (v instanceof Controller){
	            if(((Controller)v).getRouteType().equals("")){
	            	return defaultControllerColor;
	            } else if(((Controller)v).getRouteType().equals("SP")){
	            	return spColor_;
	            } else if (((Controller)v).getRouteType().equals("src")){
	            	return srcNodeColor;
	            } else if (((Controller)v).getRouteType().equals("dest")){
	            	return destNodeColor;
	            }
	    	}
			return defaultNodeColor;
	    }
	
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import common.CommonData;
import elements.Controller;
import elements.Link;
import elements.NIC;
import elements.Switch;
import org.apache.commons.collections15.Factory;

/**
 *
 * @author Daniel
 */
public class EdgeFactory implements Factory<Link>
{    

    public EdgeFactory()
    {
        
    }
    
    
    @Override
    public Link create()
    {
    	//if(common.CommonData.nodeLeft != common.CommonData.nodeRight){
	        Link l = new Link();
	        l.setNode_left(common.CommonData.nodeLeft);
	        l.setNode_right(common.CommonData.nodeRight);
	
	        NIC left = CommonData.nodeLeft.addConnection(CommonData.nodeRight);
	        NIC right = CommonData.nodeRight.addConnection(CommonData.nodeLeft);
	
	        l.setPort_left(left.getPort());
	        l.setPort_right(right.getPort());
	        
	        if(l.getNode_left() instanceof Controller || l.getNode_right() instanceof Controller){
	        	l.setArrowType("controllerEdge");
	        	//System.out.println("conEdge");
	        } 
	        
	        return l;
    	//} else {
    		//return null;
    	//}
    }
    
}

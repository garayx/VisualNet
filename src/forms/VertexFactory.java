/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import common.CommonData;
import elements.Controller;
import elements.Host;
import elements.NIC;
import elements.Node;
import elements.Switch;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.apache.commons.collections15.Factory;

/**
 *
 * @author Daniel
 */
public class VertexFactory implements Factory<Node>
{    

    public VertexFactory()
    {
        
    }
    
    
    @Override
    public Node create()
    {                
        Node node = null;
        //if (common.CommonData.selectedNode == CommonData.NodeType.Host)
        if (common.CommonData.selectedNode == "Host")
        {
            //Host host = new Host();
            //CommonData.hosts.
                    
            Host host = new Host();
            //host.setColor(CommonData.currentColor);
            //System.out.println("NEW HOST CREATED");
            CommonData.idList.put("Host", host.getID());
            node = host;
        }
        //else if(common.CommonData.selectedNode == CommonData.NodeType.Switch)
        else if(common.CommonData.selectedNode == "Switch")
        {
            Switch sw = new Switch();
            CommonData.idList.put("Switch", sw.getID());
            sw.setColor(CommonData.currentColor);
            common.CommonData.switchCount = common.CommonData.switchCount + 1;
            
            //sw.setRouteType(CommonData.domainName);
            //sw.setDomainIndex(CommonData.domainIndex);
            
            node = sw;
        }
        //else if(common.CommonData.selectedNode == CommonData.NodeType.Controller)
        else if(common.CommonData.selectedNode == "Controller")
        {
            Controller cont = new Controller();
            CommonData.idList.put("Controller", cont.getID());
            node = cont;
        }
        
        else
        {
            node = new Node()
            {
                private String id = "Unknown Node";

                public String getId()
                {
                    return id;
                }

                @Override
                public NIC addConnection(Node n)
                {
                    return new NIC(0);
                }

                @Override
                public String getToolTip()
                {
                    return "";
                }

/*                @Override
                public void removeConnection(Node n)
                {

                }*/

            };
        }
        
        //CommonData.nodesMap.put(node.getID(), node);
        
        return node;
    }
        
    
}

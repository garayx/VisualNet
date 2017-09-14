/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;
//
//import static common.CommonData.NodeType.Host;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import elements.Host;
import elements.Link;
import elements.Node;
import elements.Switch;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import javax.swing.Action;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import common.CommonData;


/**
 *
 * @author Daniel
 */
public class PopupMousePlugin<V,E> extends AbstractPopupGraphMousePlugin
{

    @Override
    protected void handlePopup(MouseEvent me)
    {
        final VisualizationViewer<V,E> ve = (VisualizationViewer<V,E>) me.getSource();
        Point2D p = me.getPoint();
        
        final GraphElementAccessor<V,E> pickupSupport = ve.getPickSupport();
        
        if( pickupSupport != null )
        {
            final V v = pickupSupport.getVertex(ve.getGraphLayout(), p.getX(), p.getY());
            
            if( v != null )
            {
                //System.out.println("vertex clicked at:" + p);
                JPopupMenu menu = new JPopupMenu();
                
                if( v instanceof Switch )
                {
                    Switch sw = (Switch)v;                    
             
                    
//                    JMenuItem routingItem = new JMenuItem("Set Routing");
//                    routingItem.addActionListener(new ActionListener()
//                    {
//                        @Override
//                        public void actionPerformed(ActionEvent e)
//                        {
//
//                            RoutingFrame rf = new RoutingFrame(sw.getRoutingList(), sw);
//                            rf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//                            rf.setSize(new Dimension(800,600));
//                            rf.setVisible(true);
//                        }
//                    });

//                    JMenuItem ipMacItem = new JMenuItem("Set IP/MAC mapping");
//                    ipMacItem.addActionListener(new ActionListener()
//                    {
//                        @Override
//                        public void actionPerformed(ActionEvent e)
//                        {
//
//                            IpMacFrame rf = new IpMacFrame(sw.getIpMacList());
//                            rf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//                            rf.setSize(new Dimension(800,600));
//                            rf.setVisible(true);
//                        }
//                    });
                    
//                    JMenuItem domainIndex = new JMenuItem("Set domain index");
//                    domainIndex.addActionListener(new ActionListener()
//                    {
//                        @Override
//                        public void actionPerformed(ActionEvent e)
//                        {
//                            String indexStr = JOptionPane.showInputDialog("Enter index:");
//                            
//                            if( indexStr != null && indexStr.isEmpty() == false )
//                            {                            
//                                sw.setDomainIndex(Integer.parseInt(indexStr));
//                            
//                                ve.repaint();
//                            }
//                        }
//                    });
                    
//                    JMenuItem routeTypeItem = new JMenuItem("Set domain name");
//                    routeTypeItem.addActionListener(new ActionListener()
//                    {
//                        @Override
//                        public void actionPerformed(ActionEvent e)
//                        {
//                            String routeTypeStr = JOptionPane.showInputDialog("Enter domain name ?");
//                            
//                            if( routeTypeStr != null && routeTypeStr.isEmpty() == false )
//                            {                            
//                                sw.setRouteType(routeTypeStr.toUpperCase());
//                                ve.repaint();
//                            }
//                            
//                            
//                        }
//                    });
                    
                    JMenuItem changeColor = new JMenuItem("Change Color");
                    changeColor.addActionListener(new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                            
                            Color color = JColorChooser.showDialog(null, "Select Color", sw.getColor());
                            
                            
                            if( color != null )
                            {
                                sw.setColor(color);
                                ve.repaint();
                            }
                        }
                    });
                                        
//                    menu.add(routingItem);            
//                    menu.add(routeTypeItem);
//                    menu.add(ipMacItem);
//                    menu.add(domainIndex);
                    menu.add(changeColor);
                }
                else if (v instanceof Host)
                {
                    Host h = (Host)v;                    
                }
                if( v instanceof Node)
                {
                    Node n = (Node)v;
                    JMenuItem removeItem = new JMenuItem("Remove Node");
                    removeItem.addActionListener(new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                            ve.getGraphLayout().getGraph().removeVertex(v);
                            
                            n.removeNode();
                            
                            if(n instanceof Host)
                            {
                            	// TODO
//                                common.CommonData.macAddresses.remove( ((Host)n).getNics().get(0).getMac() );
//                                common.CommonData.ipAddresses.remove( ((Host)n).getNics().get(0).getIp() );                                
                            }
                            
                            n.getConnections().keySet().stream().forEach((node) ->
                            {                                
                                node.removeConnection(n);
                            });          
                            // TODO 
                            
                            //common.CommonData.idList.get(n.getType()).remove(n.getID());
                            common.CommonData.idList.remove(n.getType(), n.getID());
                            ve.repaint();
                        }
                    });
                              
                    JMenuItem setSourceItem = new JMenuItem("Set as Source Node");
                    setSourceItem.addActionListener(new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                      	  CommonData.sourceNode = n;
                        }
                    });
                    
                    JMenuItem setDestinationItem = new JMenuItem("Set as Destination Node");
                    setDestinationItem.addActionListener(new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                      	  CommonData.destinationNode = n;
                        }
                    });
                    
                    /*JMenuItem renameItem = new JMenuItem("Rename");
                    renameItem.addActionListener(new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                            String name = JOptionPane.showInputDialog("Enter node name:");
                            
                            if( name != null && name.isEmpty() == false )
                            {
                                if( common.CommonData.idList.get(n.getType()).contains(name) == false )
                                {
                                                                        
                                    common.CommonData.idList.get(n.getType()).remove(n.getID());
                                    common.CommonData.switches.remove(n.getID());
                                    
                                    n.setID(name);
                                    
                                    common.CommonData.idList.get(n.getType()).add(name);
                                    common.CommonData.switches.add(n.getID());
                                    
                                                                
                                    ve.repaint();
                                }
                                else
                                {
                                    JOptionPane.showMessageDialog(null, "Node name exists");
                                }
                            }
                        }
                    });*/
                    
                    //menu.add(renameItem);
                    menu.add(setSourceItem);
                    menu.add(setDestinationItem);
                    menu.add(removeItem);
                    
                }
                
                menu.show((Component)me.getSource(), me.getX(), me.getY());
                        
            }
            else
            {
                final E e = pickupSupport.getEdge(ve.getGraphLayout(), p.getX(), p.getY());
                
                if( e != null )
                {
                    JPopupMenu menu = new JPopupMenu();
                    
                    JMenuItem weightItem = new JMenuItem("Set Capacity");
                    weightItem.addActionListener(new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent ae)
                        {                            
                            Link l = (Link)e;
                            
                            String capStr = JOptionPane.showInputDialog("Enter Capacity:");
                            
                            l.setCapacity(Integer.parseInt(capStr));
                            
                            ve.repaint();
                        }
                    });
                    
                    JMenuItem removeItem = new JMenuItem("Remove Edge");
                    removeItem.addActionListener(new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent ae)
                        {
                            Link l = (Link)e;
                            ve.getGraphLayout().getGraph().removeEdge(e);

                            l.getNode_left().removeConnection(l.getNode_right());
                            l.getNode_right().removeConnection(l.getNode_left());
                            
                            ve.repaint();
                        }
                    });
                    
                    menu.add(weightItem);
                    menu.add(removeItem);   
                    menu.show((Component)me.getSource(), me.getX(), me.getY());
                    
                }
            }
        }
    }
    
}
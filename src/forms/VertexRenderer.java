/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;
import elements.Controller;
import elements.Host;
import elements.Link;
import elements.Node;
import elements.Switch;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 *
 * @author Daniel
 */
public class VertexRenderer implements Renderer.Vertex<Node, Link>
{

    @Override
    public void paintVertex(RenderContext<Node, Link> rc, Layout<Node, Link> layout, Node v)
    {
        GraphicsDecorator graphicsContext = rc.getGraphicsContext();
        Point2D center = layout.transform(v);
        Shape shape = null;
        Color color = null;        
        
        if( v instanceof Host )
        {
            shape = new Rectangle((int)center.getX()-10, (int)center.getY()-10, 20, 20);
            color = new Color(127, 127, 0);            
        }
        else if( v instanceof Switch )
        {
            shape = new Ellipse2D.Double(center.getX()-10, center.getY()-10, 20, 20);
            
            //if( v.getID().startsWith("s"))
            /*if( ((Switch) v).getRouteType().equals("C"))
            {
                color = new Color(Color.ORANGE.getRGB());            
            }
            //else if(v.getID().startsWith("n"))
            else if(((Switch) v).getRouteType().equals("N"))
            {
                color = new Color(Color.BLUE.getRGB());
            }
            else
            {
                color = new Color(Color.BLACK.getRGB());
            }*/
            color = ((Switch)v).getColor();
        }
        else if( v instanceof Controller )
        {
            shape = new Ellipse2D.Double(center.getX()-10, center.getY()-10, 20, 20);
            color = new Color(0, 127, 127);            
        }
        
        graphicsContext.setPaint(color);
        graphicsContext.fill(shape);
    }

}

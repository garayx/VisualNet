package forms;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import elements.Link;
import elements.Node;

public class PopupViewerMenu extends JPopupMenu{
	
	public PopupViewerMenu(EditingModalGraphMouse<Node, Link> graphMouse) {
		JRadioButtonMenuItem hostItem = new JRadioButtonMenuItem("Host", createIcon("host"));
		hostItem.setSelected(false);
		//hostItem.setHorizontalTextPosition(JMenuItem.CENTER);
		//add(hostItem);

		hostItem.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					common.CommonData.selectedNode = "Host";
					graphMouse.setMode(ModalGraphMouse.Mode.EDITING);
				}
			}
		});

		JRadioButtonMenuItem switchItem = new JRadioButtonMenuItem("Switch",createIcon("switch"));
		switchItem.setSelected(false);
		//add(switchItem);
		switchItem.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					common.CommonData.selectedNode = "Switch";
					graphMouse.setMode(ModalGraphMouse.Mode.EDITING);
				}
			}
		});

		JRadioButtonMenuItem controllerItem = new JRadioButtonMenuItem("Controller", createIcon("controller"));
		controllerItem.setSelected(false);
		//add(controllerItem);
		controllerItem.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					common.CommonData.selectedNode = "Controller";
					graphMouse.setMode(ModalGraphMouse.Mode.EDITING);
				}
			}
		});
		
		
		
		
		
		
		
		
		
        JMenuItem editModeItem = new JMenuItem("Edit Mode");
        editModeItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                graphMouse.setMode(ModalGraphMouse.Mode.EDITING);

            }
        });

        JMenuItem pickModeItem = new JMenuItem("Pick Mode");
        pickModeItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
            }
        });

        JMenuItem transModeItem = new JMenuItem("Transforming Mode");
        transModeItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
            }
        });
        
        
		add(editModeItem);
		add(pickModeItem);
		add(transModeItem);
		this.addSeparator();
		add(hostItem);
		add(switchItem);
		add(controllerItem);
		
		
		
	}
	
	public static Ellipse2D.Double getSphericalVertex(float radius) {
    	Ellipse2D.Double shape = new Ellipse2D.Double();
    	shape.x = -radius;
    	shape.y = -radius;
    	shape.height = 2*radius;
    	shape.width = 2*radius;
        return shape;
    }
	
	
	
	protected static ImageIcon createIcon(String type) {

		if (type == "host") {
			BufferedImage bi = new BufferedImage(13, 13, BufferedImage.TYPE_INT_ARGB);
			Graphics g = bi.createGraphics();
			g.setColor(new Color(127, 127, 0));
			// g.fillOval(0, 0, 25, 25);
			g.fillRect(0, 0, 12, 12);
			g.setColor(Color.black);
			g.drawRect(0, 0, 12, 12);
			g.dispose();
			return new ImageIcon(bi);
		} else if (type == "switch") {
			BufferedImage bi = new BufferedImage(13, 13, BufferedImage.TYPE_INT_ARGB);
			Graphics g = bi.createGraphics();
			g.setColor(Color.ORANGE);
			g.fillOval(0, 0, 12, 12);
			g.setColor(Color.black);
			g.drawOval(0, 0, 12, 12);
			// g.fillRect(0, 0, 25, 25);
			g.dispose();
			return new ImageIcon(bi);
		} else if (type == "controller") {
			BufferedImage bi = new BufferedImage(13, 13, BufferedImage.TYPE_INT_ARGB);
			Graphics g = bi.createGraphics();
			g.setColor(new Color(0, 127, 127));
			g.fillOval(0, 0, 12, 12);
			g.setColor(Color.black);
			g.drawOval(0, 0, 12, 12);
			// g.fillRect(0, 0, 25, 25);
			g.dispose();
			return new ImageIcon(bi);
		}
		return ImageIcon(null);
	}

	private static ImageIcon ImageIcon(Object object) {
		// TODO Auto-generated method stub
		return null;
	}
}

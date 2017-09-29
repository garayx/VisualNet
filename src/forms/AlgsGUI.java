package forms;



import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.MultiPickedState;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import elements.Link;
import elements.Node;
import transformers.NodeAlgShapeTransformer;
import transformers.NodeFillColorTransformer;
import transformers.NodeShapeTransformer;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class AlgsGUI extends JFrame{
	JFrame frame = new JFrame();
	VisualizationViewer<Node, Link> vv1;
	//private Graph<Node, Link> graph = null;
	private Map<Node, Double> nodeMapAlg = null;
	private double algAvgScore =0.0;
	
	
    private NodeAlgShapeTransformer<Node> nodeShapeTransformer_ = null;
    private NodeFillColorTransformer<Node> nodeFillColorTransformer_ = null;
    private PickedState<Node> pickedState_ = null;
	
	/**
	 * Create the panel.
	 */
	public AlgsGUI(Graph<Node, Link> g, String str, Map<Node, Double> map) {
		//this.algAvgScore = num;
		this.nodeMapAlg = map;
		setResizable(false);
		Dimension preferredSizeRect = new Dimension(475, 335);
		AbstractLayout<Node, Link> layout1 = new FRLayout<Node,Link>(g);
		VisualizationModel<Node, Link> vm1 = new DefaultVisualizationModel<Node, Link>(layout1, preferredSizeRect);
		

		
		
		
		vv1 = new VisualizationViewer<Node, Link>(vm1, preferredSizeRect);
		pickedState_ = vv1.getPickedVertexState();
		
		
		vv1.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Node, Link>());
		//Color back = Color.decode("0xffffbb");
		
		
		
		
    	nodeShapeTransformer_ = new NodeAlgShapeTransformer(this.nodeMapAlg);
        nodeFillColorTransformer_ = new NodeFillColorTransformer(pickedState_);
		
		
		vv1.setBackground(Color.white);
		vv1.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
		vv1.setForeground(Color.darkGray);
		PickedState<Node> ps = new MultiPickedState<Node>();
		vv1.setPickedVertexState(ps);
		PickedState<Link> pes = new MultiPickedState<Link>();
		vv1.setPickedEdgeState(pes);

		// vv1.setLayout(new BorderLayout());
		vv1.getRenderContext().setEdgeDrawPaintTransformer(
				new PickableEdgePaintTransformer<Link>(vv1.getPickedEdgeState(), Color.black, Color.red));
//		vv1.getRenderContext().setVertexFillPaintTransformer(
//				new PickableVertexPaintTransformer<Node>(vv1.getPickedVertexState(), Color.ORANGE, Color.YELLOW));

		
		
		vv1.getRenderContext().setVertexFillPaintTransformer(nodeFillColorTransformer_);
		vv1.getRenderContext().setVertexShapeTransformer(nodeShapeTransformer_);
		
		
		
		//vv1.setVertexToolTipTransformer(new ToStringLabeller());
		vv1.setVertexToolTipTransformer(this.vv1.getRenderContext().getVertexLabelTransformer());
		
		// Print Nodes names
        this.vv1.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Node>()
        {
            @Override
            public String transform(Node node)
            {
            	String tmp = node.getType().substring(0, 1) + "" +  node.getID();
                return tmp;
            }
        });
		// Print edges capacity
        this.vv1.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<Link>()
        {
            @Override
            public String transform(Link link)
            {
            	String tmp = "" + link.getCapacity();
                return tmp;
            }
        });
        
        
        // Print nodes ToolTip
        this.vv1.setVertexToolTipTransformer(new Transformer<Node, String>()
        {
            @Override
            public String transform(Node i)
            {
                return i.getToolTip();
            }
        });
		vv1.setLayout(new BorderLayout());

		JLabel vv1Label = new JLabel("Algorithm Results");
		// vv1Label.setFont(font);
		JPanel flow1 = new JPanel();
		flow1.add(vv1Label);
		vv1.add(flow1, BorderLayout.NORTH);

		Container content = getContentPane();
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 750, 500);

		DefaultModalGraphMouse<Node, Link> gm1 = new DefaultModalGraphMouse<Node, Link>();
		vv1.setGraphMouse(gm1);
		final ScalingControl scaler = new CrossoverScalingControl();
		vv1.scaleToLayout(scaler);

		JButton plus = new JButton("+");
		plus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv1, 1.1f, vv1.getCenter());
			}
		});
		JButton minus = new JButton("-");
		minus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv1, 1 / 1.1f, vv1.getCenter());
			}
		});

		JPanel zoomPanel = new JPanel(new GridLayout(1, 2));
		zoomPanel.setBorder(BorderFactory.createTitledBorder("Zoom"));
		JPanel modePanel = new JPanel();
		modePanel.setBorder(BorderFactory.createTitledBorder("Mouse Mode"));
		modePanel.add(gm1.getModeComboBox());
		JPanel controls = new JPanel();
		controls.setBounds(0, 500, 750, 63);
		zoomPanel.add(plus);
		zoomPanel.add(minus);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(750, 0, 350, 563);
		GroupLayout gl_controls = new GroupLayout(controls);
		gl_controls.setHorizontalGroup(
			gl_controls.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_controls.createSequentialGroup()
					.addGap(26)
					.addComponent(zoomPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(5)
					.addComponent(modePanel, GroupLayout.PREFERRED_SIZE, 166, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_controls.setVerticalGroup(
			gl_controls.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_controls.createSequentialGroup()
					.addGroup(gl_controls.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_controls.createSequentialGroup()
							.addGap(8)
							.addComponent(zoomPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_controls.createSequentialGroup()
							.addGap(5)
							.addComponent(modePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		controls.setLayout(gl_controls);
		
				GraphZoomScrollPane graphZoomScrollPane = new GraphZoomScrollPane(vv1);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(graphZoomScrollPane, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(graphZoomScrollPane, GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
		);
		panel.setLayout(gl_panel);
		
		
//		 JTextArea textArea = new JTextArea(33, 35);
//		    textArea.setText(str);
//		    textArea.setEditable(false);
		
		JTextPane textpane = new JTextPane();
		textpane.setPreferredSize(new Dimension(333, 444));
		textpane.setContentType("text/html");
		textpane.setText(str);
		textpane.setEditable(false);
			textpane.setCaretPosition(0);
		    
		JScrollPane scrollPane = new JScrollPane(textpane,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
		);
		panel_1.setLayout(gl_panel_1);
		getContentPane().setLayout(null);
		getContentPane().add(controls);
		getContentPane().add(panel);
		getContentPane().add(panel_1);
		//getContentPane().setSize(1024, 768);
	}
}

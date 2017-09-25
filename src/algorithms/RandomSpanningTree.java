package algorithms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
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


/*
 *  1: Choose a starting vertex s arbitrarily. Set TV ← {s} and TE ← ∅.
	2: Do a simple random walk starting at s. Whenever we cross an edge e = {u, v} with v 6∈ V , add v to TV and add e to TE.
	3: Stop the random walk when TV = V . Output T = (TV , TE) as our spanning tree.
 */



public class RandomSpanningTree extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7641790928990256138L;
	JFrame frame = new JFrame();
	VisualizationViewer<Node, Link> vv1;
	//Dimension preferredSizeRect = new Dimension(500, 250);
	
	//private Graph<Node, Link> graph;
	//private Forest<Node, Link> tree;
	//private List<Node> nodes;
	//private List<Link> edges = new ArrayList<Link>();
	public RandomSpanningTree(Graph<Node, Link> g){
		super("Random Spanning Tree");
		Graph<Node, Link> graph;
		List<Node> nodes;
		List<Link> edges = new ArrayList<Link>();
		nodes = new ArrayList<Node>(g.getVertices());
		
		// Get random source node to start randomWalks
		Random rand = new Random();
		int randNum = rand.nextInt(g.getVertexCount() - 1);
		Node sourceNode = nodes.get(randNum);
		
		// Put the source node to completed nodes list
		if(nodes.size() == 2){
			edges = new ArrayList<Link>();
			Link tmp = new Link();
			//Node tmpNode;
			tmp.setNode_left(sourceNode);
			for(Node x : nodes){
				if(!x.equals(sourceNode) && g.isNeighbor(sourceNode, x))
					tmp.setNode_right(x);
			}
			edges.add(tmp);
		} else {
		edges = new RandomWalk(g).searchNetwork(sourceNode);
		}
		
		graph = new DelegateForest<>();
		for (Node x : nodes) {
			graph.addVertex(x);
		}
		for (Link x : edges) {
			graph.addEdge(x, x.getNode_left(), x.getNode_right());
		}
		initialize(graph);
	}
	// TODO same code as SpanningTree make reusable
	private void initialize(Graph<Node, Link> graph){
		
		Dimension preferredSizeRect = new Dimension(500, 450);
		Layout<Node, Link> layout1 = new TreeLayout<>((Forest<Node, Link>) graph);
		VisualizationModel<Node, Link> vm1 = new DefaultVisualizationModel<Node, Link>(layout1, preferredSizeRect);
		
		vv1 = new VisualizationViewer<Node, Link>(vm1, preferredSizeRect);
		vv1.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Node, Link>());
		//Color back = Color.decode("0xffffbb");
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
		vv1.getRenderContext().setVertexFillPaintTransformer(
				new PickableVertexPaintTransformer<Node>(vv1.getPickedVertexState(), Color.ORANGE, Color.YELLOW));

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

		JLabel vv1Label = new JLabel("Random Spanning Trees");
		// vv1Label.setFont(font);
		JPanel flow1 = new JPanel();
		flow1.add(vv1Label);
		vv1.add(flow1, BorderLayout.NORTH);

		Container content = getContentPane();
		JPanel grid = new JPanel(new GridLayout(0, 1));
		JPanel panel = new JPanel(new BorderLayout());

		grid.add(new GraphZoomScrollPane(vv1));
		// panel.add(new GraphZoomScrollPane(vv3), BorderLayout.EAST);
		panel.add(grid);

		content.add(panel);

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
		zoomPanel.add(plus);
		zoomPanel.add(minus);
		controls.add(zoomPanel);
		controls.add(modePanel);
		content.add(controls, BorderLayout.SOUTH);
	}
}

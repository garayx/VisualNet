package forms;

import algorithms.*;
//import com.fasterxml.jackson.databind.ObjectMapper;
import common.CommonData;
import elements.Controller;
import elements.Host;	
import elements.Node;
import elements.Switch;
import transformers.ArrowShapeTransformer;
import transformers.EdgeArrowColorTransformer;
import transformers.EdgeTransformer;
import transformers.NodeFillColorTransformer;
import transformers.NodeShapeTransformer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.apache.commons.collections15.Factory;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import edu.uci.ics.jung.visualization.picking.PickedState;
//import elements.IpMacEntry;
import elements.Link;
import elements.NIC;

//import elements.Route;
//import elements.RoutePair;
//import elements.RoutingEntry;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.commons.collections15.Transformer;

import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import javax.swing.border.TitledBorder;
import java.awt.ComponentOrientation;
import java.awt.Point;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.Cursor;
import java.awt.SystemColor;
//import org.freehep.util.export.ExportDialog;


@SuppressWarnings("rawtypes")
public class VisualNetForm extends javax.swing.JFrame implements GraphMouseListener {
    
	private static final long serialVersionUID = 5778688505441868388L;
	//private ArrayList<Node> nodes;
    private Graph<Node, Link> graph = null;
    //private Graph<Node, Link> backupGraph = new SparseGraph<Node,Link>();
    private AbstractLayout<Node, Link> layout = null;
    private VisualizationViewer<Node, Link> viewer = null;
    private EditingModalGraphMouse<Node, Link> graphMouse;
    private ArrayList<Integer> domains;
    private ArrayList<String> routeNames;
    
	private List<Link> edgesList = null;
	private List<Link> newEdgesList = null;
    
	// maps for algs results
	private Map<Node, Double> nodeMapBC = null;
	private Map<Link, Double> linkMapBC = null;
	private Map<Node, Double> nodeMapCC = null;
	private Map<Node, Double> nodeMapRWCC = null;
	private Map<Node, Double> nodeMapEVC = null;
	private Map<Node, Double> nodeMapEVCW = null;
	/*
	 * Transformers
	 */
    private ArrowShapeTransformer<Node,Link> arrowTransformer_ = null;
    private EdgeArrowColorTransformer<Link> edgeArrowColorTransformer_ = null;
    private EdgeArrowColorTransformer<Link> insideArrowColorTransformer_ = null;
    private EdgeTransformer<Link> edgeTransformer_ = null;
    private NodeShapeTransformer<Node> nodeShapeTransformer_ = null;
    private NodeFillColorTransformer<Node> nodeFillColorTransformer_ = null;
    
    private PickedState<Node> pickedState_ = null;
    private PickedState<Link> pickedStateLink_ = null;
    //static String mouseMode;
    
	public VisualNetForm()
    {
        initComponents();
        this.initialize();
        
        this.graph = new SparseGraph<Node,Link>();
        this.layout = new FRLayout<Node,Link>(this.graph, this.pnlMain.getSize());

        this.initGraph();
    }

    @SuppressWarnings("unchecked")
	private void initGraph()
    {
        // TODO comment firstLine to design
        this.viewer = new VisualizationViewer<>(layout);
        pickedState_ = this.viewer.getPickedVertexState();
        pickedStateLink_ = this.viewer.getPickedEdgeState();
        Dimension pnlSize = this.pnlMain.getBounds().getSize();
        //pickedState_ = this.viewer.getPickedVertexState();
        Dimension size = new Dimension(pnlSize.width, pnlSize.height);
        
        this.viewer.setPreferredSize(size);
        this.viewer.setBackground(Color.WHITE);
        this.viewer.addGraphMouseListener(this);

        
        
    	arrowTransformer_ = new ArrowShapeTransformer<Node,Link>(false);
    	edgeArrowColorTransformer_ = new EdgeArrowColorTransformer<Link>(pickedStateLink_);
    	edgeTransformer_ = new EdgeTransformer<Link>(false);
    	insideArrowColorTransformer_ = new EdgeArrowColorTransformer<Link>(pickedStateLink_);
    	nodeShapeTransformer_ = new NodeShapeTransformer();
        nodeFillColorTransformer_ = new NodeFillColorTransformer(pickedState_);
        

        this.viewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Node>()
        {
            @Override
            public String transform(Node node)
            {
            	String tmp = node.getType().substring(0, 1) + "" +  node.getID();
                return tmp;
            }
        });
        //this.viewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());

        /*this.viewer.getRenderContext().setEdgeLabelTransformer(MapTransformer.<Number,String>getInstance(
        		LazyMap.<Number,String>decorate(new HashMap<Number,String>(), new ToStringLabeller<Number>())));        */
        //this.viewer.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<>());
       
        
        
        this.viewer.setVertexToolTipTransformer(this.viewer.getRenderContext().getVertexLabelTransformer());

        
        
        
        // direct Lines
        //this.viewer.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Node,Link>());
        // curve lines
        this.viewer.getRenderContext().setEdgeShapeTransformer(new EdgeShape.QuadCurve<Node,Link>());
        this.viewer.getRenderContext().setEdgeArrowTransformer(arrowTransformer_); // arrow shape
        this.viewer.getRenderContext().setEdgeDrawPaintTransformer(edgeArrowColorTransformer_); // edge color
        this.viewer.getRenderContext().setArrowFillPaintTransformer(insideArrowColorTransformer_); // arrow inside color
        this.viewer.getRenderContext().setArrowDrawPaintTransformer(edgeArrowColorTransformer_); // arrow outside color
        this.viewer.getRenderContext().setEdgeStrokeTransformer(edgeTransformer_); // edge stroke
        this.viewer.getRenderContext().setVertexShapeTransformer(nodeShapeTransformer_);
        this.viewer.getRenderContext().setVertexFillPaintTransformer(nodeFillColorTransformer_);
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        final GraphZoomScrollPane gPanel = new GraphZoomScrollPane(this.viewer);
        gPanel.setName("GraphPanel");
        //panel.setSize(size);
        gPanel.setPreferredSize(size);
        //panel.setSize(this.viewer.getSize());
        this.pnlMain.add(gPanel);

        Factory<Node> vertexFactory = new VertexFactory();
        Factory<Link> edgeFactory = new EdgeFactory();

        this.graphMouse = new EditingModalGraphMouse<Node,Link>(this.viewer.getRenderContext(), vertexFactory, edgeFactory);

        graphMouse.remove(graphMouse.getPopupEditingPlugin());

        PopupMousePlugin<Node,Link> mousePlugin = new PopupMousePlugin<Node,Link>();
        graphMouse.add(mousePlugin);

        this.viewer.setGraphMouse(graphMouse);

        this.viewer.addKeyListener(graphMouse.getModeKeyListener());
        

        this.viewer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				showPickedNode();
				showPickedEdge();
				// System.out.println("KEK");
			}
//        	@Override
//        	public void mousePressed(MouseEvent e) {
//        		showPickedNode();
//        	}
			@Override
			public void mouseReleased(MouseEvent e) {
				showPickedNode();
				showPickedEdge();
			}
        });
        
        this.viewer.addMouseMotionListener(new MouseMotionListener()
        {
            @Override
            public void mouseDragged(MouseEvent e)
            {
//                Node n = this.isMouseInNode(e);
//                //System.out.println("mouseDragged in GraphPanel");
//                this.printProperties(n);
//	
            	showPickedNode();
            	showPickedEdge();
            }
            @Override
            public void mouseMoved(MouseEvent e)
            {
//                Node n = this.isMouseInNode(e);
//                Link l = this.isMouseInEdge(e);
//
//                this.printProperties(n);
//                this.printEdgeProperties(l);
//                //System.out.println("mouseMoved in GraphPanel");
            	showPickedNode();
            	showPickedEdge();
            }
//
//            private Node isMouseInNode(MouseEvent e)
//            {
//                Point2D p = e.getPoint();
//                VisualizationViewer ve = (VisualizationViewer) e.getSource();
//                final Node n = (Node) ve.getPickSupport().getVertex(ve.getGraphLayout(), p.getX(), p.getY());
//                //System.out.println("isMouseInNode in GraphPanel");
//                return n;
//            }
//
//            private Link isMouseInEdge(MouseEvent e)
//            {
//                Point2D p = e.getPoint();
//                VisualizationViewer ve = (VisualizationViewer) e.getSource();
//
//                final Link l = (Link) ve.getPickSupport().getEdge(ve.getGraphLayout(), p.getX(), p.getY());
//               // System.out.println("isMouseInEdge in GraphPanel");
//                return l;
//            }
//            // node props print
//        
//            private void printProperties(Node n)
//            {
//                if (n != null)
//                {
//                    final StringBuffer properties = new StringBuffer();
//                    properties.append("" + n.getID() + "\n");
//                    properties.append("------\n");
//
//                    if (n instanceof Switch)
//                    {
//                        properties.append("dpid:(" + ((Switch) n).getDpid() + ")\n");
//                        //properties.append("domain:" + ((Switch)n).getDomainIndex() + "\n");
//                        //properties.append(("domain:" + ((Switch) n).getRoute() + "\n"));
//                        //properties.append(((Switch) n).getConnectedControllers());
//                        //properties.append(((Switch) n).getAdjacentSwitches());
//                        //properties.append(((Switch) n).getAdjacentHosts());
//                    } else if (n instanceof Host)
//                    {
//                        //properties.append(((Host) n).getIpMacConnections());
//                    }
//                    
//                    properties.append("Picked Node: "+ pickedState_.getPicked().toString() +"\n");
//                    
//                    properties.append("Picked edge: "+ pickedStateLink_.getPicked().toString() +"\n");
//                    
//                    //properties.append("Picked Node: "+ pickedState_);
//                    txtProperties.setText(properties.toString());
//                } else
//                {
//                	// 
//                    txtProperties.setText("");
//                }
//            }
//            // edge props print
//            private void printEdgeProperties(Link l)
//            {
//                if (l != null)
//                {
//                    final StringBuffer properties = new StringBuffer();
//                    properties.append(l.toString()+ "\n");
//                    properties.append("left node:" + " " +  l.getNode_left().getToolTip()+ "\n");
//                    properties.append("right node:" + " " +  l.getNode_right().getToolTip()+ "\n");
//                    properties.append("Capacity:" + " " +  l.getCapacity()+ "\n");
//                    txtProperties.setText(properties.toString());
//                }
//            }
        });

        graphMouse.setMode(ModalGraphMouse.Mode.EDITING);

        //final ScalingControl scaler = new CrossoverScalingControl();
        /*AnnotationControls<Node,Link> annotationControls = 
                new AnnotationControls<>(graphMouse.getAnnotatingPlugin());*/
        this.viewer.setVertexToolTipTransformer(new Transformer<Node, String>()
        {
            @Override
            public String transform(Node i)
            {
                return i.getToolTip();
            }
        });
        //this.pnlMain.add(pnlSide);

        JPanel pnlRight = new JPanel();
        
        //JPanel pnlGraphProperties = new JPanel();
        pnlGraphProperties.setOpaque(false);
        pnlGraphProperties.setAlignmentY(Component.TOP_ALIGNMENT);
        pnlGraphProperties.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        pnlGraphProperties.setBorder(new TitledBorder(null, "Graph Properties", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        
        JPanel panel_1 = new JPanel();
        
        JPanel panel_2 = new JPanel();
        GroupLayout gl_pnlGraphProperties = new GroupLayout(pnlGraphProperties);
        gl_pnlGraphProperties.setHorizontalGroup(
        	gl_pnlGraphProperties.createParallelGroup(Alignment.TRAILING)
        		.addGroup(gl_pnlGraphProperties.createSequentialGroup()
        			.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 167, Short.MAX_VALUE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE))
        );
        gl_pnlGraphProperties.setVerticalGroup(
        	gl_pnlGraphProperties.createParallelGroup(Alignment.TRAILING)
        		.addGroup(gl_pnlGraphProperties.createSequentialGroup()
        			.addGroup(gl_pnlGraphProperties.createParallelGroup(Alignment.LEADING)
        				.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
        				.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
       // JLabel lblSwitchesNumber = new JLabel(" ");
        
        JLabel lblSwitches = new JLabel("Switches:");
        
       // JLabel lblControllersNumber = new JLabel(" ");
        
        JLabel lblHosts = new JLabel("Hosts:");
        lblHosts.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        
        JLabel lblControllers = new JLabel("Controllers:");
        lblControllers.setVerticalTextPosition(SwingConstants.TOP);
        lblControllers.setVerticalAlignment(SwingConstants.TOP);
        lblControllers.setName("");
        lblControllers.setHorizontalTextPosition(SwingConstants.LEFT);
        lblControllers.setHorizontalAlignment(SwingConstants.LEFT);
        lblControllers.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        lblControllersNumber = new JLabel("0");
        lblHostsNumber = new JLabel("0");
        // graph params
        lblSwitchesNumber = new JLabel("0");
        GroupLayout gl_panel_2 = new GroupLayout(panel_2);
        gl_panel_2.setHorizontalGroup(
        	gl_panel_2.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panel_2.createSequentialGroup()
        			.addGap(4)
        			.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
        				.addComponent(lblControllers)
        				.addGroup(gl_panel_2.createSequentialGroup()
        					.addGap(1)
        					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
        						.addComponent(lblHosts)
        						.addComponent(lblSwitches))))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING, false)
        				.addComponent(lblSwitchesNumber, GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
        				.addComponent(lblHostsNumber, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        				.addComponent(lblControllersNumber, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        			.addContainerGap(49, Short.MAX_VALUE))
        );
        gl_panel_2.setVerticalGroup(
        	gl_panel_2.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panel_2.createSequentialGroup()
        			.addGap(4)
        			.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblControllers)
        				.addComponent(lblControllersNumber))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblSwitches)
        				.addComponent(lblSwitchesNumber))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblHosts)
        				.addComponent(lblHostsNumber))
        			.addContainerGap(126, Short.MAX_VALUE))
        );
        panel_2.setLayout(gl_panel_2);
        
        lblSnNode = new JLabel("Node not Selected");
        
        JLabel lblSelectedNode = new JLabel("Selected Node:");
        lblSelectedNode.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblSelectedNode.setAlignmentY(Component.TOP_ALIGNMENT);
        
        btnSnDetails = new JButton("Show Details");
        btnSnDetails.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent arg0) {
        		showNodesDetails();
        	}
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		btnSnDetails.setForeground(Color.ORANGE);
        	}
        	@Override
        	public void mouseExited(MouseEvent e) {
        		btnSnDetails.setForeground(Color.BLACK);
        	}
        	@Override
        	public void mousePressed(MouseEvent e) {
        		btnSnDetails.setForeground(Color.RED);
        	}
        	@Override
        	public void mouseReleased(MouseEvent e) {
        		btnSnDetails.setForeground(Color.ORANGE);
        	}
        });
        btnSnDetails.setVisible(false);
        btnSnDetails.setOpaque(false);
        btnSnDetails.setHorizontalAlignment(SwingConstants.LEFT);
        btnSnDetails.setForeground(Color.DARK_GRAY);
        btnSnDetails.setFont(new Font("Tahoma", Font.PLAIN, 10));
        btnSnDetails.setContentAreaFilled(false);
        btnSnDetails.setBorderPainted(false);
        btnSnDetails.setBorder(null);
        btnSnDetails.setActionCommand("");
        
        JLabel lblSelectedEdge = new JLabel("Selected Edge:");
        lblSelectedEdge.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblSelectedEdge.setAlignmentY(0.0f);
        
        lblSeEdge = new JLabel("Edge not Selected");
        
        btnSeDetails = new JButton("Show Details");
        btnSeDetails.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent arg0) {
        		showEdgesDetails();
        	}
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		btnSeDetails.setForeground(Color.ORANGE);
        	}
        	@Override
        	public void mouseExited(MouseEvent e) {
        		btnSeDetails.setForeground(Color.BLACK);
        	}
        	@Override
        	public void mousePressed(MouseEvent e) {
        		btnSeDetails.setForeground(Color.RED);
        	}
        	@Override
        	public void mouseReleased(MouseEvent e) {
        		btnSeDetails.setForeground(Color.ORANGE);
        	}
        });
        btnSeDetails.setVisible(false);
        btnSeDetails.setOpaque(false);
        btnSeDetails.setHorizontalAlignment(SwingConstants.LEFT);
        btnSeDetails.setForeground(Color.DARK_GRAY);
        btnSeDetails.setFont(new Font("Tahoma", Font.PLAIN, 10));
        btnSeDetails.setContentAreaFilled(false);
        btnSeDetails.setBorderPainted(false);
        btnSeDetails.setBorder(null);
        btnSeDetails.setActionCommand("");
        GroupLayout gl_panel_1 = new GroupLayout(panel_1);
        gl_panel_1.setHorizontalGroup(
        	gl_panel_1.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panel_1.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING, false)
        				.addComponent(lblSelectedNode, Alignment.LEADING)
        				.addComponent(lblSelectedEdge, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
        				.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
        					.addGap(10)
        					.addComponent(btnSnDetails))
        				.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
        					.addGap(10)
        					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING, false)
        						.addComponent(lblSeEdge, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)
        						.addComponent(btnSeDetails, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)))
        				.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
        					.addGap(9)
        					.addComponent(lblSnNode, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)))
        			.addContainerGap())
        );
        gl_panel_1.setVerticalGroup(
        	gl_panel_1.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panel_1.createSequentialGroup()
        			.addComponent(lblSelectedNode)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(lblSnNode)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(btnSnDetails)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(lblSelectedEdge, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(lblSeEdge)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(btnSeDetails, GroupLayout.PREFERRED_SIZE, 13, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_1.setLayout(gl_panel_1);
        pnlGraphProperties.setLayout(gl_pnlGraphProperties);

        JPanel pnlRightBottom = new JPanel();
        //pnlRightBottom.setLayout(new BoxLayout(pnlRightBottom, BoxLayout.LINE_AXIS));
        pnlRightBottom.setLayout(new FlowLayout());

        JLabel lblDomain = new JLabel("Domain:");
        pnlRightBottom.add(lblDomain);

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        spinner.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                CommonData.domainIndex = (int) ((JSpinner) e.getSource()).getModel().getValue();

                if (domains.contains(CommonData.domainIndex) == false)
                {
                    domains.add(CommonData.domainIndex);
                }
            }
        });
        pnlRightBottom.add(spinner);

        CommonData.domainIndex = 1;
        this.domains = new ArrayList<>();
        this.domains.add(CommonData.domainIndex);

        JButton btnRoute = new JButton("Set Domain Name");
        btnRoute.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String name = JOptionPane.showInputDialog("Enter domain name:");

                if (name != null && name.isEmpty() == false)
                {
                    CommonData.domainName = name;

                    if (routeNames.contains(name) == false)
                    {
                        routeNames.add(CommonData.domainName);
                    }
                }
            }
        });

        CommonData.domainName = "C";
        this.routeNames = new ArrayList<>();
        this.routeNames.add(CommonData.domainName);

        pnlRightBottom.add(btnRoute);

        JButton btnColor = new JButton("Set Color");
        btnColor.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                CommonData.currentColor = JColorChooser.showDialog(null, "Select switch color", CommonData.currentColor);
            }
        });

        CommonData.currentColor = Color.ORANGE;

        pnlRightBottom.add(btnColor);
        this.pnlMain.add(pnlRight);
        
        JPanel panel = new JPanel();
        panel.setAlignmentY(Component.TOP_ALIGNMENT);
        panel.setOpaque(false);
        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        panel.setBorder(new TitledBorder(null, "Algs", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        GroupLayout gl_pnlRight = new GroupLayout(pnlRight);
        gl_pnlRight.setHorizontalGroup(
        	gl_pnlRight.createParallelGroup(Alignment.TRAILING)
        		.addComponent(pnlGraphProperties, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 307, Short.MAX_VALUE)
        		.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
        		.addGroup(Alignment.LEADING, gl_pnlRight.createSequentialGroup()
        			.addComponent(pnlRightBottom, GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
        			.addContainerGap())
        );
        gl_pnlRight.setVerticalGroup(
        	gl_pnlRight.createParallelGroup(Alignment.TRAILING)
        		.addGroup(Alignment.LEADING, gl_pnlRight.createSequentialGroup()
        			.addComponent(pnlGraphProperties, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(panel, GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
        			.addGap(6)
        			.addComponent(pnlRightBottom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        
        JLabel lblBc = new JLabel("Betweenness Centrality:");
        lblBc.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblBc.setHorizontalAlignment(SwingConstants.LEFT);
        
        JLabel lblCcRw = new JLabel("Random Walk Closeness Centrality:");
        lblCcRw.setHorizontalTextPosition(SwingConstants.LEFT);
        lblCcRw.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblCcRw.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        
        //JButton btnCcRwDetails = new JButton("Show Details");
        btnCcRwDetails.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent arg0) {
        		showDetailsClosenessCentralityRw();
        	}
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		btnCcRwDetails.setForeground(Color.ORANGE);
        	}
        	@Override
        	public void mouseExited(MouseEvent e) {
        		btnCcRwDetails.setForeground(Color.BLACK);
        	}
        	@Override
        	public void mousePressed(MouseEvent e) {
        		btnCcRwDetails.setForeground(Color.RED);
        	}
        	@Override
        	public void mouseReleased(MouseEvent e) {
        		btnCcRwDetails.setForeground(Color.ORANGE);
        	}
        });
        btnCcRwDetails.setToolTipText("Click to view random walk closeness cenrality details!");
        btnCcRwDetails.setOpaque(false);
        btnCcRwDetails.setHorizontalAlignment(SwingConstants.LEFT);
        btnCcRwDetails.setForeground(Color.DARK_GRAY);
        btnCcRwDetails.setFont(new Font("Tahoma", Font.PLAIN, 10));
        btnCcRwDetails.setContentAreaFilled(false);
        btnCcRwDetails.setBorderPainted(false);
        btnCcRwDetails.setBorder(null);
        btnCcRwDetails.setActionCommand("");
        //btnCcRwDetails = new JButton("Show Details");
        
        //JLabel lblCcRwRunAlg = new JLabel("Please run algorithm to see details");
        lblCcRwRunAlg.setEnabled(false);
        //lblCcRwRunAlg = new JLabel("Please run algorithm to see details");
        //lblBcRunAlg = new JLabel("Please run algorithm to see details");
        
                
                
                //JLabel lblBcRunAlg = new JLabel("Please run algorithm to see details");
                lblBcRunAlg.setEnabled(false);
        
        
        
      
        btnBcDetails.setFont(new Font("Tahoma", Font.PLAIN, 10));
        btnBcDetails.setForeground(Color.DARK_GRAY);
        btnBcDetails.setActionCommand("");
        btnBcDetails.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        btnBcDetails.setContentAreaFilled(false);
        btnBcDetails.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent arg0) {
        		showDetailsBetweenCentrality();
        	}
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		btnBcDetails.setForeground(Color.ORANGE);
        	}
        	@Override
        	public void mouseExited(MouseEvent e) {
        		btnBcDetails.setForeground(Color.BLACK);
        	}
        	@Override
        	public void mousePressed(MouseEvent e) {
        		btnBcDetails.setForeground(Color.RED);
        	}
        	@Override
        	public void mouseReleased(MouseEvent e) {
        		btnBcDetails.setForeground(Color.ORANGE);
        	}
        });
        btnBcDetails.setToolTipText("Click to view betweenness cenrality details!");
        btnBcDetails.setHorizontalAlignment(SwingConstants.LEFT);
        btnBcDetails.setOpaque(false);
        btnBcDetails.setBorder(null);
        btnBcDetails.setBorderPainted(false);
        //lblBcNodeAvg = new JLabel("Nodes average betweennesscentrality:");
        
        //JButton btnCcDetails = new JButton("Show Details");
        btnCcDetails.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent arg0) {
        		showDetailsClosenessCentrality();
        	}
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		btnCcDetails.setForeground(Color.ORANGE);
        	}
        	@Override
        	public void mouseExited(MouseEvent e) {
        		btnCcDetails.setForeground(Color.BLACK);
        	}
        	@Override
        	public void mousePressed(MouseEvent e) {
        		btnCcDetails.setForeground(Color.RED);
        	}
        	@Override
        	public void mouseReleased(MouseEvent e) {
        		btnCcDetails.setForeground(Color.ORANGE);
        	}
        });
        btnCcDetails.setActionCommand("");
        btnCcDetails.setToolTipText("Click to view closeness cenrality details!");
        btnCcDetails.setOpaque(false);
        btnCcDetails.setHorizontalAlignment(SwingConstants.LEFT);
        btnCcDetails.setForeground(Color.DARK_GRAY);
        btnCcDetails.setFont(new Font("Tahoma", Font.PLAIN, 10));
        btnCcDetails.setContentAreaFilled(false);
        btnCcDetails.setBorderPainted(false);
        btnCcDetails.setBorder(null);
        //btnCcDetails = new JButton("Show Details");
        
        //JLabel lblCcRunAlg = new JLabel("Please run algorithm to see details");
        lblCcRunAlg.setEnabled(false);
        lblCcNodeAvg.setHorizontalAlignment(SwingConstants.CENTER);
        lblCcNodeAvg.setHorizontalTextPosition(SwingConstants.CENTER);
        //lblCcRwNodeAvgNumber = new JLabel("0");
        
        //JButton btnEvWDetails = new JButton("Show Details");
        btnEvWDetails.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent arg0) {
        		showDetailsEigenvectorCentralityW();
        	}
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		btnEvWDetails.setForeground(Color.ORANGE);
        	}
        	@Override
        	public void mouseExited(MouseEvent e) {
        		btnEvWDetails.setForeground(Color.BLACK);
        	}
        	@Override
        	public void mousePressed(MouseEvent e) {
        		btnEvWDetails.setForeground(Color.RED);
        	}
        	@Override
        	public void mouseReleased(MouseEvent e) {
        		btnEvWDetails.setForeground(Color.ORANGE);
        	}
        });
        btnEvWDetails.setToolTipText("Click to view weighted eigenvector cenrality details!");
        btnEvWDetails.setOpaque(false);
        btnEvWDetails.setHorizontalAlignment(SwingConstants.LEFT);
        btnEvWDetails.setForeground(Color.DARK_GRAY);
        btnEvWDetails.setFont(new Font("Tahoma", Font.PLAIN, 10));
        btnEvWDetails.setContentAreaFilled(false);
        btnEvWDetails.setBorderPainted(false);
        btnEvWDetails.setBorder(null);
        btnEvWDetails.setActionCommand("");
        //lblEvWNodeAvgNumber = new JLabel("0");
        
        //JLabel lblEvWRunAlg = new JLabel("Please run algorithm to see details");
        lblEvWRunAlg.setEnabled(false);
        //lblEvNodeAvgNumber = new JLabel("0");
        
        //JButton btnEvDetails = new JButton("Show Details");
        btnEvDetails.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent arg0) {
        		showDetailsEigenvectorCentrality();
        	}
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		btnEvDetails.setForeground(Color.ORANGE);
        	}
        	@Override
        	public void mouseExited(MouseEvent e) {
        		btnEvDetails.setForeground(Color.BLACK);
        	}
        	@Override
        	public void mousePressed(MouseEvent e) {
        		btnEvDetails.setForeground(Color.RED);
        	}
        	@Override
        	public void mouseReleased(MouseEvent e) {
        		btnEvDetails.setForeground(Color.ORANGE);
        	}
        });
        btnEvDetails.setToolTipText("Click to view eigenvector cenrality details!");
        btnEvDetails.setHorizontalAlignment(SwingConstants.LEFT);
        btnEvDetails.setContentAreaFilled(false);
        btnEvDetails.setActionCommand("");
        btnEvDetails.setOpaque(false);
        btnEvDetails.setForeground(Color.DARK_GRAY);
        btnEvDetails.setFont(new Font("Tahoma", Font.PLAIN, 10));
        btnEvDetails.setBorderPainted(false);
        btnEvDetails.setBorder(null);
        //btnEvDetails = new JButton("Show Details");
        
        //JLabel lblEvRunAlg = new JLabel("Please run algorithm to see details");
        lblEvRunAlg.setEnabled(false);
        //lblEvRunAlg = new JLabel("Please run algorithm to see details");
        
        JLabel lblCc = new JLabel("Closeness Centrality:");
        lblCc.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblCc.setHorizontalTextPosition(SwingConstants.LEFT);
        
        //JLabel lblHostsNumber = new JLabel(" ");
        
        JLabel lblEv = new JLabel("Eigenvector Centrality:");
        lblEv.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblEv.setHorizontalAlignment(SwingConstants.LEFT);
        
        JLabel lblEvW = new JLabel("Weighted Eigenvector Centrality:");
        lblEvW.setHorizontalAlignment(SwingConstants.LEFT);
        lblEvW.setFont(new Font("Tahoma", Font.BOLD, 12));
        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(
        	gl_panel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panel.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        				.addComponent(lblBc)
        				.addGroup(gl_panel.createSequentialGroup()
        					.addGap(10)
        					.addComponent(lblBcNodeAvg)
        					.addGap(18)
        					.addComponent(lblBcNodeAvgNumber, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
        				.addGroup(gl_panel.createSequentialGroup()
        					.addGap(10)
        					.addComponent(lblBcEdgeAvg, GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE)
        					.addGap(18)
        					.addComponent(lblBcEdgeAvgNumber, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
        				.addComponent(lblCc)
        				.addGroup(gl_panel.createSequentialGroup()
        					.addGap(10)
        					.addComponent(lblCcNodeAvg)
        					.addGap(6)
        					.addComponent(lblCcNodeAvgNumber, GroupLayout.PREFERRED_SIZE, 185, GroupLayout.PREFERRED_SIZE))
        				.addComponent(lblCcRw)
        				.addGroup(gl_panel.createSequentialGroup()
        					.addGap(10)
        					.addComponent(lblCcRwNodeAvg)
        					.addGap(6)
        					.addComponent(lblCcRwNodeAvgNumber, GroupLayout.PREFERRED_SIZE, 185, GroupLayout.PREFERRED_SIZE))
        				.addComponent(lblEv)
        				.addGroup(gl_panel.createSequentialGroup()
        					.addGap(10)
        					.addComponent(lblEvNodeAvg)
        					.addGap(6)
        					.addComponent(lblEvNodeAvgNumber, GroupLayout.PREFERRED_SIZE, 187, GroupLayout.PREFERRED_SIZE))
        				.addComponent(lblEvW)
        				.addGroup(gl_panel.createSequentialGroup()
        					.addGap(10)
        					.addComponent(lblEvWNodeAvg)
        					.addGap(6)
        					.addComponent(lblEvWNodeAvgNumber, GroupLayout.PREFERRED_SIZE, 187, GroupLayout.PREFERRED_SIZE))
        				.addGroup(gl_panel.createSequentialGroup()
        					.addGap(10)
        					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        						.addComponent(lblEvWRunAlg)
        						.addComponent(btnEvWDetails)))
        				.addGroup(gl_panel.createSequentialGroup()
        					.addGap(10)
        					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        						.addComponent(lblEvRunAlg)
        						.addComponent(btnEvDetails)))
        				.addGroup(gl_panel.createSequentialGroup()
        					.addGap(10)
        					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        						.addComponent(lblCcRwRunAlg)
        						.addComponent(btnCcRwDetails)))
        				.addGroup(gl_panel.createSequentialGroup()
        					.addGap(10)
        					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        						.addComponent(lblCcRunAlg)
        						.addComponent(btnCcDetails)))
        				.addGroup(gl_panel.createSequentialGroup()
        					.addGap(10)
        					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        						.addComponent(lblBcRunAlg)
        						.addComponent(btnBcDetails, GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE))))
        			.addContainerGap())
        );
        gl_panel.setVerticalGroup(
        	gl_panel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panel.createSequentialGroup()
        			.addComponent(lblBc)
        			.addGap(6)
        			.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        				.addComponent(lblBcNodeAvg)
        				.addComponent(lblBcNodeAvgNumber))
        			.addGap(6)
        			.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        				.addComponent(lblBcEdgeAvg)
        				.addComponent(lblBcEdgeAvgNumber))
        			.addGap(6)
        			.addComponent(btnBcDetails)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(lblBcRunAlg)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(lblCc)
        			.addGap(6)
        			.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        				.addComponent(lblCcNodeAvg)
        				.addComponent(lblCcNodeAvgNumber))
        			.addGap(6)
        			.addComponent(btnCcDetails)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(lblCcRunAlg)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(lblCcRw)
        			.addGap(6)
        			.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        				.addComponent(lblCcRwNodeAvg)
        				.addComponent(lblCcRwNodeAvgNumber))
        			.addGap(6)
        			.addComponent(btnCcRwDetails)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(lblCcRwRunAlg)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(lblEv)
        			.addGap(6)
        			.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        				.addComponent(lblEvNodeAvg)
        				.addComponent(lblEvNodeAvgNumber))
        			.addGap(6)
        			.addComponent(btnEvDetails)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(lblEvRunAlg)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(lblEvW)
        			.addGap(6)
        			.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        				.addComponent(lblEvWNodeAvg)
        				.addComponent(lblEvWNodeAvgNumber))
        			.addGap(6)
        			.addComponent(btnEvWDetails)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(lblEvWRunAlg)
        			.addContainerGap(27, Short.MAX_VALUE))
        );
        panel.setLayout(gl_panel);
        pnlRight.setLayout(gl_pnlRight);

    }
	
	
	
	
	
	
	// GUI Menu Initialization
    private void initialize()
    {
    	
        JMenu menuFile = new JMenu("File");
        this.menuBar.add(menuFile);
        
        JMenu saveToMenu = new JMenu("Save To");
        menuFile.add(saveToMenu);

/*        JMenuItem exportItem = new JMenuItem("JSON");
        exportItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                export();                
            }
        });*/
        
/*        JMenuItem exportPDFItem = new JMenuItem("PDF");
        exportPDFItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {     
                ExportDialog dialog = new ExportDialog();                
                dialog.showExportDialog(null, "Export", viewer, "Export");
            }
        });*/
        
        //saveToMenu.add(exportPDFItem);
        //saveToMenu.add(exportItem);
        

        JMenuItem closeItem = new JMenuItem("Close");
        closeItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                close();
            }
        });

        JMenu mouseMode = new JMenu("Mode");
        this.menuBar.add(mouseMode);

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
        
        mouseMode.add(editModeItem);
        mouseMode.add(pickModeItem);
        mouseMode.add(transModeItem);
        //menuFile.add(exportItem);
        menuFile.add(closeItem);

        JMenu nodeMenu = new JMenu("Node Type");

        ButtonGroup nodeGroup = new ButtonGroup();

        JRadioButtonMenuItem hostItem = new JRadioButtonMenuItem("Host");
        hostItem.setSelected(false);
        nodeMenu.add(hostItem);

        hostItem.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if (e.getStateChange() == ItemEvent.SELECTED)
                {
                    //common.CommonData.selectedNode = common.CommonData.NodeType.Host;
                	common.CommonData.selectedNode = "Host";
                }
            }
        });

        JRadioButtonMenuItem switchItem = new JRadioButtonMenuItem("Switch");
        switchItem.setSelected(true);
        nodeMenu.add(switchItem);

        switchItem.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if (e.getStateChange() == ItemEvent.SELECTED)
                {
                    //common.CommonData.selectedNode = common.CommonData.NodeType.Switch;
                    common.CommonData.selectedNode = "Switch";
                }
            }
        });

        JRadioButtonMenuItem controllerItem = new JRadioButtonMenuItem("Controller");
        controllerItem.setSelected(false);
        nodeMenu.add(controllerItem);

        controllerItem.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if (e.getStateChange() == ItemEvent.SELECTED)
                {
                    //common.CommonData.selectedNode = common.CommonData.NodeType.Controller;
                    common.CommonData.selectedNode = "Controller";
                }
            }
        });

        nodeGroup.add(hostItem);
        nodeGroup.add(switchItem);
        nodeGroup.add(controllerItem);

        //common.CommonData.selectedNode = common.CommonData.NodeType.Switch;
        common.CommonData.selectedNode = "Switch";
        //nodeGroup.setSelected(switchItem, true);
        
        
   
        /*
         * add check if graph is empty
         */
        JMenu menuAlgs = new JMenu("Algs");
        this.menuBar.add(menuAlgs);
        
        //weighted shortestpath
        JMenuItem shortestPathWeightedItem = new JMenuItem("Shortest Path With Weights");
        menuAlgs.add(shortestPathWeightedItem);
        shortestPathWeightedItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	// check if graph has errors
				if (isGraphCreated() && isSourceDestSelected()) {
					// run algorithm
					generateShortestPathWithWeight();
				}
            }
        });
        // unweighted shortestPath selector
        JMenuItem shortestPathItem = new JMenuItem("Unweighted Shortest Path");
        menuAlgs.add(shortestPathItem);
        shortestPathItem.addActionListener(new ActionListener()
        {
			@Override
			public void actionPerformed(ActionEvent e) {
				// check if graph has errors
				if (isGraphCreated() && isSourceDestSelected()) {
					generateShortestPathWithoutWeight();
				}
			}
        });
       // randomWalk selector
        JMenuItem randomWalkItem = new JMenuItem("Random Walk");
        menuAlgs.add(randomWalkItem);
        randomWalkItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
				// check if graph has errors
				if (isGraphCreated() && isSourceDestSelected()) {
	            	generateRandomWalk();
				} 
            }
        });
        // spanning tree selector
        JMenuItem spanningTreeItem = new JMenuItem("Spanning Tree");
        menuAlgs.add(spanningTreeItem);
        spanningTreeItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
				// check if graph has nodes and edges
				if (isGraphCreated()) {
            		createSpanningTree();
				}
            }
        });
        
        // TODO FIX IF ONE NODE IS ISOLATED THE WHILE DOESNT STOP!
        // random spanning tree selector
        JMenuItem randomSpanningTreeItem = new JMenuItem("Random Spanning Tree");
        menuAlgs.add(randomSpanningTreeItem);
        randomSpanningTreeItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	if (isGraphCreated()) {
            		createRandomSpanningTree();
            	}
            }
        });
        
        // Betweenness centrality
        JMenuItem betweennessCentralityItem = new JMenuItem("Betweenness Centrality");
        menuAlgs.add(betweennessCentralityItem);
        betweennessCentralityItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	if (isGraphCreated()) {
            		getbetweennessCentrality();
            	}
            }
        });
        // closeness centrality
        JMenuItem closenessCentralitylityItem = new JMenuItem("Closeness Centrality");
        menuAlgs.add(closenessCentralitylityItem);
        closenessCentralitylityItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	if (isGraphCreated()) {
            		getclosenessCentrality();
            	}
            }
        });
        // Random Walks closeness centrality
        JMenuItem RWclosenessCentralitylityItem = new JMenuItem("Random Walk Closeness Centrality");
        menuAlgs.add(RWclosenessCentralitylityItem);
        RWclosenessCentralitylityItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	if (isGraphCreated()) {
            		getRWclosenessCentrality();
            	}
            }
        });
        // EigenvectorCentrality centrality with weights
        JMenuItem eigenvectorCentralityItem = new JMenuItem("Eigenvector Centrality With Weights");
        menuAlgs.add(eigenvectorCentralityItem);
        eigenvectorCentralityItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	if (isGraphCreated()) {
            		geteigenvectorCentrality(true);
            	}
            }
        });
        // EigenvectorCentrality centrality without weights
        JMenuItem eigenvectorCentralityWOWItem = new JMenuItem("Eigenvector Centrality Without Weights");
        menuAlgs.add(eigenvectorCentralityWOWItem);
        eigenvectorCentralityWOWItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	if (isGraphCreated()) {
            		geteigenvectorCentrality(false);
            	}
            }
        });
        
        JMenu toolsMenu = new JMenu("Tools");
        JMenu graphMenu = new JMenu("Graph");
        JMenu generateMenu = new JMenu("Generate");
        
        JMenuItem smallWorldItem = new JMenuItem("Small World");
        smallWorldItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                generateSmallWorld();
            }
        });

        JMenuItem randomGraphItem = new JMenuItem("Random Graph");
        randomGraphItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                generateRandomGraph();
            }
        });

        JMenu networkMenu = new JMenu("Network");
        JMenuItem generateHosts = new JMenuItem("Generate Hosts");
        generateHosts.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                generateHosts();
            }
        });
        
        JMenuItem addController = new JMenuItem("Add Controller");
        addController.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	addController();

                
            }
        });
        
        //saved to saved1
                        
        JMenu layoutMenu = new JMenu("Layout");
        //JMenuItem s
        JMenuItem staticLayout =  new JMenuItem("Static Layout");
        staticLayout.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	if(isGraphCreated())
            		viewer.setGraphLayout(new StaticLayout<>(graph));
            }
        });
        
        JMenuItem isomLayout = new JMenuItem("ISOMLayout");
        isomLayout.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	if(isGraphCreated())
                	viewer.setGraphLayout(new ISOMLayout<>(graph));
            }
        });

        JMenuItem springLayout = new JMenuItem("Spring Layout");
        springLayout.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	if(isGraphCreated())
            		viewer.setGraphLayout(new SpringLayout<>(graph));
            }
        });
        
        JMenuItem circleLayout = new JMenuItem("Circle Layout");
        circleLayout.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	if(isGraphCreated())
            		viewer.setGraphLayout(new CircleLayout<>(graph));
            }
        });
        
        JMenuItem refreshGraphItem = new JMenuItem("Refresh Graph");
        //menuAlgs.add(refreshGraphItem);
        refreshGraphItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	if (isGraphCreated()) {
            		refreshGraph();
            	}
            }
        });
        
        
        
        
        layoutMenu.add(staticLayout);
        layoutMenu.add(isomLayout);
        layoutMenu.add(springLayout);
        layoutMenu.add(circleLayout);
        
        networkMenu.add(generateHosts);
        networkMenu.add(addController);
        //networkMenu.add(randomRouting);
        //networkMenu.add(setCurentRoute);
       // networkMenu.add(setRoutingCurrentRoute);
        //networkMenu.add(setNewRoute);
        
        
        generateMenu.add(smallWorldItem);
        generateMenu.add(randomGraphItem);
        
        graphMenu.add(generateMenu);
        
        toolsMenu.add(graphMenu);        
        toolsMenu.add(networkMenu);
        toolsMenu.add(layoutMenu);
        toolsMenu.add(refreshGraphItem);
        
        this.menuBar.add(nodeMenu);
        this.menuBar.add(toolsMenu);

        //this.nodes = new ArrayList<>();

    }
    /**
     * refreshGraph method
     */
    private void refreshGraph(){
//    	common.CommonData.destinationNode = null;
//    	common.CommonData.sourceNode = null;
		if (newEdgesList != null && edgesList != null) {
			for (Link l : newEdgesList)
				this.graph.removeEdge(l);
			for (Link l : edgesList)
				this.graph.addEdge(l, l.getNode_left(), l.getNode_right());
		}
    	
        this.graph.getVertices().stream().forEach(
                (Node n) ->
                {
                	n.setRouteType("");
                }
        );
        this.graph.getEdges().stream().forEach(
        		(Link l) -> {
        			if(l.getNode_left() instanceof Controller || l.getNode_right() instanceof Controller){
        	        	l.setArrowType("controllerEdge");
        	        	//System.out.println("conEdge");
        	        } else {
        	        	l.setArrowType("");
        	        }
        		}
        		);
        
        newEdgesList = null;
        edgesList = null;
        //this.viewer.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Node,Link>());
        this.viewer.repaint();
        this.initToplbl("");
        //update side pnl
        updateSidePnl();
    }
    
    private void generateRandomGraph()
    {
    	// re-init common variables
    	common.CommonData.destinationNode = null;
    	common.CommonData.sourceNode = null;
		common.CommonData.switchCount = 0;
		common.CommonData.hostsCount = 0;
		common.CommonData.controllerCount = 0;
		common.CommonData.selectedNode = "Switch";
        newEdgesList = null;
        edgesList = null;
        updateSidePnlCentralities();
		
		
		// generates switches
        this.graph = new RandomGraphGenerator(new EdgeFactory(), new VertexFactory(), 6, 8, 0.5f).create();
        // adds controller
        addController();
        // adds hosts to the switches
        generateHosts();
        this.viewer.setGraphLayout(new ISOMLayout<Node, Link>(this.graph));
    }
    
    /**
     * ShortestPathWithoutWeights
     */
    private void generateShortestPathWithoutWeight(){
    	// refresh graph before running
    	refreshGraph();
    	CommonData.selectedVertex = null;
    	// init lists
    	List<Node> shortestPathResults;
    	edgesList = new ArrayList<Link>();
    	newEdgesList = new ArrayList<Link>();
    	List<Node> nodesList = new ArrayList<Node>();
    	// change mouseType to picking
    	graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
    	//run algorithm to find shortestPath
    	shortestPathResults = ShortestPath.withoutWeights(this.graph, CommonData.sourceNode, CommonData.destinationNode);
    	// create string builder to display it on topPnl
		StringBuilder strTmp = new StringBuilder();
    	strTmp.append("Unweighted shortest path is: ");
    	for(int i=0; i< shortestPathResults.size(); i++){
    		if(i == shortestPathResults.size()-1){
				// appent string
    			strTmp.append(shortestPathResults.get(i).getSid());
    		}
    		else{
    			strTmp.append(shortestPathResults.get(i).getSid() + ", ");
    			Link currentEdge = this.graph.findEdge(shortestPathResults.get(i), shortestPathResults.get(i+1));
				// add all randomWalk path edges to list
    			edgesList.add(currentEdge);
    		}
			// set Nodes routeType to change their color
    		shortestPathResults.get(i).setRouteType("SP");
			// add all randomWalk path node to list
    		nodesList.add(shortestPathResults.get(i));
    	}
		// remove old edges from graph
    	for(Link l : edgesList){
    		this.graph.removeEdge(l);
    	}
    	// change edges to directed type edges and keep their capacity
    	for(int i=0; i < nodesList.size()-1; i++){
	    	for(Link l : edgesList){
	    		if(l.getNode_left() == nodesList.get(i) && l.getNode_right() == nodesList.get(i+1)){
		    		l.setArrowType("SP");
		    		this.graph.addEdge(l, l.getNode_left(), l.getNode_right(), EdgeType.DIRECTED);
		    		newEdgesList.add(l);
	    		} else if(l.getNode_left() == nodesList.get(i+1) && l.getNode_right() == nodesList.get(i)){
		    		l.setArrowType("SP");
		    		this.graph.addEdge(l, l.getNode_right(), l.getNode_left(), EdgeType.DIRECTED);
		    		newEdgesList.add(l);
	    		}
	    	}
    	}
		// update topLBL
    	this.viewer.repaint();
    	this.initToplbl(strTmp.toString());
    }
    
 /**
  * ShortestPathWithtWeights
  */
    private void generateShortestPathWithWeight(){
    	// refresh graph before running
    	refreshGraph();
    	CommonData.selectedVertex = null;
    	// init lists
    	List<Node> shortestPathResults;
    	edgesList = new ArrayList<Link>();
    	newEdgesList = new ArrayList<Link>();
    	List<Node> nodesList = new ArrayList<Node>();
    	// change mouseType to picking
    	graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
    	//run algorithm to find shortestPath
    	shortestPathResults = ShortestPath.withWeights(this.graph, CommonData.sourceNode, CommonData.destinationNode);
    	// create string builder to display it on topPnl
		StringBuilder strTmp = new StringBuilder();
    	strTmp.append("Weighted shortest path is: ");
    	for(int i=0; i< shortestPathResults.size(); i++){
    		if(i == shortestPathResults.size()-1){
				// appent string
    			strTmp.append(shortestPathResults.get(i).getSid());
    		}
    		else{
    			strTmp.append(shortestPathResults.get(i).getSid() + ", ");
    			Link currentEdge = this.graph.findEdge(shortestPathResults.get(i), shortestPathResults.get(i+1));
				// add all randomWalk path edges to list
    			edgesList.add(currentEdge);
    		}
			// set Nodes routeType to change their color
    		shortestPathResults.get(i).setRouteType("SP");
			// add all randomWalk path node to list
    		nodesList.add(shortestPathResults.get(i));
    	}
		// remove old edges from graph
    	for(Link l : edgesList){
    		this.graph.removeEdge(l);
    	}
    	// change edges to directed type edges and keep their capacity
    	for(int i=0; i < nodesList.size()-1; i++){
	    	for(Link l : edgesList){
	    		// if there is i -> i+1 edge create
	    		if(l.getNode_left() == nodesList.get(i) && l.getNode_right() == nodesList.get(i+1)){
		    		l.setArrowType("SP");
		    		this.graph.addEdge(l, l.getNode_left(), l.getNode_right(), EdgeType.DIRECTED);
		    		newEdgesList.add(l);
		    		// if there is no i -> i+1 edge create it in reverse direction
	    		} else if(l.getNode_left() == nodesList.get(i+1) && l.getNode_right() == nodesList.get(i)){
		    		l.setArrowType("SP");
		    		this.graph.addEdge(l, l.getNode_right(), l.getNode_left(), EdgeType.DIRECTED);
		    		newEdgesList.add(l);
	    		}
	    	}
    	}
		// update topLBL
    	this.viewer.repaint();
    	this.initToplbl(strTmp.toString());
    }
    /**
     * generateRandomWalk method
     * 
     */
    private void generateRandomWalk(){
    	// refresh graph before running
    	refreshGraph();
    	CommonData.selectedVertex = null;
    	// init lists
    	List<Node> randomwalkresult;
    	edgesList = new ArrayList<Link>();
    	newEdgesList = new ArrayList<Link>();
    	List<Node> nodesList = new ArrayList<Node>();
    	// change mouseType to picking
    	graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
    	//run algorithm to find randomwalk
    	randomwalkresult = new RandomWalk(this.graph).searchNetwork(CommonData.sourceNode, CommonData.destinationNode, 5);
    	// create string builder to display it on topPnl
    	StringBuilder strTmp = new StringBuilder();
    	strTmp.append("Random walk path is: ");
    	for(int i=0; i< randomwalkresult.size(); i++){
    		if(i == randomwalkresult.size()-1){
    			// appent string
    			strTmp.append(randomwalkresult.get(i).getSid());
    		}
    		else{
    			strTmp.append(randomwalkresult.get(i).getSid() + ", ");
    			Link currentEdge = this.graph.findEdge(randomwalkresult.get(i), randomwalkresult.get(i+1));
    			// add all randomWalk path edges to list
    			edgesList.add(currentEdge);
    		}
    		// set Nodes routeType to change their color
    		randomwalkresult.get(i).setRouteType("SP");
    		// add all randomWalk path node to list
    		nodesList.add(randomwalkresult.get(i));
    	}
    	// remove old edges from graph
    	for(Link l : edgesList){
    		this.graph.removeEdge(l);
    	}
    	// create new edges from randomWalk path with arrows
    	for(int i=0; i < nodesList.size()-1; i++){
    		CommonData.nodeLeft = nodesList.get(i);
            CommonData.nodeRight = nodesList.get(i+1);
            // create new edge
            Link l = new EdgeFactory().create();
            // set edgeType to SP
            l.setArrowType("SP");
            // save all newly added edges to remove them later
            newEdgesList.add(l);
            // add directed type edges
    		this.graph.addEdge(l, l.getNode_left(), l.getNode_right(), EdgeType.DIRECTED);
    	}
    	this.viewer.repaint();
    	// update topLBL
    	this.initToplbl(strTmp.toString());
    }
    /**
     * createSpanningTree
     */
    private void createSpanningTree(){
    	refreshGraph();
    	CommonData.selectedVertex = null;
    	graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
    	SpanningTree kek = new SpanningTree(this.graph);
    	kek.pack();
    	kek.setVisible(true);
    }
    /**
     * createSpanningTree (without weights)
     */
    private void createRandomSpanningTree(){
    	refreshGraph();
    	CommonData.selectedVertex = null;
    	graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
    	RandomSpanningTree kek = new RandomSpanningTree(this.graph);
    	kek.pack();
    	kek.setVisible(true);
    }
	/**
	 * getbetweennessCentrality
	 */
    private void getbetweennessCentrality(){
    	refreshGraph();
    	BetweennessCentralityAlg bc = new BetweennessCentralityAlg(this.graph);
    	//DecimalFormat df2 = new DecimalFormat(".##");
        lblBcNodeAvgNumber.setText(""+String.format("%.03f", bc.getNodeAvgBC()));
        lblBcEdgeAvgNumber.setText(""+String.format("%.03f", bc.getEdgeAvgBC()));
    	
        nodeMapBC = bc.getNodeBC();
    	linkMapBC = bc.getEdgeBC();
        
    	initToplbl("Betweenness Centrality algorithm completed!");
    	initbetweennessCentrality(true);
    }
    
    // getclosenessCentrality
    private void getclosenessCentrality(){
    	// refresh graph
    	refreshGraph();
    	// run algorithm
    	ClosenessCentralityAlg cc = new ClosenessCentralityAlg(this.graph);
    	// format double vars
    	//DecimalFormat df2 = new DecimalFormat(".##");
    	// set lbls on right panel
        lblCcNodeAvgNumber.setText(cc.getNodeHighestCC().getToolTip()+" with score of "+String.format("%.03f", cc.getScoreHighestCC()));
        //update map
        nodeMapCC = cc.getNodeCC();
        
        initToplbl("Closeness Centrality algorithm completed!");
        
        //show rightpanel
        initClosenessCentrality(true);
    }

    // getRWclosenessCentrality
    private void  getRWclosenessCentrality(){
    	// refresh graph
    	refreshGraph();
    	// run algorithm
    	RandomWalkCentrality ccrw = new RandomWalkCentrality(this.graph);
    	// set lbls on right panel
        lblCcRwNodeAvgNumber.setText(ccrw.getNodeHighestCC().getToolTip()+" with score of "+String.format("%.03f", ccrw.getScoreHighestCC()));
        //update map
        nodeMapRWCC = ccrw.getNodeCC();
        initToplbl("Random Walk Closeness Centrality algorithm completed!");
        //show rightpanel
        initClosenessCentralityRw(true);
    }
    
    //  geteigenvectorCentrality
    private void geteigenvectorCentrality(boolean weighted){
    	// refresh graph
    	refreshGraph();
    	// run algorithm
    	EigenvectorCentralityAlg evc = new EigenvectorCentralityAlg(this.graph, weighted);
    	if(!evc.getIsWeighted()){
            lblEvNodeAvgNumber.setText(evc.getNodeHighestEVC().getToolTip()+" with score of "+String.format("%.03f", evc.getScoreHighestEVC()));
            nodeMapEVC = evc.getNodeEVC();
            initToplbl("Eigenvector Centrality algorithm completed!");
            initEigenvectorCentrality(true);
    	} else {
            lblEvWNodeAvgNumber.setText(evc.getNodeHighestEVC().getToolTip()+" with score of "+String.format("%6.3e", evc.getScoreHighestEVC()));
            nodeMapEVCW = evc.getNodeEVC();
            initToplbl("Weighted Eigenvector Centrality algorithm completed!");
            initEigenvectorCentralityW(true);
    	}
    }
    
    
    private void generateSmallWorld()
    {
    	if (newEdgesList != null && edgesList != null) {
    		refreshGraph();
    	}
    	common.CommonData.destinationNode = null;
    	common.CommonData.sourceNode = null;
		common.CommonData.switchCount = 0;
		common.CommonData.hostsCount = 0;
		common.CommonData.controllerCount = 0;
        newEdgesList = null;
        edgesList = null;
		
		
		//common.CommonData.selectedNode = "Switch";
        this.graph = new WattsStrogatzSmallWorldGenerator(new EdgeFactory(), new VertexFactory(), 50, 8, 0.2f).create();                
        this.viewer.setGraphLayout(new ISOMLayout<Node, Link>(this.graph));
        //update side pnl
        updateSidePnl();
    }
    private void generateHosts()
    {
    	if(hasSwitch()){
        Queue<Link> queue = new LinkedList<>();
        this.graph.getVertices().stream().filter((n)-> n instanceof Switch).forEach(
                (Node n) ->
                {
                    Host h = new Host();

                    CommonData.nodeLeft = n;
                    CommonData.nodeRight = h;
                    common.CommonData.hostsCount = common.CommonData.hostsCount + 1;
                    queue.add(new EdgeFactory().create());
                }
        );
        
        queue.stream().forEach(e -> this.graph.addEdge(e, e.getNode_left(), e.getNode_right()));                        
        this.viewer.setGraphLayout(new ISOMLayout<Node, Link>(this.graph));
    	}
        //update side pnl
        updateSidePnl();
    }
    private void addController(){
    	if(hasSwitch()){
        Controller c = new Controller();
        Queue<Link> queue = new LinkedList<>();
        
        graph.getVertices().stream().filter(node -> node instanceof Switch).forEach(node ->
        {
           common.CommonData.nodeLeft = c;
           common.CommonData.nodeRight = node;                                     
                              
           queue.add(new EdgeFactory().create());
           
        });
        
        queue.stream().forEach(l -> graph.addEdge(l, l.getNode_left(), l.getNode_right()));
        //graph.addEdge(null, c, c, null);
        this.viewer.setGraphLayout(new ISOMLayout<Node, Link>(this.graph));
        //this.viewer.repaint();
        
        common.CommonData.controllerCount = common.CommonData.controllerCount + 1;
        
        //update side pnl
        updateSidePnl();
    	}
    }

    private void close()
    {
        this.dispose();
        System.exit(0);
    }
	
	/** 
	 * Methods to check errors and popup msgs
	 */
	private boolean isGraphCreated(){
		if(this.graph.getEdgeCount() > 0 && this.graph.getVertexCount() > 0){
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "The graph has no edges or nodes, please create a graph.",
					"No Graph", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	private boolean hasSwitch(){
		//boolean kek = false;
		if(common.CommonData.switchCount > 0){
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "The graph has no switches, please create a switch node.",
					"No Switch Node", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	private boolean isSourceDestSelected(){
		if(CommonData.sourceNode != null && CommonData.destinationNode != null){
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "Please select source and destination nodes.\nRight click a node to select it as source or destination.",
					"No nodes selected", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
    
	// 
	private void initbetweennessCentrality(boolean bcEnabled){
			lblBcNodeAvgNumber.setVisible(bcEnabled);
			lblBcEdgeAvgNumber.setVisible(bcEnabled);
			lblBcNodeAvg.setVisible(bcEnabled);
			lblBcEdgeAvg.setVisible(bcEnabled);
			btnBcDetails.setVisible(bcEnabled);
			lblBcRunAlg.setVisible(!bcEnabled);
			if(bcEnabled == false){
				/*
				 * maps are set in getBC method
				 */
				nodeMapBC = null;
				linkMapBC = null;
			}
	}
	private void initClosenessCentrality(boolean bcEnabled){
		lblCcNodeAvg.setVisible(bcEnabled);
		lblCcNodeAvgNumber.setVisible(bcEnabled);
		btnCcDetails.setVisible(bcEnabled);
		lblCcRunAlg.setVisible(!bcEnabled);
		if(bcEnabled == false){
			/*
			 * maps are set in getBC method
			 */
			nodeMapCC = null;
		}
	}
	private void initClosenessCentralityRw(boolean bcEnabled){
		lblCcRwNodeAvg.setVisible(bcEnabled);
		lblCcRwNodeAvgNumber.setVisible(bcEnabled);
		btnCcRwDetails.setVisible(bcEnabled);
		lblCcRwRunAlg.setVisible(!bcEnabled);
		if(bcEnabled == false){
			/*
			 * maps are set in getBC method
			 */
			nodeMapRWCC = null;
			//linkMapBC = null;
		}
	}
	private void initEigenvectorCentrality(boolean bcEnabled){
		lblEvNodeAvg.setVisible(bcEnabled);
		lblEvNodeAvgNumber.setVisible(bcEnabled);
		btnEvDetails.setVisible(bcEnabled);
		lblEvRunAlg.setVisible(!bcEnabled);
		if(bcEnabled == false){
			/*
			 * maps are set in getBC method
			 */
			nodeMapEVC = null;
			//linkMapBC = null;
		}
	}
	private void initEigenvectorCentralityW(boolean bcEnabled){
		lblEvWNodeAvg.setVisible(bcEnabled);
		lblEvWNodeAvgNumber.setVisible(bcEnabled);
		btnEvWDetails.setVisible(bcEnabled);
		lblEvWRunAlg.setVisible(!bcEnabled);
		if(bcEnabled == false){
			/*
			 * maps are set in getBC method
			 */
			nodeMapEVCW = null;
			//linkMapBC = null;
		}
	}
	
	
	
	/**
	 * showDetailsBetweenCentrality()
	 */
	private void showDetailsBetweenCentrality() {
		// if maps isnt null -> create popup and show BC details from maps.
		// else do nothing (maybe display on topPnl that need to run BC)
		double maxNodeScore = 0.0;
		String maxNodeScoreNode = null;
		double maxEdgeScore = 0.0;
		String maxEdgeScoreEdge = null;
		if (nodeMapBC != null && linkMapBC != null) {
			// DecimalFormat df2 = new DecimalFormat(".##");
			StringBuilder bcDetails = new StringBuilder();
			bcDetails.append("Nodes Betweenness Centrality:\n");
			for (Map.Entry<Node, Double> entry : nodeMapBC.entrySet()) {
				String key = entry.getKey().getToolTip();
				double value = entry.getValue();
				if (value > maxNodeScore) {
					maxNodeScore = value;
					maxNodeScoreNode = key;
				}
				bcDetails.append("Node:"+"\t"+key + "\t" + "Score:" + "\t" + String.format("%.03f", value) + "\n");
			}
			bcDetails.append("\nHighest Betweenness Centrality Node:\n");
			bcDetails.append(maxNodeScoreNode + " with score of: " + String.format("%.03f", maxNodeScore)+"\n");
			bcDetails.append("\nEdges Betweenness Centrality:\n");
			for (Map.Entry<Link, Double> entry : linkMapBC.entrySet()) {
				String key = entry.getKey().toString();
				double value = entry.getValue();
				if (value > maxEdgeScore) {
					maxEdgeScore = value;
					maxEdgeScoreEdge = key;
				}
				bcDetails.append("Edge:"+"\t"+key + "\t" + "Score:" + "\t" + String.format("%.03f", value) + "\n");
			}
			bcDetails.append("\nHighest Betweenness Centrality Edge:\n");
			bcDetails.append(maxEdgeScoreEdge + " with score of: " + String.format("%.03f", maxEdgeScore));
			showLongTextMessageInDialog(bcDetails.toString(), this, "Betweenness Centrality Details");
		} else {
			initToplbl("Please run Betweenness Centrality algorithm to see details.");
		}
	}

	/** showDetailsClosenessCentrality()
	 * 
	 */
	private void showDetailsClosenessCentrality() {
		if (nodeMapCC != null) {
			double maxScore = 0.0;
			String maxScoreNode = null;
			StringBuilder ccDetails = new StringBuilder();
			ccDetails.append("Nodes ClosenessCentrality:\n");
			for (Map.Entry<Node, Double> entry : nodeMapCC.entrySet()) {
				String key = entry.getKey().getToolTip();
				double value = entry.getValue();
				if (value > maxScore) {
					maxScore = value;
					maxScoreNode = entry.getKey().getToolTip();
				}
				ccDetails.append("Node:"+"\t"+key + "\t" + "Score:" + "\t" + String.format("%.03f", value) + "\n");
			}
			ccDetails.append("\nHighest Closeness Centrality Node:\n");
			ccDetails.append(maxScoreNode + " with score of: " + String.format("%.03f", maxScore));
			showLongTextMessageInDialog(ccDetails.toString(), this, "Closeness Centrality Details");
		} else {
			initToplbl("Please run Closeness Centrality algorithm to see details.");
		}
	}
	/** showDetailsClosenessCentralityRw
	 * 
	 */
	private void showDetailsClosenessCentralityRw(){
		//nodeMapRWCC
		if (nodeMapRWCC != null) {
			double maxScore = 0.0;
			String maxScoreNode = null;
			StringBuilder ccDetails = new StringBuilder();
			ccDetails.append("Nodes RandomWalk ClosenessCentrality:\n");
			for (Map.Entry<Node, Double> entry : nodeMapRWCC.entrySet()) {
				String key = entry.getKey().getToolTip();
				double value = entry.getValue();
				if (value > maxScore) {
					maxScore = value;
					maxScoreNode = entry.getKey().getToolTip();
				}
				ccDetails.append("Node:"+"\t"+key + "\t" + "Score:" + "\t" + String.format("%.03f", value) + "\n");
			}
			ccDetails.append("\nHighest RandomWalk Closeness Centrality Node:\n");
			ccDetails.append(maxScoreNode + " with score of: " + String.format("%.03f", maxScore));
			showLongTextMessageInDialog(ccDetails.toString(), this, "RandomWalk Closeness Centrality Details");
		} else {
			initToplbl("Please run RandomWalk Closeness Centrality algorithm to see details.");
		}
	}
	// showDetailsEigenvectorCentrality()
	private void showDetailsEigenvectorCentrality(){
		if (nodeMapEVC != null) {
			double maxScore = 0.0;
			String maxScoreNode = null;
			StringBuilder evDetails = new StringBuilder();
			evDetails.append("Nodes Eigenvector Centrality:\n");
			for (Map.Entry<Node, Double> entry : nodeMapEVC.entrySet()) {
				String key = entry.getKey().getToolTip();
				double value = entry.getValue();
				if (value > maxScore) {
					maxScore = value;
					maxScoreNode = entry.getKey().getToolTip();
				}
				evDetails.append("Node:"+"\t"+key + "\t" + "Score:" + "\t" + String.format("%.03f", value) + "\n");
			}
			evDetails.append("\nHighest Eigenvector Centrality Node:\n");
			evDetails.append(maxScoreNode + " with score of: " + String.format("%.03f", maxScore));
			showLongTextMessageInDialog(evDetails.toString(), this, "Eigenvector Centrality Details");
		} else {
			initToplbl("Please run Eigenvector Centrality algorithm to see details.");
		}
	}

	//  showDetailsEigenvectorCentralityW()
	private void showDetailsEigenvectorCentralityW(){
		if (nodeMapEVCW != null) {
			double maxScore = 0.0;
			String maxScoreNode = null;
			StringBuilder evDetails = new StringBuilder();
			evDetails.append("Nodes Weighted Eigenvector Centrality:\n");
			for (Map.Entry<Node, Double> entry : nodeMapEVCW.entrySet()) {
				String key = entry.getKey().getToolTip();
				double value = entry.getValue();
				if (value > maxScore) {
					maxScore = value;
					maxScoreNode = entry.getKey().getToolTip();
				}
				evDetails.append("Node:"+"\t"+key + "\t" + "Score:" + "\t" + String.format("%6.3e", value) + "\n");
				//evDetails.append(key + "\t" + "CC:" + "\t" + value + "\n");
			}
			evDetails.append("\nHighest Weighted Eigenvector Centrality Node:\n");
			evDetails.append(maxScoreNode + " with score of: " + String.format("%6.3e", maxScore));
			//evDetails.append(maxScoreNode + " with score of: " + maxScore);
			showLongTextMessageInDialog(evDetails.toString(), this, "Weighted Eigenvector Centrality Details");
		} else {
			initToplbl("Please run Weighted Eigenvector Centrality algorithm to see details.");
		}
	}
	
	/**
	 * method to show centralities algs details
	 * @param longMessage
	 * @param frame
	 */
	private void showLongTextMessageInDialog(String longMessage, Frame frame, String title) {
	    JTextArea textArea = new JTextArea(33, 35);
	    textArea.setText(longMessage);
	    textArea.setEditable(false);
	    JScrollPane scrollPane = new JScrollPane(textArea);
	    
	    JOptionPane.showMessageDialog(frame, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	
	private void showPickedNode(){
		if(this.pickedState_.getPicked().size() == 1){
			//this.pickedState_.getPicked().forEach(action);
			lblSnNode.setText(this.pickedState_.getPicked().iterator().next().getToolTip());
			btnSnDetails.setVisible(true);
		} else if (this.pickedState_.getPicked().size() > 1){
			lblSnNode.setText("Multiply nodes selected");
			btnSnDetails.setVisible(true);
		} else {
			lblSnNode.setText("Node not selected");
			btnSnDetails.setVisible(false);
		}
	}
	private void showPickedEdge(){
		if(this.pickedStateLink_.getPicked().size() == 1){
			//this.pickedState_.getPicked().forEach(action);
			lblSeEdge.setText(this.pickedStateLink_.getPicked().iterator().next().toString());
			btnSeDetails.setVisible(true);
		} else {
			lblSeEdge.setText("Edge not selected");
			btnSeDetails.setVisible(false);
		}
	}
	
	
	private void showNodesDetails(){
		if(this.pickedState_.getPicked().size() == 0){
			//Show error
		} else {
			StringBuilder nodesDetails = new StringBuilder();
			Iterator<Node> nodeItearator = this.pickedState_.getPicked().iterator();
			while (nodeItearator.hasNext()) {
				Node node = nodeItearator.next();
				//node.get
				Map<Node, NIC> nodeConnectrions = node.getConnections();
				nodesDetails.append("<b>"+node.getToolTip()+"</b>" + "<br>");
				nodesDetails.append("IP: " +node.getIP()+ "<br>");
				nodesDetails.append("MAC: " +node.getMAC().toUpperCase()+ "<br>");
				if(nodeConnectrions.isEmpty() == false){
				nodesDetails.append("Connections:<br><table>"
						+ "<tbody>"
						+ "<tr>"
						+ "<td>Node:</td>"+"<td>IP:</td>" + "<td>MAC:</td>" + "<td>PORT:</td></tr>" + "<br>");
				for (Map.Entry<Node, NIC> entry : nodeConnectrions.entrySet()) {
					String key = entry.getKey().getToolTip();
					NIC value = entry.getValue();
					// nodesDetails.append(key);
					nodesDetails
							.append("<tr><td>"+key  + "</td><td>" + value.getIp() + "</td><td>" + value.getMac().toUpperCase() + "</td><td>" + value.getPort() + "</td></tr>");
				}
				nodesDetails.append("</tbody></table><br>");
			} else {
				nodesDetails.append("No Connections<br>");
			}
			}
			JTextPane textpane = new JTextPane();
			textpane.setPreferredSize(new Dimension(333, 444));
			
			textpane.setContentType("text/html");
			textpane.setText(nodesDetails.toString());
			textpane.setEditable(false);
			textpane.setCaretPosition(0);

		    JScrollPane scrollPane = new JScrollPane(textpane,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		    JOptionPane.showMessageDialog(null, scrollPane, "Selected Nodes Details", JOptionPane.PLAIN_MESSAGE);
		}
	}
	private void showEdgesDetails(){
		if(this.pickedStateLink_.getPicked().size() == 0){
			//Show error
		} else {
			StringBuilder nodesDetails = new StringBuilder();
			Iterator<Link> nodeItearator = this.pickedStateLink_.getPicked().iterator();
			while (nodeItearator.hasNext()) {
				Link link = nodeItearator.next();
				//node.get
				//Map<Node, NIC> nodeConnectrions = node.getConnections();
				nodesDetails.append("<b>Link: "+link.toString()+"</b>" + "<br>");
				nodesDetails.append("Capacity: " +link.getCapacity()+ "<br>");
				nodesDetails.append("Link nodes: " +link.getNode_left().getToolTip()+", "+link.getNode_right().getToolTip()+ "<br>");
			}
			
			JTextPane textpane = new JTextPane();
			textpane.setPreferredSize(new Dimension(333, 444));
			
			textpane.setContentType("text/html");
			textpane.setText(nodesDetails.toString());
			textpane.setEditable(false);
			textpane.setCaretPosition(0);

		    JScrollPane scrollPane = new JScrollPane(textpane,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		    JOptionPane.showMessageDialog(null, scrollPane, "Selected Nodes Details", JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	private void updateSidePnlCentralities(){
		initbetweennessCentrality(false);
		initClosenessCentrality(false);
		initClosenessCentralityRw(false);
		initEigenvectorCentrality(false);
		initEigenvectorCentralityW(false);
	}
	
	private void initToplbl(String lbltext){
		pnlTop.removeAll();
        JLabel lblTop = new JLabel(lbltext);
        lblTop.setHorizontalAlignment(SwingConstants.CENTER);
        lblTop.setIcon(null);
        pnlTop.add(lblTop);
        pnlTop.updateUI();
        //pnlTop.ref
	}
	public static void updateSidePnl(){
		lblControllersNumber.setText(""+common.CommonData.controllerCount);
		lblHostsNumber.setText(""+common.CommonData.hostsCount);
		lblSwitchesNumber.setText(""+common.CommonData.switchCount);
	}
	
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents()
    {

        pnlMain = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();
        pnlTop = new javax.swing.JPanel();
        pnlGraphProperties = new JPanel();
        
        
        // BC params
        lblBcNodeAvgNumber = new JLabel("0");
        lblBcNodeAvgNumber.setVisible(false);
        lblBcEdgeAvgNumber = new JLabel("0");
        lblBcEdgeAvgNumber.setVisible(false);
        lblBcNodeAvg = new JLabel("Nodes average betweennesscentrality:");
        lblBcNodeAvg.setVisible(false);
        lblBcEdgeAvg = new JLabel("Edges average betweennesscentrality:");
        lblBcEdgeAvg.setVisible(false);
        btnBcDetails = new JButton("Show Details");
        btnBcDetails.setVisible(false);
        // CC params
        
        lblCcNodeAvg = new JLabel("Highest score: ");
        lblCcNodeAvg.setVisible(false);
        lblCcNodeAvgNumber = new JLabel("0");
        lblCcNodeAvgNumber.setVisible(false);
        btnCcDetails = new JButton("Show Details");
        btnCcDetails.setVisible(false);
        lblCcRunAlg = new JLabel("Please run algorithm to see details");
        // RWCC params
        lblCcRwNodeAvg = new JLabel("Highest score: ");
        lblCcRwNodeAvg.setVisible(false);
        lblCcRwNodeAvgNumber = new JLabel("0");
        lblCcRwNodeAvgNumber.setVisible(false);
        btnCcRwDetails = new JButton("Show Details");
        btnCcRwDetails.setVisible(false);
        lblCcRwRunAlg = new JLabel("Please run algorithm to see details");
        // EVC params
        lblEvNodeAvg = new JLabel("Highest score: ");
        lblEvNodeAvg.setVisible(false);
        lblEvNodeAvgNumber = new JLabel("0");
        lblEvNodeAvgNumber.setVisible(false);
        btnEvDetails = new JButton("Show Details");
        btnEvDetails.setVisible(false);
        lblEvRunAlg = new JLabel("Please run algorithm to see details");
        // EVCW params
        lblEvWNodeAvg = new JLabel("Highest score: ");
        lblEvWNodeAvg.setVisible(false);
        lblEvWNodeAvgNumber = new JLabel("0");
        lblEvWNodeAvgNumber.setVisible(false);
        btnEvWDetails = new JButton("Show Details");
        btnEvWDetails.setVisible(false);
        lblEvWRunAlg = new JLabel("Please run algorithm to see details");
        lblBcRunAlg = new JLabel("Please run algorithm to see details");
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        pnlMain.setLayout(new javax.swing.BoxLayout(pnlMain, javax.swing.BoxLayout.LINE_AXIS));
        
        setJMenuBar(menuBar);
        
        JPanel pnlBot = new JPanel();
        pnlBot.setVisible(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(pnlBot, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(pnlMain, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
        			.addContainerGap())
        		.addComponent(pnlTop, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addComponent(pnlTop, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(pnlMain, GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(pnlBot, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        JLabel lblBot = new JLabel("kek2");
        lblBot.setVerticalAlignment(SwingConstants.TOP);
        pnlBot.add(lblBot);
        
        getContentPane().setLayout(layout);
       
        pack();
    }// </editor-fold>               
	
	


    // Variables declaration - do not modify     
	// panels
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JPanel pnlGraphProperties;
    // labels
    private static javax.swing.JLabel lblSwitchesNumber;
    private static javax.swing.JLabel lblControllersNumber;
    private static javax.swing.JLabel lblHostsNumber;
    private static javax.swing.JLabel lblSnNode;
    private static javax.swing.JLabel lblSeEdge;
    //buttons
    private javax.swing.JButton btnSnDetails;
    private javax.swing.JButton btnSeDetails;
    
    
    // bc
    private javax.swing.JLabel lblBcNodeAvgNumber;
    private javax.swing.JLabel lblBcEdgeAvgNumber;
    private javax.swing.JLabel lblBcNodeAvg;
    private javax.swing.JLabel lblBcEdgeAvg;
    private javax.swing.JButton btnBcDetails;
    private javax.swing.JLabel lblBcRunAlg;
    // cc
    private javax.swing.JLabel lblCcNodeAvg;
    private javax.swing.JLabel lblCcNodeAvgNumber;
    private javax.swing.JButton btnCcDetails;
    private javax.swing.JLabel lblCcRunAlg;
    // RWCC
    private javax.swing.JLabel lblCcRwNodeAvg;
    private javax.swing.JLabel lblCcRwNodeAvgNumber;
    private javax.swing.JButton btnCcRwDetails;
    private javax.swing.JLabel lblCcRwRunAlg;
    // EVC
    private javax.swing.JLabel lblEvNodeAvg;
    private javax.swing.JLabel lblEvNodeAvgNumber;
    private javax.swing.JButton btnEvDetails;
    private javax.swing.JLabel lblEvRunAlg;
    // EVCW
    private javax.swing.JLabel lblEvWNodeAvg;
    private javax.swing.JLabel lblEvWNodeAvgNumber;
    private javax.swing.JButton btnEvWDetails;
    private javax.swing.JLabel lblEvWRunAlg;
    
    
    
    
    
    
    // End of variables declaration                   

    @Override
    public void graphClicked(Object v, MouseEvent me)
    {
        if (v instanceof Node)
        {
            if (me.getButton() == MouseEvent.BUTTON1)
            {
                common.CommonData.selectedVertex = (Node) v;

            }
        }
        showPickedNode();
        showPickedEdge();
        //updateSidePnl();
    }

    @Override
    public void graphPressed(Object v, MouseEvent me)
    {
        if (v instanceof Node)
        {
            if (me.getButton() == MouseEvent.BUTTON1)
            {
            		common.CommonData.nodeLeft = (Node) v;
            }
        }
        //showPickedNode();
        //updateSidePnl();
    }

    @Override
    public void graphReleased(Object v, MouseEvent me)
    {
//    	if(!common.CommonData.nodeLeft.equals((Node) v)){
        if (v instanceof Node)
        {
            if (me.getButton() == MouseEvent.BUTTON1)
            {
            		common.CommonData.nodeRight = (Node) v;
            		
            		
            		//updateSidePnl();
            }
        }
//    }
//    	else{
//    		common.CommonData.nodeLeft = null;
//    		common.CommonData.nodeRight = null;
//    		System.out.println("left=right");
//    	}
        //showPickedNode();
        updateSidePnl();
    }
}

	
	


package forms;



// TODO
import algorithms.*;
//import com.fasterxml.jackson.databind.ObjectMapper;
import common.CommonData;
//import common.Parser;
import common.Utils;
import edu.uci.ics.jung.algorithms.generators.random.KleinbergSmallWorldGenerator;
import elements.Controller;
import elements.Host;
import elements.Node;
import elements.Switch;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.apache.commons.collections15.Factory;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.event.GraphEvent.Edge;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.annotations.AnnotationControls;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
//import elements.IpMacEntry;
import elements.Link;
import elements.NIC;
//import elements.Route;
//import elements.RoutePair;
//import elements.RoutingEntry;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.MapTransformer;
import org.apache.commons.collections15.map.LazyMap;
//import org.freehep.util.export.ExportDialog;


public class VisualNetForm extends javax.swing.JFrame implements GraphMouseListener {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 5778688505441868388L;
	private ArrayList<Node> nodes;
    private Graph<Node, Link> graph;
    private AbstractLayout<Node, Link> layout;
    private VisualizationViewer<Node, Link> viewer;
    private EditingModalGraphMouse<Node, Link> graphMouse;
    private JTextArea txtProperties;
    private ArrayList<Integer> domains;
    private ArrayList<String> routeNames;
	
    static String mouseMode;
    
	public VisualNetForm()
    {
        initComponents();
        this.initialize();
        
        this.graph = new SparseGraph<>();
        this.layout = new StaticLayout<>(this.graph, this.pnlMain.getSize());
        //this.layout = new ISOMLayout<>(this.graph, this.pnlMain.getSize());
        this.initGraph();
                
    }
	
	
	
    private void initGraph()
    {
        Transformer<Link, Stroke> strokeTransformer = new Transformer<Link, Stroke>()
        {
            @Override
            public Stroke transform(Link l)
            {
                if (l.getNode_left() instanceof Controller || l.getNode_right() instanceof Controller)
                {
                    return new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                            10.0f, new float[]
                            {
                                10.0f
                            }, 0.0f);
                } else
                {
                    return new BasicStroke();
                }
            }

        };

        Transformer<Link, Paint> paintTransformer = new Transformer<Link, Paint>()
        {
            @Override
            public Paint transform(Link l)
            {
                if (l.getNode_left() instanceof Controller || l.getNode_right() instanceof Controller)
                {
                    return Color.GRAY;
                } else
                {
                    return Color.BLACK;
                }
            }
        };

        this.viewer = new VisualizationViewer<>(layout);
        Dimension pnlSize = this.pnlMain.getBounds().getSize();

        Dimension size = new Dimension(pnlSize.width, pnlSize.height);

        this.viewer.setPreferredSize(size);
        this.viewer.setBackground(Color.WHITE);
        this.viewer.addGraphMouseListener(this);

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
        this.viewer.getRenderer().setVertexRenderer(new VertexRenderer());

        this.viewer.setVertexToolTipTransformer(this.viewer.getRenderContext().getVertexLabelTransformer());

        this.viewer.getRenderContext().setEdgeStrokeTransformer(strokeTransformer);
        this.viewer.getRenderContext().setEdgeDrawPaintTransformer(paintTransformer);

        /**
         * *
         */
        final GraphZoomScrollPane panel = new GraphZoomScrollPane(this.viewer);
        panel.setName("GraphPanel");
        //panel.setSize(size);
        panel.setPreferredSize(size);
        //panel.setSize(this.viewer.getSize());
        this.pnlMain.add(panel);

        Factory<Node> vertexFactory = new VertexFactory();
        Factory<Link> edgeFactory = new EdgeFactory();

        this.graphMouse = new EditingModalGraphMouse(this.viewer.getRenderContext(), vertexFactory, edgeFactory);

        graphMouse.remove(graphMouse.getPopupEditingPlugin());

        PopupMousePlugin mousePlugin = new PopupMousePlugin();
        graphMouse.add(mousePlugin);

        this.viewer.setGraphMouse(graphMouse);

        this.viewer.addKeyListener(graphMouse.getModeKeyListener());
        
        this.viewer.addMouseMotionListener(new MouseMotionListener()
        {
            @Override
            public void mouseDragged(MouseEvent e)
            {
                Node n = this.isMouseInNode(e);
                this.printProperties(n);

            }
            @Override
            public void mouseMoved(MouseEvent e)
            {
                Node n = this.isMouseInNode(e);
                Link l = this.isMouseInEdge(e);

                this.printProperties(n);
                this.printEdgeProperties(l);
            }

            private Node isMouseInNode(MouseEvent e)
            {
                Point2D p = e.getPoint();
                VisualizationViewer ve = (VisualizationViewer) e.getSource();
                final Node n = (Node) ve.getPickSupport().getVertex(ve.getGraphLayout(), p.getX(), p.getY());

                return n;
            }

            private Link isMouseInEdge(MouseEvent e)
            {
                Point2D p = e.getPoint();
                VisualizationViewer ve = (VisualizationViewer) e.getSource();

                final Link l = (Link) ve.getPickSupport().getEdge(ve.getGraphLayout(), p.getX(), p.getY());

                return l;
            }
            // node props print
            private void printProperties(Node n)
            {
                if (n != null)
                {
                    final StringBuffer properties = new StringBuffer();
                    properties.append("" + n.getID() + "\n");
                    properties.append("------\n");

                    if (n instanceof Switch)
                    {
                        properties.append("dpid:(" + ((Switch) n).getDpid() + ")\n");
                        //properties.append("domain:" + ((Switch)n).getDomainIndex() + "\n");
                        //properties.append(("domain:" + ((Switch) n).getRoute() + "\n"));
                        //properties.append(((Switch) n).getConnectedControllers());
                        //properties.append(((Switch) n).getAdjacentSwitches());
                        //properties.append(((Switch) n).getAdjacentHosts());
                    } else if (n instanceof Host)
                    {
                        //properties.append(((Host) n).getIpMacConnections());
                    }

                    txtProperties.setText(properties.toString());
                } else
                {
                	// 
                    txtProperties.setText("");
                }
            }
            // edge props print
            private void printEdgeProperties(Link l)
            {
                if (l != null)
                {
                    final StringBuffer properties = new StringBuffer();
                    properties.append(l.toString()+ "\n");
                    properties.append("left node:" + " " +  l.getNode_left().getToolTip()+ "\n");
                    properties.append("right node:" + " " +  l.getNode_right().getToolTip()+ "\n");
                    properties.append("Capacity:" + " " +  l.getCapacity()+ "\n");
                    txtProperties.setText(properties.toString());
                }
            }
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

        txtProperties = new JTextArea();
        txtProperties.setLineWrap(true);

        JScrollPane pnlSide = new JScrollPane(txtProperties);
        pnlSide.setBorder(BorderFactory.createTitledBorder("Properties"));
        //pnlSide.setPreferredSize(new Dimension((int)size.getWidth(), (intsize.getHeight() / 2));
        pnlSide.setPreferredSize(size);
        //this.pnlMain.add(pnlSide);

        JPanel pnlRight = new JPanel();
        pnlRight.setLayout(new BoxLayout(pnlRight, BoxLayout.PAGE_AXIS));
        pnlRight.add(pnlSide);

        JPanel pnlRightBottom = new JPanel();
        //pnlRightBottom.setLayout(new BoxLayout(pnlRightBottom, BoxLayout.LINE_AXIS));
        pnlRightBottom.setLayout(new FlowLayout());
        pnlRight.add(pnlRightBottom);

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
        
        
        // TODO Algorithms menu 
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
                viewer.setGraphLayout(new StaticLayout<>(graph));
            }
        });
        
        JMenuItem isomLayout = new JMenuItem("ISOMLayout");
        isomLayout.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                viewer.setGraphLayout(new ISOMLayout<>(graph));
            }
        });

        JMenuItem springLayout = new JMenuItem("Spring Layout");
        springLayout.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                viewer.setGraphLayout(new SpringLayout<>(graph));
            }
        });
        
        JMenuItem circleLayout = new JMenuItem("Circle Layout");
        circleLayout.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                viewer.setGraphLayout(new CircleLayout<>(graph));
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
        
        this.menuBar.add(nodeMenu);
        this.menuBar.add(toolsMenu);

        this.nodes = new ArrayList<>();

    }
    
    private void generateRandomGraph()
    {
        this.graph = new RandomGraphGenerator(new EdgeFactory(), new VertexFactory(), 6, 8, 0.5f).create();
        addController();
        generateHosts();
        this.viewer.setGraphLayout(new ISOMLayout<Node, Link>(this.graph));
    }
    
    // TODO ShortestPathWithoutWeights
    private void generateShortestPathWithoutWeight(){
	    	List<Node> shortestPath;
	    	CommonData.selectedVertex = null;
	    	graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
	    	shortestPath = ShortestPath.withoutWeights(this.graph, CommonData.sourceNode, CommonData.destinationNode);
	    	System.out.println("Unweighted shortestPath");
	    	for(Node x : shortestPath){
	    		System.out.println(x.getToolTip());
	    	}
	    	System.out.println();
    }
 // TODO ShortestPathWithtWeights
    private void generateShortestPathWithWeight(){
    	List<Node> shortestPath;
    	CommonData.selectedVertex = null;
    	graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
    	shortestPath = ShortestPath.withWeights(this.graph, CommonData.sourceNode, CommonData.destinationNode);
    	System.out.println("Weighted shortestPath");
    	for(Node x : shortestPath){
    		System.out.println(x.getToolTip());;
    	}
    	System.out.println();
    }
    // TODO generateRandomWalk
    private void generateRandomWalk(){
    	boolean success = false;
    	CommonData.selectedVertex = null;
    	List<Node> randomwalkresult;
    	graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
    	randomwalkresult = new RandomWalk(this.graph).searchNetwork(CommonData.sourceNode, CommonData.destinationNode, 5);
    	System.out.println();System.out.println(success);
    	System.out.println();
    }
    // TODO createSpanningTree
    private void createSpanningTree(){
    	//Forest<Node, Link> tree;
    	///////////////// delete ////////////
    	CommonData.selectedVertex = null;
    	graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
    	
    	SpanningTree kek = new SpanningTree(this.graph);
    	//kek.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    	kek.getContentPane().add(new MinimumSpanningTreeDemo());
    	kek.pack();
    	kek.setVisible(true);
    	
    	
    	///////////////// delete ////////////
    	//tree = SpanningTree.getSpanningTree(graph);
//    	Dimension preferredSizeRect = new Dimension(500,250);
//    	Layout<Node, Link> layout1 = new TreeLayout<Node, Link>(tree);
//    	VisualizationModel<Node, Link> vm1 = new DefaultVisualizationModel<Node, Link>(layout1, preferredSizeRect);
//    	
    	//create a popup window with the created spanning tree printed.
    }
    // TODO Random createSpanningTree (without weights)
    private void createRandomSpanningTree(){
    	CommonData.selectedVertex = null;
    	graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
    	
    	RandomSpanningTree kek = new RandomSpanningTree(this.graph);
    	kek.pack();
    	kek.setVisible(true);
    }
 // TODO getbetweennessCentrality
    private void getbetweennessCentrality(){
    	//CommonData.selectedVertex = null;
    	//graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
    	
    	BetweennessCentralityAlg kek = new BetweennessCentralityAlg(this.graph);
    	//kek.pack();
    	//kek.setVisible(true);
    }
    
    // TODO getclosenessCentrality
    private void getclosenessCentrality(){
    	//CommonData.selectedVertex = null;
    	//graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
    	
    	ClosenessCentralityAlg kek = new ClosenessCentralityAlg(this.graph);
    	//kek.pack();
    	//kek.setVisible(true);
    }

    // TODO getRWclosenessCentrality
    private void  getRWclosenessCentrality(){
    	//CommonData.selectedVertex = null;
    	//graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
    	
    	RandomWalkCentrality kek = new RandomWalkCentrality(this.graph);
    	
    	//kek.nodesRWCC
    	double highestRWCC = 0.0;
    	Node highestRWCCnode = null;
		 for (Map.Entry<Node, Double> entry : kek.nodesRWCC.entrySet()) {
			    String key = entry.getKey().getToolTip();
			    double value = entry.getValue();
			    if(entry.getValue() > highestRWCC){
			    	highestRWCC = entry.getValue();
			    	highestRWCCnode = entry.getKey();
			    }
			    System.out.println(key + "\t" + "RWCC:" +"\t" + value);
		}
		 System.out.println("Highest RW Closeness Centrality Node:");
		 System.out.println(highestRWCCnode.getToolTip() + "\tCC Score:\t"+ highestRWCC);
    	//kek.pack();
    	//kek.setVisible(true);
    }
    
    // TODO geteigenvectorCentrality
    private void geteigenvectorCentrality(boolean weighted){
    	EigenvectorCentralityAlg kek = new EigenvectorCentralityAlg(this.graph, weighted);
    }
    
    
    private void generateSmallWorld()
    {        
        this.graph = new WattsStrogatzSmallWorldGenerator(new EdgeFactory(), new VertexFactory(), 50, 8, 0.2f).create();                
        this.viewer.setGraphLayout(new ISOMLayout<Node, Link>(this.graph));
    }
    
    private void generateHosts()
    {
        Queue<Link> queue = new LinkedList<>();
        
        this.graph.getVertices().stream().filter((n)-> n instanceof Switch).forEach(
                (Node n) ->
                {
                    Host h = new Host();

                    CommonData.nodeLeft = n;
                    CommonData.nodeRight = h;

                    queue.add(new EdgeFactory().create());
                }
        );
        
        queue.stream().forEach(e -> this.graph.addEdge(e, e.getNode_left(), e.getNode_right()));                        
        this.viewer.setGraphLayout(new ISOMLayout<Node, Link>(this.graph));
        //this.viewer.repaint();
        
    }
    private void addController(){
        Controller c = new Controller();
        Queue<Link> queue = new LinkedList<>();
        
        graph.getVertices().stream().filter(node -> node instanceof Switch).forEach(node ->
        {
           common.CommonData.nodeLeft = c;
           common.CommonData.nodeRight = node;                                     
                              
           queue.add(new EdgeFactory().create());
           
        });
        
        queue.stream().forEach(l -> graph.addEdge(l, l.getNode_left(), l.getNode_right()));
        
        viewer.repaint();
    }
    
    
    private void close()
    {
        this.dispose();
        System.exit(0);
    }
	
	
	/** TODO
	 * Methods to check errors and popup msgs
	 */
	private boolean isGraphCreated(){
		if(this.graph.getEdgeCount() > 0 && this.graph.getVertexCount() > 0){
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "The graph has no edges or nodes, please create a graph.",
					"Graph error", JOptionPane.ERROR_MESSAGE);
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
    
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents()
    {

        pnlMain = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlMain.setLayout(new javax.swing.BoxLayout(pnlMain, javax.swing.BoxLayout.LINE_AXIS));
        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>               
	
	


    // Variables declaration - do not modify                     
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JPanel pnlMain;
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
    }

    @Override
    public void graphReleased(Object v, MouseEvent me)
    {
        if (v instanceof Node)
        {
            if (me.getButton() == MouseEvent.BUTTON1)
            {
            		common.CommonData.nodeRight = (Node) v;

            }
        }

    }
}

	
	


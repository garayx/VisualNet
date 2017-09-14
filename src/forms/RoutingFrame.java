///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package forms;
//
////import static common.CommonData.NodeType.Switch;
//import elements.NIC;
////import elements.RoutingEntry;
//import elements.Switch;
//import java.awt.Component;
//import java.awt.ScrollPane;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.FocusEvent;
//import java.awt.event.FocusListener;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.util.ArrayList;
//import java.util.Enumeration;
//import javax.swing.AbstractAction;
//import javax.swing.Action;
//import javax.swing.DefaultCellEditor;
//import javax.swing.JComboBox;
//import javax.swing.JComponent;
//import javax.swing.JMenuItem;
//import javax.swing.JPopupMenu;
//import javax.swing.JScrollPane;
//import javax.swing.JTable;
//import javax.swing.SwingUtilities;
//import javax.swing.event.CellEditorListener;
//import javax.swing.event.ChangeEvent;
//import javax.swing.event.ListSelectionEvent;
//import javax.swing.event.ListSelectionListener;
//import javax.swing.event.TableColumnModelListener;
//import javax.swing.event.TableModelEvent;
//import javax.swing.event.TableModelListener;
//import javax.swing.table.TableColumn;
//import javax.swing.text.JTextComponent;
//
///**
// *
// * @author Daniel
// */
//public class RoutingFrame extends javax.swing.JFrame
//{
//    private ArrayList<RoutingEntry> routingList; 
//    private RoutingTableModel model;
//    private int selectedRow;
//    private int selectedCol;    
//    private final int COL_SRC = 0;
//    private final int COL_DST = 1;
//    private final int COL_PORT = 2;
//    private Switch sw;
//
//    /**
//     * Creates new form RoutingFrame
//     */
//    public RoutingFrame(ArrayList<RoutingEntry> list, Switch sw)
//    {
//        initComponents();
//        
//        this.routingList = list;
//        this.selectedRow = -1;
//        this.selectedCol = -1;
//        
//        this.sw = sw;
//        
//        this.initialize();
//    }
//    
//    private void initialize()
//    {
//        JScrollPane scrollPane = new JScrollPane();
//        
//        //String[] columns = {"src", "in-port", "dst", "out-port"};
//        //String[][] data = new String[0][0];
//        
//        //JTable routesTable = new JTable(data, columns);
//        this.model = new RoutingTableModel(routingList);
//        final JTable tblRoutes = new JTable(this.model);         
//        tblRoutes.addMouseListener(new MouseHandler());
//        tblRoutes.setName("tblroutes");
//        
//        JComboBox srcBox = new JComboBox();
//            
//        common.CommonData.macAddresses.stream().forEach((macAddress) ->
//        {
//            srcBox.addItem(macAddress);
//        });
//        
//        tblRoutes.getColumnModel().getColumn(COL_SRC).setCellEditor(new DefaultCellEditor(srcBox));
//        
//        JComboBox dstBox = new JComboBox();
//        
//        common.CommonData.macAddresses.stream().forEach((macAddress) ->
//        {
//            dstBox.addItem(macAddress);
//        });
//        
//        tblRoutes.getColumnModel().getColumn(COL_DST).setCellEditor(new DefaultCellEditor(srcBox));
//        
//        JComboBox portBox = new JComboBox();
//        
//        for( NIC nic : sw.getConnections().values() )
//        {
//           portBox.addItem(nic.getPort());
//        }
//        
//        tblRoutes.getColumnModel().getColumn(COL_PORT).setCellEditor(new DefaultCellEditor(portBox));
//                                
//        scrollPane.getViewport().add(tblRoutes);
//        this.pnlMain.add(scrollPane);
//        scrollPane.setName("scrollpane");
//        scrollPane.addMouseListener(new MouseHandler());
//    }
//    
//    private class MouseHandler implements MouseListener
//    {
//
//        @Override
//        public void mouseClicked(MouseEvent e)
//        {
//                if(SwingUtilities.isRightMouseButton(e))
//                {
//                    if( ((JComponent)e.getSource()).getName().equals("scrollpane") )
//                    {
//                        JPopupMenu menu = new JPopupMenu();
//
//                        JMenuItem addItem = new JMenuItem("Add Entry");
//                        addItem.addActionListener(new ActionListener()
//                        {
//                            @Override
//                            public void actionPerformed(ActionEvent e)
//                            {                        
//                                model.addRow(new RoutingEntry());
//                                model.fireTableDataChanged();
//
//                            }
//                        });
//
//                        menu.add(addItem);
//                        menu.show((Component)e.getSource(), e.getX(), e.getY());
//                    }
//                    else if(((JComponent)e.getSource()).getName().equals("tblroutes"))
//                    {
//                        JTable table = (JTable)e.getSource();
//                        JPopupMenu menu = new JPopupMenu();
//                        
//                        JMenuItem addItem = new JMenuItem("Add Entry");
//                        addItem.addActionListener(new ActionListener()
//                        {
//                            @Override
//                            public void actionPerformed(ActionEvent e)
//                            {                        
//                                model.addRow(new RoutingEntry());
//                                model.fireTableDataChanged();
//
//                            }
//                        });
//
//                        menu.add(addItem);                        
//
//                        JMenuItem removeItem = new JMenuItem("Remove Row");
//                        removeItem.addActionListener(new ActionListener()
//                        {
//                            @Override
//                            public void actionPerformed(ActionEvent e)
//                            {                        
//                                if( table.getSelectedRow() > -1 )
//                                {
//                                    model.removeRow( table.getSelectedRow() );
//                                    model.fireTableDataChanged();
//                                }
//                            }
//                        });
//
//                        menu.add(removeItem);
//                        menu.show((Component)e.getSource(), e.getX(), e.getY());
//                        
//                    }
//                }
//            
//        }
//
//        @Override
//        public void mousePressed(MouseEvent e)
//        {
//            
//        }
//
//        @Override
//        public void mouseReleased(MouseEvent e)
//        {
//            
//        }
//
//        @Override
//        public void mouseEntered(MouseEvent e)
//        {
//            
//        }
//
//        @Override
//        public void mouseExited(MouseEvent e)
//        {
//            
//        }
//        
//    }
//
//    /**
//     * This method is called from within the constructor to initialize the form.
//     * WARNING: Do NOT modify this code. The content of this method is always
//     * regenerated by the Form Editor.
//     */
//    @SuppressWarnings("unchecked")
//    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
//    private void initComponents()
//    {
//
//        pnlMain = new javax.swing.JPanel();
//
//        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
//        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));
//
//        pnlMain.setLayout(new javax.swing.BoxLayout(pnlMain, javax.swing.BoxLayout.LINE_AXIS));
//        getContentPane().add(pnlMain);
//
//        pack();
//    }// </editor-fold>                        
//
//    // Variables declaration - do not modify                     
//    private javax.swing.JPanel pnlMain;
//    // End of variables declaration                   
//}

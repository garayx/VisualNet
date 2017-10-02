package visualnet;

import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.UIManager;

import forms.VisualNetForm;

public class VisualNet {

	public static void main(String[] args) {
		// TODO Auto-generated method stub/
		 try {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		 } catch (Exception e) { }
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        
        common.CommonData.initialize();
               
        VisualNetForm vnw = new VisualNetForm();        
        vnw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);           
        //vnw.setSize(1024, 768);
        vnw.setTitle("SDN");
        vnw.setFont(new Font("Tahoma", Font.PLAIN, 12));
        vnw.setExtendedState(JFrame.MAXIMIZED_BOTH);
        vnw.setVisible(true);
		
		
	}

}

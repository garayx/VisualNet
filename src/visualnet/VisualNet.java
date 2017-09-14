package visualnet;

import javax.swing.JFrame;

import forms.VisualNetForm;

public class VisualNet {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("kek");
		
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        
        common.CommonData.initialize();
               
        VisualNetForm vnw = new VisualNetForm();        
        vnw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);           
        vnw.setSize(1024, 768);
        vnw.setVisible(true);
		
		
	}

}

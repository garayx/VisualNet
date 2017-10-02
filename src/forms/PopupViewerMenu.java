package forms;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

public class PopupViewerMenu extends JPopupMenu{
	
	public PopupViewerMenu() {
		JRadioButtonMenuItem hostItem = new JRadioButtonMenuItem("Host");
		hostItem.setSelected(false);
		add(hostItem);

		hostItem.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					common.CommonData.selectedNode = "Host";
				}
			}
		});

		JRadioButtonMenuItem switchItem = new JRadioButtonMenuItem("Switch");
		switchItem.setSelected(false);
		add(switchItem);
		switchItem.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					common.CommonData.selectedNode = "Switch";
				}
			}
		});

		JRadioButtonMenuItem controllerItem = new JRadioButtonMenuItem("Controller");
		controllerItem.setSelected(false);
		add(controllerItem);
		controllerItem.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					common.CommonData.selectedNode = "Controller";
				}
			}
		});
	}
}

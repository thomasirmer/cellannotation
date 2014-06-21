import java.awt.Color;

import javax.swing.ComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataListener;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;

import sun.lwawt.macosx.CImage;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class CellTypeEditGUI extends JFrame {
	
	private CellAnnotationGUI annotationGUI;
	private CellTypeEditGUI editGUI;
	
	private JTextField txfCellName;
	private JTextField txfSelectionSize;
	private JComboBox<String> comboBoxColor;
	
	public CellTypeEditGUI(final CellAnnotationGUI annotationGUI) {
		this.annotationGUI= annotationGUI; 
		this.editGUI = this;
		
		setTitle("Edit cell type");
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(6, 6, 278, 151);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblCellName = new JLabel("Cell name");
		lblCellName.setBounds(6, 12, 93, 16);
		panel.add(lblCellName);
		
		JLabel lblSelectionColor = new JLabel("Selection color");
		lblSelectionColor.setBounds(6, 92, 93, 16);
		panel.add(lblSelectionColor);
		
		JLabel lblSelectionSize = new JLabel("Selection size");
		lblSelectionSize.setBounds(6, 52, 93, 16);
		panel.add(lblSelectionSize);
		
		txfCellName = new JTextField();
		txfCellName.setBounds(133, 6, 134, 28);
		panel.add(txfCellName);
		txfCellName.setColumns(10);
		
		txfSelectionSize = new JTextField();
		txfSelectionSize.setBounds(133, 46, 134, 28);
		panel.add(txfSelectionSize);
		txfSelectionSize.setColumns(10);
		
		comboBoxColor = new JComboBox<String>();
		comboBoxColor.setModel(new DefaultComboBoxModel<String>(new String[] {"Red", "Orange", "Yellow", "Green", "Blue"}));
		comboBoxColor.setBounds(133, 88, 134, 27);
		panel.add(comboBoxColor);
		
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				annotationGUI.setCellTypeProperties(txfCellName.getText(),
						Integer.parseInt(txfSelectionSize.getText()),
						(String)comboBoxColor.getSelectedItem());
				editGUI.dispose();
			}
		});
		btnOk.setBounds(6, 120, 75, 29);
		panel.add(btnOk);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editGUI.dispose();
			}
		});
		btnCancel.setBounds(174, 120, 93, 29);
		panel.add(btnCancel);
	}
	
	public void setEditFieldValues(String name, int selectionSize, String selectionColor) {
		txfCellName.setText(name);
		txfSelectionSize.setText(Integer.toString(selectionSize));
		
		if (selectionColor == "Red") comboBoxColor.setSelectedIndex(0);
		else if (selectionColor == "Orange") comboBoxColor.setSelectedIndex(1);
		else if (selectionColor == "Yellow") comboBoxColor.setSelectedIndex(2);
		else if (selectionColor == "Green") comboBoxColor.setSelectedIndex(3);
		else if (selectionColor == "Blue") comboBoxColor.setSelectedIndex(4);
	}
}

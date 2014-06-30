import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddCellTypeGUI extends JFrame {
	
	private JTextField textFieldAbbreviation;
	private JTextField textFieldDescription;
	private JPanel panelColor;
	
	private AddCellTypeGUI reference;
	CellAnnotationGUI callerReference;
	
	public AddCellTypeGUI(CellAnnotationGUI guiRef) {
		this.reference = this;
		this.callerReference = guiRef;
		getContentPane().setLayout(null);
		
		JPanel panelAddCellType = new JPanel();
		panelAddCellType.setBounds(6, 6, 286, 151);
		getContentPane().add(panelAddCellType);
		panelAddCellType.setLayout(null);
		
		JLabel lblAddNewCell = new JLabel("Add new cell type");
		lblAddNewCell.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblAddNewCell.setBounds(6, 6, 121, 16);
		panelAddCellType.add(lblAddNewCell);
		
		JLabel lblAbbreviation = new JLabel("Abbreviation");
		lblAbbreviation.setBounds(6, 40, 80, 16);
		panelAddCellType.add(lblAbbreviation);
		
		textFieldAbbreviation = new JTextField();
		textFieldAbbreviation.setBounds(141, 34, 131, 28);
		panelAddCellType.add(textFieldAbbreviation);
		textFieldAbbreviation.setColumns(10);
		
		JLabel lblFullName = new JLabel("Description");
		lblFullName.setBounds(6, 80, 73, 16);
		panelAddCellType.add(lblFullName);
		
		textFieldDescription = new JTextField();
		textFieldDescription.setColumns(10);
		textFieldDescription.setBounds(141, 74, 131, 28);
		panelAddCellType.add(textFieldDescription);
		
		JLabel lblSelectionColor = new JLabel("Selection color");
		lblSelectionColor.setBounds(6, 119, 93, 16);
		panelAddCellType.add(lblSelectionColor);
		
		JButton btnChooseColor = new JButton("Choose...");
		btnChooseColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color selectionColor = JColorChooser.showDialog(null, "Choose selection color", panelColor.getBackground());
				panelColor.setBackground(selectionColor);
			}
		});
		btnChooseColor.setBounds(141, 114, 103, 29);
		panelAddCellType.add(btnChooseColor);
		
		panelColor = new JPanel();
		panelColor.setBackground(new Color(238, 238, 238));
		panelColor.setBounds(243, 112, 29, 29);
		panelAddCellType.add(panelColor);
		
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String abbreviation  = textFieldAbbreviation.getText();
				String description	 = textFieldDescription.getText();
				Color selectionColor = panelColor.getBackground();
				
				if (abbreviation.isEmpty()) {
					JOptionPane.showMessageDialog(
							reference,
							"Please enter an abbreviation for your cell!",
							"Warning",
							JOptionPane.WARNING_MESSAGE);
				} else if (description.isEmpty()) {
					JOptionPane.showMessageDialog(
							reference,
							"Please enter a description for your cell!",
							"Warning",
							JOptionPane.WARNING_MESSAGE);
				} else {
					CellType cellType = new CellType(abbreviation, description, selectionColor);
					callerReference.addNewCellType(cellType);
					dispose();
				}
			}
		});
		btnOk.setBounds(6, 169, 75, 29);
		getContentPane().add(btnOk);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(206, 169, 86, 29);
		getContentPane().add(btnCancel);
	}
}

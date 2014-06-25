import ij.IJ;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class CellAnnotationGUI extends JFrame {
	
	private CellAnnotationGUI guiReference;
	private Cell_Annotation appReference;
	
	private final ButtonGroup buttonGroupCellTypes = new ButtonGroup();

	private JRadioButton rdbtnType3;
	private JRadioButton rdbtnType2;
	private JRadioButton rdbtnType1;

	private JLabel lblpx1;
	private JLabel lblpx2;
	private JLabel lblpx3;
	private JLabel lblColor1;
	private JLabel lblColor2;
	private JLabel lblColor3;
	
	public CellAnnotationGUI(final Cell_Annotation application) {
		appReference = application;
		guiReference = this;
		
		this.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
			public void windowClosing(WindowEvent e) { appReference.dispose(); }
			public void windowClosed(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
		});
		
		setTitle("Cell Annotation");
		setResizable(false);
		getContentPane().setLayout(null);
		
		JPanel panelCellTypes = new JPanel();
		panelCellTypes.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelCellTypes.setForeground(Color.BLACK);
		panelCellTypes.setBounds(6, 6, 388, 201);
		getContentPane().add(panelCellTypes);
		panelCellTypes.setLayout(null);
		
		rdbtnType3 = new JRadioButton("Type 3");
		buttonGroupCellTypes.add(rdbtnType3);
		rdbtnType3.setBounds(6, 132, 146, 23);
		panelCellTypes.add(rdbtnType3);
		
		rdbtnType2 = new JRadioButton("Type 2");
		buttonGroupCellTypes.add(rdbtnType2);
		rdbtnType2.setBounds(6, 97, 146, 23);
		panelCellTypes.add(rdbtnType2);
		
		rdbtnType1 = new JRadioButton("Type 1");
		buttonGroupCellTypes.add(rdbtnType1);
		rdbtnType1.setBounds(6, 62, 146, 23);
		rdbtnType1.setSelected(true);
		panelCellTypes.add(rdbtnType1);
		
		JLabel lblSelectCellType = new JLabel("Select Cell Type");
		lblSelectCellType.setBounds(6, 6, 106, 16);
		panelCellTypes.add(lblSelectCellType);
		lblSelectCellType.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		
		lblpx1 = new JLabel("100 px");
		lblpx1.setBounds(164, 66, 61, 16);
		panelCellTypes.add(lblpx1);
		
		lblpx2 = new JLabel("200 px");
		lblpx2.setBounds(164, 101, 61, 16);
		panelCellTypes.add(lblpx2);
		
		lblpx3 = new JLabel("300 px");
		lblpx3.setBounds(164, 136, 61, 16);
		panelCellTypes.add(lblpx3);
		
		final JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String cellName = "";
				int selectionSize = 0;
				String selectionColor = "Red";
				
				if (buttonGroupCellTypes.getSelection() == rdbtnType1.getModel()) {
					cellName = rdbtnType1.getText();
					selectionSize = Integer.parseInt(lblpx1.getText().replaceAll(" px", ""));
					selectionColor = lblColor1.getText();
				}
				else if (buttonGroupCellTypes.getSelection() == rdbtnType2.getModel()) {
					cellName = rdbtnType2.getText();
					selectionSize = Integer.parseInt(lblpx2.getText().replaceAll(" px", ""));
					selectionColor = lblColor2.getText();
				}
				else if (buttonGroupCellTypes.getSelection() == rdbtnType3.getModel()) {
					cellName = rdbtnType3.getText();
					selectionSize = Integer.parseInt(lblpx3.getText().replaceAll(" px", ""));
					selectionColor = lblColor3.getText();
				}
				
				CellTypeEditGUI editGUI = new CellTypeEditGUI(guiReference);
				editGUI.setEditFieldValues(cellName, selectionSize, selectionColor);
				editGUI.setSize(290, 185);
				editGUI.setLocation(btnEdit.getLocationOnScreen().x, btnEdit.getLocationOnScreen().y + editGUI.getLocation().y);
				editGUI.setVisible(true);
			}
		});
		btnEdit.setBounds(6, 167, 61, 29);
		panelCellTypes.add(btnEdit);
		
		lblColor1 = new JLabel("Red");
		lblColor1.setBounds(288, 66, 61, 16);
		panelCellTypes.add(lblColor1);
		lblColor1.setForeground(new Color(0, 0, 0));
		
		lblColor2 = new JLabel("Yellow");
		lblColor2.setBounds(288, 101, 61, 16);
		panelCellTypes.add(lblColor2);
		lblColor2.setForeground(new Color(0, 0, 0));
		
		lblColor3 = new JLabel("Orange");
		lblColor3.setBounds(288, 136, 61, 16);
		panelCellTypes.add(lblColor3);
		lblColor3.setForeground(new Color(0, 0, 0));
		
		JLabel lblCellName = new JLabel("Cell name");
		lblCellName.setBounds(6, 34, 146, 16);
		panelCellTypes.add(lblCellName);
		
		JLabel lblSelectionSize = new JLabel("Selection size");
		lblSelectionSize.setBounds(164, 34, 86, 16);
		panelCellTypes.add(lblSelectionSize);
		
		JLabel lblSelectionColor = new JLabel("Selection color");
		lblSelectionColor.setBounds(288, 34, 94, 16);
		panelCellTypes.add(lblSelectionColor);
	}
	
	public void setCellTypeProperties(String cellName, int selectionSize, String selectionColor) {
		if (buttonGroupCellTypes.getSelection() == rdbtnType1.getModel()) {
			rdbtnType1.setText(cellName);
			lblpx1.setText(selectionSize + " px");
			lblColor1.setText(selectionColor);
		}
		else if (buttonGroupCellTypes.getSelection() == rdbtnType2.getModel()) {
			rdbtnType2.setText(cellName);
			lblpx2.setText(selectionSize + " px");
			lblColor2.setText(selectionColor);
		}
		else if (buttonGroupCellTypes.getSelection() == rdbtnType3.getModel()) {
			rdbtnType3.setText(cellName);
			lblpx3.setText(selectionSize + " px");
			lblColor3.setText(selectionColor);
		}
	}
	
	public CellTypeSelection getSelectedCellType() {
		
		String cellName;
		int selectionSize;
		String selectionColor;
		
		if (buttonGroupCellTypes.getSelection() == rdbtnType1.getModel()) {
			cellName = rdbtnType1.getText();
			selectionSize = Integer.parseInt(lblpx1.getText().replaceAll(" px", ""));
			selectionColor = lblColor1.getText();
		}
		else if (buttonGroupCellTypes.getSelection() == rdbtnType2.getModel()) {
			cellName = rdbtnType2.getText();
			selectionSize = Integer.parseInt(lblpx2.getText().replaceAll(" px", ""));
			selectionColor = lblColor2.getText();
		}
		else if (buttonGroupCellTypes.getSelection() == rdbtnType3.getModel()) {
			cellName = rdbtnType3.getText();
			selectionSize = Integer.parseInt(lblpx3.getText().replaceAll(" px", ""));
			selectionColor = lblColor3.getText();
		}
		else return null;
		
		return new CellTypeSelection(cellName, selectionSize, selectionColor);
	}
}

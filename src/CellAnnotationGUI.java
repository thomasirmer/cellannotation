import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataListener;

@SuppressWarnings("serial")
public class CellAnnotationGUI extends JFrame {
	
	private Cell_Annotation appReference;
	
	private JPanel panelCellTypesList;
	private JList<CellType> listCellTypes;
	private JScrollPane scrollPaneCellList;
	private final DefaultListModel<CellType> cellTypesModel;
	
	private CellAnnotationGUI guiReference;
	
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
		
		panelCellTypesList = new JPanel();
		panelCellTypesList.setBounds(6, 6, 388, 339);
		getContentPane().add(panelCellTypesList);
		panelCellTypesList.setLayout(null);
		
		JLabel lblSelectCellTypeList = new JLabel("Select Cell Type");
		lblSelectCellTypeList.setBounds(6, 6, 106, 16);
		lblSelectCellTypeList.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		panelCellTypesList.add(lblSelectCellTypeList);
		
		scrollPaneCellList = new JScrollPane();
		scrollPaneCellList.setBounds(6, 34, 376, 258);
		panelCellTypesList.add(scrollPaneCellList);
		
		listCellTypes = new JList<CellType>();
		cellTypesModel = new DefaultListModel<CellType>();
		cellTypesModel.addElement(new CellType("atut", "atypische Urothelzelle", Color.RED));
		cellTypesModel.addElement(new CellType("ut"	 , "Urothelzelle normal", Color.RED));
		cellTypesModel.addElement(new CellType("tv"	 , "Urothelzelle tumorverdächtig low grade", Color.RED));
		cellTypesModel.addElement(new CellType("tvh" , "Urothelzelle tumorverdächtig high grade", Color.RED));
		cellTypesModel.addElement(new CellType("pe"	 , "Plattenepithelzelle normal", Color.RED));
		cellTypesModel.addElement(new CellType("atpe", "atypisches Plattenepithel", Color.RED));
		cellTypesModel.addElement(new CellType("af"	 , "Artefakt", Color.RED));
		cellTypesModel.addElement(new CellType("kr"	 , "Kristall", Color.RED));
		cellTypesModel.addElement(new CellType("bak" , "Bakterien", Color.RED));
		cellTypesModel.addElement(new CellType("pi"	 , "Pilze", Color.RED));
		cellTypesModel.addElement(new CellType("leu" , "Leukozyt", Color.RED));
		cellTypesModel.addElement(new CellType("gan" , "Granulozyt", Color.RED));
		cellTypesModel.addElement(new CellType("ery" , "Erythrozyt", Color.RED));
		cellTypesModel.addElement(new CellType("pla" , "Plasmazelle", Color.RED));
		
		listCellTypes.setModel(cellTypesModel);
		listCellTypes.setSelectedIndex(0);
		
		listCellTypes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneCellList.setViewportView(listCellTypes);
		
		final JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int selectedIndex = listCellTypes.getSelectedIndex();
				
				if (selectedIndex != -1) {
					cellTypesModel.remove(selectedIndex);
					
					if (cellTypesModel.getSize() == 0) {
						btnRemove.setEnabled(false);
					} else { // select the next index
						if (selectedIndex == cellTypesModel.getSize()) { // last item was removed
							selectedIndex--;
						}
						listCellTypes.setSelectedIndex(selectedIndex);
					}
				}
			}
		});
		btnRemove.setBounds(289, 304, 93, 29);
		panelCellTypesList.add(btnRemove);
		
		JButton btnAdd = new JButton("Add...");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddCellTypeGUI addGUI = new AddCellTypeGUI(guiReference);
				addGUI.setSize(300, 228);
				addGUI.setLocation(getLocation());
				addGUI.setVisible(true);
				
				if (cellTypesModel.getSize() > 0) {
					btnRemove.setEnabled(true);
				}
			}
		});
		btnAdd.setBounds(196, 304, 81, 29);
		panelCellTypesList.add(btnAdd);
	}

	public CellType getSelectedCellType() {
		return listCellTypes.getSelectedValue();
	}

	public void addNewCellType(CellType cellType) {
		cellTypesModel.addElement(cellType);
	}
}

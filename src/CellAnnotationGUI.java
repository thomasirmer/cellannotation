import ij.IJ;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class CellAnnotationGUI extends JFrame {
	
	private Cell_Annotation appReference;
	
	private JPanel panelCellTypesList;
	private JList<CellType> listCellTypes;
	private JScrollPane scrollPaneCellList;
	private final DefaultListModel<CellType> cellTypesModel;
	
	private final SelectionManager selectionManager;
	private CellAnnotationGUI guiReference;
	private ImportExportManager importExportManager;
	
	public CellAnnotationGUI(final Cell_Annotation application, final SelectionManager selectionManager) {
		appReference = application;
		guiReference = this;
		
		this.selectionManager = selectionManager;
		importExportManager = new ImportExportManager(selectionManager);
		
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
		panelCellTypesList.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
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
		cellTypesModel.addElement(new CellType("ut"	 , "Urothelzelle normal"					, new Color(23,82,125)));
		cellTypesModel.addElement(new CellType("atut", "atypische Urothelzelle"					, new Color(204,102,0)));
		cellTypesModel.addElement(new CellType("tv"	 , "Urothelzelle tumorverdächtig low grade"	, new Color(204,51,0)));
		cellTypesModel.addElement(new CellType("tvh" , "Urothelzelle tumorverdächtig high grade", new Color(204,0,0)));
		cellTypesModel.addElement(new CellType("pe"	 , "Plattenepithelzelle normal"				, new Color(51,102,0)));
		cellTypesModel.addElement(new CellType("atpe", "atypisches Plattenepithel"				, new Color(204,153,0)));
		cellTypesModel.addElement(new CellType("af"	 , "Artefakt"								, new Color(51,51,51)));
		cellTypesModel.addElement(new CellType("kr"	 , "Kristall"								, new Color(0,53,96)));
		cellTypesModel.addElement(new CellType("bak" , "Bakterien"								, new Color(204,0,153)));
		cellTypesModel.addElement(new CellType("pi"	 , "Pilze"									, new Color(153,102,51)));
		cellTypesModel.addElement(new CellType("leu" , "Leukozyt"								, new Color(0,102,0)));
		cellTypesModel.addElement(new CellType("gan" , "Granulozyt"								, new Color(51,102,51)));
		cellTypesModel.addElement(new CellType("ery" , "Erythrozyt"								, new Color(102,102,153)));
		cellTypesModel.addElement(new CellType("pla" , "Plasmazelle"							, new Color(0,102,102)));
		
		this.selectionManager.initCellTypes(cellTypesModel.toArray());
				
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
					selectionManager.removeCellType(selectedIndex);
					
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
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(6, 357, 388, 215);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblExportAnnotation = new JLabel("Export/Import annotation");
		lblExportAnnotation.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblExportAnnotation.setBounds(6, 6, 173, 16);
		panel.add(lblExportAnnotation);
		
		JLabel lblExportAsTxtFile = new JLabel("Export annotation as CSV-File");
		lblExportAsTxtFile.setBounds(6, 75, 218, 16);
		panel.add(lblExportAsTxtFile);
		
		JButton btnExportTxt = new JButton("Export...");
		btnExportTxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {				
				JFileChooser fileSaveChooser = new JFileChooser();
				fileSaveChooser.setDialogTitle("Save as...");
				fileSaveChooser.setSelectedFile(new File("CellAnnotation.csv"));
				
				int selection = fileSaveChooser.showSaveDialog(null);
				
				if (selection == JFileChooser.APPROVE_OPTION) {
					if (! fileSaveChooser.getSelectedFile().getAbsolutePath().endsWith(".csv")) {
						IJ.showMessage("You must specify a CSV-File!");
						return;
					} else {
						try {
							importExportManager.saveAnnotationAsFile(fileSaveChooser.getSelectedFile());
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
			}
		});
		btnExportTxt.setBounds(285, 70, 97, 29);
		panel.add(btnExportTxt);
		
		JLabel lblImportAnnotationAs = new JLabel("Import annotation as CSV_File");
		lblImportAnnotationAs.setBounds(6, 34, 189, 16);
		panel.add(lblImportAnnotationAs);
		
		JButton btnImport = new JButton("Import...");
		btnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JFileChooser fileOpenChooser = new JFileChooser();
				fileOpenChooser.setDialogTitle("Open file...");
				
				int selection = fileOpenChooser.showOpenDialog(null);
				
				if (selection == JFileChooser.APPROVE_OPTION) {
					if (! fileOpenChooser.getSelectedFile().getAbsolutePath().endsWith(".csv")) {
						IJ.showMessage("You must chosse a CSV-File!");
						return;
					} else {
						try {
							importExportManager.openAnnotationFromFile(fileOpenChooser.getSelectedFile());
							application.drawSelections();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		btnImport.setBounds(285, 29, 97, 29);
		panel.add(btnImport);
	}

	public CellType getSelectedCellType() {
		return listCellTypes.getSelectedValue();
	}

	public void addNewCellType(CellType cellType) {
		cellTypesModel.addElement(cellType);
		selectionManager.addCellType(cellType);
	}

	public Object[] getCellList() {
		return this.cellTypesModel.toArray();
	}
}

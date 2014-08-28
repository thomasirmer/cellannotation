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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class CellAnnotationGUI extends JFrame {

	// plugin class references
	private final Cell_Annotation appReference;
	private final SelectionManager selectionManager;
	private final ImportExportManager importExportManager;

	// top panel: cell type list
	private final JPanel panelCellTypesList;
	private final JLabel lblCellTypeList;
	private final JScrollPane scrollPaneCellList;
	private final JList<CellType> listCellTypes;
	private final DefaultListModel<CellType> cellTypesModel;

	// middle panel: export / import
	private final JPanel panelExportImport;
	private final JLabel lblExportAsTxtFile;
	private final JLabel lblExportAnnotation;
	private final JLabel lblImportAnnotation;
	private final JButton btnExport;
	private final JButton btnImport;

	// bottom panel: annotated cells
	private final JPanel panelAnnotatedCells;
	private final JLabel lblAnnotatedCells;
	private final JScrollPane scrollPaneAnnotatedCells;
	private final JList<CellSelection> listAnnotatedCells;
	private final DefaultListModel<CellSelection> annotatedCellsModel;
	private final JLabel lblSearch;
	private final JTextField textFieldSearch;
	private final JButton btnDelete;
	
	public CellAnnotationGUI(final Cell_Annotation application,
			final SelectionManager selectionManager) {
		appReference = application;

		this.selectionManager = selectionManager;
		importExportManager = new ImportExportManager(selectionManager);

		this.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) 	 {}
			public void windowIconified(WindowEvent e) 	 {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
			public void windowClosing(WindowEvent e) 	 {appReference.dispose();}
			public void windowClosed(WindowEvent e) 	 {}
			public void windowActivated(WindowEvent e) 	 {}
		});

		setTitle("Cell Annotation");
		setResizable(false);
		getContentPane().setLayout(null);

		panelCellTypesList = new JPanel();
		panelCellTypesList.setBorder(new EtchedBorder(EtchedBorder.LOWERED,
				null, null));
		panelCellTypesList.setBounds(6, 6, 388, 281);
		getContentPane().add(panelCellTypesList);
		panelCellTypesList.setLayout(null);

		lblCellTypeList = new JLabel("Select Cell Type");
		lblCellTypeList.setBounds(6, 6, 230, 16);
		lblCellTypeList.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		panelCellTypesList.add(lblCellTypeList);

		scrollPaneCellList = new JScrollPane();
		scrollPaneCellList.setBounds(6, 34, 376, 242);
		panelCellTypesList.add(scrollPaneCellList);

		listCellTypes = new JList<CellType>();

		cellTypesModel = new DefaultListModel<CellType>();
		cellTypesModel.addElement(new CellType("ut", 	"Urothelzelle normal", new Color(175, 122, 11)));
		cellTypesModel.addElement(new CellType("atut", 	"atypische Urothelzelle", new Color(175, 122, 11)));
		cellTypesModel.addElement(new CellType("tv", 	"Urothelzelle tumorverdächtig low grade", new Color(175, 11, 11)));
		cellTypesModel.addElement(new CellType("tvh",	"Urothelzelle tumorverdächtig high grade", new Color(175, 11, 11)));
		cellTypesModel.addElement(new CellType("pe",	"Plattenepithelzelle normal", new Color(20, 46, 118)));
		cellTypesModel.addElement(new CellType("atpe",	"atypisches Plattenepithel", new Color(20, 46, 118)));
		cellTypesModel.addElement(new CellType("af", 	"Artefakt", new Color(9, 140, 9)));
		cellTypesModel.addElement(new CellType("kr", 	"Kristall", new Color(9, 140, 9)));
		cellTypesModel.addElement(new CellType("bak", 	"Bakterien", new Color(9, 140, 9)));
		cellTypesModel.addElement(new CellType("pi", 	"Pilze", new Color(9, 140, 9)));
		cellTypesModel.addElement(new CellType("leu", 	"Leukozyt", new Color(9, 140, 9)));
		cellTypesModel.addElement(new CellType("gan", 	"Granulozyt", new Color(9, 140, 9)));
		cellTypesModel.addElement(new CellType("ery", 	"Erythrozyt", new Color(9, 140, 9)));
		cellTypesModel.addElement(new CellType("pla", 	"Plasmazelle", new Color(9, 140, 9)));

		this.selectionManager.initCellTypes(cellTypesModel.toArray());

		listCellTypes.setModel(cellTypesModel);
		listCellTypes.setSelectedIndex(0);

		listCellTypes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneCellList.setViewportView(listCellTypes);

		panelExportImport = new JPanel();
		panelExportImport.setBorder(new EtchedBorder(EtchedBorder.LOWERED,
				null, null));
		panelExportImport.setBounds(6, 299, 388, 83);
		getContentPane().add(panelExportImport);
		panelExportImport.setLayout(null);

		lblExportAnnotation = new JLabel("Export/Import annotation");
		lblExportAnnotation.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblExportAnnotation.setBounds(6, 6, 218, 16);
		panelExportImport.add(lblExportAnnotation);

		lblExportAsTxtFile = new JLabel("Export annotation as CSV-File");
		lblExportAsTxtFile.setBounds(6, 57, 218, 16);
		panelExportImport.add(lblExportAsTxtFile);

		btnExport = new JButton("Export...");
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JFileChooser fileSaveChooser = new JFileChooser();
				fileSaveChooser.setDialogTitle("Save as...");
				fileSaveChooser.setSelectedFile(new File(application.getFileName() + ".csv"));

				int selection = fileSaveChooser.showSaveDialog(null);

				if (selection == JFileChooser.APPROVE_OPTION) {
					if (!fileSaveChooser.getSelectedFile().getAbsolutePath().endsWith(".csv")) {
						IJ.showMessage("You must specify a CSV-File!");
						return;
					} else {
						try {
							importExportManager
									.saveAnnotationAsFile(fileSaveChooser.getSelectedFile());
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
			}
		});
		btnExport.setBounds(285, 52, 97, 29);
		panelExportImport.add(btnExport);

		btnImport = new JButton("Import...");
		btnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JFileChooser fileOpenChooser = new JFileChooser();
				fileOpenChooser.setDialogTitle("Open file...");

				int userSelection = fileOpenChooser.showOpenDialog(null);

				if (userSelection == JFileChooser.APPROVE_OPTION) {
					if (!fileOpenChooser.getSelectedFile().getAbsolutePath()
							.endsWith(".csv")) {
						IJ.showMessage("You must chosse a CSV-File!");
						return;
					} else {
						try {
							importExportManager.openAnnotationFromFile(fileOpenChooser.getSelectedFile());
							// add imported selections to list
							for (CellSelection selection : selectionManager.getSelections()) {
								annotatedCellsModel.addElement(selection);
							}
							application.drawSelections();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		btnImport.setBounds(285, 24, 97, 29);
		panelExportImport.add(btnImport);

		lblImportAnnotation = new JLabel("Import annotation as CSV-File");
		lblImportAnnotation.setBounds(6, 29, 218, 16);
		panelExportImport.add(lblImportAnnotation);

		panelAnnotatedCells = new JPanel();
		panelAnnotatedCells.setBounds(6, 394, 388, 278);
		getContentPane().add(panelAnnotatedCells);
		panelAnnotatedCells.setLayout(null);

		lblAnnotatedCells = new JLabel("Annotated cells");
		lblAnnotatedCells.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblAnnotatedCells.setBounds(6, 6, 227, 16);
		panelAnnotatedCells.add(lblAnnotatedCells);

		textFieldSearch = new JTextField();
		textFieldSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (Object selection : annotatedCellsModel.toArray()) {
					if (selection.toString().equals(textFieldSearch.getText())) {
						listAnnotatedCells.setSelectedIndex(annotatedCellsModel.indexOf(selection));
					}
				}
				listAnnotatedCells.ensureIndexIsVisible(listAnnotatedCells.getSelectedIndex());
			}
		});
		textFieldSearch.setBounds(65, 244, 221, 28);
		panelAnnotatedCells.add(textFieldSearch);
		textFieldSearch.setColumns(10);

		lblSearch = new JLabel("Search");
		lblSearch.setBounds(6, 250, 59, 16);
		panelAnnotatedCells.add(lblSearch);

		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (listAnnotatedCells.isSelectionEmpty()) {
					JOptionPane.showMessageDialog(
							null,
							"Please select a cell first!",
							"Warning",
							JOptionPane.WARNING_MESSAGE);
				} else {
					CellSelection cellSelection = listAnnotatedCells.getSelectedValue();
					annotatedCellsModel.removeElement(cellSelection);
					selectionManager.remove(cellSelection);
					
					application.drawSelections();
					repaint();
				}
				
			}
		});
		btnDelete.setBounds(298, 245, 84, 29);
		panelAnnotatedCells.add(btnDelete);

		scrollPaneAnnotatedCells = new JScrollPane();
		scrollPaneAnnotatedCells.setBounds(6, 34, 376, 204);
		panelAnnotatedCells.add(scrollPaneAnnotatedCells);

		listAnnotatedCells = new JList<CellSelection>();
		listAnnotatedCells.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		annotatedCellsModel = new DefaultListModel<CellSelection>();

		listAnnotatedCells.setModel(annotatedCellsModel);
		scrollPaneAnnotatedCells.setViewportView(listAnnotatedCells);
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

	public void addAnnotatedCellsToList(CellSelection selection) {
		annotatedCellsModel.addElement(selection);
	}
}

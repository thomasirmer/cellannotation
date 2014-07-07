import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.gui.ImageCanvas;
import ij.gui.ImageRoi;
import ij.gui.ImageWindow;
import ij.gui.NewImage;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.plugin.filter.PlugInFilter;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class Cell_Annotation implements PlugInFilter {

	// plug-in references
	private CellAnnotationGUI 	gui;
	private SelectionManager 	selectionManager;
	
	// original cell image
	private ImageCanvas 	imgCanvas;
	private ImagePlus 		imgPlus;
	private ImageProcessor 	imgProcessor;
	
	// overlay for selection rectangles
	private BufferedImage	selectionImage;
	private Overlay 		selectionOverlay;
	private ImageRoi 		selectionRoi;
	private Graphics2D		selectionGraphics;
		
	// other
	private Dimension screenSize;
	
	public void run(ImageProcessor ip) {
		
		imgProcessor = ip;
		
		IJ.setTool("rectangle");
	}

	public int setup(String arg, ImagePlus imp) {

		// cell image
		imgCanvas = imp.getCanvas();
		imgPlus   = imp;

		// system properties
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		// selection manager
		selectionManager = new SelectionManager();
		
		// gui
		gui = new CellAnnotationGUI(this, selectionManager);
		gui.setSize(400, 600);
		gui.setLocation(screenSize.width - gui.getWidth(), 0);
		gui.validate();
		gui.setVisible(true);
					
		// mouse listener
		imgCanvas.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) 	{}
			public void mouseReleased(MouseEvent e) {selectionPerformed(e);}
			public void mousePressed(MouseEvent e) 	{adjustRoiColor(e);}
			public void mouseExited(MouseEvent e) 	{}
			public void mouseEntered(MouseEvent e) 	{}
		});
				
		// overlay for selections
		selectionImage 		= new BufferedImage(imgPlus.getWidth(), imgPlus.getHeight(), BufferedImage.TYPE_INT_ARGB);
		selectionGraphics 	= (Graphics2D) selectionImage.getGraphics();
		selectionRoi 		= new ImageRoi(0, 0, selectionImage);
		selectionOverlay 	= new Overlay();
		selectionOverlay.add(selectionRoi);
		imgPlus.setOverlay(selectionOverlay);

		return DOES_RGB;
	}

	public void dispose() {
		
		for (MouseListener listener : imgCanvas.getMouseListeners()) {
			imgCanvas.removeMouseListener(listener);
		}
		
		System.gc();
	}

	private void selectionPerformed(MouseEvent e) {
		
		if (IJ.getToolName().equals("rectangle")) {
		
			CellType cellType 		= gui.getSelectedCellType();
			Roi selectionRoi 	  	= imgPlus.getRoi();
			Rectangle selectionRect = selectionRoi.getBounds();
			ImagePlus slice   		= createSlice(cellType, selectionRoi);
			
			// ask if selection was ok or should be canceled
			GenericDialog dialog = new GenericDialog("Ok?");
			dialog.setLocation(slice.getWindow().getX() + slice.getWindow().getWidth(),
							   slice.getWindow().getY() + slice.getWindow().getHeight() / 2);
			dialog.showDialog();
			
			if (dialog.wasOKed()) {
				CellSelection cellSelection = new CellSelection(cellType, selectionRect, selectionManager.getSelectionCount());
				drawSelection(cellType, cellSelection);
				selectionManager.addSelection(cellSelection);
			}
			
			slice.close();
			imgPlus.killRoi();
		}
	}

	private ImagePlus createSlice(CellType cellType, Roi selection) {
		
		// create new image from selected area
		ImagePlus slice = NewImage.createRGBImage(cellType.toString(),
												  selection.getBounds().height,
												  selection.getBounds().width,
												  imgPlus.getSlice(), 0);
		slice.setProcessor(new ColorProcessor(selection.getBounds().width, selection.getBounds().height));
		slice.getProcessor().insert(imgProcessor, -selection.getBounds().x, -selection.getBounds().y);
		slice.show();
		
		// show slice in the middle of the screen
		slice.getWindow().setSize(screenSize.height/2, screenSize.height/2);
		slice.getWindow().setLocation((screenSize.width  - slice.getWindow().getWidth()) / 2,
									  (screenSize.height - slice.getWindow().getHeight()) / 2);
		slice.getCanvas().fitToWindow();
		
		return slice;
	}

	private void drawSelection(CellType cellType, CellSelection cellSelection) {
		
		
		Rectangle selectionRect = cellSelection.getBounds();
		selectionGraphics.setColor(cellType.getColor());

		// draw rect - line thickness = 2
		selectionGraphics.drawRect(selectionRect.x - 1,
								   selectionRect.y - 1,
								   selectionRect.width  + 1,
								   selectionRect.height + 1);
		selectionGraphics.drawRect(selectionRect.x - 2,
								   selectionRect.y - 2,
								   selectionRect.width  + 3,
								   selectionRect.height + 3);

		// draw cell identifier and serial number
		selectionGraphics.setFont(new Font("Arial", Font.PLAIN, 14));
		selectionGraphics.drawString(cellType.getIdentifier() + "_" + cellSelection.getSerialNumber(),
									 selectionRect.getLocation().x + 4,
									 selectionRect.getLocation().y + selectionRect.height - 4);
	}
			
	private void adjustRoiColor(MouseEvent e) {
		
		// change roi color to selection color of current cell type
		CellType cellType = gui.getSelectedCellType();
		Roi.setColor(cellType.getColor());
	}

	public void drawSelections() {
		
		// draw all selection
		for (CellSelection selection : selectionManager.getSelections()) {
			drawSelection(selection.getCellType(), selection);
		}
		imgPlus.updateAndDraw();
	}
}




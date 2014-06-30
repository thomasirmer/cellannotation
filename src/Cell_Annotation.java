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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Cell_Annotation implements PlugInFilter {

	private CellAnnotationGUI 	gui;
	private ImageCanvas 		origCanvas;
	private ImageWindow 		origWindow;
	private ImagePlus 			origImagePlus;
	private ImageProcessor 		origProcessor;
	
	private Overlay 		overlays;
	
	private BufferedImage	selectionImage;
	private ImageRoi 		selectionRoi;
	private Graphics2D		selectionGraphics;
		
	private Dimension screenSize;

	@Override
	public void run(ImageProcessor ip) {
		this.origProcessor = ip;
	}

	@Override
	public int setup(String arg, ImagePlus imp) {

		origWindow = imp.getWindow();
		origCanvas = imp.getCanvas();
		origImagePlus = imp;

		// get system properties
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		// initialize GUI
		gui = new CellAnnotationGUI(this);
		gui.setSize(400, 600);
		gui.setLocation(screenSize.width - gui.getWidth(), 0);
		gui.validate();
		gui.setVisible(true);
					
		// define mouse listener
		origCanvas.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) 	{}
			public void mouseReleased(MouseEvent e) {selectionPerformed(e);}
			public void mousePressed(MouseEvent e) 	{adjustRoiColor(e);}
			public void mouseExited(MouseEvent e) 	{}
			public void mouseEntered(MouseEvent e) 	{}
		});
				
		// set up overlay for selections
		overlays = new Overlay();
		
		selectionImage = new BufferedImage(origImagePlus.getWidth(), origImagePlus.getHeight(), BufferedImage.TYPE_INT_ARGB);
		selectionRoi = new ImageRoi(0, 0, selectionImage);
		selectionGraphics = (Graphics2D) selectionImage.getGraphics();
		overlays.add(selectionRoi);
		
		IJ.setTool("rectangle");

		return DOES_RGB;
	}

	public void dispose() {
		for (MouseListener listener : origCanvas.getMouseListeners()) { origCanvas.removeMouseListener(listener); }
		System.gc();
	}

	private void selectionPerformed(MouseEvent e) {
		
		if (IJ.getToolName().equals("rectangle")) {
		
			// get selection properties
			CellType cellType = gui.getSelectedCellType();
			
			// get selected ROI
			Roi selection = origImagePlus.getRoi();
			
			// create new image from selected area
			ImagePlus slice = NewImage.createRGBImage(cellType.toString(),
													  selection.getBounds().height,
													  selection.getBounds().width,
													  origImagePlus.getSlice(), 0);
			
			slice.setProcessor(new ColorProcessor(selection.getBounds().width, selection.getBounds().height));
			slice.getProcessor().insert(origProcessor, -selection.getBounds().x, -selection.getBounds().y);
			slice.show();
			// show slice nicely big in the middle of the screen
			slice.getWindow().setSize(screenSize.height/2, screenSize.height/2);
			slice.getWindow().setLocation((screenSize.width  - slice.getWindow().getWidth()) / 2,
										  (screenSize.height - slice.getWindow().getHeight()) / 2);
			slice.getCanvas().fitToWindow();
			
			// ask if selection was ok or should be canceled
			GenericDialog dialog = new GenericDialog("Ok?");
			dialog.setLocation((screenSize.width  - dialog.getWidth()) / 2,
							   (screenSize.height - dialog.getHeight()) / 2);
			dialog.showDialog();
			
			if (dialog.wasOKed()) {
				
				// draw selected ROI
				Rectangle selectionRect = selection.getBounds();
				selectionGraphics.setColor(cellType.getColor());
				// line thickness = 2
				selectionGraphics.drawRect(selectionRect.x - 1,
										   selectionRect.y - 1,
										   selectionRect.width + 1,
										   selectionRect.height + 1);
				selectionGraphics.drawRect(selectionRect.x - 2,
						   				   selectionRect.y - 2,
						   				   selectionRect.width + 3,
						   				   selectionRect.height + 3);
				selectionGraphics.setFont(new Font("Arial", Font.PLAIN, 14));
				selectionGraphics.drawString(cellType.toString(),
						 			selection.getBounds().getLocation().x + 4,
						 			selection.getBounds().getLocation().y + selection.getBounds().height - 4);
			}
			
			slice.close();
			origImagePlus.killRoi();
		}
	}
			
	private void adjustRoiColor(MouseEvent e) {
		CellType cellType = gui.getSelectedCellType();
		Roi.setColor(cellType.getColor());
	}
}




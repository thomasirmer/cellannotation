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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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
	
	private BufferedImage	crosshairImage;
	private ImageRoi 		crosshairRoi;
	private Graphics2D		crosshairGraphics;
	private int				lineColor;

	@Override
	public void run(ImageProcessor ip) {
		this.origProcessor = ip;
	}

	@Override
	public int setup(String arg, ImagePlus imp) {

		origWindow = imp.getWindow();
		origCanvas = imp.getCanvas();
		origImagePlus = imp;

		// initialize GUI
		gui = new CellAnnotationGUI(this);
		gui.setSize(400, 600);
		gui.setLocation(origWindow.getLocation().x + origWindow.getWidth(), origWindow.getLocation().y);
		gui.validate();
		gui.setVisible(true);
		
		// set up image stack for selection overlay
//		selProcessor = new ByteProcessor(origImagePlus.getWidth(), origImagePlus.getHeight());
//		selImagePlus = NewImage.createRGBImage("Selection overlay", origImagePlus.getWidth(), origImagePlus.getHeight(), 1, 0);
//		selImagePlus.setProcessor(selProcessor);
//		selRoi = new ImageRoi(0, 0, selProcessor);
//		selOverlay = new Overlay(selRoi);
//		origImagePlus.setOverlay(selOverlay);
		
		// define mouse listener
		origCanvas.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) 	{}
			public void mouseReleased(MouseEvent e) {selectionPerformed(e);}
			public void mousePressed(MouseEvent e) 	{adjustRoiColor(e);}
			public void mouseExited(MouseEvent e) 	{}
			public void mouseEntered(MouseEvent e) 	{}
		});
		
		origCanvas.addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) 	{}
			public void mouseDragged(MouseEvent e) 	{}
		});
				
		// set up overlay for selections
		overlays = new Overlay();
		
		selectionImage = new BufferedImage(origImagePlus.getWidth(), origImagePlus.getHeight(), BufferedImage.TYPE_INT_ARGB);
		selectionRoi = new ImageRoi(0, 0, selectionImage);
		selectionGraphics = (Graphics2D) selectionImage.getGraphics();
		overlays.add(selectionRoi);

		// set up overlay for crosshair
		crosshairImage = new BufferedImage(origImagePlus.getWidth(), origImagePlus.getHeight(), BufferedImage.TYPE_INT_ARGB);
		crosshairRoi = new ImageRoi(0, 0, crosshairImage);
		overlays.add(crosshairRoi);
		crosshairGraphics = (Graphics2D) crosshairImage.getGraphics();
		lineColor = getIntColor(Color.YELLOW);
		
		origImagePlus.setOverlay(overlays);
		
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
			CellTypeSelection selectionType = gui.getSelectedCellType();
			
			// get selected ROI
			Roi selection = origImagePlus.getRoi();
			
			// create new image from selected area
			ImagePlus slice = NewImage.createRGBImage(selectionType.cellName,
													  selection.getBounds().height,
													  selection.getBounds().width,
													  origImagePlus.getSlice(), 0);
			
			slice.setProcessor(new ColorProcessor(selection.getBounds().width, selection.getBounds().height));
			slice.getProcessor().insert(origProcessor, -selection.getBounds().x, -selection.getBounds().y);
			slice.show();
			slice.getWindow().setLocation(e.getXOnScreen(), e.getYOnScreen());
			
			// ask if selection was ok or should be canceled
			GenericDialog dialog = new GenericDialog("Ok?");
			dialog.setLocation(slice.getWindow().getLocation().x, slice.getWindow().getLocation().y + slice.getWindow().getHeight());
			dialog.showDialog();
			
			if (dialog.wasOKed()) {
				
				// draw selected ROI
				Rectangle selectionRect = selection.getBounds();
				selectionGraphics.setColor(selectionType.selectionColor);
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
				selectionGraphics.drawString(String.valueOf(new Random(System.currentTimeMillis()).nextInt()),
						 			selection.getBounds().getLocation().x + 4,
						 			selection.getBounds().getLocation().y + selection.getBounds().height - 4);
						
				// draw selected ROI
//				origProcessor.setColor(selType.selectionColor);
//				origProcessor.setLineWidth(4);
//				origProcessor.draw(selection);
//				origProcessor.setFont(new Font("Courier New", Font.PLAIN, 12));
//				origProcessor.drawString(String.valueOf(new Random(System.currentTimeMillis()).nextInt()),
//										 selection.getBounds().getLocation().x + 4,
//										 selection.getBounds().getLocation().y + selection.getBounds().height - 4);
			}
			
			slice.close();
			origImagePlus.killRoi();
			
			// draw selection
//			origProcessor.setColor(selType.selectionColor);
//			int lineWidth = (selType.selectionSize < 50) ? 1 : Math.round(selType.selectionSize / 50); // lineWidth 50 times smaller than selection dimensions but at least 1 pixel
//			origProcessor.setLineWidth(lineWidth);
//			origProcessor.draw(selection);
//			origProcessor.setFont(new Font("Courier New", Font.PLAIN, selType.selectionSize / 10));
//			origProcessor.drawString(String.valueOf(new Random(System.currentTimeMillis()).nextInt()),
//					clickX - selType.selectionSize / 2 + 2 * lineWidth,
//					clickY - selType.selectionSize / 2 + selType.selectionSize - 2 * lineWidth);
//			origImagePlus.updateAndDraw();
//			
//			origImagePlus.killRoi();
			
//			// create new image from selected area
//			ImagePlus slice = NewImage.createRGBImage(selType.cellName, selType.selectionSize, selType.selectionSize, image.getSlice(), 0);
//			slice.setProcessor(new ColorProcessor(selType.selectionSize, selType.selectionSize));
//			slice.getProcessor().insert(processor, -roiX, -roiY);
//			slice.show();
//			slice.getWindow().setLocation(e.getXOnScreen(), e.getYOnScreen());
			
//			// ask if selection was ok or should be canceled
//			GenericDialog dialog = new GenericDialog("Ok?");
//			dialog.setLocation(slice.getWindow().getLocation().x, slice.getWindow().getLocation().y + slice.getWindow().getHeight());
//			dialog.showDialog();
//						
//			// ***** DEBUG OUTPUT *****
//			IJ.showMessage("Cell name: " + selType.cellName + "\n" +
//			"Selection size: " + selType.selectionSize + "\n" +
//			"Selection color: " + selType.selectionColor.toString());
		}
	}
		
	private int getIntColor(Color color) {
		int red 	= color.getRed();
		int green 	= color.getGreen();
		int blue 	= color.getBlue();
		int alpha	= color.getAlpha();
		return (alpha << 24) | (red << 16) | (green << 8) | blue;
	}
	
	private void adjustRoiColor(MouseEvent e) {
		CellTypeSelection selectionType = gui.getSelectedCellType();
		Roi.setColor(selectionType.selectionColor);
	}

	private void drawOrientationLines(MouseEvent e) {
		
		if (IJ.getToolName().equals("rectangle")) {
			
			int locationX = origCanvas.offScreenX(e.getX());
			int locationY = origCanvas.offScreenY(e.getY());
			
			crosshairGraphics.setColor(new Color(1,1,1,0));
			crosshairGraphics.fillRect(0, 0, crosshairImage.getWidth(), crosshairImage.getHeight());
			
			// horizontal line
			for (int x = 0; x < crosshairImage.getWidth(); x++) {
				crosshairImage.setRGB(x, locationY, lineColor);
			}
			
			// vertical line
			for (int y = 0; y < crosshairImage.getHeight(); y++) {
				crosshairImage.setRGB(locationX, y, lineColor);
			}
			
			origImagePlus.updateAndDraw();
		}
	}
}




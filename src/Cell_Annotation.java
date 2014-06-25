import ij.CompositeImage;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.ImageCanvas;
import ij.gui.ImageRoi;
import ij.gui.ImageWindow;
import ij.gui.NewImage;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class Cell_Annotation implements PlugInFilter {

	private CellAnnotationGUI 	gui;
	private ImageCanvas 		origCanvas;
	private ImageWindow 		origWindow;
	private ImagePlus 			origImagePlus;
	private ImageProcessor 		origProcessor;
	private ImagePlus			selImagePlus;
	private ImageProcessor		selProcessor;
	private ImageRoi 			selRoi;
	private Overlay 			selOverlay;

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
			public void mouseClicked(MouseEvent e) { selectionPerformed(e); }
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
		});

		IJ.setTool("point");

		return DOES_RGB;
	}

	public void dispose() {
		for (MouseListener listener : origCanvas.getMouseListeners()) { origCanvas.removeMouseListener(listener); }
		System.gc();
	}

	private void selectionPerformed(MouseEvent e) {
		if (IJ.getToolName().equals("point")) {

			// get click position
			int clickX = origCanvas.offScreenX(e.getX());
			int clickY = origCanvas.offScreenY(e.getY());

			CellTypeSelection selType = gui.getSelectedCellType();

			// calculate middle position of click and set ROI
			int roiX = clickX - (int) selType.selectionSize / 2;
			int roiY = clickY - (int) selType.selectionSize / 2;
			Roi selection = new Roi(roiX, roiY, selType.selectionSize, selType.selectionSize);
			origImagePlus.setRoi(selection);
			
			// draw selection
			origProcessor.setColor(selType.selectionColor);
			int lineWidth = (selType.selectionSize < 50) ? 1 : Math.round(selType.selectionSize / 50); // lineWidth 50 times smaller than selection dimensions but at least 1 pixel
			origProcessor.setLineWidth(lineWidth);
			origProcessor.draw(selection);
			origProcessor.setFont(new Font("Courier New", Font.PLAIN, selType.selectionSize / 10));
			origProcessor.drawString(String.valueOf(new Random(System.currentTimeMillis()).nextInt()),
					clickX - selType.selectionSize / 2 + 2 * lineWidth,
					clickY - selType.selectionSize / 2 + selType.selectionSize - 2 * lineWidth);
			origImagePlus.updateAndDraw();
			
			origImagePlus.killRoi();
			
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
}
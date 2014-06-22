import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.gui.ImageCanvas;
import ij.gui.ImageWindow;
import ij.gui.NewImage;
import ij.plugin.filter.PlugInFilter;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Cell_Annotation implements PlugInFilter {

	private CellAnnotationGUI 	gui;
	private ImageCanvas 		canvas;
	private ImageWindow 		window;
	private ImagePlus 			image;
	private ImageProcessor 		processor;

	@Override
	public void run(ImageProcessor ip) {
		this.processor = ip;
	}

	@Override
	public int setup(String arg, ImagePlus imp) {

		window = imp.getWindow();
		canvas = imp.getCanvas();
		image = imp;

		// initialize GUI
		gui = new CellAnnotationGUI();
		gui.setSize(400, 600);
		gui.setLocation(window.getLocation().x + window.getWidth(), window.getLocation().y);
		gui.validate();
		gui.setVisible(true);

		// define mouse listener
		canvas.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				selectionPerformed(e);
			}
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
		});

		IJ.setTool("point");

		return DOES_RGB;
	}

	public void dispose() {
		for (MouseListener listener : canvas.getMouseListeners()) { canvas.removeMouseListener(listener); }
	}

	private void selectionPerformed(MouseEvent e) {
		if (IJ.getToolName().equals("point")) {

			// get click position
			int clickX = canvas.offScreenX(e.getX());
			int clickY = canvas.offScreenY(e.getY());

			CellTypeSelection selType = gui.getSelectedCellType();

			// calculate middle position of click and set ROI
			int roiX = clickX - (int) selType.selectionSize / 2;
			int roiY = clickY - (int) selType.selectionSize / 2;
			image.setRoi(roiX, roiY, selType.selectionSize, selType.selectionSize);
			image.updateAndDraw();
			
			// create new image from selected area
			ImagePlus slice = NewImage.createRGBImage(selType.cellName, selType.selectionSize, selType.selectionSize, image.getSlice(), 0);
			slice.setProcessor(new ColorProcessor(selType.selectionSize, selType.selectionSize));
			slice.getProcessor().insert(processor, -roiX, -roiY);
			slice.getWindow().setLocation(e.getX() + (int) selType.selectionSize, e.getY());
			slice.show();
			
			// ask if selection was ok or should be canceled
			GenericDialog dialog = new GenericDialog("Ok?");
			dialog.showDialog();

			// ***** DEBUG OUTPUT *****
			
			IJ.showMessage("Cell name: " + selType.cellName + "\n" +
			"Selection size: " + selType.selectionSize + "\n" +
			"Selection color: " + selType.selectionColor.toString());
			 
		}
	}
}
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;


public class ImportExportManager {
	
	private SelectionManager selectionManager;

	public ImportExportManager (SelectionManager selectionManager) {
		this.selectionManager = selectionManager;
	}
	
	public void saveAnnotationAsFile(File destination) throws Exception {
		String fileContent = "CellType;SerialNumber;TopLeftX;TopLeftY;Width;Height" + "\n";
		
		for (CellSelection selection : selectionManager.getSelections()) {
			fileContent += selection.toString() + "\n";
		}
		// Print to given file
		PrintWriter writer;
		writer = new PrintWriter(destination);
		writer.write(fileContent);
		writer.close();
	}
	
	public void openAnnotationFromFile(File source) throws Exception {
		
		BufferedReader reader = new BufferedReader(new FileReader(source));
		String line 		  = reader.readLine();
		
		while ((line = reader.readLine()) != null) {
			
			String[] values = line.split(";");
			CellType type = selectionManager.getCellTypeByIdentifier(values[0]);
			
			if (type != null) {
				int serial = Integer.parseInt(values[1]);
				int locX   = Integer.parseInt(values[2]);
				int locY   = Integer.parseInt(values[3]);
				int width  = Integer.parseInt(values[4]);
				int height = Integer.parseInt(values[5]);
				
				Rectangle selection = new Rectangle(locX, locY, width, height);
				selectionManager.addSelection(new CellSelection(type, selection, serial));
			}
		}
		
		reader.close();
	}
}
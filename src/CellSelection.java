import java.awt.Rectangle;

public class CellSelection {
		
	private CellType 	cellType;
	private Rectangle 	selection;
	private int 		serialNumber;
	
	public CellSelection(CellType cellType, Rectangle selection, int serialNumber) {
		this.cellType 		= cellType;
		this.selection 		= selection;
		this.serialNumber 	= serialNumber;
	}
	
	public CellType getCellType() {
		return this.cellType;
	}
	
	public Rectangle getBounds() {
		return this.selection;
	}
	
	public int getSerialNumber() {
		return this.serialNumber;
	}
	
	public String getExportDescription() {
		String description = "";
		
		description += cellType.getIdentifier() + ";" + serialNumber + ";" +
					   selection.x + ";" + selection.y + ";" + selection.width + ";" + selection.height;
		
		return description;
	}
	
	public String toString() {
		// TODO toString method for listModel
		return "";
	}
}

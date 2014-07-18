import java.awt.Rectangle;

public class CellSelection {
		
	private CellType 	cellType;
	private Rectangle 	selection;
	private int 		serialNumber;
	
	public CellSelection(CellType cellType, Rectangle selection) {
		this.cellType 		= cellType;
		this.selection 		= selection;
		this.serialNumber	= SelectionManager.getNextSerialNumber();
	}
	
	public CellSelection(CellType cellType, Rectangle selection, int serial) {
		this.cellType 		= cellType;
		this.selection 		= selection;
		this.serialNumber	= serial;
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
		String description = "";
		description += cellType.getIdentifier() + "_" + serialNumber;
		return description;
	}
}

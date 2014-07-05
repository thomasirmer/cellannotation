import java.awt.Rectangle;

public class CellSelection {
	
	private static int globalSerialNumber = 0;
	
	private CellType cellType;
	private Rectangle selection;
	private int serialNumber;
	
	public CellSelection(CellType cellType, Rectangle selection) {
		this.cellType = cellType;
		this.selection = selection;
		this.serialNumber = CellSelection.globalSerialNumber;
		CellSelection.globalSerialNumber++;
	}
	
	public CellType getCellType() {
		return this.cellType;
	}
	
	public Rectangle getSelection() {
		return this.selection;
	}
	
	public int getSerialNumber() {
		return this.serialNumber;
	}
	
	public void setSerialNumber(int nextSerialNumber) {
		CellSelection.globalSerialNumber = nextSerialNumber;
	}
	
	public static int getGlobalSerialNumber() {
		return CellSelection.globalSerialNumber;
	}
}

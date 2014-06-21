import java.awt.Color;

public class CellTypeSelection {
	
	public String cellName;
	public int selectionSize;
	public Color selectionColor;

	public CellTypeSelection(String cellName, int selectionSize, String selectionColor) {
		
		this.cellName = cellName;
		this.selectionSize = selectionSize;
		
		if (selectionColor == "Red") 			this.selectionColor = Color.RED;
		else if (selectionColor == "Orange") 	this.selectionColor = Color.ORANGE;
		else if (selectionColor == "Yellow") 	this.selectionColor = Color.YELLOW;
		else if (selectionColor == "Green") 	this.selectionColor = Color.GREEN;
		else if (selectionColor == "Blue") 		this.selectionColor = Color.BLUE;
	}
}
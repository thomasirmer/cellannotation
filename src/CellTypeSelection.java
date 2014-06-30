import java.awt.Color;

public class CellTypeSelection {
	
	public String cellName;
	public int selectionSize;
	public Color selectionColor;

	public CellTypeSelection(String cellName, int selectionSize, String selectionColor) {
		
		this.cellName = cellName;
		this.selectionSize = selectionSize;
		
		if (selectionColor == "Red") 			this.selectionColor = new Color(1, 0, 0, 0.75f);
		else if (selectionColor == "Orange") 	this.selectionColor = new Color(1, 0.5f, 0, 0.75f);
		else if (selectionColor == "Yellow") 	this.selectionColor = new Color(1, 1, 0, 0.75f);
		else if (selectionColor == "Green") 	this.selectionColor = new Color(0, 1, 0, 0.75f);
		else if (selectionColor == "Blue") 		this.selectionColor = new Color(0, 0, 1, 0.75f);
	}
}
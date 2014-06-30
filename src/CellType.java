import java.awt.Color;


public class CellType {
	
	private String 	abbreviation;
	private String 	fullName;
	private Color	selectionColor;

	public CellType(String abbreviation, String fullName, Color selectionColor) {
		this.abbreviation 	= abbreviation;
		this.fullName 		= fullName;
		this.selectionColor	= selectionColor;
	}
	
	public Color getColor() {
		return this.selectionColor;
	}
	
	public String toString() {
		return this.abbreviation + "\t" + "(" + this.fullName + ")";
	}
}

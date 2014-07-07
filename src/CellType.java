import java.awt.Color;


public class CellType {
	
	private String 	identifier;
	private String 	fullName;
	private Color	selectionColor;

	public CellType(String identifier, String fullName, Color selectionColor) {
		this.identifier 	= identifier;
		this.fullName 		= fullName;
		this.selectionColor	= selectionColor;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public Color getColor() {
		return this.selectionColor;
	}
	
	public String toString() {
		return this.identifier + "\t" + "(" + this.fullName + ")";
	}
}

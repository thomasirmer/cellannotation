import java.util.ArrayList;

public class SelectionManager {
	
	private ArrayList<CellType> cellTypeList;
	private ArrayList<CellSelection> selectionList;

	public SelectionManager() {
		cellTypeList = new ArrayList<CellType>();
		selectionList = new ArrayList<CellSelection>();
	}
	
	public void initCellTypes(Object[] cellTypes) {
		for (Object current : cellTypes) {
			cellTypeList.add((CellType) current);
		}
	}
	
	public void addCellType(Object cellType) {
		cellTypeList.add((CellType) cellType);
	}
	
	public void removeCellType(int index) {
		cellTypeList.remove(index);
	}
	
	public void addSelection(CellSelection selection) {
		selectionList.add(selection);
	}
	
	public int getSelectionCount() {
		return selectionList.size();
	}
	
	public ArrayList<CellSelection> getSelections() {
		return selectionList;
	}
	
	public CellType getCellTypeByIdentifier (String abbreviation) {
		for (CellType type : cellTypeList) {
			if (type.getIdentifier().equals(abbreviation))
				return type;
		}
		return null;
	}
}
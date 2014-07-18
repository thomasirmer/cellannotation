import java.util.ArrayList;

public class SelectionManager {
	
	private ArrayList<CellType> cellTypeList;
	private ArrayList<CellSelection> selectionList;
	
	private static int serialNumber = 0;

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
	
	public static int getNextSerialNumber() {
		int currentNumber = serialNumber;
		serialNumber++;
		return currentNumber;
	}
	
	public ArrayList<CellSelection> getSelections() {
		return selectionList;
	}
	
	public CellType getCellTypeByIdentifier (String id) {
		for (CellType type : cellTypeList) {
			if (type.getIdentifier().equals(id))
				return type;
		}
		return null;
	}
	
	public void remove(CellSelection cellSelection) {
		selectionList.remove(cellSelection);
	}

	public void updateSerial() {
		for (CellSelection selection : selectionList) {
			serialNumber = Math.max(selection.getSerialNumber(), serialNumber);
		}
		if (selectionList.size() > 0) {
			serialNumber++;
		}
	}
}
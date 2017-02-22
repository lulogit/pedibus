package polimi.or.pedibus.model;

import java.util.Map;

public class CostMatrix {
	private final float[][] dm;
	private int currentRow;
	private int size;

	public CostMatrix(int size) {
		dm = new float[size][size];
		currentRow = 0;
		this.size = size;
	}

	protected void setSetAtColumn(int columnIndex, float value) {
		if (0<=columnIndex && columnIndex<size){
			this.dm[currentRow][columnIndex] = value;
		}
	}

	protected void moveToNextRow() {
		if (currentRow<size-1){
			this.currentRow++;
		}
	}

	public static CostMatrix euclideanDistances(Map<Integer, Node> nodes) {
		CostMatrix dm = new CostMatrix(nodes.size());
		for (int i: nodes.keySet()){
			for (int j: nodes.keySet()){
				dm.setSetAtColumn(j, 
						nodes.get(i).distanceTo(nodes.get(j)));
			}
			dm.moveToNextRow();
		}
		return dm;
	}
	
	public float at(int i, int j){
		return dm[i][j];
	}
}

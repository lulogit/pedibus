package polimi.or.pedibus.model;

import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class CostMatrix.
 */
public class CostMatrix {
	
	/** The dm. */
	private final float[][] dm;
	
	/** The current row. */
	private int currentRow;
	
	/** The size. */
	private int size;

	/**
	 * Instantiates a new cost matrix.
	 *
	 * @param size the size
	 */
	public CostMatrix(int size) {
		dm = new float[size][size];
		currentRow = 0;
		this.size = size;
	}

	/**
	 * Sets the set at column.
	 *
	 * @param columnIndex the column index
	 * @param value the value
	 */
	protected void setSetAtColumn(int columnIndex, float value) {
		if (0<=columnIndex && columnIndex<size){
			this.dm[currentRow][columnIndex] = value;
		}
	}

	/**
	 * Move to next row.
	 */
	protected void moveToNextRow() {
		if (currentRow<size-1){
			this.currentRow++;
		}
	}

	/**
	 * Euclidean distances.
	 *
	 * @param nodes the nodes
	 * @return the cost matrix
	 */
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
	
	/**
	 * At.
	 *
	 * @param i the i
	 * @param j the j
	 * @return the float
	 */
	public float at(int i, int j){
		return dm[i][j];
	}
}

package polimi.or.pedibus.model;

import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class CostMatrix.
 * This is a matrix for a generic cost (it can be danger or distance)
 * In order to insert the values, there are two functions
 * one for changing the row which start from the 0 and can only be increased
 * the other is for insert a value for a given column index
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
	 * It returns a cost matrix in which each cost is the euclidean distance 
	 * of every pair of nodes.
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
	 * It return the cost for couple of nodes.
	 *
	 * @param i the i
	 * @param j the j
	 * @return the float
	 */
	public float at(int i, int j){
		return dm[i][j];
	}
}

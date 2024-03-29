package polimi.or.pedibus.model;

// TODO: Auto-generated Javadoc
/**
 * The Class Node.
 */
public class Node {
	
	/** The y. */
	protected int x,y;

	/**
	 * Sets the x.
	 *
	 * @param x the new x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Sets the y.
	 *
	 * @param y the new y
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * Distance to.
	 *
	 * @param other the other
	 * @return the float
	 */
	public float distanceTo(Node other){
		float dx = this.x-other.x;
		float dy = this.y-other.y;
		float dist = (float) Math.sqrt(dx*dx+dy*dy);
		return (float) Math.round(dist * 10000) / 10000.f;
	}
}

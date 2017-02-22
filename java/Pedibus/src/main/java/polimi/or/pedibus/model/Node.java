package polimi.or.pedibus.model;

public class Node {
	protected int x,y;

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public float distanceTo(Node other){
		float dx = this.x-other.x;
		float dy = this.y-other.y;
		float dist = (float) Math.sqrt(dx*dx+dy*dy);
		return (float) Math.round(dist * 10000) / 10000;
	}
}

package tiralabrashakki;

public class Location {
	private int x, y;
	
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void set(Location loc) {
		this.x = loc.x;
		this.y = loc.y;
	}
}

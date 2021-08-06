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
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		
		if (getClass() != o.getClass()) {
			return false;
		}
		
		Location loc = (Location) o;
		return x == loc.x && y == loc.y;
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + x;
		hash = 31 * hash + y;
		
		return hash;
	}
}

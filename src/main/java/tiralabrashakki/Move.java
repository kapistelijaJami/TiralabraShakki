package tiralabrashakki;

public class Move {
	private Location start;
	private Location dest;
	private char takes;
	private boolean firstMoveForPiece;
	
	private Move(Location start, Location dest, boolean firstMoveForPiece) {
		this(start, dest, ' ', firstMoveForPiece);
	}
	
	private Move(Location start, Location dest, char takes, boolean firstMoveForPiece) {
		this.start = start;
		this.dest = dest;
		this.takes = takes;
		this.firstMoveForPiece = firstMoveForPiece;
	}
	
	public static Move createMove(Board board, int startX, int startY, int destX, int destY) {
		return new Move(new Location(startX, startY), new Location(destX, destY), board.get(destX, destY), !board.pieceHasMoved(startX, startY));
	}

	public Location getStart() {
		return start;
	}

	public void setStart(Location start) {
		this.start = start;
	}

	public Location getDest() {
		return dest;
	}

	public void setDest(Location dest) {
		this.dest = dest;
	}

	public char getTakes() {
		return takes;
	}

	public void setTakes(char takes) {
		this.takes = takes;
	}

	public boolean isFirstMoveForPiece() {
		return firstMoveForPiece;
	}

	public void setFirstMoveForPiece(boolean firstMoveForPiece) {
		this.firstMoveForPiece = firstMoveForPiece;
	}
}

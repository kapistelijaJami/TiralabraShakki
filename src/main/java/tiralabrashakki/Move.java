package tiralabrashakki;

public class Move {
	private Location start;
	private Location dest;
	private char piece;
	private char takes;
	private boolean givesCheck = false;
	private boolean firstMoveForPiece;
	private Location erasedEnPassant = null;
	private boolean isEnpassant = false;
	
	private Move(Location start, Location dest, char piece, boolean firstMoveForPiece) {
		this(start, dest, piece, ' ', firstMoveForPiece);
	}
	
	private Move(Location start, Location dest, char piece, char takes, boolean firstMoveForPiece) {
		this.start = start;
		this.dest = dest;
		this.piece = piece;
		this.takes = takes;
		this.firstMoveForPiece = firstMoveForPiece;
	}
	
	private static Move createBaseMove(Board board, int startX, int startY, int destX, int destY) {
		return new Move(new Location(startX, startY), new Location(destX, destY), board.get(startX, startY), board.get(destX, destY), !board.pieceHasMoved(startX, startY));
	}
	
	public static Move createMove(Board board, int startX, int startY, int destX, int destY) {
		if (Character.toUpperCase(board.get(startX, startY)) == 'P' && board.get(destX, destY) == ' ' && startX != destX) {
			return createMoveEnPassant(board, startX, startY, destX, destY);
		}
		return createBaseMove(board, startX, startY, destX, destY);
	}
	
	private static Move createMoveEnPassant(Board board, int startX, int startY, int destX, int destY) {
		Move move = createBaseMove(board, startX, startY, destX, destY);
		move.isEnpassant = true;
		move.takes = board.get(destX, startY);
		return move;
	}
	
	public Location getStart() {
		return start;
	}
	
	public Location getDest() {
		return dest;
	}
	
	public char getPiece() {
		return piece;
	}
	
	public char getTakes() {
		return takes;
	}

	public boolean givesCheck() {
		return givesCheck;
	}

	public void setGivesCheck(boolean givesCheck) {
		this.givesCheck = givesCheck;
	}
	
	public boolean isFirstMoveForPiece() {
		return firstMoveForPiece;
	}

	public Location getErasedEnPassant() {
		return erasedEnPassant;
	}

	public void setErasedEnPassant(Location erasedEnPassant) {
		this.erasedEnPassant = erasedEnPassant;
	}

	public boolean isIsEnpassant() {
		return isEnpassant;
	}

	public void setIsEnpassant(boolean isEnpassant) {
		this.isEnpassant = isEnpassant;
	}
	
	@Override
	public String toString() {
		String ifTakes = "";
		if (takes != ' ') {
			ifTakes = " takes: " + takes;
		}
		return (char)(start.getX() + 'a') + "" + (8 - start.getY()) + " -> " + (char)(dest.getX() + 'a') + "" + (8 - dest.getY()) + ifTakes;
	}
}

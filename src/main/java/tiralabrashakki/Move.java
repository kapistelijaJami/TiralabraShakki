package tiralabrashakki;

import tiralabrashakki.ai.Heuristics;

public class Move implements Comparable<Move> {
	private final Location start;
	private final Location dest;
	private final char piece;
	private char takes;
	private boolean givesCheck = false;
	private final boolean firstMoveForPiece;
	private Location erasedEnPassant = null;
	private boolean isEnpassant = false;
	private boolean isCastle = false;
	
	private Move(Location start, Location dest, char piece, char takes, boolean firstMoveForPiece) {
		this.start = start;
		this.dest = dest;
		this.piece = piece;
		this.takes = takes;
		this.firstMoveForPiece = firstMoveForPiece;
	}
	
	public static Move createMove(Board board, int startX, int startY, int destX, int destY) {
		if (Character.toUpperCase(board.get(startX, startY)) == 'P' && board.get(destX, destY) == ' ' && startX != destX) {
			return createMoveEnPassant(board, startX, startY, destX, destY);
		} else if (Character.toUpperCase(board.get(startX, startY)) == 'K' && (startX - destX == 2 || destX - startX == 2)) {
			return createMoveCastle(board, startX, startY, destX, destY);
		}
		return createBaseMove(board, startX, startY, destX, destY);
	}
	
	private static Move createBaseMove(Board board, int startX, int startY, int destX, int destY) {
		return new Move(new Location(startX, startY), new Location(destX, destY), board.get(startX, startY), board.get(destX, destY), !board.pieceHasMoved(startX, startY));
	}
	
	private static Move createMoveEnPassant(Board board, int startX, int startY, int destX, int destY) {
		Move move = createBaseMove(board, startX, startY, destX, destY);
		move.isEnpassant = true;
		move.takes = board.get(destX, startY);
		return move;
	}
	
	private static Move createMoveCastle(Board board, int startX, int startY, int destX, int destY) {
		Move move = createBaseMove(board, startX, startY, destX, destY);
		move.isCastle = true;
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

	public boolean isEnpassant() {
		return isEnpassant;
	}

	public boolean isCastle() {
		return isCastle;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		
		if (!(o instanceof Move)) {
			return false;
		}
		
		Move move = (Move) o;
		return start.equals(move.start) && dest.equals(move.dest);
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + start.hashCode();
		hash = 31 * hash + dest.hashCode();
		
		return hash;
	}
	
	@Override
	public String toString() {
		String ifTakes = "";
		if (takes != ' ') {
			ifTakes = " takes: " + takes;
		}
		return (char)(start.getX() + 'a') + "" + (8 - start.getY()) + " -> " + (char)(dest.getX() + 'a') + "" + (8 - dest.getY()) + ifTakes;
	}

	public boolean isCapture() {
		return takes != ' ';
	}

	public boolean isQuiet() {
		return !isCapture() && !givesCheck();
	}
	
	private int compareCaptures(Move o) {
		return isCapture() && !o.isCapture() ? -1 : (isCapture() == o.isCapture() ? 0 : 1);
	}
	
	/**
	 * Material difference of captured piece and moved piece.
	 * Higher value the more the move is worth.
	 * @return 
	 */
	public int moveMaterialTradeValue() {
		return Heuristics.pieceComparison(getTakes(), getPiece());
	}

	@Override
	public int compareTo(Move o) {
		int captures = compareCaptures(o);
		if (captures == 0 && isCapture()) {
			int val1 = this.moveMaterialTradeValue();
			int val2 = o.moveMaterialTradeValue();
			return val2 - val1;
		}
		
		return captures;
	}
}
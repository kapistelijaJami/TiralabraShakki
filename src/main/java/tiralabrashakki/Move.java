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
	private boolean isPromotion = false;
	private char promotesTo;
	
	private Move(Location start, Location dest, char piece, char takes, boolean firstMoveForPiece) {
		this.start = start;
		this.dest = dest;
		this.piece = piece;
		this.takes = takes;
		this.firstMoveForPiece = firstMoveForPiece;
	}
	
	public static Move createMove(Board board, int startX, int startY, int destX, int destY) {
		Move baseMove = createBaseMove(board, startX, startY, destX, destY);
		
		if (Character.toUpperCase(baseMove.piece) == 'P'
				&& board.get(destX, destY) == ' ' && startX != destX) {
			
			baseMove.isEnpassant = true;
			baseMove.takes = board.get(destX, startY);
		} else if (Character.toUpperCase(board.get(startX, startY)) == 'K'
				&& (startX - destX == 2 || destX - startX == 2)) {
			
			baseMove.isCastle = true;
		} else if (Character.toUpperCase(board.get(startX, startY)) == 'P'
				&& (destY == 0 || destY == 7)) {
			
			baseMove.isPromotion = true;
			baseMove.promotesTo = destY == 0 ? 'Q' : 'q';
		}
		
		return baseMove;
	}
	
	private static Move createBaseMove(Board board, int startX, int startY, int destX, int destY) {
		return new Move(new Location(startX, startY), new Location(destX, destY), board.get(startX, startY), board.get(destX, destY), !board.pieceHasMoved(startX, startY));
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

	/**
	 * If the move gives a check. Only available after checking king safety.
	 * @return 
	 */
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
	
	public boolean isPromotion() {
		return isPromotion;
	}
	
	public char getPromotesTo() {
		return promotesTo;
	}
	
	public void setPromotesTo(char c) {
		promotesTo = c;
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
		String p = (Character.toUpperCase(piece) == 'P' && takes == ' ' ? "" : "" + piece);
		String move = p + (takes != ' ' ? 'x' : "") + (char)(dest.getX() + 'a') + "" + (8 - dest.getY());
		String coordinates = " (" + (char)(start.getX() + 'a') + "" + (8 - start.getY()) + " -> " + (char)(dest.getX() + 'a') + "" + (8 - dest.getY()) + ifTakes + ")";
		
		//return (char)(start.getX() + 'a') + "" + (8 - start.getY()) + " -> " + (char)(dest.getX() + 'a') + "" + (8 - dest.getY()) + ifTakes;
		return move + coordinates;
	}

	public boolean isCapture() {
		return takes != ' ';
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
			int myVal = this.moveMaterialTradeValue();
			int otherVal = o.moveMaterialTradeValue();
			return otherVal - myVal;
		}
		
		return captures;
	}
}
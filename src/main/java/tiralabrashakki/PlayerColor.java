package tiralabrashakki;

import tiralabrashakki.possibleMoves.PossibleMoves;

public enum PlayerColor {
	WHITE, BLACK;
	
	public PlayerColor opposite() {
		if (this == WHITE) {
			return BLACK;
		} else {
			return WHITE;
		}
	}
	
	public boolean isWhite() {
		return this == WHITE;
	}
	
	public boolean isMyPiece(char piece) {
		return turnEqualsPieceColor(isWhite(), piece);
	}
	
	public boolean isEnemyPiece(char piece) {
		return !isMyPiece(piece);
	}
	
	public static boolean turnEqualsPieceColor(boolean whiteTurn, char piece) {
		return whiteTurn == pieceIsWhite(piece);
	}
	
	public static boolean pieceIsWhite(char c) {
		return Character.isUpperCase(c);
	}
}

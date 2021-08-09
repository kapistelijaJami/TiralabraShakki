package tiralabrashakki.possibleMoves;

import tiralabrashakki.Board;
import tiralabrashakki.PlayerColor;

public class SquareSafety {
	public static boolean isKingSafe(Board board, PlayerColor colorTurn) {
		if (colorTurn.isWhite()) {
			return isSquareSafe(board, board.getKingW().getX(), board.getKingW().getY(), colorTurn);
		} else {
			return isSquareSafe(board, board.getKingB().getX(), board.getKingB().getY(), colorTurn);
		}
	}
	
	public static boolean isSquareSafe(Board board, int x, int y, PlayerColor colorTurn) {
		return isSafeFromStraight(board, x, y, colorTurn) && isSafeFromDiagonal(board, x, y, colorTurn) && isSafeFromKnight(board, x, y, colorTurn) && isSafeFromPawn(board, x, y, colorTurn);
	}
	
	private static boolean isSafeFromStraight(Board board, int x, int y, PlayerColor colorTurn) {
		for (int dir = -1; dir < 2; dir += 2) {
			if (!isSafeVertically(board, x, y, dir, colorTurn) || !isSafeHorizontally(board, x, y, dir, colorTurn)) {
				return false;
			}
		}
		
		return true;
	}
	
	private static boolean isSafeVertically(Board board, int x, int y, int dir, PlayerColor colorTurn) {
		for (int i = dir; board.isInside(y + i); i += dir) {
			char c = board.get(x, y + i);
			if (c == ' ') {
				continue;
			}
			
			if (colorTurn.isMyPiece(c)) {
				return true;
			}

			return !(Character.toUpperCase(c) == 'R' || Character.toUpperCase(c) == 'Q' || (i * dir == 1 && Character.toUpperCase(c) == 'K'));
		}
		
		return true;
	}
	
	private static boolean isSafeHorizontally(Board board, int x, int y, int dir, PlayerColor colorTurn) {
		for (int i = dir; board.isInside(x + i); i += dir) {
			char c = board.get(x + i, y);
			if (c == ' ') {
				continue;
			}
			
			if (colorTurn.isMyPiece(c)) {
				return true;
			}

			return !(Character.toUpperCase(c) == 'R' || Character.toUpperCase(c) == 'Q' || (i * dir == 1 && Character.toUpperCase(c) == 'K'));
		}
		
		return true;
	}
	
	private static boolean isSafeFromDiagonal(Board board, int x, int y, PlayerColor colorTurn) {
		for (int dirY = -1; dirY < 2; dirY += 2) {
			for (int dirX = -1; dirX < 2; dirX += 2) {
				if (!isSafeDiagonally(board, x, y, dirX, dirY, colorTurn)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private static boolean isSafeDiagonally(Board board, int x, int y, int dirX, int dirY, PlayerColor colorTurn) {
		for (int i = 1; board.isInside(x + dirX * i, y + dirY * i); i++) {
			char c = board.get(x + dirX * i, y + dirY * i);
			if (c == ' ') {
				continue;
			}
			
			if (colorTurn.isMyPiece(c)) {
				return true;
			}
			
			return !(Character.toUpperCase(c) == 'B' || Character.toUpperCase(c) == 'Q' || (i == 1 && Character.toUpperCase(c) == 'K'));
		}
		return true;
	}
	
	private static boolean isSafeFromKnight(Board board, int x, int y, PlayerColor colorTurn) {
		for (int dirY = -1; dirY < 2; dirY += 2) {
			for (int dirX = -1; dirX < 2; dirX += 2) {
				
				if (board.isInside(x + dirX, y + 2 * dirY)) {
					char c = board.get(x + dirX, y + 2 * dirY);
					
					if (colorTurn.isEnemyPiece(c) && Character.toUpperCase(c) == 'N') {
						return false;
					}
				}
				
				if (board.isInside(x + 2 * dirX, y + dirY)) {
					char c = board.get(x + 2 * dirX, y + dirY);
					
					if (colorTurn.isEnemyPiece(c) && Character.toUpperCase(c) == 'N') {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private static boolean isSafeFromPawn(Board board, int x, int y, PlayerColor colorTurn) { //TODO: does this need en passant? - probably not
		int dir = 1;
		if (colorTurn.isWhite()) {
			dir = -1;
		}
		
		for (int i = -1; i < 2; i += 2) {
			if (board.isInside(x + i, y + dir)) {
				char c = board.get(x + i, y + dir);
				if (c != ' ' && colorTurn.isEnemyPiece(c) && Character.toUpperCase(c) == 'P') {
					return false;
				}
			}
		}
		return true;
	}
}

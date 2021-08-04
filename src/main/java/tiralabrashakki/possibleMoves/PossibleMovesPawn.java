package tiralabrashakki.possibleMoves;

import java.util.ArrayList;
import tiralabrashakki.Board;
import tiralabrashakki.Move;
import tiralabrashakki.PlayerColor;

public class PossibleMovesPawn extends PossibleMoves {
	public static void addPossibleMoves(Board board, int x, int y, ArrayList<Move> possibleMoves) {
		PlayerColor colorTurn = PlayerColor.BLACK;
		int dir = 1;
		if (isWhite(board.get(x, y))) {
			dir = -1;
			colorTurn = PlayerColor.WHITE;
		}
		
		checkMove(board, possibleMoves, x, y, dir, colorTurn);
		checkCapture(board, possibleMoves, x, y, dir, colorTurn);
	}
	
	private static void checkMove(Board board, ArrayList<Move> possibleMoves, int x, int y, int dir, PlayerColor colorTurn) {
		if (board.isInside(y + dir) && board.get(x, y + dir) == ' ') {
			addMoveIfKingSafe(board, Move.createMove(board, x, y, x, y + dir), colorTurn, possibleMoves);
			
			if (board.isInside(y + dir * 2) && board.get(x, y + dir * 2) == ' ' && !board.pieceHasMoved(x, y)) {
				addMoveIfKingSafe(board, Move.createMove(board, x, y, x, y + dir * 2), colorTurn, possibleMoves);
			}
		}
	}
	
	private static void checkCapture(Board board, ArrayList<Move> possibleMoves, int x, int y, int dir, PlayerColor colorTurn) {
		for (int i = -1; i < 2; i += 2) {
			if (!board.isInside(x + i, y + dir)) {
				continue;
			}
			
			char c = board.get(x + i, y + dir);
			if (c != ' ') {
				if (colorTurn.isEnemyPiece(c)) {
					addMoveIfKingSafe(board, Move.createMove(board, x, y, x + i, y + dir), colorTurn, possibleMoves);
				}
			} else { //en passant
				c = board.get(x + i, y); //next to start
				if (colorTurn.isEnemyPiece(c) && Character.toUpperCase(c) == 'P' && board.getPieceHasMoved(x + i, y + dir) == 2) { //landing on en passant square
					addMoveIfKingSafe(board, Move.createMove(board, x, y, x + i, y + dir), colorTurn, possibleMoves);
				}
			}
		}
	}
}

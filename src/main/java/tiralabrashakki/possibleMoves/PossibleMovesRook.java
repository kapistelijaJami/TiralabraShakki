package tiralabrashakki.possibleMoves;

import java.util.ArrayList;
import tiralabrashakki.Board;
import tiralabrashakki.Move;
import tiralabrashakki.PlayerColor;

public class PossibleMovesRook extends PossibleMoves {
	
	/**
	 * Finds all the possible moves for a rook that is in a square (x, y) and adds them to possibleMoves.
	 * @param board
	 * @param x
	 * @param y
	 * @param possibleMoves
	 */
	public static void addPossibleMoves(Board board, int x, int y, ArrayList<Move> possibleMoves, MoveCategory category) {
		PlayerColor colorTurn = PlayerColor.BLACK;
		if (isWhite(board.get(x, y))) {
			colorTurn = PlayerColor.WHITE;
		}
		
		for (int dir = -1; dir < 2; dir += 2) {
			checkVertical(board, possibleMoves, x, y, dir, colorTurn, category);
			checkHorizontal(board, possibleMoves, x, y, dir, colorTurn, category);
		}
	}
	
	/**
	 * Checks vertical direction. Moves up if dir is -1, and down if dir is 1.
	 * @param board
	 * @param possibleMoves
	 * @param x
	 * @param y
	 * @param dir -1 or 1
	 * @param colorTurn 
	 */
	private static void checkVertical(Board board, ArrayList<Move> possibleMoves, int x, int y, int dir, PlayerColor colorTurn, MoveCategory category) {
		for (int i = dir; board.isInside(y + i); i += dir) {
			char c = board.get(x, y + i);
			if (c != ' ') {
				if (colorTurn.isEnemyPiece(c)) {
					addMoveIfKingSafe(board, Move.createMove(board, x, y, x, y + i), colorTurn, possibleMoves, category);
				}
				return;
			}
			
			addMoveIfKingSafe(board, Move.createMove(board, x, y, x, y + i), colorTurn, possibleMoves, category);
		}
	}
	
	/**
	 * Checks horizontal direction. Moves left if dir is -1, and right if dir is 1.
	 * @param board
	 * @param possibleMoves
	 * @param x
	 * @param y
	 * @param dir -1 or 1
	 * @param colorTurn 
	 */
	private static void checkHorizontal(Board board, ArrayList<Move> possibleMoves, int x, int y, int dir, PlayerColor colorTurn, MoveCategory category) {
		for (int i = dir; board.isInside(x + i); i += dir) {
			char c = board.get(x + i, y);
			if (c != ' ') {
				if (colorTurn.isEnemyPiece(c)) {
					addMoveIfKingSafe(board, Move.createMove(board, x, y, x + i, y), colorTurn, possibleMoves, category);
				}
				return;
			}
			
			addMoveIfKingSafe(board, Move.createMove(board, x, y, x + i, y), colorTurn, possibleMoves, category);
		}
	}
}

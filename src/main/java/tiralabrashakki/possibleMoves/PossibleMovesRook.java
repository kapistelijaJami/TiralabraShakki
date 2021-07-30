package tiralabrashakki.possibleMoves;

import java.util.ArrayList;
import tiralabrashakki.Board;
import tiralabrashakki.Move;
import tiralabrashakki.PlayerColor;

public class PossibleMovesRook extends PossibleMoves {
	
	/**
	 * Finds all the possible moves for a rook that is in a square (x, y).
	 * @param board
	 * @param x
	 * @param y
	 * @return 
	 */
	public static ArrayList<Move> possibleMoves(Board board, int x, int y) {
		ArrayList<Move> possibleMoves = new ArrayList<>();
		
		PlayerColor colorTurn = PlayerColor.BLACK;
		if (isUpperCase(board.get(x, y))) {
			colorTurn = PlayerColor.WHITE;
		}
		
		for (int dir = -1; dir < 2; dir += 2) {
			checkVertical(board, possibleMoves, x, y, dir, colorTurn);
			checkHorizontal(board, possibleMoves, x, y, dir, colorTurn);
		}
		
		return possibleMoves;
	}
	
	/**
	 * Checks vertical direction. Moves up if dir is -1, and down if dir is 1.
	 * @param board
	 * @param possibleMoves
	 * @param x
	 * @param y
	 * @param dir
	 * @param colorTurn 
	 */
	public static void checkVertical(Board board, ArrayList<Move> possibleMoves, int x, int y, int dir, PlayerColor colorTurn) {
		for (int i = dir; isInsideBoard(y + i); i += dir) {
			char c = board.get(x, y + i);
			if (c != ' ') {
				if (!turnEqualsPieceColor(colorTurn, c)) {
					Move move = Move.createMove(board, x, y, x, y + i);
					if (testMoveKingSafety(board, possibleMoves, move, colorTurn)) {
						possibleMoves.add(move);
					}
				}
				break;
			}
			
			Move move = Move.createMove(board, x, y, x, y + i);
			if (testMoveKingSafety(board, possibleMoves, move, colorTurn)) {
				possibleMoves.add(move);
			}
		}
	}
	
	/**
	 * Checks horizontal direction. Moves left if dir is -1, and right if dir is 1.
	 * @param board
	 * @param possibleMoves
	 * @param x
	 * @param y
	 * @param dir
	 * @param colorTurn 
	 */
	public static void checkHorizontal(Board board, ArrayList<Move> possibleMoves, int x, int y, int dir, PlayerColor colorTurn) {
		for (int i = dir; isInsideBoard(x + i); i += dir) {
			char c = board.get(x + i, y);
			if (c != ' ') {
				if (!turnEqualsPieceColor(colorTurn, c)) {
					Move move = Move.createMove(board, x, y, x + i, y);
					if (testMoveKingSafety(board, possibleMoves, move, colorTurn)) {
						possibleMoves.add(move);
					}
				}
				break;
			}
			
			Move move = Move.createMove(board, x, y, x + i, y);
			if (testMoveKingSafety(board, possibleMoves, move, colorTurn)) {
				possibleMoves.add(move);
			}
		}
	}
}

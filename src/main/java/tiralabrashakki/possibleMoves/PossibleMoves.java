package tiralabrashakki.possibleMoves;

import java.util.ArrayList;
import tiralabrashakki.Board;
import tiralabrashakki.Move;
import tiralabrashakki.PlayerColor;

public class PossibleMoves {
	public static ArrayList<Move> getPossibleMoves(Board board, PlayerColor colorTurn) {
		ArrayList<Move> possibleMoves = new ArrayList<>();
		
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				switch (board.get(x, y)) { //TODO: add other pieces
					case 'P':
						possibleMoves.addAll(PossibleMovesPawn.possibleMoves(board, y, x));
						break;
					case 'R':
						possibleMoves.addAll(PossibleMovesRook.possibleMoves(board, y, x));
						break;
				}
			}
		}
		
		return possibleMoves;
	}
	
	public static boolean isUpperCase(char c) {
		return c >= 'A' && c <= 'Z';
	}
	
	public static char toUpperCase(char c) {
		if (c <= 'Z') {
			return c;
		}
		
		return (char) (c - ('a' - 'A'));
	}
	
	public static boolean isInsideBoard(int i) {
		return i >= 0 && i < 8;
	}
	
	public static boolean isInsideBoard(int x, int y) {
		return isInsideBoard(x) && isInsideBoard(y);
	}
	
	public static boolean turnEqualsPieceColor(PlayerColor colorTurn, char piece) {
		return (colorTurn == PlayerColor.WHITE) == isUpperCase(piece);
	}
	
	/**
	 * Tests if after making the move, own king is not under attack. If this is true, the move is possible.
	 * @param board
	 * @param possibleMoves
	 * @param move
	 * @param colorTurn
	 * @return 
	 */
	public static boolean testMoveKingSafety(Board board, ArrayList<Move> possibleMoves, Move move, PlayerColor colorTurn) {
		//TODO: make move
		
		boolean possible = false;
		/*if (//TODO: check for king safety) {
			possible = true;
		}*/
		
		//TODO: unmake move
		
		return possible;
	}
}

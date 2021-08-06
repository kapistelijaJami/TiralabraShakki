package tiralabrashakki.possibleMoves;

import java.util.ArrayList;
import tiralabrashakki.Board;
import tiralabrashakki.Move;
import tiralabrashakki.PlayerColor;

public class PossibleMoves {
	public static ArrayList<Move> getPossibleMoves(Board board) {
		ArrayList<Move> possibleMoves = new ArrayList<>();
		
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				char c = board.get(x, y);
				if (c == ' ' || board.getTurnColor().isEnemyPiece(c)) {
					continue;
				}
				
				switch (Character.toUpperCase(c)) {
					case 'P':
						PossibleMovesPawn.addPossibleMoves(board, x, y, possibleMoves);
						break;
						
					case 'R':
						PossibleMovesRook.addPossibleMoves(board, x, y, possibleMoves);
						break;
						
					case 'N':
						PossibleMovesKnight.addPossibleMoves(board, x, y, possibleMoves);
						break;
						
					case 'B':
						PossibleMovesBishop.addPossibleMoves(board, x, y, possibleMoves);
						break;
						
					case 'Q':
						PossibleMovesRook.addPossibleMoves(board, x, y, possibleMoves);
						PossibleMovesBishop.addPossibleMoves(board, x, y, possibleMoves);
						break;
						
					case 'K':
						PossibleMovesKing.addPossibleMoves(board, x, y, possibleMoves);
						break;
				}
			}
		}
		
		return possibleMoves;
	}
	
	public static boolean isWhite(char c) {
		return Character.isUpperCase(c);
	}
	
	/**
	 * Tests if after making the move, own king is not under attack. If this is true, the move is possible.
	 * Also tests if enemy king is safe, if not, the move gives a check.
	 * @param board
	 * @param move
	 * @param colorTurn
	 * @return If move was possible or not
	 */
	public static boolean testMoveKingSafety(Board board, Move move, PlayerColor colorTurn) {
		board.makeMove(move);
		
		boolean possible = false;
		if (SquareSafety.isKingSafe(board, colorTurn)) {
			possible = true;
			
			if (!SquareSafety.isKingSafe(board, colorTurn.opposite())) {
				move.setGivesCheck(true);
			}
		}
		
		board.unmakeMove(move);
		
		return possible;
	}
	
	public static void addMoveIfKingSafe(Board board, Move move, PlayerColor colorTurn, ArrayList<Move> possibleMoves) {
		if (testMoveKingSafety(board, move, colorTurn)) {
			possibleMoves.add(move); //TODO: add to the first in the list based on takes and checks, maybe implement linked list instead
		}
	}
}

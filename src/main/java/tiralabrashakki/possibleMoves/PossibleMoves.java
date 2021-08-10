package tiralabrashakki.possibleMoves;

import java.util.ArrayList;
import tiralabrashakki.Board;
import static tiralabrashakki.Constants.BOARD_SIZE;
import tiralabrashakki.Move;
import tiralabrashakki.PlayerColor;
import static tiralabrashakki.possibleMoves.MoveCategory.LEGAL;

public class PossibleMoves {
	public static ArrayList<Move> getPossibleMoves(Board board, MoveCategory category) {
		ArrayList<Move> possibleMoves = new ArrayList<>();
		
		for (int y = 0; y < BOARD_SIZE; y++) {
			for (int x = 0; x < BOARD_SIZE; x++) {
				char c = board.get(x, y);
				if (c == ' ' || board.getTurnColor().isEnemyPiece(c)) {
					continue;
				}
				
				switch (Character.toUpperCase(c)) {
					case 'P':
						PossibleMovesPawn.addPossibleMoves(board, x, y, possibleMoves, category);
						break;
						
					case 'R':
						PossibleMovesRook.addPossibleMoves(board, x, y, possibleMoves, category);
						break;
						
					case 'N':
						PossibleMovesKnight.addPossibleMoves(board, x, y, possibleMoves, category);
						break;
						
					case 'B':
						PossibleMovesBishop.addPossibleMoves(board, x, y, possibleMoves, category);
						break;
						
					case 'Q':
						PossibleMovesRook.addPossibleMoves(board, x, y, possibleMoves, category);
						PossibleMovesBishop.addPossibleMoves(board, x, y, possibleMoves, category);
						break;
						
					case 'K':
						PossibleMovesKing.addPossibleMoves(board, x, y, possibleMoves, category);
						break;
				}
			}
		}
		
		return possibleMoves;
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
	
	public static void addMoveIfKingSafe(Board board, Move move, PlayerColor colorTurn, ArrayList<Move> possibleMoves, MoveCategory category) {
		if (!category.generateCapture() && move.isCapture()) {
			return;
		}
		
		if (category.isPseudoLegal() || testMoveKingSafety(board, move, colorTurn)) {
			if (!category.generateCheck() && move.givesCheck()) {
				return;
			}
			
			//possibleMoves.add(move);
			addMoveMVV_LVA(possibleMoves, move);
		}
	}
	
	private static void addMoveMVV_LVA(ArrayList<Move> possibleMoves, Move move) { //TODO: add killer move ordering, and maybe hashMove here as well
		if (move.isCapture()) {
			for (int i = 0; i < possibleMoves.size(); i++) {
				Move m = possibleMoves.get(i);
				if (move.compareTo(m) <= 0) {
					possibleMoves.add(i, move);
					return;
				}
			}
			possibleMoves.add(move);
		} else {
			possibleMoves.add(move);
		}
	}
	
	public static boolean isPossibleMove(Board board, Move move) {
		ArrayList<Move> possibleMoves = getPossibleMoves(board, LEGAL);
		
		return possibleMoves.contains(move);
	}
	
	public static void printMoves(Board board) {
		ArrayList<Move> moves = PossibleMoves.getPossibleMoves(board, LEGAL);
		
		for (Move move : moves) {
			System.out.println(move);
		}
		System.out.println("");
	}
}

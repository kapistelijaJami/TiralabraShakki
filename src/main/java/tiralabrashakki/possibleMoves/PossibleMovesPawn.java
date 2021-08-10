package tiralabrashakki.possibleMoves;

import java.util.ArrayList;
import tiralabrashakki.Board;
import tiralabrashakki.Move;
import tiralabrashakki.PlayerColor;

public class PossibleMovesPawn extends PossibleMoves {
	public static void addPossibleMoves(Board board, int x, int y, ArrayList<Move> possibleMoves, MoveCategory category) {
		PlayerColor colorTurn = PlayerColor.BLACK;
		int dir = 1;
		if (PlayerColor.pieceIsWhite(board.get(x, y))) {
			dir = -1;
			colorTurn = PlayerColor.WHITE;
		}
		
		checkMove(board, possibleMoves, x, y, dir, colorTurn, category);
		if (category.generateCheckOrCapture()) {
			checkCapture(board, possibleMoves, x, y, dir, colorTurn, category);
		}
	}
	
	private static void checkMove(Board board, ArrayList<Move> possibleMoves, int x, int y, int dir, PlayerColor colorTurn, MoveCategory category) {
		if (board.isInside(y + dir) && board.get(x, y + dir) == ' ') {
			
			if (y + dir == 0 || y + dir == 7) {
				for (int i = 0; i < 4; i++) {
					Move move = Move.createMove(board, x, y, x, y + dir);
					char c = getPromotionOption(i);
					
					move.setPromotesTo(colorTurn.isWhite() ? Character.toUpperCase(c) : c);
					addMoveIfKingSafe(board, move, colorTurn, possibleMoves, category);
				}
			} else {
				addMoveIfKingSafe(board, Move.createMove(board, x, y, x, y + dir), colorTurn, possibleMoves, category);
			}
			
			if (board.isInside(y + dir * 2) && board.get(x, y + dir * 2) == ' ' && !board.pieceHasMoved(x, y)) {
				addMoveIfKingSafe(board, Move.createMove(board, x, y, x, y + dir * 2), colorTurn, possibleMoves, category);
			}
		}
	}
	
	private static void checkCapture(Board board, ArrayList<Move> possibleMoves, int x, int y, int dir, PlayerColor colorTurn, MoveCategory category) {
		for (int i = -1; i < 2; i += 2) {
			if (!board.isInside(x + i, y + dir)) {
				continue;
			}
			
			char c = board.get(x + i, y + dir);
			if (c != ' ') {
				if (colorTurn.isEnemyPiece(c)) {
					if (y + dir == 0 || y + dir == 7) {
						for (int j = 0; j < 4; j++) {
							Move move = Move.createMove(board, x, y, x + i, y + dir);
							char promotesTo = getPromotionOption(j);

							move.setPromotesTo(colorTurn.isWhite() ? Character.toUpperCase(promotesTo) : promotesTo);
							addMoveIfKingSafe(board, move, colorTurn, possibleMoves, category);
						}
					} else {
						addMoveIfKingSafe(board, Move.createMove(board, x, y, x + i, y + dir), colorTurn, possibleMoves, category);
					}
				}
			} else { //en passant
				c = board.get(x + i, y); //next to start square
				if (colorTurn.isEnemyPiece(c) && Character.toUpperCase(c) == 'P' && board.getPieceHasMoved(x + i, y + dir) == 2) { //landing on en passant square
					addMoveIfKingSafe(board, Move.createMove(board, x, y, x + i, y + dir), colorTurn, possibleMoves, category);
				}
			}
		}
	}
	
	private static char getPromotionOption(int i) {
		switch (i) {
			case 0:
				return 'q';
			case 1:
				return 'n';
			case 2:
				return 'b';
			case 3:
				return 'r';
		}
		return 'q';
	}
}

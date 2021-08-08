package tiralabrashakki.possibleMoves;

import java.util.ArrayList;
import tiralabrashakki.Board;
import tiralabrashakki.Move;
import tiralabrashakki.PlayerColor;
import static tiralabrashakki.possibleMoves.PossibleMoves.isWhite;

public class PossibleMovesKing extends PossibleMoves {
	public static void addPossibleMoves(Board board, int x, int y, ArrayList<Move> possibleMoves) {
		PlayerColor colorTurn = PlayerColor.BLACK;
		if (isWhite(board.get(x, y))) {
			colorTurn = PlayerColor.WHITE;
		}
		
		for (int dirY = -1; dirY < 2; dirY++) {
			for (int dirX = -1; dirX < 2; dirX++) {
				if (dirX == 0 && dirY == 0) continue;
				
				if (board.isInside(x + dirX, y + dirY)) {
					char c = board.get(x + dirX, y + dirY);
					if (c != ' ' && colorTurn.isMyPiece(c)) {
						continue;
					}
					
					addMoveIfKingSafe(board, Move.createMove(board, x, y, x + dirX, y + dirY), colorTurn, possibleMoves);
				}
			}
		}
		
		//castling
		if (!board.pieceHasMoved(x, y)) {
			checkCastleKingside(board, possibleMoves, x, y, colorTurn);
			checkCastleQueenside(board, possibleMoves, x, y, colorTurn);
		}
	}
	
	private static void checkCastleKingside(Board board, ArrayList<Move> possibleMoves, int x, int y, PlayerColor colorTurn) {
		if (!board.isInside(x + 3)) {
			return;
		}
		
		char rook = board.get(x + 3, y);
		if (!board.pieceHasMoved(x + 3, y) && Character.toUpperCase(rook) == 'R') {
			if (board.get(x + 1, y) == ' ' && board.get(x + 2, y) == ' ') {
				if (SquareSafety.isSquareSafe(board, x, y, colorTurn) && SquareSafety.isSquareSafe(board, x + 1, y, colorTurn) && SquareSafety.isSquareSafe(board, x + 2, y, colorTurn)) {
					possibleMoves.add(Move.createMove(board, x, y, x + 2, y));
				}
			}
		}
	}
	
	private static void checkCastleQueenside(Board board, ArrayList<Move> possibleMoves, int x, int y, PlayerColor colorTurn) {
		if (!board.isInside(x - 4)) {
			return;
		}
		
		char rook = board.get(x - 4, y);
		if (!board.pieceHasMoved(x - 4, y) && Character.toUpperCase(rook) == 'R') {
			if (board.get(x - 1, y) == ' ' && board.get(x - 2, y) == ' ' && board.get(x - 3, y) == ' ') {
				if (SquareSafety.isSquareSafe(board, x, y, colorTurn)
						&& SquareSafety.isSquareSafe(board, x - 1, y, colorTurn)
						&& SquareSafety.isSquareSafe(board, x - 2, y, colorTurn)) {
					possibleMoves.add(Move.createMove(board, x, y, x - 2, y));
				}
			}
		}
	}
}

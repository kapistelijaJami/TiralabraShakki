package tiralabrashakki.possibleMoves;

import java.util.ArrayList;
import tiralabrashakki.Board;
import tiralabrashakki.Move;
import tiralabrashakki.PlayerColor;

public class PossibleMovesBishop extends PossibleMoves {
	public static void addPossibleMoves(Board board, int x, int y, ArrayList<Move> possibleMoves, MoveCategory category) {
		PlayerColor colorTurn = PlayerColor.BLACK;
		if (PlayerColor.pieceIsWhite(board.get(x, y))) {
			colorTurn = PlayerColor.WHITE;
		}
		
		for (int dirY = -1; dirY < 2; dirY += 2) {
			for (int dirX = -1; dirX < 2; dirX += 2) {
				checkDiagonally(board, possibleMoves, x, y, dirX, dirY, colorTurn, category);
			}
		}
	}

	private static void checkDiagonally(Board board, ArrayList<Move> possibleMoves, int x, int y, int dirX, int dirY, PlayerColor colorTurn, MoveCategory category) {
		for (int i = 1; board.isInside(x + dirX * i, y + dirY * i); i++) {
			char c = board.get(x + dirX * i, y + dirY * i);
			if (c != ' ') {
				if (colorTurn.isEnemyPiece(c)) {
					addMoveIfKingSafe(board, Move.createMove(board, x, y, x + dirX * i, y + dirY * i), colorTurn, possibleMoves, category);
				}
				return;
			}
			
			addMoveIfKingSafe(board, Move.createMove(board, x, y, x + dirX * i, y + dirY * i), colorTurn, possibleMoves, category);
		}
	}
}

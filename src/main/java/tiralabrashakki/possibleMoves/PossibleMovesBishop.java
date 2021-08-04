package tiralabrashakki.possibleMoves;

import java.util.ArrayList;
import tiralabrashakki.Board;
import tiralabrashakki.Move;
import tiralabrashakki.PlayerColor;
import static tiralabrashakki.possibleMoves.PossibleMoves.isWhite;

public class PossibleMovesBishop extends PossibleMoves {
	public static void addPossibleMoves(Board board, int x, int y, ArrayList<Move> possibleMoves) {
		PlayerColor colorTurn = PlayerColor.BLACK;
		if (isWhite(board.get(x, y))) {
			colorTurn = PlayerColor.WHITE;
		}
		
		for (int dirY = -1; dirY < 2; dirY += 2) {
			for (int dirX = -1; dirX < 2; dirX += 2) {
				checkDiagonally(board, possibleMoves, x, y, dirX, dirY, colorTurn);
			}
		}
	}

	private static void checkDiagonally(Board board, ArrayList<Move> possibleMoves, int x, int y, int dirX, int dirY, PlayerColor colorTurn) {
		for (int i = 1; board.isInside(x + dirX * i, y + dirY * i); i++) {
			char c = board.get(x + dirX * i, y + dirY * i);
			if (c != ' ') {
				if (colorTurn.isEnemyPiece(c)) {
					addMoveIfKingSafe(board, Move.createMove(board, x, y, x + dirX * i, y + dirY * i), colorTurn, possibleMoves);
				}
				return;
			}
			
			addMoveIfKingSafe(board, Move.createMove(board, x, y, x + dirX * i, y + dirY * i), colorTurn, possibleMoves);
		}
	}
}

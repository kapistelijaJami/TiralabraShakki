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
		
		//TODO: castling
	}
}

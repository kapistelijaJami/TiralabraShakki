package tiralabrashakki.possibleMoves;

import java.util.ArrayList;
import tiralabrashakki.Board;
import tiralabrashakki.Move;
import tiralabrashakki.PlayerColor;
import static tiralabrashakki.possibleMoves.PossibleMoves.isWhite;

public class PossibleMovesKnight extends PossibleMoves {
	public static void addPossibleMoves(Board board, int x, int y, ArrayList<Move> possibleMoves) {
		PlayerColor colorTurn = PlayerColor.BLACK;
		if (isWhite(board.get(x, y))) {
			colorTurn = PlayerColor.WHITE;
		}
		
		for (int dirY = -1; dirY < 2; dirY += 2) {
			for (int dirX = -1; dirX < 2; dirX += 2) {
				checkDirection(board, possibleMoves, x, y, 2 * dirX, dirY, colorTurn); //checks horizontal
				checkDirection(board, possibleMoves, x, y, dirX, 2 * dirY, colorTurn); //checks vertical
			}
		}
	}
	
	/**
	 * Checks one of the 8 squares the knight can attack with offsets.
	 * @param board
	 * @param possibleMoves
	 * @param x
	 * @param y
	 * @param xOff Offset x
	 * @param yOff Offset y
	 * @param colorTurn 
	 */
	private static void checkDirection(Board board, ArrayList<Move> possibleMoves, int x, int y, int xOff, int yOff, PlayerColor colorTurn) {
		if (board.isInside(x + xOff, y + yOff)) {
			char c = board.get(x + xOff, y + yOff);
			if (c != ' ' && colorTurn.isMyPiece(c)) {
				return;
			}
			
			addMoveIfKingSafe(board, Move.createMove(board, x, y, x + xOff, y + yOff), colorTurn, possibleMoves);
		}
	}
}

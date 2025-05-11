package tiralabrashakki.ai;

import tiralabrashakki.Board;
import tiralabrashakki.Constants;
import static tiralabrashakki.Constants.BISHOP_PAIR;
import static tiralabrashakki.Constants.BISHOP_VAL;
import static tiralabrashakki.Constants.BOARD_SIZE;
import static tiralabrashakki.Constants.CHECKMATE_VAL;
import static tiralabrashakki.Constants.KING_VAL;
import static tiralabrashakki.Constants.KNIGHT_VAL;
import static tiralabrashakki.Constants.PAWN_VAL;
import static tiralabrashakki.Constants.QUEEN_VAL;
import static tiralabrashakki.Constants.ROOK_VAL;
import static tiralabrashakki.Constants.STALEMATE_VAL;
import tiralabrashakki.Location;
import tiralabrashakki.PlayerColor;

public class Heuristics {
	
	private static final int[][] pawnPosition = {
        { 0,  0,  0,  0,  0,  0,  0,  0},
        {50, 50, 50, 50, 50, 50, 50, 50},
        {10, 10, 20, 30, 30, 20, 10, 10},
        { 5,  5, 10, 25, 25, 10,  5,  5},
        { 0,  0,  0, 20, 20,  0,  0,  0},
        { 5, -5,-10,  0,  0,-10, -5,  5},
        { 5, 10, 10,-20,-20, 10, 10,  5},
        { 0,  0,  0,  0,  0,  0,  0,  0}};
	
    private static final int[][] knightPosition = {
        {-50,-40,-30,-30,-30,-30,-40,-50},
        {-40,-20,  0,  0,  0,  0,-20,-40},
        {-30,  0, 10, 15, 15, 10,  0,-30},
        {-30,  5, 15, 20, 20, 15,  5,-30},
        {-30,  0, 15, 20, 20, 15,  0,-30},
        {-30,  5, 10, 15, 15, 10,  5,-30},
        {-40,-20,  0,  5,  5,  0,-20,-40},
        {-50,-40,-30,-30,-30,-30,-40,-50}};
	
    private static final int[][] bishopPosition = {
        {-20,-10,-10,-10,-10,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5, 10, 10,  5,  0,-10},
        {-10,  5,  5, 10, 10,  5,  5,-10},
        {-10,  0, 10, 10, 10, 10,  0,-10},
        {-10, 10, 10, 10, 10, 10, 10,-10},
        {-10,  5,  0,  0,  0,  0,  5,-10},
        {-20,-10,-10,-10,-10,-10,-10,-20}};
	
	private static final int[][] rookPosition = {
        { 0,  0,  0,  0,  0,  0,  0,  0},
        { 5, 10, 10, 10, 10, 10, 10,  5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        { 0,  0,  0,  5,  5,  0,  0,  0}};
	
    private static final int[][] queenPosition = {
        {-20,-10,-10, -5, -5,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5,  5,  5,  5,  0,-10},
        { -5,  0,  5,  5,  5,  5,  0, -5},
        {  0,  0,  5,  5,  5,  5,  0, -5},
        {-10,  5,  5,  5,  5,  5,  0,-10},
        {-10,  0,  5,  0,  0,  0,  0,-10},
        {-20,-10,-10, -5, -5,-10,-10,-20}};
	
    private static final int[][] kingMidPosition = {
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-20,-30,-30,-40,-40,-30,-30,-20},
        {-10,-20,-20,-20,-20,-20,-20,-10},
        { 20, 20,  0,  0,  0,  0, 20, 20},
        { 20, 30, 10,  0,  0, 10, 30, 20}};
	
    private static final int[][] kingEndPosition = {
        {-50,-40,-30,-20,-20,-30,-40,-50},
        {-30,-20,-10,  0,  0,-10,-20,-30},
        {-30,-10, 20, 30, 30, 20,-10,-30},
        {-30,-10, 30, 40, 40, 30,-10,-30},
        {-30,-10, 30, 40, 40, 30,-10,-30},
        {-30,-10, 20, 30, 30, 20,-10,-30},
        {-30,-30,  0,  0,  0,  0,-30,-30},
        {-50,-30,-30,-30,-30,-30,-30,-50}};
	
	
	public static int evaluate(Board board, int depth, boolean inCheck) {
		return evaluate(board, depth, inCheck, -1); //possible moves is not known
	}
	
	public static int evaluate(Board board, int depth, boolean inCheck, int possibleMoves) { //possible moves can be -1 if not known
		
		if (board.isDrawByRepetition()) {
			return 0;
		}
		
		if (possibleMoves == 0) {
			int multiplier = 1;
			if (board.getTurnColor().isWhite()) {
				multiplier = -1;
			}
			
			int val = inCheck ? CHECKMATE_VAL - depth : STALEMATE_VAL;
			
			return (val) * multiplier;
		}
		
		int points = 0;
		points += material(board);
		points += position(board);
		return points;
	}
	
	public static int material(Board board) {
		int points = 0;
		int whiteBishopCount = 0;
		int blackBishopCount = 0;
		
		for (int y = 0; y < BOARD_SIZE; y++) {
			for (int x = 0; x < BOARD_SIZE; x++) {
				char c = board.get(x, y);
				if (c == ' ') {
					continue;
				}
				
				int multiplier = -1;
				if (PlayerColor.WHITE.isMyPiece(c)) {
					multiplier = 1;
				}
				
				c = Character.toUpperCase(c);
				
				switch (c) {
					case 'P':
						points += PAWN_VAL * multiplier;
						break;
					case 'R':
						points += ROOK_VAL * multiplier;
						break;
					case 'N':
						points += KNIGHT_VAL * multiplier;
						break;
					case 'B':
						points += BISHOP_VAL * multiplier;
						if (multiplier == 1) {
							whiteBishopCount++;
						} else {
							blackBishopCount++;
						}
						break;
					case 'Q':
						points += QUEEN_VAL * multiplier;
						break;
					case 'K':
						points += KING_VAL * multiplier;
						break;
				}
			}
		}
		
		if (whiteBishopCount == 2) points += BISHOP_PAIR;
		if (blackBishopCount == 2) points -= BISHOP_PAIR;
		
		return points;
	}
	
	public static int position(Board board) {
		int points = 0;
		int pieces = 0;
		
		for (int y = 0; y < BOARD_SIZE; y++) {
			for (int x = 0; x < BOARD_SIZE; x++) {
				char c = board.get(x, y);
				if (c == ' ') {
					continue;
				}
				
				int multiplier = -1;
				if (PlayerColor.WHITE.isMyPiece(c)) {
					multiplier = 1;
				}
				
				int newX = 7 - x;
				int newY = 7 - y;
				
				if (multiplier == 1) {
					newX = x;
					newY = y;
				}
				
				c = Character.toUpperCase(c);
				
				switch (c) {
					case 'P':
						points += pawnPosition[newY][newX] * multiplier;
						break;
					case 'R':
						points += rookPosition[newY][newX] * multiplier;
						break;
					case 'N':
						points += knightPosition[newY][newX] * multiplier;
						break;
					case 'B':
						points += bishopPosition[newY][newX] * multiplier;
						break;
					case 'Q':
						points += queenPosition[newY][newX] * multiplier;
						break;
				}
				
				pieces++;
			}
		}
		
		Location kingW = board.getKingW();
		Location kingB = board.getKingB();
		if (pieces >= 15) {
			points += kingMidPosition[kingW.getY()][kingW.getX()];
			points -= kingMidPosition[7 - kingB.getY()][7 - kingB.getX()];
		} else {
			points += kingEndPosition[kingW.getY()][kingW.getX()];
			points -= kingEndPosition[7 - kingB.getY()][7 - kingB.getX()];
		}
		
		return points;
	}
	
	public static int getPieceCount(Board board) {
		int count = 0;
		
		for (int y = 0; y < BOARD_SIZE; y++) {
			for (int x = 0; x < BOARD_SIZE; x++) {
				char c = board.get(x, y);
				if (c != ' ') {
					count++;
				}
			}
		}
		return count;
	}
	
	/**
	 * Returns the difference of the material value of the pieces.
	 * Positive means first is more valuable, negative means second is more valuable.
	 * @param first
	 * @param second
	 * @return 
	 */
	public static int pieceComparison(char first, char second) {
		return pieceValue(first) - pieceValue(second);
	}
	
	public static int pieceValue(char c) {
		c = Character.toUpperCase(c);
		switch (c) {
			case 'P':
				return PAWN_VAL;
			case 'R':
				return ROOK_VAL;
			case 'N':
				return KNIGHT_VAL;
			case 'B':
				return BISHOP_VAL;
			case 'Q':
				return QUEEN_VAL;
			case 'K':
				return KING_VAL;
			default:
				return 0;
		}
	}
	
	public static boolean isWinningMove(int val) {
		return Constants.CHECKMATE_VAL - 100 <= val;
	}
	
	public static boolean isLosingMove(int val) {
		return -Constants.CHECKMATE_VAL + 100 >= val;
	}
	
	public static int staticExcangeEval(Board board) {
		return 0;
	}
}

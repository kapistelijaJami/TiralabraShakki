package tiralabrashakki;

public class Constants {
	public static final int FULL_BOARD_SIZE = 128;
	public static final int BOARD_SIZE = 8;
	public static final int CHECKMATE_VAL = 100000;
	public static final int STALEMATE_VAL = 0;	//was planning on doing -25, but then if it's -25 from white perspective,
												//then it's automatically +25 from black perspective, and that doesnt make any sense
												//Should probably give this from the perspective of root mover, so it's consistent throughout
	
	public static final int VALUE_UNKNOWN = 9999999;
	public static final int VALUE_TIMED_OUT = 9999998;
	
	
	//piece values:
	public static final int PAWN_VAL = 100;
	public static final int ROOK_VAL = 500;
	public static final int KNIGHT_VAL = 300;
	public static final int BISHOP_VAL = 300;
	public static final int QUEEN_VAL = 900;
	public static final int KING_VAL = 10000;
	
	public static final int BISHOP_PAIR = 50;
	
	public static final int WIDTH = 900; //def: 900
	public static final int HEIGHT = 900;
	
	
	//piece move directions
	public static final int PAWN_MOVE_WHITE = -16;
	public static final int PAWN_MOVE_BLACK = 16;
	public static final int[] PAWN_ATTACKS_WHITE = new int[] {-15, -17};
	public static final int[] PAWN_ATTACKS_BLACK = new int[] {15, 17};
	public static final int[] ROOK_MOVES = new int[] {-16, -1, 1, 16};
	public static final int[] BISHOP_MOVES = new int[] {-15, -17, 15, 17};
	public static final int[] KNIGHT_MOVES = new int[] {-31, -33, 31, 33, -14, -18, 14, 18};
}

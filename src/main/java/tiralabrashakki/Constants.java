package tiralabrashakki;

public class Constants {
	public static final int BOARD_SIZE = 8;
	public static final int CHECKMATE_VAL = 100000;
	public static final int STALEMATE_VAL = 0;	//was planning on doing -25, but then if it's -25 from white perspective,
												//then it's automatically +25 from black perspective, and that doesnt make any sense
}

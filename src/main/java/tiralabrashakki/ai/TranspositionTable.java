package tiralabrashakki.ai;

import java.util.HashMap;
import java.util.Random;
import tiralabrashakki.Board;
import static tiralabrashakki.Constants.BOARD_SIZE;
import tiralabrashakki.PlayerColor;

public class TranspositionTable {
	private static final long[] PIECE_KEYS = new long[12 * 64];
	private static final long WHITE_TO_MOVE_KEY;
	private static final long[] CASTLE_KEYS = new long[4];
	private static final long[] EN_PASSANT_KEYS = new long[BOARD_SIZE];
	
	private static final HashMap<Character, Integer> PIECE_LETTER_MAP;
	
	public static HashMap<Long, TranspositionData> TABLE;
	
	static {
		Random rand = new Random();
		
		for (int i = 0; i < PIECE_KEYS.length; i++) {
			PIECE_KEYS[i] = rand.nextLong(); //map piece to this with pieceNumber * 64 + (y * 8 + x)
		}
		
		WHITE_TO_MOVE_KEY = rand.nextLong();
		
		for (int i = 0; i < EN_PASSANT_KEYS.length; i++) {
			EN_PASSANT_KEYS[i] = rand.nextLong();
		}
		
		for (int i = 0; i < CASTLE_KEYS.length; i++) {
			CASTLE_KEYS[i] = rand.nextLong();
		}
		
		PIECE_LETTER_MAP = new HashMap<>();
		addPieces();
		
		TABLE = new HashMap<>();
	}
	
	public static long generateHash(Board board) {
		long key = 0;
		
		//hashKeys for pieces
		for (int y = 0; y < BOARD_SIZE; y++) {
			for (int x = 0; x < BOARD_SIZE; x++) {
				int piece = PIECE_LETTER_MAP.get(board.get(x, y));
				if (piece != -1) {
					key ^= PIECE_KEYS[piece * 64 + y * BOARD_SIZE + x];
				}
			}
		}
		
		//hashKeys for en passant squares
		for (int x = 0; x < BOARD_SIZE; x++) {
			if (board.isEnPassantSquare(x, 2) || board.isEnPassantSquare(x, 5)) {
				key ^= EN_PASSANT_KEYS[x];
			}
		}
		
		//hashKeys for castling rights
		if (board.canCastleKingside(PlayerColor.WHITE)) key ^= CASTLE_KEYS[0];
		if (board.canCastleKingside(PlayerColor.BLACK)) key ^= CASTLE_KEYS[1];
		if (board.canCastleQueenside(PlayerColor.WHITE)) key ^= CASTLE_KEYS[2];
		if (board.canCastleQueenside(PlayerColor.BLACK)) key ^= CASTLE_KEYS[3];
		
		if (board.getTurnColor().isWhite()) {
			key ^= WHITE_TO_MOVE_KEY;
		}
		
		return key;
	}
	
	private static void addPieces() {
		PIECE_LETTER_MAP.put(' ', -1);
		
		char[] pieceLetters = new char[] {'P', 'R', 'N', 'B', 'Q', 'K', 'p', 'r', 'n', 'b', 'q', 'k'};
		
		for (int i = 0; i < pieceLetters.length; i++) {
			PIECE_LETTER_MAP.put(pieceLetters[i], i);
		}
	}
}

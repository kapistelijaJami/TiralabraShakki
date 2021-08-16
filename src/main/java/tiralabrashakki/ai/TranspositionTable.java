package tiralabrashakki.ai;

import java.util.HashMap;
import java.util.Random;
import tiralabrashakki.Board;
import tiralabrashakki.ChessGame;
import static tiralabrashakki.Constants.BOARD_SIZE;
import static tiralabrashakki.Constants.VALUE_UNKNOWN;
import tiralabrashakki.Location;
import tiralabrashakki.Move;
import tiralabrashakki.PlayerColor;

public class TranspositionTable {
	private final long[] PIECE_KEYS = new long[12 * 64];
	private final long WHITE_TO_MOVE_KEY;
	private final long[] CASTLE_KEYS = new long[4];
	private final long[] EN_PASSANT_KEYS = new long[BOARD_SIZE];
	
	private final HashMap<Character, Integer> PIECE_LETTER_MAP;
	
	//public static HashMap<Long, TranspositionData> TABLE = new HashMap<>(1000000);
	public TT TABLE = new TT();
	
	public TranspositionTable() {
		Random rand = new Random(2123456789); //seed so I can test the performance of my algorithm between edits
		
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
	}
	
	public long generateHash(Board board) {
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
	
	public int getPieceNumberByChar(char c) {
		return PIECE_LETTER_MAP.get(c);
	}
	
	private void addPieces() {
		PIECE_LETTER_MAP.put(' ', -1);
		
		char[] pieceLetters = new char[] {'P', 'R', 'N', 'B', 'Q', 'K', 'p', 'r', 'n', 'b', 'q', 'k'};
		
		for (int i = 0; i < pieceLetters.length; i++) {
			PIECE_LETTER_MAP.put(pieceLetters[i], i);
		}
	}
	
	public TranspositionData probe(Board board, int depth, int alpha, int beta) {
		long hash = board.getHash();
		TranspositionData data = TABLE.get(hash);
		if (data == null) {
			return null;
		}
		
		int value = VALUE_UNKNOWN;
		
		if (data.depth >= depth) {
			if (data.flag == HashFlag.HASH_EXACT) {
				return data;
			}
			
			if (data.flag == HashFlag.HASH_ALPHA && data.value <= alpha) {
				value = alpha;
			}
			if (data.flag == HashFlag.HASH_BETA && data.value >= beta) {
				value = beta;
			}
		}
		
		return new TranspositionData(data.startX, data.startY, data.destX, data.destY, value, data.depth);
	}
	
	/**
	 * Records a best move on the board and its value with a hash to transposition table.
	 * @param board
	 * @param bestMove
	 * @param value
	 * @param depth Depth from this node to the leaf node. (Ignores search extensions etc.)
	 * @param flag 
	 */
	public void recordHash(Board board, Move bestMove, int value, int depth, HashFlag flag) {
		/*if (!ChessGame.TRANSPOSITION_TABLE) {
			return;
		}*/
		
		long hash = board.getHash();
		TABLE.put(hash, new TranspositionData(bestMove, value, depth, flag));
	}
	
	private long getPieceKey(char c, int x, int y) {
		int piece = PIECE_LETTER_MAP.get(c);
		if (piece != -1) {
			return PIECE_KEYS[piece * 64 + y * BOARD_SIZE + x];
		}
		return 0;
	}
	
	private long getEnPassantKey(int x) {
		return EN_PASSANT_KEYS[x];
	}
	
	private long getCastleKey(int i) {
		return CASTLE_KEYS[i]; //w_king, b_king, w_queen, b_queen
	}
	
	private long getTurnKey() {
		return WHITE_TO_MOVE_KEY;
	}
	
	/**
	 * Toggles the bits representing the changing attributes of the move.
	 * The board state has to be what it was right before making the move,
	 * so in unmake, you have to first unmake the move, and then toggle the hash.
	 * @param board
	 * @param move 
	 */
	public void updateHash(Board board, Move move) {
		long hash = board.getHash();
		
		Location start = move.getStart();
		Location dest = move.getDest();
		char piece = move.getPiece();
		
		//basic moving
		hash ^= getPieceKey(piece, start.getX(), start.getY());
		hash ^= getPieceKey(move.isPromotion() ? move.getPromotesTo() : piece, dest.getX(), dest.getY());
		
		//handle special moves
		if (move.isEnpassant()) {
			hash ^= getPieceKey(move.getTakes(), dest.getX(), start.getY());
			
		} else if (move.isCapture()) {
			hash ^= getPieceKey(move.getTakes(), dest.getX(), dest.getY());
		}
		
		if (move.isCastle()) {
			boolean kingside = false;
			if (start.getX() < dest.getX()) {
				kingside = true;
			}
			
			char rook = 'r';
			int extra = 1;
			if (board.getTurnColor().isWhite()) {
				rook = 'R';
				extra = 0;
			}

			if (kingside) {
				hash ^= getPieceKey(rook, start.getX() + 3, start.getY());
				hash ^= getPieceKey(rook, start.getX() + 1, start.getY());
				hash ^= getCastleKey(extra);
			} else {
				hash ^= getPieceKey(rook, start.getX() - 4, start.getY());
				hash ^= getPieceKey(rook, start.getX() - 1, start.getY());
				hash ^= getCastleKey(2 + extra);
			}
			
		} else if (move.isFirstMoveForPiece()) { //castling rights
			int extra = 1;
			if (board.getTurnColor().isWhite()) {
				extra = 0;
			}
			
			if (Character.toUpperCase(piece) == 'K') {
				hash ^= getCastleKey(0 + extra);
				hash ^= getCastleKey(2 + extra);
			} else if (Character.toUpperCase(piece) == 'R') {
				int i = 0;
				if (dest.getX() == 6) {
					i += 2;
				}
				hash ^= getCastleKey(i + extra);
			}
		}
		
		//pawn double jump
		if (Character.toUpperCase(piece) == 'P' && Math.abs(dest.getY() - start.getY()) == 2) {
			hash ^= getEnPassantKey(start.getX());
		}
		
		//turn changes
		hash ^= getTurnKey();
		
		board.setHash(hash);
	}
}

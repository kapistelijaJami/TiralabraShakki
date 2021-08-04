package tiralabrashakki.ai;

import java.util.HashMap;
import java.util.Random;
import tiralabrashakki.Board;
import tiralabrashakki.PlayerColor;

public class TranspositionTable {
	private static long[] pieceKeys;
	private static long sideToMoveKey;
	private static long[] castleKeys;
	private static long[] enPassantKeys;
	
	private static HashMap<Character, Integer> pieceLetterMap;
	
	public static HashMap<Long, TranspositionData> TABLE;
	
	static {
		Random rand = new Random();
		
		pieceKeys = new long[12 * 64];
		for (int i = 0; i < pieceKeys.length; i++) {
			pieceKeys[i] = rand.nextLong(); //map piece to this with pieceNumber * 64 + (y * 8 + x)
		}
		
		sideToMoveKey = rand.nextLong();
		
		enPassantKeys = new long[8];
		for (int i = 0; i < 8; i++) {
			enPassantKeys[i] = rand.nextLong();
		}
		
		castleKeys = new long[4];
		for (int i = 0; i < 4; i++) {
			castleKeys[i] = rand.nextLong();
		}
		
		pieceLetterMap = new HashMap<>();
		addPieces();
		
		TABLE = new HashMap<>();
	}
	
	public static long generateHash(Board board) {
		long key = 0;
		
		//hashKeys for pieces
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				int piece = pieceLetterMap.get(board.get(x, y));
				if (piece != -1) {
					key ^= pieceKeys[piece * 64 + y * 8 + x];
				}
			}
		}
		
		//hashKeys for en passant squares
		for (int x = 0; x < 8; x++) {
			if (board.isEnPassantSquare(x, 2) || board.isEnPassantSquare(x, 5)) {
				key ^= enPassantKeys[x];
			}
		}
		
		//hashKeys for castling rights
		if (board.canCastleKingside(PlayerColor.WHITE)) key ^= castleKeys[0];
		if (board.canCastleKingside(PlayerColor.BLACK)) key ^= castleKeys[1];
		if (board.canCastleQueenside(PlayerColor.WHITE)) key ^= castleKeys[2];
		if (board.canCastleQueenside(PlayerColor.BLACK)) key ^= castleKeys[3];
		
		key ^= sideToMoveKey;
		
		return key;
	}
	
	private static void addPieces() {
		pieceLetterMap.put(' ', -1);
		
		char[] pieceLetters = new char[] {'P', 'R', 'N', 'B', 'Q', 'K', 'p', 'r', 'n', 'b', 'q', 'k'};
		
		for (int i = 0; i < pieceLetters.length; i++) {
			pieceLetterMap.put(pieceLetters[i], i);
		}
	}
}

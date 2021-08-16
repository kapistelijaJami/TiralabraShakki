package tiralabrashakki;

import java.awt.Point;
import static tiralabrashakki.Constants.BOARD_SIZE;
import static tiralabrashakki.PlayerColor.BLACK;
import static tiralabrashakki.PlayerColor.WHITE;

public class Board {
	private char[][] board =
			{{'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'},
			{'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
			{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
			{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
			{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
			{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
			{'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
			{'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'}};
	
	
	/*private char[][] board = 
			{{'r', ' ', ' ', ' ', 'k', ' ', ' ', 'r'},
			{'p', ' ', 'p', 'p', 'q', 'p', 'b', ' '},
			{'b', 'n', ' ', ' ', 'p', 'n', 'p', ' '},
			{' ', ' ', ' ', 'P', 'N', ' ', ' ', ' '},
			{' ', 'p', ' ', ' ', 'P', ' ', ' ', ' '},
			{' ', ' ', 'N', ' ', ' ', 'Q', ' ', 'p'},
			{'P', 'P', 'P', 'B', 'B', 'P', 'P', 'P'},
			{'R', ' ', ' ', ' ', 'K', ' ', ' ', 'R'}};*/ //kiwipete
	
	/*private char[][] board = 
			{{' ', ' ', ' ', ' ', ' ', 'N', ' ', ' '},
			{'r', ' ', ' ', ' ', 'B', 'p', 'k', 'p'},
			{' ', ' ', ' ', ' ', ' ', ' ', 'p', ' '},
			{' ', ' ', ' ', 'q', ' ', ' ', ' ', ' '},
			{' ', ' ', ' ', ' ', ' ', 'Q', ' ', 'K'},
			{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
			{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
			{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}};*/ //forced mate
	
	public static String FEN = "r1b1kb1r/p1p1qppp/2p2B2/3p4/4P3/2N5/PPP2PPP/R2QKB1R b KQkq - 0 1";
	public static String FEN2 = "rnbqkbnr/pppp3p/6p1/4p1N1/2P5/4K1P1/PP1P2BP/RNBQ1q1R b kq - 0 1";
	public static String FEN3 = "r3rbk1/1pp3p1/p3b2p/1q3p2/2PQN2B/1P6/P4PPP/R3R1K1 b - - 0 1";
	public static String FEN4 = "8/8/8/7K/5k2/8/6r1/8 w - - 0 1";
	public static String FEN_KING_QUEEN_MATE = "8/8/8/1q6/4K3/8/1k6/8 w - - 0 1";
	
	
	private int[][] pieceHasMoved; //0 no, 1 yes, 2 en passant marker (pawn can eat and land to this square)
	private Location kingW;
	private Location kingB;
	private Long currentHash = null;
	private PlayerColor colorTurn = PlayerColor.WHITE;
	private int nbrOfPliesPlayed = 0;
	
	public Board() {
		pieceHasMoved = new int[BOARD_SIZE][BOARD_SIZE];
		findKings();
		resetPieceHasMoved();
		
		generateHash();
	}
	
	public Board(String FEN) {
		resetBoard();
		pieceHasMoved = new int[BOARD_SIZE][BOARD_SIZE];
		resetPieceHasMoved(); //resets all to 1, except pawn starting rows.
		readFen(FEN);
		
		findKings();
		
		generateHash();
	}
	
	public Board copy() {
		Board b = new Board();
		for (int y = 0; y < this.pieceHasMoved.length; y++) {
			for (int x = 0; x < this.pieceHasMoved[0].length; x++) {
				b.pieceHasMoved[y][x] = this.pieceHasMoved[y][x];
			}
		}
		
		for (int y = 0; y < this.board.length; y++) {
			for (int x = 0; x < this.board[0].length; x++) {
				b.board[y][x] = this.board[y][x];
			}
		}
		
		b.kingW = this.kingW;
		b.kingB = this.kingB;
		b.currentHash = this.currentHash;
		b.colorTurn = this.colorTurn;
		b.nbrOfPliesPlayed = this.nbrOfPliesPlayed;
		
		return b;
	}
	
	public char get(int x, int y) {
		return this.board[y][x];
	}
	
	public int getPieceHasMoved(int x, int y) {
		return this.pieceHasMoved[y][x];
	}
	
	public boolean pieceHasMoved(int x, int y) {
		return this.pieceHasMoved[y][x] != 0;
	}
	
	public boolean isEnPassantSquare(int x, int y) {
		return this.pieceHasMoved[y][x] == 2;
	}
	
	public boolean canCastleKingside(PlayerColor color) {
		if (color.isWhite()) {
			return pieceHasMoved[7][4] == 0 && pieceHasMoved[7][7] == 0;
		} else {
			return pieceHasMoved[0][4] == 0 && pieceHasMoved[0][7] == 0;
		}
	}
	
	public boolean canCastleQueenside(PlayerColor color) {
		if (color.isWhite()) {
			return pieceHasMoved[7][0] == 0 && pieceHasMoved[7][4] == 0;
		} else {
			return pieceHasMoved[0][0] == 0 && pieceHasMoved[0][4] == 0;
		}
	}
	
	public Location getKingW() {
		return kingW;
	}
	
	public Location getKingB() {
		return kingB;
	}
	
	public PlayerColor getTurnColor() {
		return colorTurn;
	}
	
	public void setTurnColor(PlayerColor colorTurn) {
		this.colorTurn = colorTurn;
	}

	public int getNbrOfPliesPlayed() {
		return nbrOfPliesPlayed;
	}
	
	private void resetPieceHasMoved() {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				if (y == 1 || y == 6) {
					pieceHasMoved[y][x] = 0; //pawn starting rows
					continue;
				}
				pieceHasMoved[y][x] = (board[y][x] == ' ' ? 1 : 0);
			}
		}
	}
	
	private void findKings() {
		for (int y = 0; y < board.length; y++) {
			for (int x = 0; x < board[0].length; x++) {
				if (board[y][x] == 'K') {
					kingW = new Location(x, y);
				}
				
				if (board[y][x] == 'k') {
					kingB = new Location(x, y);
				}
			}
		}
	}
	
	public void makeMove(Move move) {
		Location start = move.getStart();
		Location dest = move.getDest();
		
		move.setErasedEnPassant(eraseEnPassantMarker());
		
		updateKingPosition(start, dest);
		
		//creates en passant marker when pawn double jumps
		if (move.getPiece() == 'P' && start.getY() - dest.getY() == 2) {
			pieceHasMoved[start.getY() - 1][start.getX()] = 2;
		} else if (move.getPiece() == 'p' && dest.getY() - start.getY() == 2) {
			pieceHasMoved[start.getY() + 1][start.getX()] = 2;
		}
		
		if (move.isEnpassant()) {
			board[start.getY()][dest.getX()] = ' ';
		} else if (move.isCastle()) {
			makeCastle(start, dest);
		} else if (move.isPromotion()) {
			board[start.getY()][start.getX()] = move.getPromotesTo();
		}
		
		movePiece(start, dest);
		
		colorTurn = colorTurn.opposite();
		nbrOfPliesPlayed++;
	}
	
	public void unmakeMove(Move move) {
		Location start = move.getStart();
		Location dest = move.getDest();
		
		updateKingPosition(dest, start);
		
		//removes en passant marker if pawn double jumped
		if (move.getPiece() == 'P' && start.getY() - dest.getY() == 2) {
			pieceHasMoved[start.getY() - 1][start.getX()] = 1;
		} else if (move.getPiece() == 'p' && dest.getY() - start.getY() == 2) {
			pieceHasMoved[start.getY() + 1][start.getX()] = 1;
		}
		
		if (move.isPromotion()) {
			board[dest.getY()][dest.getX()] = WHITE.isMyPiece(move.getPiece()) ? 'P' : 'p';
		}
		
		if (move.isEnpassant()) {
			unmovePiece(start, dest, ' ', move.isFirstMoveForPiece());
			board[start.getY()][dest.getX()] = move.getTakes();
		} else {
			unmovePiece(start, dest, move.getTakes(), move.isFirstMoveForPiece());
		}
		
		if (move.isCastle()) {
			unmakeCastle(start, dest);
		}
		
		remakeEnPassantMarker(move.getErasedEnPassant());
		
		colorTurn = colorTurn.opposite();
		nbrOfPliesPlayed--;
	}
	
	public void updateKingPosition(Location start, Location dest) {
		if (board[start.getY()][start.getX()] == 'K') {
			kingW.set(dest);
		} else if (board[start.getY()][start.getX()] == 'k') {
			kingB.set(dest);
		}
	}
	
	public boolean isInside(int i) {
		return i >= 0 && i < 8;
	}
	
	public boolean isInside(int x, int y) {
		return isInside(x) && isInside(y);
	}
	
	/*TODO: keep track of hash and only update it
	by little when making and unmaking moves.
	Updates captures, moves, colorTurn etc. You can
	toggle pieces/attributes with the same XOR key.*/
	public void updateHashMake(Move move) {
		long hash = getHash(); //needs to be up to date before making a move
	}
	
	public void updateHashUnmake(Move move) {
		long hash = getHash();
	}
	
	public long getHash() {
		if (currentHash == null) {
			generateHash();
			return currentHash;
		}
		
		generateHash(); //TODO: remove this when board can keep track of the hash itself
		return currentHash;
	}
	
	private void generateHash() {
		currentHash = ChessGame.TT.generateHash(this);
	}
	
	private void resetBoard() {
		for (int y = 0; y < BOARD_SIZE; y++) {
			for (int x = 0; x < BOARD_SIZE; x++) {
				board[y][x] = ' ';
			}
		}
	}
	
	private Location eraseEnPassantMarker() {
		for (int x = 0; x < 8; x++) {
			if (pieceHasMoved[5][x] == 2) {
				pieceHasMoved[5][x] = 1;
				return new Location(x, 5);
			} else if (pieceHasMoved[2][x] == 2) {
				pieceHasMoved[2][x] = 1;
				return new Location(x, 2);
			}
		}
		
		return null;
	}
	
	private void remakeEnPassantMarker(Location location) {
		if (location == null) {
			return;
		}
		pieceHasMoved[location.getY()][location.getX()] = 2;
	}
	
	/**
	 * Moves only the rook during a castle. King is moved normally in makeMove.
	 * @param start
	 * @param dest 
	 */
	private void makeCastle(Location start, Location dest) {
		boolean kingside = false;
		
		if (start.getX() < dest.getX()) {
			kingside = true;
		}
		
		if (kingside) {
			movePiece(start.getX() + 3, start.getY(), start.getX() + 1, start.getY());
		} else {
			movePiece(start.getX() - 4, start.getY(), start.getX() - 1, start.getY());
		}
	}
	
	private void unmakeCastle(Location start, Location dest) {
		boolean kingside = false;
		
		if (start.getX() < dest.getX()) {
			kingside = true;
		}
		
		if (kingside) {
			unmovePiece(start.getX() + 3, start.getY(), start.getX() + 1, start.getY(), ' ', true);
		} else {
			unmovePiece(start.getX() - 4, start.getY(), start.getX() - 1, start.getY(), ' ', true);
		}
	}
	
	private void movePiece(Location start, Location dest) {
		movePiece(start.getX(), start.getY(), dest.getX(), dest.getY());
	}
	
	private void movePiece(int startX, int startY, int destX, int destY) {
		board[destY][destX] = board[startY][startX];
		board[startY][startX] = ' ';
		pieceHasMoved[startY][startX] = 1;
	}
	
	private void unmovePiece(Location start, Location dest, char takes, boolean isFirstMoveForPiece) {
		unmovePiece(start.getX(), start.getY(), dest.getX(), dest.getY(), takes, isFirstMoveForPiece);
	}
	
	private void unmovePiece(int startX, int startY, int destX, int destY, char takes, boolean isFirstMoveForPiece) {
		board[startY][startX] = board[destY][destX];
		board[destY][destX] = takes;
		pieceHasMoved[startY][startX] = isFirstMoveForPiece ? 0 : 1;
	}
	
	public void makeNullMove() {
		colorTurn = colorTurn.opposite();
		nbrOfPliesPlayed++;
	}
	
	public void unmakeNullMove() {
		colorTurn = colorTurn.opposite();
		nbrOfPliesPlayed--;
	}

	private void readFen(String FEN) {
		String[] rows = FEN.split("[/ ]");
		
		for (int y = 0; y < BOARD_SIZE; y++) {
			int x = 0;
			for (int i = 0; i < rows[y].length(); i++) {
				char c = rows[y].charAt(i);
				if (Character.isDigit(c)) {
					for (int j = 0; j < c - '0'; j++) {
						board[y][x] = ' ';
						x++;
					}
				} else {
					board[y][x] = c;
					x++;
				}
			}
		}
		
		colorTurn = rows[8].charAt(0) == 'w' ? WHITE : BLACK;
		String castlingRights = rows[9];
		readFENCastlingRights(castlingRights);
		
		String enPassantSquare = rows[10];
		if (enPassantSquare.charAt(0) != '-') {
			Point p = squareToPoint(enPassantSquare);
			pieceHasMoved[p.y][p.x] = 2;
		}
		
		if (rows.length == 12) {
			nbrOfPliesPlayed = (Integer.parseInt(rows[11]) - 1) * 2 + (colorTurn == BLACK ? 1 : 0);
		}
	}
	
	private void readFENCastlingRights(String rights) {
		if (rights.charAt(0) == '-') {
			return;
		}
		for (int i = 0; i < rights.length(); i++) {
			char c = rights.charAt(i);
			
			int y = 0;
			if (PlayerColor.pieceIsWhite(c)) {
				y = 7;
			}
			
			if (Character.toUpperCase(c) == 'K') {
				pieceHasMoved[y][4] = 0;
				pieceHasMoved[y][7] = 0;
			} else {
				pieceHasMoved[y][4] = 0;
				pieceHasMoved[y][0] = 0;
			}
		}
	}
	
	private static Point squareToPoint(String sqr) {
		int x = sqr.charAt(0) - 'a';
		int y = 8 - (sqr.charAt(1) - '0');
		
		return new Point(x, y);
	}
}

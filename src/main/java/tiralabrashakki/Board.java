package tiralabrashakki;

import static tiralabrashakki.Constants.BOARD_SIZE;

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
	
	
	private int[][] pieceHasMoved; //0 no, 1 yes, 2 en passant marker (pawn can eat and land to this square)
	private Location kingW;
	private Location kingB;
	private Long currentHash = null;
	private PlayerColor colorTurn = PlayerColor.WHITE;
	private int nbrOfPliesPlayed = 0;
	
	public Board() {
		this.pieceHasMoved = new int[BOARD_SIZE][BOARD_SIZE];
		kingW = new Location(4, 7);
		kingB = new Location(4, 0);
		resetPieceHasMoved();
		
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
				pieceHasMoved[y][x] = (board[y][x] == ' ' ? 1 : 0);
			}
		}
	}
	
	public void makeMove(Move move) {
		Location start = move.getStart();
		Location dest = move.getDest();
		
		//TODO: promotion
		
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
		}
		
		movePiece(start, dest);
		
		colorTurn = colorTurn.opposite();
		nbrOfPliesPlayed++;
	}
	
	public void unmakeMove(Move move) {
		Location start = move.getStart();
		Location dest = move.getDest();
		
		//TODO: promotion
		
		updateKingPosition(dest, start);
		
		//removes en passant marker if pawn double jumped
		if (move.getPiece() == 'P' && start.getY() - dest.getY() == 2) {
			pieceHasMoved[start.getY() - 1][start.getX()] = 1;
		} else if (move.getPiece() == 'p' && dest.getY() - start.getY() == 2) {
			pieceHasMoved[start.getY() + 1][start.getX()] = 1;
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
	
	/*public Board copy() {
		
	}*/
}

package tiralabrashakki;

import tiralabrashakki.ai.TranspositionTable;

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
	private int[][] pieceHasMoved; //0 no, 1 yes, 2 en passant marker (pawn can eat and land to this square)
	private Location kingW;
	private Location kingB;
	private Long currentHash = null;
	private PlayerColor colorTurn = PlayerColor.WHITE;
	
	public Board() {
		this.pieceHasMoved = new int[8][8];
		kingW = new Location(4, 7);
		kingB = new Location(4, 0);
		resetPieceHasMoved();
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
		
		//TODO: castle and promotion
		
		move.setErasedEnPassant(eraseEnPassantMarker());
		
		setKingPosition(start, dest);
		
		//creates en passant marker when pawn double jumps
		if (move.getPiece() == 'P' && start.getY() - dest.getY() == 2) {
			pieceHasMoved[start.getY() - 1][start.getX()] = 2;
		} else if (move.getPiece() == 'p' && dest.getY() - start.getY() == 2) {
			pieceHasMoved[start.getY() + 1][start.getX()] = 2;
		}
		
		if (move.isIsEnpassant()) {
			board[start.getY()][dest.getX()] = ' ';
		}
		
		board[dest.getY()][dest.getX()] = move.getPiece();
		board[start.getY()][start.getX()] = ' ';
		pieceHasMoved[start.getY()][start.getX()] = 1;
		
		colorTurn = colorTurn.opposite();
	}
	
	public void unmakeMove(Move move) {
		Location start = move.getStart();
		Location dest = move.getDest();
		
		//TODO: castle and promotion
		
		setKingPosition(dest, start);
		
		//removes en passant marker if pawn double jumped
		if (move.getPiece() == 'P' && start.getY() - dest.getY() == 2) {
			pieceHasMoved[start.getY() - 1][start.getX()] = 1;
		} else if (move.getPiece() == 'p' && dest.getY() - start.getY() == 2) {
			pieceHasMoved[start.getY() + 1][start.getX()] = 1;
		}
		
		board[start.getY()][start.getX()] = move.getPiece();
		
		if (move.isIsEnpassant()) {
			board[dest.getY()][dest.getX()] = ' ';
			board[start.getY()][dest.getX()] = move.getTakes();
		} else {
			board[dest.getY()][dest.getX()] = move.getTakes();
		}
		
		if (move.isFirstMoveForPiece()) {
			pieceHasMoved[start.getY()][start.getX()] = 0;
		}
		
		remakeEnPassantMarker(move.getErasedEnPassant());
		
		colorTurn = colorTurn.opposite();
	}
	
	public void setKingPosition(Location start, Location dest) {
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
	
	public void updateHashMake(Move move) { //TODO: keep track of hash and only update it by little when making and unmaking moves. Updates captures, moves, colorTurn etc. You can toggle pieces/attributes with the same XOR key.
		long hash = getHash();
	}
	
	public void updateHashUnmake(Move move) {
		long hash = getHash();
	}
	
	public long getHash() {
		if (currentHash == null) {
			currentHash = TranspositionTable.generateHash(this);
		}
		return currentHash;
	}
	
	private Location eraseEnPassantMarker() {
		int y = 2;
		if (colorTurn.isWhite()) {
			y = 5;
		}
		for (int x = 0; x < 8; x++) {
			if (pieceHasMoved[y][x] == 2) {
				pieceHasMoved[y][x] = 1;
				return new Location(x, y);
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
}

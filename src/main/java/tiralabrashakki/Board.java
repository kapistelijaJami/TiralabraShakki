package tiralabrashakki;

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
	private boolean[][] pieceHasMoved;
	
	public Board() {
		this.pieceHasMoved = new boolean[8][8];
		resetPieceHasMoved();
	}
	
	public char get(int x, int y) {
		return this.board[y][x];
	}
	
	public boolean pieceHasMoved(int x, int y) {
		return this.pieceHasMoved[y][x];
	}
	
	private void resetPieceHasMoved() {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				pieceHasMoved[y][x] = (board[y][x] == ' ');
			}
		}
	}
}

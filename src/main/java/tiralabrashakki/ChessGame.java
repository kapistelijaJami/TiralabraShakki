package tiralabrashakki;

public class ChessGame {
	public static void main(String[] args) {
		Board board = new Board();
		
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				if (x != 0) {
					System.out.print(", ");
				}
				System.out.print(board.get(x, y));
			}
			System.out.println("");
		}
	}
}

package tiralabrashakki;

import tiralabrashakki.ui.Game;
import java.util.ArrayList;
import tiralabrashakki.ai.AlphaBeta;
import tiralabrashakki.ai.HashFlag;
import tiralabrashakki.ai.Perft;
import tiralabrashakki.ai.TranspositionData;
import tiralabrashakki.ai.TranspositionTable;
import tiralabrashakki.possibleMoves.MoveCategory;
import static tiralabrashakki.possibleMoves.MoveCategory.LEGAL;
import tiralabrashakki.possibleMoves.PossibleMoves;
import tiralabrashakki.possibleMoves.SquareSafety;

public class ChessGame {
	public static final TranspositionTable TT = new TranspositionTable();
	public static final boolean ASPIRATION_WINDOW = true;
	
	public static long nodes = 0;
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
		
		
		//move generation tests
		/*Perft perft = new Perft();
		Board board = new Board();
		
		int depth = 6;
		long time = System.currentTimeMillis();
		for (int i = 1; i <= depth; i++) {
			perft.dividePerft(board, i);
			System.out.println("");
		}
		System.out.println("total: " + perft.doPerft(board, depth));
		System.out.println(System.currentTimeMillis() - time);*/
	}
	
	public static void printBoard(Board board) {
		System.out.print("     ");
		for (int i = 0; i < 8; i++) {
			System.out.print(i + ". ");
		}
		System.out.println("");
		
		System.out.print("    ");
		for (int i = 0; i < 8; i++) {
			System.out.print("___");
		}
		
		System.out.println("");
		for (int y = 0; y < 8; y++) {
			System.out.print((8 - y) + ". | ");
			for (int x = 0; x < 8; x++) {
				if (x != 0) {
					System.out.print(", ");
				}
				System.out.print(board.get(x, y));
			}
			System.out.println(" |  " + y + ".");
		}
		
		System.out.print("    ");
		for (int i = 0; i < 8; i++) {
			System.out.print("¯¯¯");
		}
		
		System.out.println("");
		System.out.print("     ");
		for (int i = 0; i < 8; i++) {
			System.out.print((char)(i + 'a') + ". ");
		}
		
		System.out.println("\n\n");
	}
}

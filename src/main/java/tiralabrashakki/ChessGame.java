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

//TODO: Some problems with move generation: https://i.imgur.com/gGK5FZ3.png and https://i.imgur.com/cYUZARO.png
public class ChessGame {
	public static final TranspositionTable TT = new TranspositionTable();
	public static boolean ASPIRATION_WINDOW = true;
	public static boolean NULL_MOVE_PRUNING = true;
	public static boolean SORT_HASH_MOVE = true;
	public static boolean LATE_MOVE_REDUCTION = true;
	public static boolean PRINCIPAL_VARIATION = true;
	public static boolean QUIESCENCE_SEARCH = true;
	
	public static long nodes = 0;
	
	public static void main(String[] args) {
		if (args.length != 0 && args[0].toLowerCase().equals("performance")) {
			performanceTest();
		} else {
			Game game = new Game();
			game.start();
		}
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
	
	public static void performanceTest() {
		Board board = new Board();
		
		int depth = 6;
		AlphaBeta ab = new AlphaBeta();
		NULL_MOVE_PRUNING = false;
		SORT_HASH_MOVE = false;
		LATE_MOVE_REDUCTION = false;
		//PRINCIPAL_VARIATION = false;
		QUIESCENCE_SEARCH = false;
		ASPIRATION_WINDOW = false;
		
		System.out.println("Performance tests, quiescence search off.");
		System.out.println("Only alpha-beta, depth " + depth + ":");
		
		long time = System.nanoTime();
		ab.findBestMove(board, depth, false);
		System.out.println("Time taken total: " + ((System.nanoTime() - time) / 1e6) + " ms");
		
		
		System.out.println();
		System.out.println("Alpha-beta with aspiration window, depth " + depth + ":");
		ASPIRATION_WINDOW = true;
		
		time = System.nanoTime();
		ab.findBestMove(board, depth, false);
		System.out.println("Time taken total: " + ((System.nanoTime() - time) / 1e6) + " ms");
		
		
		System.out.println();
		System.out.println("Alpha-beta, aspiration window, sort hash move, depth " + depth + ":");
		SORT_HASH_MOVE = true;
		
		time = System.nanoTime();
		ab.findBestMove(board, depth, false);
		System.out.println("Time taken total: " + ((System.nanoTime() - time) / 1e6) + " ms");
		
		
		System.out.println();
		System.out.println("Alpha-beta, aspiration window, sort hash move, null move pruning, depth " + depth + ":");
		NULL_MOVE_PRUNING = true;
		
		time = System.nanoTime();
		ab.findBestMove(board, depth, false);
		System.out.println("Time taken total: " + ((System.nanoTime() - time) / 1e6) + " ms");
		
		
		System.out.println();
		System.out.println("Alpha-beta, aspiration window, sort hash move, null move pruning, late move reduction, depth " + depth + ":");
		LATE_MOVE_REDUCTION = true;
		
		time = System.nanoTime();
		ab.findBestMove(board, depth, false);
		System.out.println("Time taken total: " + ((System.nanoTime() - time) / 1e6) + " ms");
		
		System.out.println();
	}
}

package tiralabrashakki;

import java.util.ArrayList;
import tiralabrashakki.ai.AlphaBeta;
import tiralabrashakki.ai.AlphaBeta2;
import tiralabrashakki.ai.HashFlag;
import tiralabrashakki.ai.TranspositionData;
import tiralabrashakki.ai.TranspositionTable;
import tiralabrashakki.possibleMoves.MoveCategory;
import static tiralabrashakki.possibleMoves.MoveCategory.LEGAL;
import tiralabrashakki.possibleMoves.PossibleMoves;
import tiralabrashakki.possibleMoves.SquareSafety;

public class ChessGame {
	public static final TranspositionTable TT = new TranspositionTable();
	public static final boolean ASPIRATION_WINDOW = true;
	public static boolean TRANSPOSITION_TABLE = true;
	public static final boolean ALPHABETA_PRUNING = true;
	
	public static long nodes = 0;
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
		
		/*AlphaBeta2 ab = new AlphaBeta2();
		Board board = new Board();
		printBoard(board);
		PossibleMoves.printMoves(board);*/
		
		/*int depth = 8;
		
		Move move = ab.findBestMove(board, depth);
		System.out.println(board.getTurnColor() + " played: " + move);*/
		
		
		/*while (!PossibleMoves.getPossibleMoves(board).isEmpty()) {
			Move move = ab.findBestMove(board, depth);
			System.out.println("Move " + board.getNbrOfPliesPlayed() + ": " + board.getTurnColor() + " played: " + move);
			System.out.println();
			board.makeMove(move);
			printBoard(board);
		}*/
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

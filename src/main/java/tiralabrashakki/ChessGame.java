package tiralabrashakki;

import java.util.ArrayList;
import tiralabrashakki.ai.AlphaBeta;
import tiralabrashakki.possibleMoves.PossibleMoves;

public class ChessGame {
	public static void main(String[] args) {
		Board board = new Board();
		
		ArrayList<Move> moves = PossibleMoves.getPossibleMoves(board);
		System.out.print("{");
		for (Move move : moves) {
			System.out.print("{new Location(" + move.getStart().getX() + ", " + move.getStart().getY() + "), new Location(" + move.getDest().getX() + ", " + move.getDest().getY() + ")}, ");
		}
		
		/*printBoard(board);
		
		AlphaBeta ab = new AlphaBeta();
		int depth = 5;
		
		while (!PossibleMoves.getPossibleMoves(board).isEmpty()) {
			Move move = ab.findBestMove(board, depth);
			System.out.println(board.getTurnColor() + " played: " + move);
			board.makeMove(move);
			printBoard(board);
		}*/
		
		/*printBoard(board);
		//printMoves(board);
		
		board.makeMove(Move.createMove(board, 4, 6, 4, 4));
		printBoard(board);
		//printMoves(board);
		
		board.makeMove(Move.createMove(board, 4, 1, 4, 3));
		printBoard(board);
		//printMoves(board);
		
		board.makeMove(Move.createMove(board, 3, 6, 3, 4));
		printBoard(board);
		//printMoves(board);
		
		Move move = Move.createMove(board, 4, 3, 3, 4);
		board.makeMove(move);
		printBoard(board);
		//printMoves(board);
		
		board.unmakeMove(move);
		printBoard(board);
		//printMoves(board);*/
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
	
	public static void printMoves(Board board) {
		ArrayList<Move> moves = PossibleMoves.getPossibleMoves(board);
		
		for (Move move : moves) {
			System.out.println(move);
		}
		System.out.println("");
	}
}

package tiralabrashakki;

import java.util.ArrayList;
import tiralabrashakki.possibleMoves.PossibleMoves;

public class ChessGame {
	public static void main(String[] args) {
		Board board = new Board();
		
		printBoard(board);
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
		//printMoves(board);
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
		
		System.out.println("");
		System.out.println("");
	}
	
	public static void printMoves(Board board) {
		ArrayList<Move> moves = PossibleMoves.getPossibleMoves(board);
		
		for (Move move : moves) {
			System.out.println(move);
		}
		System.out.println("");
	}
}

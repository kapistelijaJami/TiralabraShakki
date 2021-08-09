package tests;

import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import tiralabrashakki.Board;
import tiralabrashakki.Location;
import tiralabrashakki.Move;
import static tiralabrashakki.possibleMoves.MoveCategory.LEGAL;
import tiralabrashakki.possibleMoves.PossibleMoves;

public class PossibleMovesTest {
	private Board board;
	
	public PossibleMovesTest() {
	}
	
	@BeforeAll
	public static void setUpClass() {
	}
	
	@AfterAll
	public static void tearDownClass() {
	}
	
	@BeforeEach
	public void setUp() {
		board = new Board();
	}
	
	@AfterEach
	public void tearDown() {
	}
	
	@Test
	public void givesCheckTest() {
		board.makeMove(Move.createMove(board, 5, 6, 5, 5));
		board.makeMove(Move.createMove(board, 4, 1, 4, 3));
		board.makeMove(Move.createMove(board, 6, 6, 6, 4));
		
		ArrayList<Move> moves = PossibleMoves.getPossibleMoves(board, LEGAL);
		
		Move move = null;
		for (Move m : moves) {
			if (m.getStart().equals(new Location(3, 0)) && m.getDest().equals(new Location(7, 4))) { //checkmate
				move = m;
			}
		}
		
		assertNotNull(move);
		assertTrue(move.givesCheck());
		
		board.makeMove(move);
		
		moves = PossibleMoves.getPossibleMoves(board, LEGAL);
		
		assertEquals(0, moves.size());
	}
	
	@Test
	public void checkAllPossibleMovesTest() {
		int[][] allPossibleMoves = new int[][] {
			{0, 6, 0, 5},
			{0, 6, 0, 4},
			{1, 6, 1, 5},
			{1, 6, 1, 4},
			{2, 6, 2, 5},
			{2, 6, 2, 4},
			{3, 6, 3, 5},
			{3, 6, 3, 4},
			{4, 6, 4, 5},
			{4, 6, 4, 4},
			{5, 6, 5, 5},
			{5, 6, 5, 4},
			{6, 6, 6, 5},
			{6, 6, 6, 4},
			{7, 6, 7, 5},
			{7, 6, 7, 4},
			{1, 7, 0, 5},
			{1, 7, 2, 5},
			{6, 7, 5, 5},
			{6, 7, 7, 5}};
		
		ArrayList<Move> moves = PossibleMoves.getPossibleMoves(board, LEGAL);
		
		for (int[] loc : allPossibleMoves ){
			assertTrue(moves.contains(Move.createMove(board, loc[0], loc[1], loc[2], loc[3])));
		}
		
		assertEquals(allPossibleMoves.length, moves.size());
	}
}

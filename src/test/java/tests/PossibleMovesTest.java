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
		
		ArrayList<Move> moves = PossibleMoves.getPossibleMoves(board);
		
		Move move = null;
		for (Move m : moves) {
			if (m.getStart().equals(new Location(3, 0)) && m.getDest().equals(new Location(7, 4))) { //checkmate
				move = m;
			}
		}
		
		assertNotNull(move);
		assertTrue(move.givesCheck());
		
		board.makeMove(move);
		
		moves = PossibleMoves.getPossibleMoves(board);
		
		assertEquals(0, moves.size());
	}
	
	@Test
	public void checkAllPossibleMovesTest() {
		Location[][] locations = new Location[][] { //list of all possible white moves at the start of the game
			{new Location(0, 6), new Location(0, 5)},
			{new Location(0, 6), new Location(0, 4)},
			{new Location(1, 6), new Location(1, 5)},
			{new Location(1, 6), new Location(1, 4)},
			{new Location(2, 6), new Location(2, 5)},
			{new Location(2, 6), new Location(2, 4)},
			{new Location(3, 6), new Location(3, 5)},
			{new Location(3, 6), new Location(3, 4)},
			{new Location(4, 6), new Location(4, 5)},
			{new Location(4, 6), new Location(4, 4)},
			{new Location(5, 6), new Location(5, 5)},
			{new Location(5, 6), new Location(5, 4)},
			{new Location(6, 6), new Location(6, 5)},
			{new Location(6, 6), new Location(6, 4)},
			{new Location(7, 6), new Location(7, 5)},
			{new Location(7, 6), new Location(7, 4)},
			{new Location(1, 7), new Location(0, 5)},
			{new Location(1, 7), new Location(2, 5)},
			{new Location(6, 7), new Location(5, 5)},
			{new Location(6, 7), new Location(7, 5)}};
		
		ArrayList<Move> moves = PossibleMoves.getPossibleMoves(board);
		
		for (Location[] loc : locations) {
			assertTrue(moves.contains(Move.createMove(board, loc[0].getX(), loc[0].getY(), loc[1].getX(), loc[1].getY())));
		}
	}
}

package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import tiralabrashakki.Board;
import tiralabrashakki.Move;
import tiralabrashakki.ai.MoveAndValue;

public class MoveAndValueTest {
	
	public MoveAndValueTest() {
	}
	
	@BeforeAll
	public static void setUpClass() {
	}
	
	@AfterAll
	public static void tearDownClass() {
	}
	
	@BeforeEach
	public void setUp() {
	}
	
	@AfterEach
	public void tearDown() {
	}
	
	@Test
	public void moveAndValueTest() {
		Board board = new Board();
		MoveAndValue move = new MoveAndValue(Move.createMove(board, 4, 6, 4, 4), 15);
		assertEquals(move.getMove().getStart().getX(), 4);
		move.setMove(Move.createMove(board, 3, 6, 3, 5));
		
		assertEquals(move.getMove().getDest().getY(), 5);
		
		assertEquals(move.getValue(), 15);
		
		move.setValue(18);
		assertEquals(move.getValue(), 18);
	}
}

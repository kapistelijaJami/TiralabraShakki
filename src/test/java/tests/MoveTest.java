package tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tiralabrashakki.Board;
import tiralabrashakki.Move;

public class MoveTest {
	private Board board;
	@BeforeEach
	public void setUp() {
		board = new Board();
	}
	
	@Test
	public void moveCreationTest() {
		Move move = Move.createMove(board, 4, 6, 4, 4);
		
		assertEquals(move.getStart().getX(), 4);
		assertEquals(move.getStart().getY(), 6);
		assertEquals(move.getDest().getX(), 4);
		assertEquals(move.getDest().getY(), 4);
		assertEquals(move.getTakes(), ' ');
	}
	
	@Test
	public void makeMoveTest() {
		assertEquals(board.get(4, 6), 'P');
		assertEquals(board.get(4, 4), ' ');
		
		board.makeMove(Move.createMove(board, 4, 6, 4, 4));
		
		assertEquals(board.get(4, 6), ' ');
		assertEquals(board.get(4, 4), 'P');
	}
	
	@Test
	public void makeCaptureTest() {
		board.makeMove(Move.createMove(board, 4, 6, 4, 4));
		board.makeMove(Move.createMove(board, 4, 1, 4, 3));
		board.makeMove(Move.createMove(board, 3, 6, 3, 4));
		
		assertEquals(board.get(3, 4), 'P');
		assertEquals(board.get(4, 3), 'p');
		Move move = Move.createMove(board, 4, 3, 3, 4);
		board.makeMove(move);
		
		assertEquals(board.get(3, 4), 'p');
		assertEquals(board.get(4, 3), ' ');
		assertEquals(move.getTakes(), 'P');
	}
	
	@Test
	public void createEnPassantMoveTest() {
		board.makeMove(Move.createMove(board, 0, 6, 0, 5));
		board.makeMove(Move.createMove(board, 5, 1, 5, 3));
		board.makeMove(Move.createMove(board, 1, 6, 1, 5));
		board.makeMove(Move.createMove(board, 5, 3, 5, 4));
		Move move1 = Move.createMove(board, 4, 6, 4, 4);
		assertFalse(move1.isEnpassant());
		board.makeMove(move1);
		
		Move move2 = Move.createMove(board, 5, 4, 4, 5);
		assertTrue(move2.isEnpassant());
	}
	
	@Test
	public void givesCheckTest() {
		Move move = Move.createMove(board, 4, 6, 4, 4);
		
		assertFalse(move.givesCheck());
		move.setGivesCheck(true);
		
		assertTrue(move.givesCheck());
	}
	
	@Test
	public void firstMoveTest() {
		Move move = Move.createMove(board, 4, 6, 4, 4);
		
		assertTrue(move.isFirstMoveForPiece());
		move = Move.createMove(board, 4, 4, 4, 3);
		
		assertFalse(move.isFirstMoveForPiece());
	}
	
	@Test
	public void erasedEnPassantTest() {
		Move move = Move.createMove(board, 4, 6, 4, 4);
		
		assertNull(move.getErasedEnPassant());
		board.makeMove(move);
		assertNull(move.getErasedEnPassant());
		move = Move.createMove(board, 4, 1, 4, 3);
		
		assertNull(move.getErasedEnPassant());
		board.makeMove(move);
		
		assertNotNull(move.getErasedEnPassant());
		assertEquals(4, move.getErasedEnPassant().getX());
		assertEquals(5, move.getErasedEnPassant().getY());
	}
}
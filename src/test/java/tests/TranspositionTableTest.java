package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import tiralabrashakki.Board;
import tiralabrashakki.ChessGame;
import tiralabrashakki.Move;

public class TranspositionTableTest {
	
	public TranspositionTableTest() {
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
	public void hashingTest() {
		Board board = new Board();
		long hash = ChessGame.TT.generateHash(board);
		
		assertEquals(hash, ChessGame.TT.generateHash(board));
		
		Move move = Move.createMove(board, 4, 6, 4, 4);
		board.makeMove(move);
		
		long hash2 = ChessGame.TT.generateHash(board);
		
		assertNotEquals(hash, hash2);
		
		assertEquals(hash2, ChessGame.TT.generateHash(board));
		
		board.unmakeMove(move);
		
		assertEquals(hash, ChessGame.TT.generateHash(board));
		
		board.makeMove(move);
		assertEquals(hash2, ChessGame.TT.generateHash(board));
	}
	
	@Test
	public void updateHashTest() {
		Board board = new Board();
		
		board.makeMove(Move.createMove(board, 4, 6, 4, 4));
		board.makeMove(Move.createMove(board, 4, 1, 4, 3));
		board.makeMove(Move.createMove(board, 3, 6, 3, 4));
		
		assertEquals(ChessGame.TT.generateHash(board), board.getHash());
	}
}

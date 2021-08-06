package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import tiralabrashakki.Board;
import tiralabrashakki.Move;
import tiralabrashakki.ai.TranspositionTable;

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
		long hash = TranspositionTable.generateHash(board);
		
		assertEquals(hash, TranspositionTable.generateHash(board));
		
		Move move = Move.createMove(board, 4, 6, 4, 4);
		board.makeMove(move);
		
		long hash2 = TranspositionTable.generateHash(board);
		
		assertNotEquals(hash, hash2);
		
		assertEquals(hash2, TranspositionTable.generateHash(board));
		
		board.unmakeMove(move);
		
		assertEquals(hash, TranspositionTable.generateHash(board));
		
		board.makeMove(move);
		assertEquals(hash2, TranspositionTable.generateHash(board));
	}
}

package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import tiralabrashakki.Board;
import tiralabrashakki.Move;

public class BoardTest {
	
	
	@Test
	public void pieceHasMovedTest() {
		Board board = new Board();
		assertEquals(board.getPieceHasMoved(4, 6), 0);
		Move move = Move.createMove(board, 4, 6, 4, 4);
		board.makeMove(move);
		assertEquals(board.getPieceHasMoved(4, 6), 1);
		board.unmakeMove(move);
		assertEquals(board.getPieceHasMoved(4, 6), 0);
		
		
		
		board.makeMove(move);
		assertEquals(board.getPieceHasMoved(4, 6), 1);
		assertEquals(board.getPieceHasMoved(4, 5), 2);
		
		
		assertEquals(board.getPieceHasMoved(4, 1), 0);
		Move move2 = Move.createMove(board, 4, 1, 4, 3);
		board.makeMove(move2);
		assertEquals(board.getPieceHasMoved(4, 1), 1);
		assertEquals(board.getPieceHasMoved(4, 5), 1);
		
		board.unmakeMove(move2);
		assertEquals(board.getPieceHasMoved(4, 5), 2);
		
		
		assertEquals(board.getPieceHasMoved(4, 2), 1);
		board.makeMove(move2);
		assertEquals(board.getPieceHasMoved(4, 2), 2);
		
		move = Move.createMove(board, 0, 6, 0, 5);
		board.makeMove(move);
		
		assertNotNull(move.getErasedEnPassant());
		
		assertEquals(board.getPieceHasMoved(4, 2), 1);
		board.unmakeMove(move);
		assertEquals(board.getPieceHasMoved(4, 2), 2);
	}
}

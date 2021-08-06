package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import tiralabrashakki.PlayerColor;

public class PlayerColorTest {
	
	public PlayerColorTest() {
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
	public void colorTest() {
		PlayerColor color = PlayerColor.WHITE;
		
		assertTrue(color.isWhite());
		assertFalse(!color.isWhite());
	}
	
	@Test
	public void isMyPieceTest() {
		PlayerColor color = PlayerColor.WHITE;
		
		assertTrue(color.isMyPiece('P'));
		assertFalse(color.isMyPiece('p'));
		
		color = PlayerColor.BLACK;
		
		assertTrue(color.isMyPiece('p'));
		assertFalse(color.isMyPiece('P'));
	}
	
	@Test
	public void isEnemyPieceTest() {
		PlayerColor color = PlayerColor.WHITE;
		
		assertTrue(color.isEnemyPiece('p'));
		assertFalse(color.isEnemyPiece('P'));
		
		color = PlayerColor.BLACK;
		
		assertTrue(color.isEnemyPiece('P'));
		assertFalse(color.isEnemyPiece('p'));
	}
	
	@Test
	public void oppositeTest() {
		PlayerColor color = PlayerColor.WHITE;
		
		assertEquals(color.opposite(), PlayerColor.BLACK);
		
		color = PlayerColor.BLACK;
		assertEquals(color.opposite(), PlayerColor.WHITE);
	}
}

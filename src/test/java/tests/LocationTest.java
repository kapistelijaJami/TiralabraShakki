package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import tiralabrashakki.Location;

public class LocationTest {
	Location loc;
	
	public LocationTest() {
	}
	
	@BeforeAll
	public static void setUpClass() {
	}
	
	@AfterAll
	public static void tearDownClass() {
	}
	
	@BeforeEach
	public void setUp() {
		loc = new Location(0, 0);
	}
	
	@AfterEach
	public void tearDown() {
	}
	
	@Test
	public void LocationTest() {
		assertEquals(loc.getX(), 0);
		assertEquals(loc.getY(), 0);
		loc.setX(5);
		assertEquals(loc.getX(), 5);
		loc.setY(6);
		assertEquals(loc.getY(), 6);
		
		loc.set(new Location(4, 9));
		assertEquals(loc.getX(), 4);
		assertEquals(loc.getY(), 9);
	}
}

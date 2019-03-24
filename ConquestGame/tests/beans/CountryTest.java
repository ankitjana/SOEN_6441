package beans;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CountryTest {
	private Player player, gamer1, gamer2;
	
	private Country vn;
	private Country indi;
	private Country usa;
	private Country can;
	private Country mex;
	private Country eng;
	private Country ger;
	private Country france;
	private Country rus;
	private Country china;
	private Country congo;
	private Country ugan;
	
	private Continent america;
	private Continent europe;
	private Continent asia;
	private Continent africa;

	@Before
	public void setUp() throws Exception {
		player = new Player("PlayerOne");
		gamer1 = new Player("gamer1");
		gamer2 = new Player("gamer2");
		
		vn = new Country("Vietnam");
		indi = new Country("India");
		usa = new Country("USA");
		can = new Country("Canada");
		mex = new Country("Mexico");
		eng = new Country("England");
		ger = new Country("Germany");
		france = new Country("France");
		rus	= new Country("Russia");
		china = new Country("China");
		congo = new Country("Congo");
		ugan = new Country("Uganda");
		
		america = new Continent("Ameria", 3);
		europe = new Continent("Europe", 2);
		asia = new Continent("Asia", 3);
		africa = new Continent("Africa", 1);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		eng.setOwner(player);
		
		List<String> adjacentCountries = new ArrayList<String>();
		adjacentCountries.add(indi.getName());
		adjacentCountries.add(china.getName());
		
		assertEquals(eng.getNumArmies(), 0);
		assertEquals(eng.getOwner(), player);
		eng.setAdjacentCountries(adjacentCountries);
		
		assertSame(adjacentCountries, eng.getAdjacentCountries());
		assertNotNull(eng.getlongitude());
		assertNotNull(eng.getLatitude());
	
	}
	
	@Test
	public void testPlaceInitialArmies() {
		
	}

}

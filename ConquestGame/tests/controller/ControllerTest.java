/**
 * 
 */
package controller;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import beans.Continent;
import beans.Country;
import beans.Player;
import gui.UI;

/**
 * @author vanduong
 *
 */
public class ControllerTest {

	GameController controller = null;
//	ReEnforcement phase = null;
	UI ui = null;
	Country vn = new Country("Vietnam");
	Country indi = new Country("India");
	Country usa = new Country("USA");
	Country can = new Country("Canada");
	Country mex = new Country("Mexico");
	Country eng = new Country("England");
	Country ger = new Country("Germany");
	Country france = new Country("France");
	Country rus	= new Country("Russia");
	Country china = new Country("China");
	Country congo = new Country("Congo");
	Country ugan = new Country("Uganda");
	
	Continent america = new Continent("Ameria", 3);
	Continent europe = new Continent("Europe", 2);
	Continent asia = new Continent("Asia", 3);
	Continent africa = new Continent("Africa", 1);
	
	Player gamer1 = null;
	Player gamer2 = null;
	Player gamer3 = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		controller = GameController.getInstance();
		ui = new UI();
		gamer1 = new Player("gamer1");
		controller.addPlayer(gamer1);
		gamer2 = new Player("gamer2");
		controller.addPlayer(gamer2);
		gamer3 = new Player("gamer3");
		controller.addPlayer(gamer3);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRandomizeCountryDistribution() {
		List<Player> players = new ArrayList<Player>();
		players.add(gamer1);
		players.add(gamer2);
		players.add(gamer3);
		
		List<Country> countries = new ArrayList<Country>();
		countries.add(eng);
		countries.add(vn);
		countries.add(indi);
		countries.add(can);
		countries.add(usa);
		countries.add(mex);
		countries.add(ger);
		countries.add(france);
		countries.add(rus);
		countries.add(china);
		countries.add(congo);
		countries.add(ugan);
		
		controller.randomizeCountryDistribution(countries, players);
		
		assertNotNull(gamer1.getPlayerCountries());
		assertNotNull(gamer2.getPlayerCountries());
		assertNotNull(gamer3.getPlayerCountries());
		
		assertEquals(4, gamer1.getPlayerCountries().size());
		assertEquals(4, gamer2.getPlayerCountries().size());
		assertEquals(4, gamer3.getPlayerCountries().size());

	}
	
	@Test
	public void testPlaceInitialNumArmies() throws NoSuchMethodException, SecurityException {
		gamer1.setArmies(10);
		gamer2.setArmies(10);
		gamer3.setArmies(10);
		
		gamer1.addCountry(vn.getName(), vn);
		gamer1.addCountry(ger.getName(), ger);
		gamer2.addCountry(indi.getName(), indi);
		gamer3.addCountry(usa.getName(), usa);
		
		Class reflectController = controller.getClass();
		Method method;
		
		method = reflectController.getDeclaredMethod("placeInitialArmies");
		method.setAccessible(true);
		
		try {
			method.invoke(controller);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			fail();

		}
		assertEquals(2, gamer1.getNumArmiesDispatched());
		
	}
	
	@Test
	public void testMain() {
		
	}
	
//	@Test
//	public void testTakePhases() {
//		controller.addPlayer(gamer1);
//		controller.setCurrentPlayer(gamer1);
//		controller.takePhases();
//	}
//	
//	@Test
//	public void testTakeTurns() {
//		controller.addPlayer(gamer1);
//		controller.setCurrentPlayer(gamer1);
//		controller.takeTurns();
//	}

}

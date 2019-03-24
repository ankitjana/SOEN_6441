/**
 * 
 */
package phases;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import beans.Continent;
import beans.Country;
import beans.Player;
import controller.GameController;
import exception.MapInvalidException;

/**
 * @author vanduong
 *
 */
public class ReEnforcementTest {
	GameController controller = null;
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

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		//add one player to system
		controller = GameController.getInstance();
		gamer1 = new Player("gamer1");
		controller.addPlayer(gamer1);
		controller.setCurrentPlayer(gamer1);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		gamer1 = null;
		controller = null;
	}

	@Test
	public void testObtainArmies() throws MapInvalidException {
		//add 1 continent to gamer1
//		controller.setCurrentPlayer(gamer1);
		gamer1.addContinent(africa.getName(), africa);
		String [] countryNames = {vn.getName(), indi.getName(), usa.getName(), can.getName()};
		Country [] countries = {vn, indi, usa, can};
		//add 4 countries to gamer1
		gamer1.addCountries(countryNames, countries );
		//create phase
//		phase = new ReEnforcement();
		Player curPlayer = controller.getCurrentPlayer();
		curPlayer.obtainNewArmies();
		assertEquals(4, gamer1.getArmies());
		String [] moreCountryNames = {mex.getName(), eng.getName(), ger.getName(), rus.getName()};
		Country [] moreCountries = {mex, eng, ger, rus};
		gamer1.addCountries(moreCountryNames, moreCountries);
		assertEquals(8, gamer1.getPlayerCountries().size());
		curPlayer.obtainNewArmies();
		assertEquals(8, gamer1.getArmies());
		String [] names = {france.getName(), china.getName(), ugan.getName(), congo.getName()};
		Country [] Additionalcountries = {france, china, ugan, congo};
		gamer1.addCountries(names, Additionalcountries);
		assertEquals(12, gamer1.getPlayerCountries().size());
		curPlayer.obtainNewArmies();
		assertEquals(13, gamer1.getArmies());
		
	}
	
	@Test
	public void testDistributeArmies() throws MapInvalidException {
		Map<Country, Integer> list = new HashMap<Country, Integer>();
		String [] countryNames = {vn.getName(), indi.getName(), usa.getName(), can.getName()};
		Country [] countries = {vn, indi, usa, can};
		//add countries to player
		controller.setCurrentPlayer(gamer1);
		gamer1.addCountries(countryNames, countries);
		assertEquals(gamer1, vn.getOwner());
		//start with some armies
		assertEquals(4, gamer1.getPlayerCountries().size());
		gamer1.setArmies(9);
		Player curPlayer = controller.getCurrentPlayer();
		curPlayer.obtainNewArmies();
		assertEquals(12, gamer1.getArmies());
		for(int i = 0; i < countries.length; i++) {
			list.put(countries[i], 4);
		}
		//distribute armies to countries
		curPlayer.distributeArmies(list);
		assertEquals(4, (int)list.get(vn));
		assertEquals(4, (int)list.get(indi));
		assertEquals(4, (int)list.get(usa));
		assertEquals(4, (int)list.get(can));
	}

}

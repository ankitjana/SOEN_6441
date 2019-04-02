/**
 * 
 */
package strategies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import beans.Continent;
import beans.Country;
import beans.Player;
import controller.GameController;
import utilities.CustomMapGenerator;
import utilities.DiceRoller;

// TODO: Auto-generated Javadoc
/**
 * Represent a game play strategy, different strategies have different
 * implementation reEnforcement, Attack and Fortification .
 *
 * @author vanduong
 */
public abstract class Strategy {
	/** The minimum new armies each user gets in ReEnforcement phase. */
	static final int MIN_NEW_ARMIES = 3;

	/** The controller. */
	static GameController controller = null;

	/** The map. */
	static CustomMapGenerator map = null;

	/** The player. */
	private Player player = null;
	private Human humanPlayer;
	private AggressiveStrategy aggressivePlayer;
	transient Scanner scan= new Scanner(System.in);

	/**
	 * Instantiates a new strategy.
	 *
	 * @param player the player
	 */
	public Strategy(Player player) {
		this.player = player;
		controller = GameController.getInstance();
		map = CustomMapGenerator.getInstance();
	}

	/**
	 * Gets the player.
	 *
	 * @return the player
	 */
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * Distribute armies.
	 *
	 * @param list the list of countries with corresponding armies.
	 * @VisibleForTesting
	 */
	public void distributeArmies(Map<Country, Integer> list) {
		for (Map.Entry<Country, Integer> entry : list.entrySet()) {
			Country country = entry.getKey();
			int numArmies = entry.getValue();
			int totalArmiesToSet = numArmies + country.getNumArmies();
			player.getCountryByName(country.getName()).setNumArmies(totalArmiesToSet);
			player.setNumArmiesDispatched(player.getNumArmiesDispatched() + numArmies);
		}
	}

	/**
	 * attacks in all-out mode.
	 *
	 * @param attackingCountry the attacking country
	 * @param attackedCountry  the attacked country
	 */
	public void goAllOut(Country attackingCountry, Country attackedCountry) {
		// keep attacking as long as there's enough armies to attack and haven't
		// occupied defender's country
		while (attackingCountry.getNumArmies() > 1 && attackedCountry.getOwner() != this.player) {
			// get max number of dices possible for attacker
			int numDiceAttacker = (attackingCountry.getNumArmies() > 3) ? 3 : attackingCountry.getNumArmies() - 1;
			int numDiceDefender = (attackedCountry.getNumArmies() >= 2) ? 2 : attackedCountry.getNumArmies();
			int[] attackerDice = rollDiceAttacker(attackingCountry, numDiceAttacker);
			int[] defenderDice = rollDiceDefender(attackedCountry, numDiceDefender);
			// battle
			int[] result = goToBattle(attackerDice, defenderDice);
			invade(result, attackingCountry, attackedCountry, numDiceAttacker);
		}

	}

	/**
	 * validate user choice of number of dices and.. get dice result for the
	 * attacker's country.
	 *
	 * @param attackingCountry the attacking country
	 * @param numDice          the num dice
	 * @return the result
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public int[] rollDiceAttacker(Country attackingCountry, int numDice) throws IllegalArgumentException {
		if (numDice > 3 || numDice < 0) {
			throw new IllegalArgumentException("Please enter 1, 2, or 3 only");
		}
		// must have at least one more army than number of dices
		if (attackingCountry.getNumArmies() - numDice < 1) {
			throw new IllegalArgumentException("must have at least one more army than number of dices!");
		}
		DiceRoller dicer = DiceRoller.getInstance();

		return dicer.roll(numDice);
	}

	/**
	 * validates user's choice of number of dices and.. get dice result for
	 * defender's country
	 *
	 * @param defendingCountry the defending country
	 * @param numDice          the num dice
	 * @return the result
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public int[] rollDiceDefender(Country defendingCountry, int numDice) throws IllegalArgumentException {
		if (numDice > 2 || numDice < 0) {
			throw new IllegalArgumentException("Please enter 1 or 2 only");
		}
		// must have at least 2 army to roll 2 dices
		if (defendingCountry.getNumArmies() < 2 && numDice >= 2) {
			throw new IllegalArgumentException("must have at least 2 army to roll 2 dices!");
		}

		DiceRoller dicer = DiceRoller.getInstance();

		return dicer.roll(numDice);
	}

	/**
	 * compares 2 results of dices to decide who wins the battle returns the result
	 * in an array of 2 elements: [0] contains number of attacker's lost armies, [1]
	 * contains that of defender.
	 *
	 * @param attackerDice the attacker dice
	 * @param defenderDice the defender dice
	 * @return the number indicating the winner
	 */
	public int[] goToBattle(int[] attackerDice, int[] defenderDice) {
		// stores result of the battle [0] contains number of attacker's lost armies,
		// [1] contains that of defender
		int[] result = new int[2];

		// stores the smaller number of dices between the 2 sets of dices
		int numDice = Math.min(attackerDice.length, defenderDice.length);
		for (int i = 0; i < numDice; i++) {
			// compare the highest (and second highest) dice of both
			if (attackerDice[i] > defenderDice[i]) {
				// defender loses 1 army
				result[1] += 1;

			} else {
				// attacker loses 1 army
				result[0] += 1;
			}
		}
		return result;
	}

	/**
	 * Deduct armies on both sides based on dice results and attacker occupies
	 * defender's country if possible.
	 *
	 * @param result                the dice result
	 * @param attackingCountry      the attacking country
	 * @param attackedCountry       the attacked country
	 * @param attackerSelectNumDice the attacker select num dice
	 */
	public void invade(int[] result, Country attackingCountry, Country attackedCountry, int attackerSelectNumDice) {
		Player defender = attackedCountry.getOwner();
		player.setIsCountryInvaded(false);
		if (result[0] > 0) {
			controller.showDialog(player.getPlayerName() + " lost " + result[0] + " army");
			attackingCountry.setNumArmies(attackingCountry.getNumArmies() - result[0]);
			player.setArmies(player.getArmies() - result[0]);
		}
		if (result[1] > 0) {
			controller.showDialog(defender.getPlayerName() + " lost " + result[1] + " army");
			attackedCountry.setNumArmies(attackedCountry.getNumArmies() - result[1]);
			defender.setArmies(defender.getArmies() - result[1]);
		}

		// check if attacker can occupy defender's territory (attackedCountry)
		if (attackedCountry.getNumArmies() == 0) {
			// flag the counter if player invades a country
			player.setIsCountryInvaded(true);
			player.addCountry(attackedCountry.getName(), attackedCountry);
			defender.removeCountry(attackedCountry.getName());
			attackedCountry.setNumArmies(attackedCountry.getNumArmies() + attackerSelectNumDice);
			attackingCountry.setNumArmies(attackingCountry.getNumArmies() - attackerSelectNumDice);
			controller.showDialog(player.getPlayerName() + " has conquered " + attackedCountry.getName());

			// check if attacker has conquered a whole continent
			Continent continent = map.getContinent(attackedCountry.getContinent());
			if (player.hasConqueredContinent(continent)) {
				player.addContinent(continent.getName(), continent);
				controller.showDialog(player.getPlayerName() + " has conquered " + continent.getName());

			}

			// check if defender just lost a continent
			if (defender.hasLostContinent(continent)) {
				defender.removeContinent(continent.getName());
				controller.showDialog(defender.getPlayerName() + " has lost full control of " + continent.getName());
				if (defender.getPlayerCountries().size() == 0) {
					player.getCardsAcquired().addAll(defender.getCardsAcquired());
				}

			}
		}

	}

	/**
	 * Obtain new armies.
	 *
	 * @return total new armies current player is granted to be added to existing
	 *         armies.
	 */
	public int obtainNewArmies() {

		// redeem armies by cards
		int tradeNumber = player.getTradeCount();
		int armiesByCards = tradeNumber * 5;

		// obtain armies by number of territories occupied
		int numCountries = player.getPlayerCountries().size();
		int numArmies = numCountries / 3;
		int armiesByCountries = ((numArmies > MIN_NEW_ARMIES)) ? numArmies : MIN_NEW_ARMIES;

		// obtain armies by number of continents controlled
		List<Continent> continents = player.getPlayerContinents();
		int armiesByContinents = 0;
		for (Continent c : continents) {
			armiesByContinents += c.getMaxArmies();
		}

		int totalNewArmies = armiesByCountries + armiesByContinents + armiesByCards;
		player.increaseArmies(totalNewArmies);
		return totalNewArmies;
	}

	/**
	 * Move armies.
	 *
	 * @param fromCountry the country from which armies are transfered
	 * @param toCountry   the country to which armies are transfered
	 * @param numArmies   number of armies transfered
	 * @exception IllegalArgumentException There must be at least one army in one
	 *                                     country
	 */
	public void moveArmies(String fromCountry, String toCountry, int numArmies) throws IllegalArgumentException {
		Country from = player.getCountryByName(fromCountry);
		Country to = player.getCountryByName(toCountry);
		if (from == null || to == null) {
			throw new IllegalArgumentException("The selected country is not occupied by you!");
		}
		List<String> adjFrom = from.getAdjacentCountries();
		List<String> adjTo = to.getAdjacentCountries();
		// check if 2 countries are adjacent
		if (adjFrom == null || adjTo == null || !adjFrom.contains(to.getName()) || !adjTo.contains(from.getName())) {
			throw new IllegalArgumentException("Two countries must be adjacent");
		}
		// check if number of armies move is valid
		if (from.getNumArmies() - numArmies < 1) {
			throw new IllegalArgumentException("there must be at least 1 army per country");
		}
		if (numArmies < 0) {
			throw new IllegalArgumentException("Number of armies must be non-negative!");
		}
		from.setNumArmies(from.getNumArmies() - numArmies);
		to.setNumArmies(to.getNumArmies() + numArmies);

	}

	/**
	 * Get the weakest or strongest country
	 * 
	 * @param getType         :Determines the type of country in terms of armies;
	 *                        greater the army stronger the territory
	 * @param countryToRemove :Removes country if the strongest country is not
	 *                        strong enough for aggressive mode.
	 * @return :County instance.
	 */
	public Country compareCountries(String getType, Country countryToRemove) {
		List<Country> countryList = player.getPlayerCountries();
		if (countryToRemove == null) {
			countryList.remove(countryToRemove);
		}
		Country toReturn = countryList.get(0);
		for (int i = 0; i < countryList.size(); i++) {
			for (int j = i + 1; j < countryList.size(); j++) {
				if (getType.equalsIgnoreCase("strongest")) {
					if (countryList.get(i).getNumArmies() > countryList.get(j).getNumArmies()
							&& countryList.get(i).getNumArmies() > toReturn.getNumArmies()
							&& getdefendingNeighbours(countryList.get(i)).size() > 0) {
						toReturn = countryList.get(i);
					} else if (countryList.get(j).getNumArmies() > countryList.get(i).getNumArmies()
							&& countryList.get(j).getNumArmies() > toReturn.getNumArmies()
							&& getdefendingNeighbours(countryList.get(j)).size() > 0) {
						toReturn = countryList.get(j);
					}
				} else if (getType.equalsIgnoreCase("weakest")) {
					if (countryList.get(i).getNumArmies() < countryList.get(j).getNumArmies()
							&& countryList.get(i).getNumArmies() < toReturn.getNumArmies()
							&& getdefendingNeighbours(countryList.get(i)).size() > 0) {
						toReturn = countryList.get(i);
					} else if (countryList.get(j).getNumArmies() < countryList.get(i).getNumArmies()
							&& countryList.get(j).getNumArmies() < toReturn.getNumArmies()
							&& getdefendingNeighbours(countryList.get(j)).size() > 0) {
						toReturn = countryList.get(j);
					}
				}
			}
		}
		return toReturn;
	}
	
	/**
	 * Responsible for returning
	 * @return
	 */
	public Country compareCountries() {
		List<Country> countryList = player.getPlayerCountries();
		Country toReturn = countryList.get(0);
		int countryISize=0, countryJSize=0,toReturnSize=0;
		for (int i = 0; i < countryList.size(); i++) {
			countryISize =getdefendingNeighbours(countryList.get(i)).size();
			toReturnSize =getdefendingNeighbours(toReturn).size();
			for (int j = i + 1; j < countryList.size(); j++) {
				countryJSize =getdefendingNeighbours(countryList.get(j)).size();
				if(countryJSize<countryISize && countryISize<toReturnSize ) {
					toReturn = countryList.get(i);
				}else if(countryJSize>toReturnSize){
					toReturn = countryList.get(j);
				}
			}
		}
		return toReturn;
	}

	/**
	 * sets the map to be used for distributing armies
	 * 
	 * @param countryToSet
	 * @return
	 */
	public Map<Country, Integer> generateArmyCountyMap(Country countryToSet) {
		Map<Country, Integer> armyCountryMap = new HashMap<Country, Integer>();
		for (Country con : player.getPlayerCountries()) {
			armyCountryMap.put(con, con.getNumArmies());
		}
		armyCountryMap.put(countryToSet, countryToSet.getNumArmies());
		return armyCountryMap;

	}

	/**
	 * Returns list of adjacent countries that can be attacked.
	 * 
	 * @param attacking :attacking country
	 * @return adjacent countries that does not belong to current player.
	 */
	public List<Country> getdefendingNeighbours(Country attacking) {
		List<Country> defendingNeighbours = map.getInstance().getAdjacentCountry(attacking);
		defendingNeighbours.removeAll(player.getPlayerCountries());
		return defendingNeighbours;
	}

	public boolean isStronger(Country attacking, Country defending) {
		boolean isTrue = false;
		if (attacking.getNumArmies() > defending.getNumArmies()) {
			isTrue = true;
		}
		return isTrue;
	}

	/**
	 * Re- enforce player's armies. Each strategy has a different reEnforcement
	 * behavior
	 */
	public abstract void reEnforce();

	/**
	 * Attack another player. Each strategy has a different attack behavior
	 */
	public abstract void attack();

	/**
	 * Fortify player armies.
	 */
	public abstract void fortify();

	/**
	 * player places their armies on their territories in setup phase. Each strategy
	 * has a different way of placing armies behavior for setup
	 */
	public abstract void placeArmiesForSetup();
}

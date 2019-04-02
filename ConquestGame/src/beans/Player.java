package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import controller.GameController;
import gui.CardExchangeView;
import gui.Observer;
import strategies.AggressiveStrategy;
import strategies.Human;
import strategies.Strategy;
import utilities.CustomMapGenerator;

// TODO: Auto-generated Javadoc
/**
 * <p> This class stores the players details.<p>
 *
 */
public class Player implements Observable, Serializable{
	

	/** The observer list. */
	Map<Integer, Observer> observerList;
	
	/** The invade count. */
	int invadeCount =0;
	
	/** The trade count. */
	int tradeCount =0;
	
	/** The card type. */
	String cardType;
	
	/** The card to remove. */
	 List<String> cardToRemove;
	
	public void setCardToRemove(String name) {
		cardToRemove.add(name);
		//System.out.println("Inside the setCarToRemove" + name);
	}

	/** The scan. */
	transient Scanner scan= new Scanner(System.in);
	
	/** flag to check if any country invaded.  */
	boolean isCountryInvaded;
	

	/** The player name. */
	private final String playerName;
	
	/** The is human. */
	private final boolean isHuman;
	
	/** The armies. */
	private int armies;
	
	/** The occupied countries. */
	private HashMap<String, Country> occupiedCountries;
	
	/** The occupied continents. */
	private HashMap<String, Continent> occupiedContinents;
	
	/** The cards. */
	private CardType cards;
	
	/** The random. */
	private Random random;
	
	/** The cards acquired. */
	private List<String> cardsAcquired= new ArrayList<String>();
	
	
	/** The observer list. */
	private List<Observer> obList = null;
	
	/** The num armies dispatched (number of armies that has been deployed to countries*/
	private int numArmiesDispatched = 0;
	
	/** The controller. */
	private static GameController controller = GameController.getInstance();
	
	/** The map. */
	private static CustomMapGenerator map = CustomMapGenerator.getInstance();

	/** The card view. */
	private static CardExchangeView cardView= new CardExchangeView();
	
	private Strategy strategy= null;
	
	
	/**
	 * returns player's strategy
	 * @return the strategy
	 */
	public Strategy getStrategy() {
		return strategy;
	}


	/**
	 * Sets player's strategy
	 * @param strategy the strategy to set
	 */
	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}



	/**
	 * Gets the num armies dispatched.
	 *
	 * @return the number of armies has been dispatched to occupied countries
	 */
	public int getNumArmiesDispatched() {
		return numArmiesDispatched;
	}



	/**
	 * set number of armies dispatched to occupied countries.
	 *
	 * @param numArmiesDispatched the numArmiesDispatched to set
	 */
	public void setNumArmiesDispatched(int numArmiesDispatched) {
		this.numArmiesDispatched = numArmiesDispatched;
	}



	/**
	 * Gets the cards.
	 *
	 * @return The CardType object.
	 */
	public List<String> getCardsAcquired(){
		return cardsAcquired;
	}
	
	/**
	 * Gets the trade count.
	 *
	 * @return the trade count
	 */
	public int getTradeCount() {
		return tradeCount;
	}
	
	/**
	 * Gets the cards.
	 *
	 * @return the cards
	 */
	public CardType getCards() {
		return cards;
	}



	/**
	 * Sets the cards.
	 *
	 * @param cards The CardType object
	 */
	public void setCards(CardType cards) {
		this.cards = cards;
	}



	/**
	 * Instantiates a new player.
	 *
	 * @param playerName This is the player's name.
	 * @param isHuman    true if the player is human, otherwise false.
	 * @param armies     The number of armies player holds.
	 */
	public Player(String playerName, boolean isHuman, int armies) {
		
		super();
		this.playerName = playerName;
		this.isHuman = isHuman;
		this.armies = armies;
		this.occupiedContinents = new HashMap<String, Continent>();
		this.occupiedCountries = new HashMap<String, Country>();
		observerList = new HashMap<Integer, Observer>();
		cardToRemove= new ArrayList<String>();
	}
		
	/**
	 * Instantiates a new player.
	 *
	 * @param name the name
	 */
	public Player(String name) {
		this(name, true, 0);
	}
	
	
	
	/**
	 * Gets the player name.
	 *
	 * @return The name of the player.
	 */
	public String getPlayerName() {
		return playerName;
	}
	
	
	
	/**
	 * Checks if is human.
	 *
	 * @return true if the player is human, otherwise returns false.
	 */
	public boolean isHuman() {
		return isHuman;
	}
	
	
	/**
	 * Gets the armies.
	 *
	 * @return Number of armies of the player.
	 */
	public int getArmies() {
		return armies;
	}
	
	
	/**
	 * Sets the armies.
	 *
	 * @param armies The assign the player with the armies.
	 */
	public void setArmies(int armies) {
		this.armies = armies;
	}
	
	
	/**
	 * Increase armies.
	 *
	 * @param armies The additional armies that need to be added to the current
	 *               armies.
	 */
	public void increaseArmies(int armies) {
		
		this.armies = this.armies + armies;
	}
	
	
	/**
	 * Decrease armies.
	 *
	 * @param armies The number of army members that need to be decreased from the
	 *               current armies.
	 */
	public void decreaseArmies(int armies) {
		
		if(this.armies >= armies)
			this.armies = this.armies - armies;
	}
	
	/**
	 * Adds the cards.
	 *
	 * @return the list
	 */
	public List<String> addCards() {
		random= new Random();
		List<String> cardType = Arrays.asList("INFANTRY", "CAVALRY", "ARTILLERY");
		int RandomCard = random.nextInt(cardType.size());
		cardsAcquired.add(cardType.get(RandomCard));
		System.out.println("CONGRATULATIONS !! A Random card is added to your Card Inventory");
		return cardsAcquired;
	}
	
	
	/**
	 * Adds the continent.
	 *
	 * @param continent_name The name of the continent that needs to be added.
	 * @param continent      The continent object for the corresponding continent
	 *                       name.
	 */
	public void addContinent(String continent_name, Continent continent) {
		
		this.occupiedContinents.put(continent_name, continent);
		continent.setOwner(this);
	}
	
	
	/**
	 * Adds the country.
	 *
	 * @param country_name The name of the country that needs to be added.
	 * @param country      The country object for the corresponding country name.
	 */
	public void addCountry(String country_name, Country country) {
		
		this.occupiedCountries.put(country_name, country);
		country.setOwner(this);
	}
		
	
	/**
	 * Adds the countries.
	 *
	 * @param countries_name The names of the countries that needs to be added.
	 * @param countries      The country objects for the respective country.
	 */
	public void addCountries(String[] countries_name, Country[] countries) {
		
		for(int i = 0; i < countries.length ; i++) {
			this.occupiedCountries.put(countries_name[i],
					countries[i]);
			countries[i].setOwner(this);
		}
			
	}
	
	
	/**
	 * Adds the continents.
	 *
	 * @param continents_name The names of the continents that need to be added
	 * @param continents      The continent objects for the respective continent.
	 */
	public void addContinents(String[] continents_name, Continent[] continents) {
		
		for(int i = 0; i <continents.length; i++) {
			this.occupiedContinents.put(continents_name[i],
					continents[i]);
			continents[i].setOwner(this);
		}
			
	}
	
	
	/**
	 * Removes the country.
	 *
	 * @param country Name of the country that need to be removed from the players
	 *                list of countries.
	 */
	public void removeCountry(String country) {
		
		this.occupiedCountries.remove(country);
	}
	
	
	
	/**
	 * Removes the continent.
	 *
	 * @param continent Name of the continent that need to be removed from the
	 *                  players list of continents.
	 */
	public void removeContinent(String continent) {
		
		this.occupiedContinents.remove(continent);
	}
	
	
	
	/**
	 * Gets the player countries.
	 *
	 * @return The list of countries that the player occupied.
	 */
	public List<Country> getPlayerCountries() {
		
		List<Country> countries = new ArrayList<Country>(
				this.occupiedCountries.values());
		
		return countries;
	}
	
	/**
	 * Gets the country by name.
	 *
	 * @param name the name
	 * @return the country by name
	 */
	public Country getCountryByName(String name) {
		return occupiedCountries.get(name);
	}
	
	/**
	 * Gets the player continents.
	 *
	 * @return The list of continents that the player occupied.
	 */
	public List<Continent> getPlayerContinents() {
		List<Continent> continents = new ArrayList<Continent>(
				this.occupiedContinents.values());
		
		return continents;
	}



	/* (non-Javadoc)
	 * @see beans.Observable#attach(gui.Observer)
	 */
	@Override
	public void attach(Observer ob, int event) {
		observerList.put(event, ob);
		
	}



	/* (non-Javadoc)
	 * @see beans.Observable#detach(gui.Observer)
	 */
	@Override
	public void detach(Observer ob) {
		obList.remove(ob);
		
	}



	/* (non-Javadoc)
	 * @see beans.Observable#notifyChanges(java.util.List)
	 */
	@Override
	public void notifyChanges(int event) {
//		for(Observer o : obList) {
//			o.update(this);
//		}
		observerList.get(event).update(this);
	}
	
	
	/**
	 * Attack phase.
	 *
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public void attack() throws IllegalArgumentException{
		this.strategy.attack();
	}

	

	/**
	 * @return flag to check if any country invaded or not
	 */
	public boolean isAnyCountryInvaded() {
		return isCountryInvaded;
	}
	
	/**
	 * Sets  isCountryInvaded
	 *
	 * @param bool the new checks if is country invaded
	 */
	public void setIsCountryInvaded(boolean bool) {
		this.isCountryInvaded = bool;
	}

	/**
	 * checks if a player has lost control of a continent.
	 *
	 * @param continent The continent to be checked
	 * @return true if lost control
	 */
	public boolean hasLostContinent(Continent continent) {
		String continentName = continent.getName();
		if(occupiedContinents.containsKey(continentName)) {
			return true;
		}
		return false;
	}



	/**
	 * Checks if the given continent is conquered by player.
	 *
	 * @param continent The continent to check
	 * @return true, if successful
	 */
	public boolean hasConqueredContinent(Continent continent) {
		List<Country> countryList = continent.getCountries();
		
		for(Country con : countryList) {
			String countryName = con.getName();
			if(!occupiedCountries.containsKey(countryName)) {
				return false;
			}
		}
		return true;
	}
	
	

	/**
	 * Exchange cards.
	 *
	 * @return the int
	 */
	public int exchangeCards() {
		setCardsToRemove();
		if(cardView.isExchangeCardsPossible()== true) {
			removeCards(cardToRemove);
			tradeCount++;
			System.out.println("You would get additional " + " " + (tradeCount*5) + " " + "armies for this card trade during re-enforcement phase");
		}
		else {
			System.out.println("SORRY !! You don't have a CARDSET to exchange for additional armies");
		}
		return tradeCount;
	}

	/**
	 * Removes the cards.
	 *
	 * @param cardsRemove the cards remove
	 */
	public void removeCards(List<String> cardsRemove) {
		getCardsAcquired().removeAll(cardsRemove);
	
	}
	
	/**
	 * @return list of cards to remove 
	 */
	public List<String> getCardsToRemove() {
		return cardToRemove;
	}
	
	public void setCardsToRemove(){
		
		System.out.println("You have the following cards :"+ getCardsAcquired());
		System.out.println("Please select three cards you want to trade off :");
		System.out.println("(Names should be either INFANTRY or CAVALRY or ARTILLERY)");
		for(int i=0;i<3;i++) {
			cardType= scan.next();
			cardToRemove.add(cardType);
		}
	}

	/**
	 * ReEnforcement phase
	 * This methods calls 2 other private methods to 1) obtain new armies and 2)
	 * distribute armies among occupied countries.
	 */
	public void reEnforce() {
		this.strategy.reEnforce();
		
	}
	
	
	/**
	 * Fortify player's countries in fortification phase.
	 *
	 * @throws IllegalArgumentException if 2 countries given are not adjacent or if one of the countries is not owned by player
	 */
	public void fortify() throws IllegalArgumentException {
		this.strategy.fortify();
	}
	
}

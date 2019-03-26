package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import beans.Continent;
import beans.Country;
import beans.EventType;
import beans.Player;
import exception.MapInvalidException;
import gui.CardExchangeView;
import gui.Observer;
import gui.PhaseView;
import gui.UI;
//import phases.Attack;
import gui.WorldDominationView;
import utilities.CustomMapGenerator;
import utilities.MapValidator;

// TODO: Auto-generated Javadoc
/**
 * The Class GameController.
 */
/*
 * @description :
 * 
 * @author
 */
public class GameController {

	/** The Constant INPUTFILE. */
	private final static String INPUTFILE = "src/resources/World.map";
	/** The controller. */
	private static GameController controller = null;

	/** The current phase Info. */
	private String currentPhase;

	/** The World Domination View. */
	private WorldDominationView wdView = null;


	private PhaseView phaseView = null;
	Scanner scan = new Scanner(System.in);
	private static CardExchangeView cardView= new CardExchangeView();

	/** The country list. */
	private static List<Country> countryList = new ArrayList<Country>();

	/** The number of players. */
//	HashMap<Player,WorldMap> countryOwnership = new HashMap<Player,WorldMap>();
	private int numberOfPlayers;
	
	/** The continent list by name. */
	private HashMap<String, Continent> continentListByName = null;

	/** The country ownership. */
	Map<Player, ArrayList<Country>> countryOwnership = null;

//	/** The current phase. */
//	TurnPhase currentPhase = null;

	/** The ready for next phase. */
	private boolean readyForNextPhase = false;

	/** The current player. */
	private Player currentPlayer;

	/** The player list. */
	private ArrayList<Player> playerList;

	/** The Constant MIN_ARGS. */
	private final static int MIN_ARGS = 1;

	/** The winner. */
	private Player winner = null;

	/** The ui. */
	private UI ui = null;


	private CustomMapGenerator customMap=null;
	
	/** The continent list. */
	private List<Continent> continentList = null;
	
	
	

	/**
	 * Gets the current phase.
	 *
	 * @return String, the current phase of the game.
	 */
	public String getCurrentPhase() {
		return currentPhase;
	}

	/**
	 * Sets the current phase.
	 *
	 * @param currentPhase The current phase name
	 */
	public void setCurrentPhase(String currentPhase) {
		this.currentPhase = currentPhase;
	}

	/**
	 * Instantiates a new game controller.
	 */
	private GameController() {
		countryOwnership = new HashMap();
		playerList = new ArrayList<Player>();

	}

	/**
	 * Gets the single instance of GameController.
	 *
	 * @return single instance of GameController
	 */
	public static GameController getInstance() {
		if (controller == null) {
			synchronized (GameController.class) {
				if (controller == null) {
					controller = new GameController();
				}
			}
		}
		return controller;
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException         Signals that an I/O exception has occurred.
	 * @throws MapInvalidException the map invalid exception
	 */
	/*
	 * @description :
	 * 
	 * @author
	 */
	public static void main(String[] args) throws IOException, MapInvalidException {
		GameController controller = GameController.getInstance();
		controller.createUI();
		controller.loadMap();
		controller.createWorldDominationView();
	//	controller.createPhaseView();
		controller.initGame();
	}

	/**
	 * Register given observer with all player.
	 *
	 * @param ob the ob
	 * @param event the event
	 */
	public void registerObserver(Observer ob, int event) {

		for (int i = 0; i < numberOfPlayers; i++) {
			controller.playerList.get(i).attach(ob, event);
		}
	}

	/**
	 * place one army on each and every country occupied by players.
	 */
	private void placeInitialArmies() {
		for (int i = 0; i < controller.playerList.size(); i++) {
			Player player = controller.getPlayer(i);
			for (int j = 0; j < player.getPlayerCountries().size(); j++) {
				Country c = player.getPlayerCountries().get(j);
				c.setNumArmies(1);
				c.setOwner(player);
				player.setNumArmiesDispatched(j + 1);
			}
		}

	}

	
	/**
	 * each player takes turn to place their armies on their territories.
	 */

	private void placeArmiesForSetup() {
		// each player take turns to place their armies
		for (int i = 0; i < controller.playerList.size(); i++) {
			Player player = playerList.get(i);
			player.notifyChanges(EventType.PHASE_NOTIFY);
			ui.showDialog("Please assign armies to countries for " + player.getPlayerName());
			int numArmiesToDispatch = player.getArmies() - player.getNumArmiesDispatched();
			Map<Country, Integer> selection = ui.distributeArmies(player.getPlayerCountries(), numArmiesToDispatch);
			player.distributeArmies(selection);
		}
	}

	/**
	 * create and register a UI interface as observer to player1.
	 */
	private void createUI() {
		ui = new UI();
	}

	/**
	 * create and register a WorldDomination interface as observer to player.
	 */
	public void createWorldDominationView() {
		wdView = WorldDominationView.getInstance();
	}

	/**
	 * create and register a PhaseView interface as observer to player.
	 *
	 * @return the player list
	 */
//	public void createPhaseView() {
//		PhaseView phaseView = new PhaseView();
//	}

	/**
	 * Gets the player list.
	 *
	 * @return the list of players
	 */
	public List<Player> getPlayerList() {
		return this.playerList;
	}

	/**
	 * Show help.
	 */
	public void showHelp() {
		// TODO
	}

	/**
	 * Adds the player.
	 *
	 * @param player the player
	 */
	public void addPlayer(Player player) {
		playerList.add(player);
		numberOfPlayers++;
	}

	/**
	 * Gets the num players.
	 *
	 * @return the num players
	 */
	public int getNumPlayers() {
		return numberOfPlayers;
	}

	/**
	 * Gets the player.
	 *
	 * @param idx the idx
	 * @return the player
	 */
	public Player getPlayer(int idx) {
		return playerList.get(idx);
	}

	/**
	 * Gets the winner.
	 *
	 * @return the winner
	 */
	public Player getWinner() {
		return winner;
	}

	/**
	 * Load map.
	 *
	 * @return true, if successful
	 */
	/*
	 * @description :
	 * 
	 * @author
	 */
	public void loadMap() {
		customMap = CustomMapGenerator.getInstance();
		System.out.println("----------Welcome----------");
		System.out.println(
				"Please select the following options.\n1)Load exisiting map\n2)Create map\n3)Edit existing map");
		Scanner mapOption = new Scanner(System.in);
		int selectedMapOption = mapOption.nextInt();
		if (selectedMapOption == 1) {
			try {
				customMap.LoadMap();
			} catch (IOException | MapInvalidException e) {
				ui.handleExceptions(e.getMessage());
				System.exit(1);
			}
		} else if (selectedMapOption == 2) {

			try {
				customMap.createCustomMap();
			} catch (IOException | MapInvalidException e) {
				ui.handleExceptions(e.getMessage());
				System.exit(1);
			}
		} else if (selectedMapOption == 3) {
			try {
				customMap.editExistingMap();
			} catch (IOException | MapInvalidException e) {
				ui.handleExceptions(e.getMessage());
				System.exit(1);
			}
		} else {
			System.out.println("Enter appropiate choice");
			System.exit(1);
		}
		countryList.addAll(customMap.countryDefault);
	}


//	/**
//	 * Sets the phase.
//	 *
//	 * @param turnPhase the new phase
//	 */
//	/*
//	 * @description :
//	 * @author
//	 */
//	public void setPhase(TurnPhase turnPhase) {
//		currentPhase = turnPhase;
//	}


	/**
 * Take turns.
 *
 * @throws MapInvalidException the map invalid exception
 */
	public void takeTurns() throws MapInvalidException {
		int i = 0;
		while (winner == null) {
			i = i % playerList.size();
			currentPlayer = playerList.get(i);
			System.out.println("==============" + currentPlayer.getPlayerName() + "'S TURN==================");
			System.out.println("Initial Number of Armies: " + currentPlayer.getArmies());
			takePhases();
			i++;
		}
	}

	/**
	 * Take phases.
	 * 
	 * @throws MapInvalidException
	 */
	public void takePhases() throws MapInvalidException {
//		currentPhase = new ReEnforcement();
		cardView.getCardProgress();
		if (currentPlayer.getCardsAcquired().size() >= 3) {
			System.out.println("Do you want to exchange your cards for army reinforcement ? Y/N");
			char exchangeChoice = scan.next().charAt(0);
			if (exchangeChoice == 'Y') {
				currentPlayer.exchangeCards();
			}
			if (exchangeChoice == 'N') {
				if (currentPlayer.getCardsAcquired().size() >= 5) {
					System.out.println("Since you have 5 or more cards, you have to exchange one set of your cards");
					currentPlayer.exchangeCards();
				}
			}
		}
		currentPlayer.reEnforce();
		currentPlayer.notifyChanges(EventType.PHASE_NOTIFY);
		while (true) {
			boolean	isAnyCountryInvaded =false;
			try {
				// ask user if wants to make an attack and check if user is able to attack (at
				// least 2 armies in one country)
				if (isWar() && canAttack()) {
					do {
						currentPlayer.attack();
						// check if current player has won the game
						if (currentPlayer.getPlayerCountries().size() == MapValidator.countriesList.size()) {
							winner = currentPlayer;
							ui.showDialog(currentPlayer.getPlayerName() + " HAS CONQUER THE WORLD!!");
							ui.showDialog("THE END!!!");
							System.exit(0);
						}
						currentPlayer.notifyChanges(EventType.PHASE_NOTIFY);
					} while (canAttack() && keepWar());
				isAnyCountryInvaded = currentPlayer.isAnyCountryInvaded();
				}
				if (isAnyCountryInvaded == true) {
					currentPlayer.addCards();
				}
				currentPlayer.fortify();
				currentPlayer.notifyChanges(EventType.PHASE_NOTIFY);
				break;
			} catch (IllegalArgumentException e) {
				ui.handleExceptions(e.getMessage());
			}
		}
	}

	/**
	 * checks if player can attack by having at least 2 armies in one of player's countries and such country is adjacent to another player's countries.
	 *
	 * @return true, player can attack
	 */
	private boolean canAttack() {
		List<Country> curPlayerCountries = currentPlayer.getPlayerCountries();

		for(Country country : curPlayerCountries) {
			if(country.getNumArmies() >= 2) {
				//check if this country is adjacent to at least a country occupied by another player
				List<String> adjCountries = country.getAdjacentCountries();
				for(String countryName : adjCountries) {
					Country con = this.getCountryByCountryName(countryName);
					if(!con.getOwner().getPlayerName().equals(currentPlayer.getPlayerName())){
						return true;
					}
				}
			}
		}
		ui.showDialog(currentPlayer.getPlayerName() + " does not qualify to start any attack");
		return false;
	}

	/**
	 * This method calls appropriate UI to ask user if he/she wants to continue
	 * killing.
	 *
	 * @return true, if user wants to
	 */
	private boolean keepWar() {
		return UI.keepWar();
	}

	/**
	 * Request GUI to ask if user wants to go to war.
	 *
	 * @return true if user wants to go to war
	 */
	private boolean isWar() {
		return UI.isWar();

	}

	/**
	 * Gets the current player.
	 *
	 * @author Van
	 * @return current player
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * Sets the current player.
	 *
	 * @param player the new current player
	 */
	public void setCurrentPlayer(Player player) {
		currentPlayer = player;
	}

	/**
	 * @throws MapInvalidException Inits the game. @throws
	 */
	public void initGame() throws MapInvalidException {

		//Getting Player Info
				
				System.out.println("Please enter the number of players between 2 and 6: ");
				Scanner inputNumPlayers = new Scanner(System.in);	
				int numberOfPlayers = inputNumPlayers.nextInt();
				//if user inputs number of players less than 2 or greater than 6 then exit the game
				if (!(numberOfPlayers >= 2 && numberOfPlayers <= 6)) {
					System.exit(0);
				}
				int initialArmies = 0;
				switch(numberOfPlayers) {
					case 2:
						initialArmies = 40;
					break;
					case 3:
						initialArmies = 35;
					break;
					case 4:
						initialArmies = 30;
					break;
					case 5:
						initialArmies = 25;
					break;
					case 6:
						initialArmies = 20;
					break;	
				}
				for(int i = 1; i <=numberOfPlayers ; i++) {
					String playerName = "Player " + i;
					Player player = new Player(playerName, true, initialArmies);
					controller.addPlayer(player);
				}
				
				controller.registerObserver(ui, EventType.PHASE_NOTIFY);
				controller.registerObserver(wdView, EventType.FORTIFICATION_NOTIFY);
//				controller.registerObserver(phaseView, EventType.PHASE_VIEW_NOTIFY);
				
				
				System.out.println("evenly distributing countries among players in random fashion...");
				controller.randomizeCountryDistribution(countryList, controller.getPlayerList());
				System.out.println("-------- Setup --------");
				controller.placeInitialArmies();
				controller.placeArmiesForSetup();
				controller.takeTurns();
	}

	/**
	 * evenly distributes countries among players in a random fashion.
	 *
	 * @author vanduong
	 * @param countries the countries
	 * @param players   the players
	 */
	public void randomizeCountryDistribution(List<Country> countries, List<Player> players) {
		Random rand = new Random();
		int playerIdx = 0;

		// players take turn to add a country to their occupied_list until the
		// unoccupied country list is empty
		while (countries.size() > 0) {
			// if playerIdx >= playerList size, reset playerIdx
			playerIdx = playerIdx % players.size();
			Player player = players.get(playerIdx);
			int randIdx = rand.nextInt(countries.size());
			Country country = countries.get(randIdx);
			player.addCountry(country.getName(), country);
			playerIdx++;
			countries.remove(randIdx);
		}

	}

	/**
	 * Asks GUI to ask user to input number of armies to be distributed to each
	 * occupied countries.
	 *
	 * @return the map
	 */
	public Map<Country, Integer> distributeArmies() {
		int numArmiesToDispatch = currentPlayer.getArmies() - currentPlayer.getNumArmiesDispatched();
		return ui.distributeArmies(currentPlayer.getPlayerCountries(), numArmiesToDispatch);
	}

//	/**
//	 * Ask GUI to ask if user is ready for next phase.
//	 *
//	 * @return true, if successful
//	 */
//	public boolean readyForNextPhase() {
//		return UI.readyForNextPhase();
//		
//	}

	/**
	 * Gets the params for fortification.
	 *
	 * @param countryFrom the country from
	 * @return a set of 3 objects: country to move armies from, country to move
	 *         armies to, and number of armies
	 */
	public int getParamsForFortification(Country countryFrom) {
		return ui.getFortificationInfo(countryFrom);

	}

	/**
	 * Select country to transfer from.
	 *
	 * @param playerCountries the player countries
	 * @return the string
	 */
	public String selectCountryToTransferFrom(List<Country> playerCountries) {
		return ui.selectCountryToTransferFrom(playerCountries);
	}

	/**
	 * Select country to transfer to.
	 *
	 * @param adjCountries the adj countries
	 * @return the string
	 */
	public String selectCountryToTransferTo(List<String> adjCountries) {
		return ui.selectCountryToTransferTo(adjCountries);
	}

	/**
	 * Call UI to get the attacked country from player.
	 *
	 * @return the attacked country
	 */
	public Country getAttackedCountry() {
		UI.showAdjCountriesAndOwner(currentPlayer);
		String countryName = UI.getAttackedCountryByName();
		return this.getCountryByCountryName(countryName);
	}

	/**
	 * Gets the owner by country name. Null if the country has no owner
	 *
	 * @param name the name
	 * @return the owner by country name
	 */
	public Player getOwnerByCountryName(String name) {
		Player player = null;
		for (Player gamer : playerList) {
			if (gamer.getCountryByName(name) != null) {
				player = gamer;
				break;
			}
		}
		return (player != null) ? player : null;
	}

	/**
	 * Gets the country by country name.
	 *
	 * @param countryName the country name
	 * @return the country by country name
	 */
	public Country getCountryByCountryName(String countryName) {
		Country country = null;
		for (Player gamer : playerList) {
			if (gamer.getCountryByName(countryName) != null) {
				country = gamer.getCountryByName(countryName);
				break;
			}
		}
		return (country != null) ? country : null;
	}

	/**
	 * Calls GUI selectAttackingCountry() method and return the selected country
	 * name This method gets called inside attack phase only when there are two or
	 * more options for attacking country.
	 *
	 * @param attackingCountries the attacking countries
	 * @return the string
	 */
	public String selectAttackingCountry(List<String> attackingCountries) {
		return UI.selectAttackingCountry(attackingCountries);
	}

	/**
	 * Ask UI to get the number dice from attacker.
	 *
	 * @return the number dice
	 */
	public int getNumDiceAttacker() {
		return ui.getNumDiceAttacker();
	}

	/**
	 * Gets the num dice defender.
	 *
	 * @return the num dice defender
	 */
	public int getNumDiceDefender() {

		return ui.getNumDiceDefender();
	}

	/**
	 * Shows dialog to user.
	 *
	 * @param message the message
	 */
	public void showDialog(String message) {
		ui.showDialog(message);
	}

	/**
	 * tells UI to ask user to choose if is all out mode.
	 *
	 * @return true, if is all out mode
	 */
	public boolean isAllOutMode() {
		return ui.isAllOutMode();
	}


	/**
	 * @param continent
	 * @return
	 */
	public Continent getContinentByName(String continent) {
		// TODO Auto-generated method stub
		return null;
	}

}
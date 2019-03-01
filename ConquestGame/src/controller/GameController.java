package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import beans.*;
import exception.MapInvalidException;
import gui.GUI;
import phases.Attack;
import phases.ReEnforcement;
import phases.TurnPhase;
import utilities.DiceRoller;
import utilities.MapValidator;
import utilities.Tuple;

// TODO: Auto-generated Javadoc
/**
 * The Class GameController.
 */
/*
 * @description :
 * @author
 */
public class GameController {
	
	
	/** The controller. */
	private static GameController controller= null;

/** The number of players. */
//	HashMap<Player,WorldMap> countryOwnership = new HashMap<Player,WorldMap>();
	private int numberOfPlayers;



	/** The country ownership. */
	Map<Player, ArrayList<Country>> countryOwnership = null;
	
	/** The current phase. */
	TurnPhase currentPhase = null;
	
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
	
	/**
	 * Instantiates a new game controller.
	 */
	private GameController(){
		countryOwnership = new HashMap();
		playerList = new ArrayList<Player>();
	}
	

	/**
	 * Gets the single instance of GameController.
	 *
	 * @return single instance of GameController
	 */
	public static GameController getInstance(){
		    if(controller == null){
		        synchronized (GameController.class) {
		            if(controller == null){
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
	 * @throws MapInvalidException 
	 */
	/*
	 * @description :
	 * @author
	 */
	public static void main(String[] args) throws IOException, MapInvalidException {
		/*File inFile = null;
		if (0 < args.length) {
		   inFile = new File(args[0]);
		} else {
		   System.err.println("Invalid arguments count:" + args.length);
		   System.exit(1);
		} */
		
		/*Added to parse the default file.
		 * To provide input file use src/resources/World.map as argument
		 * 
		 */
		try {
			if (args == null || args.length < MIN_ARGS) {
			    showHelp();
			    return;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String inputFile = args[0];
		new utilities.MapValidator(inputFile).createCountryGraph();
		GameController controller = GameController.getInstance();
	    //TODO  add create map
		
		
		
		
		//Getting Player Info
		System.out.println("Please enter the number of players: ");
		Scanner inputNumPlayers = new Scanner(System.in);	
		
		for(int i = 1; i <= inputNumPlayers.nextInt(); i++) {
			String playerName = "Player " + i;
			//TODO
			//Need to change third parameter in player
			//after map is implemented 
			controller.addPlayer(new Player(playerName, true, 3));
		}
		
	}
	
	
	
	/**
	 * Show help.
	 */
	public static void showHelp() {
		//TODO
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
	 * @author
	 */
	public static boolean loadMap(){
		//TODO this method should belong to GUI
		return false;
		
	}
	
	/**
	 * Sets the phase.
	 *
	 * @param turnPhase the new phase
	 */
	/*
	 * @description :
	 * @author
	 */
	public void setPhase(TurnPhase turnPhase) {
		currentPhase = turnPhase;
	}
	
	/**
	 * Take turns.
	 */
	public void takeTurns() {
		int i = 0;
		while (winner == null) {
			i = i % playerList.size();
			currentPlayer = playerList.get(i);
			takePhases();
			// check if current player has won the game
			if(countryOwnership.size() == MapValidator.countriesList.size()) {
				winner = currentPlayer;
			}
			i++;
		}
		
	}
    
	/**
	 * Take phases.
	 */
	public void takePhases() {
		currentPhase = new ReEnforcement();
		while(currentPhase != null) {
			while(!readyForNextPhase) {
				try {
					//ask user if wants to init an attack
					if(currentPhase instanceof Attack) {
						if(!isWar()) {
							break;
						}
					}
					currentPhase.takePhase();
					//ask user if ready for next phase
					readyForNextPhase = readyForNextPhase();
				}catch(IllegalArgumentException e) {
					GUI.handleExceptions(e.getMessage());
				}
			}
			currentPhase.setNextPhase();
		}
	}

	/**
	 * Request GUI to ask if user wants to go to war.
	 *
	 * @return true if user wants to go to war
	 */
	private boolean isWar() {
		return GUI.isWar();
		
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
	 * Inits the game.
	 */
	public void initGame() {
		//TODO	get number of players in from user
		//TODO	assign each player an initial number of armies (based on risk rule)
		//each player take turn to play
	}
	
	
	/**
	 * evenly distributes countries among players in a random fashion .
	 *
	 * @author vanduong
	 * @param countries the countries
	 * @param players   the players
	 */
	public void randomizeCountryDistribution(List<Country> countries, List<Player> players) {
	    Random rand = new Random();
	    int numCountriesPerPick = 1;
	    int playerIdx = 0;
	    Map<String, ArrayList<String>> list = new HashMap();
	    //players take turn to add a country to their occupied_list until the unoccupied country list is empty
	    while(countries.size() >= 0) {
	    	//if playerIdx >= playerList size, reset playerIdx
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
		 
		return GUI.distributeArmies(currentPlayer.getPlayerCountries(), currentPlayer.getArmies());
	}


	/**
	 * Ask GUI to ask if user is ready for next phase.
	 *
	 * @return true, if successful
	 */
	public boolean readyForNextPhase() {
		return GUI.readyForNextPhase();
		
	}


	/**
	 * Gets the params for fortification.
	 *
	 * @return a set of 3 objects: country to move armies from, country to move
	 *         armies to, and number of armies
	 */
	public Tuple getParamsForFortification() {
		return GUI.getFortificationInfo();
		
	}
}

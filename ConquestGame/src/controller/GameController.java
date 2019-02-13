package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import beans.*;
import phases.TurnPhase;

/*
 * @description :
 * @author
 */
public class GameController {
	
//	HashMap<Player,WorldMap> countryOwnership = new HashMap<Player,WorldMap>();
	int numberOfPlayers;
	Map<Player, ArrayList<Country>> countryOwnership = null;
	
	GameController(){
		countryOwnership = new HashMap();
	}
	/*
	 * @description :
	 * @author
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		GameController controller = new GameController();
	    //TODO  
		
	}
	
	/*
	 * @description :
	 * @author
	 */
	public boolean loadMap(){
		//TODO
		return false;
		
	}
	
	/*
	 * @description :
	 * @author
	 */
	public boolean setPhase(TurnPhase turnPhase) {
		return false;
	}
    
	/*
	 * @description :
	 * @author
	 */
	public boolean nextPhase() {
		return false;
	}

	/**
	 * @return current player
	 * @author Van
	 */
	public Player getCurrentPlayer() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void initGame() {
		// TODO 
	}
	
	
	/**
	 * evenly distributes countries among players in a random fashion 
	 * @author vanduong
	 * @param countries
	 * @param players
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
}

/**
 * 
 */
package gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import beans.Continent;
import beans.Country;
import beans.Observable;
import beans.Player;
import controller.GameController;
import utilities.Tuple;

// TODO: Auto-generated Javadoc
/**
 * The user interface of the application. 
 * @author vanduong
 *
 */
public class UI implements Observer{
	
	/** The controller. */
	private static GameController controller = GameController.getInstance();

	/**
	 * Gets the fortification info.
	 *
	 * @param country the country
	 * @return a tuple containing: country to move armies from, country to move
	 *         armies to, and number of armies
	 */
	public int getFortificationInfo(Country country) {
		Scanner scan = new Scanner(System.in);
		int numArmies = 0;
		
		while(true) {
			System.out.print("Please enter number of armies being transferred: ");
			int input2 = scan.nextInt();
			System.out.println();
			//make sure there is at least one army left in a country
			if(input2 <= country.getNumArmies() -1) {
				numArmies = input2;
				break;
			} else {
				this.handleExceptions("There must be at least one army per country!");
			}
		}
		
		return numArmies;
	}


	/**
	 * Show dialog.
	 *
	 * @param message the message
	 */
	public void showDialog(String message) {
		System.out.println("**INFO**: " + message);
	}
	/**
 * display error messages.
 *
 * @param message the message
 */
	public  void handleExceptions(String message) {
		System.out.println("ERROR: "+ message);
	}

	/* (non-Javadoc)
	 * @see gui.Observer#update(beans.Observable)
	 */
	@Override
	public void update(Observable sub) {
		Player player = (Player)sub;
		this.showDialog(player.getPlayerName()+"'s Information:");
		// display number of countries occupied
		System.out.println("Player's total number of armies: " + player.getArmies());
		//System.out.println("Player's total number of dispatched armies: " + player.getNumArmiesDispatched());
		List<Country> countries = player.getPlayerCountries();
		System.out.println("Player's total number of occupied countries: " + countries.size());
		//1System.out.println("Player's occupied countries: " + countries.toString());
		System.out.println("Number of armies in each occupied country: ");
		for(int i = 0; i < countries.size(); i++) {
			Country c = countries.get(i);
			System.out.println(c.getName() + ": " + c.getNumArmies());
		}
		//display number of continents occupied
		System.out.print("Player's occupied continents: ");
		List<Continent> continents = player.getPlayerContinents();
		if(continents == null || continents.size() == 0) {
			System.out.println(0);
		} else {
			System.out.println();
			for(int i = 0; i < continents.size(); i++) {
				System.out.println(continents.get(i).getName());
			}
		}
		
	}

	/**
	 * Ask if user wants to go to war.
	 *
	 * @return true if user wants to go to war
	 */
	public static boolean isWar() {
		System.out.println("Would you like to go to war with another player?(Y/N):");
		Scanner scan = new Scanner(System.in);
		String input = scan.nextLine();
		input = input.toLowerCase();
		switch(input) {
			case "y":
				return true;
			case "n":
				return false;
		}
		return false;
	}

	/**
	 * Ask user to select number of armies to be distributed for each country.
	 *
	 * @param list      List of countries owned by current player
	 * @param numArmies the num armies
	 * @return list of countries and their assigned armies
	 */
	public Map<Country, Integer> distributeArmies(List<Country> list, int numArmies) {
		Map<Country, Integer> armyInfo = new HashMap<Country, Integer>();
		Scanner userInput = new Scanner(System.in);
		int temp = 0;
		//while(numArmies > 0)
//			for(Country c : list) {
//				System.out.println(c.getName());
//			}
			for(Country c : list) {
				while(true) {
					System.out.println("Remaining number of armies to be deployed: " + numArmies);
					System.out.print("PLease assign army for " + c.getName() + ": ");
					temp = userInput.nextInt();
					System.out.println();
					//each country must have at least one army
					if(temp >= 0 && temp <= numArmies) {
						numArmies = numArmies - temp;
						break;
					}
					this.handleExceptions("Invalid number!");
				}
				//c.setNumArmies(temp);
				armyInfo.put(c, temp);
				if(numArmies == 0)
				{
					break;
				}
			}
		
		return armyInfo;
	}

	/**
	 * Select country to transfer from.
	 *
	 * @param playerCountries the player countries
	 * @return the string
	 */
	public String selectCountryToTransferFrom(List<Country> playerCountries) {
		Scanner scan = new Scanner(System.in);
		String fromCountry = null;
		 
		System.out.print("Please enter a country name to move armies From:");
		fromCountry = scan.nextLine();
		System.out.println();
			
		
		return fromCountry;
	}

	/**
	 * Select country to transfer to.
	 *
	 * @param adjCountries the adj countries
	 * @return the string
	 */
	public String selectCountryToTransferTo(List<String> adjCountries) {
		System.out.println("Below is a list of your countries adjacent to the selected country:" );
		for(int i = 0; i < adjCountries.size(); i++) {
			System.out.println(adjCountries.get(i));
		}
		System.out.println("Please enter a country name shown above to transfer to:");
		Scanner scan = new Scanner(System.in);
		return scan.nextLine();
	}

	/**
	 * Show countries and their owners that are adjacent to current player.
	 *
	 * @param currentPlayer the current player
	 */
	public static void showAdjCountriesAndOwner(Player currentPlayer) {
		System.out.println("Below is a list of all countries that are adjacent to "+ currentPlayer.getPlayerName() + "'s countries:");
		System.out.println("**NOTE**: Only attacker's countries having at least 2 armies are shown here");
		List<Country> occupiedCountries = currentPlayer.getPlayerCountries();
		for(Country country : occupiedCountries) {
			//only show attacking countries with at least 2 armies
			if(country.getNumArmies() < 2) {
				continue;
			}
			System.out.println("Your country: " + country.getName() + " -- "+ "Armies: " +country.getNumArmies());
			List<String> adjCountries = country.getAdjacentCountries();
			System.out.println("   Adjacent countries:");
			for(String name : adjCountries) {
				String ownerName = controller.getOwnerByCountryName(name).getPlayerName();
				if(ownerName != null) {
					//only show other player's countries
					if(!ownerName.equals(currentPlayer.getPlayerName())) {
						System.out.println("      "+ name + " -- Owner: "+ ownerName);
					}
				} else {
					System.out.println("      "+ name + " -- Owner: "+ "No owner");
				}
			}
		
		}
		
	}

	/**
	 * Gets the attacked country by name.
	 *
	 * @return the attacked country by name
	 */
	public static String getAttackedCountryByName() {
		System.out.print("Please enter a country you would like to attack: ");
		Scanner scan = new Scanner(System.in);
		String countryName = scan.nextLine();
		return countryName;
	}

	/**
	 * Asks user to select a country where an attack is initiated from.
	 *
	 * @param attackingCountries the attacking countries
	 * @return the country name
	 */
	public static String selectAttackingCountry(List<String> attackingCountries) {
		System.out.println("Below is attacker's countries that are adjacent to the selected attacked country:");
		for(String name : attackingCountries) {
			System.out.println("   "+ name);
		}
		System.out.print("Please select a country where attacker wants to initiate an attack from:");
		Scanner scan = new Scanner(System.in);
		String attackingCountry = scan.nextLine();
		return attackingCountry;
	}

	/**
	 * Ask if user wants to continue attacking.
	 *
	 * @return true, if yes, else false
	 */
	public static boolean keepWar() {
		System.out.println("Would you like to continue killing?(Y/N):");
		Scanner scan = new Scanner(System.in);
		String input = scan.nextLine();
		input = input.toLowerCase();
		switch(input) {
			case "y":
				return true;
			case "n":
				return false;
		}
		return false;
	}


	/**
	 * Ask player to enter the number of dice for attacker.
	 *
	 * @return the number dice attacker
	 */
	public int getNumDiceAttacker() {
		System.out.print("Please enter number of dices for attacker (1/2/3): ");
		Scanner scan = new Scanner(System.in);
		int num = scan.nextInt();
		scan.nextLine();
		return num;
	}


	/**
	 * Gets the number of dice defender.
	 *
	 * @return the number dice defender
	 */
	public int getNumDiceDefender() {
		System.out.print("Please enter number of dices for defender (1 or 2): ");
		Scanner scan = new Scanner(System.in);
		int num = scan.nextInt();
		scan.nextLine();
		return num;
	}


	/**
	 * asks user to choose if is all out mode.
	 *
	 * @return true, if is all out mode
	 */
	public boolean isAllOutMode() {
		System.out.println("Would you like to go ALL-OUT like a samurai?(y/n)");
		Scanner scan = new Scanner(System.in);
		String input = scan.next();
		scan.nextLine();
		input = input.toLowerCase();
		switch(input) {
			case "y":
				return true;
			case "n":
				return false;
		}
		return false;
	}
	

}

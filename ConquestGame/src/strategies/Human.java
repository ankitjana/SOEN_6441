/**
 * 
 */
package strategies;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import beans.Continent;
import beans.Country;
import beans.EventType;
import beans.Player;
import gui.PhaseView;
import gui.UI;
import utilities.DiceRoller;

// TODO: Auto-generated Javadoc
/**
 * Represents human strategy, user inputs are required for all phases.
 *
 * @author vanduong
 */
public class Human extends Strategy implements Serializable {
	
	/** The player. */
	private Player player = null;

	/**
	 * Instantiates a new human.
	 *
	 * @param player the player
	 */
	public Human(Player player) {
		super(player);
		this.player=player;
	}

	
	/* (non-Javadoc)
	 * @see strategies.Strategy#reEnforce()
	 */
	public void reEnforce() {
		int newArmies = obtainNewArmies();
		player.notifyChanges(EventType.PHASE_NOTIFY);
		Map<Country, Integer> list = controller.distributeArmies(newArmies);
		this.distributeArmies(list);
		
	}
	
	
	
	

	
	/* (non-Javadoc)
	 * @see strategies.Strategy#attack()
	 */
	public void attack() {
		controller.setCurrentPhase("Attack");
		PhaseView phaseView = new PhaseView();
		controller.registerObserver(phaseView, EventType.PHASE_VIEW_NOTIFY);
		player.notifyChanges(EventType.PHASE_VIEW_NOTIFY);
		
		// get attacked country from user, controller
		Country attackedCountry = controller.getAttackedCountry();
		if(attackedCountry == null) {
			throw new IllegalArgumentException("The attacked country is not occupied by any player!");
		}
		//list of attacker's country name adj to selected attacked country
		List<String> attackingCountries = new ArrayList<String>();
		String attackedCountryName = attackedCountry.getName();
		
		List<Country> occupiedCountries = player.getPlayerCountries();
		//it's possible that there many attacker's countries adj the selected attacked country
		for(Country con : occupiedCountries) {
			//only consider attaker's countries with at least 2 armies
			if(con.getNumArmies() >= 2) {
				List<String> adjCountries = con.getAdjacentCountries();
				for(String name : adjCountries) {
					if(name.equals(attackedCountryName) ) {
						attackingCountries.add(con.getName());
					}
				}
			}
			
		}
		// check if the attacked country is adjacent to attacker's territories
		if(attackingCountries.isEmpty()) {
			throw new IllegalArgumentException("The attacked country must be adjacent to any of attacker's countries!");
		}
		
		//if there are 2 or more attacking country options, ask user to select one
		String attackingCountryName = null;
		if(attackingCountries.size() > 1) {
			attackingCountryName = controller.selectAttackingCountry(attackingCountries);
		}else {
			attackingCountryName = attackingCountries.get(0);
		}
		
		Country attackingCountry = map.getCountry(attackingCountryName);
		//check if the attacking country has at least 2 armies
		if(attackingCountry.getNumArmies() < 2) {
			throw new IllegalArgumentException("The attacking country must have at least 2 armies!");
		}
		
		//request user to choose all-out mode
		if(controller.isAllOutMode()) {
			this.goAllOut(attackingCountry, attackedCountry);
		}else {
			//roll dice
			int[] attackerDice = null;
			int[] defenderDice = null;
			int attackerSelectNumDice = 0;
			int defenderSelectNumDice = 0;
			
			//get number of dice from attacker
			while(true) {
				try {
					attackerSelectNumDice = controller.getNumDiceAttacker();
					attackerDice = rollDiceAttacker(attackingCountry, attackerSelectNumDice);
					break;
				}catch(IllegalArgumentException e) {
					throw e;
				}
			}
			
			//get number of dice from defender
			while(true) {
				try {
					defenderSelectNumDice = controller.getNumDiceDefender();
					defenderDice = rollDiceDefender(attackedCountry,defenderSelectNumDice);
					break;
				}catch(IllegalArgumentException e){
					throw e;
				}
			}
			
			//battle
			int[] result = goToBattle(attackerDice, defenderDice);
			invade(result, attackingCountry, attackedCountry, attackerSelectNumDice );
		}

	}
	
	
	
	/* (non-Javadoc)
	 * @see strategies.Strategy#fortify()
	 */
	public void fortify() {
		controller.setCurrentPhase("Fortification");
		PhaseView phaseView = new PhaseView();
		controller.registerObserver(phaseView, EventType.PHASE_VIEW_NOTIFY);
		player.notifyChanges(EventType.PHASE_VIEW_NOTIFY);
		//move armies from one (and only one) country to another neighboring country
		String fromName = controller.selectCountryToTransferFrom(player.getPlayerCountries());
		Country fromCountry = player.getCountryByName(fromName);
		if(fromCountry == null) {
			throw new IllegalArgumentException(fromName + " is not a country you occupied!");
		}
		List<String> adjCountries = fromCountry.getAdjacentCountries();
		//get the names of countries occupied by this player among adjacent countries
		List<String> occupiedCountries = new ArrayList<String>();
		for(int i = 0; i < adjCountries.size(); i++) {
			Country country = player.getCountryByName(adjCountries.get(i));
			if(country != null) {
				occupiedCountries.add(adjCountries.get(i));
			}
		}
		if( occupiedCountries.size() == 0) {
			throw new IllegalArgumentException("There is no adjacent countries occupied by you!");
		}
		String toName = controller.selectCountryToTransferTo(occupiedCountries);
		int numArmies = controller.getParamsForFortification(fromCountry);
		Country toCountry = player.getCountryByName(toName);
//				fromCountry.setNumArmies(fromCountry.getNumArmies() - numArmies);
//				toCountry.setNumArmies(toCountry.getNumArmies() + numArmies);
		this.moveArmies(fromName, toName, numArmies);
		player.notifyChanges(EventType.FORTIFICATION_NOTIFY);

	}
	
	
	
	/**
	 * player places their armies on their territories in setup phase.
	 */

	public void placeArmiesForSetup() {
		player.notifyChanges(EventType.PHASE_NOTIFY);
		controller.showDialog("Please assign armies to countries for " + player.getPlayerName());
		int numArmiesToDispatch = player.getArmies() - player.getNumArmiesDispatched();
		Map<Country, Integer> selection = controller.distributeArmies(numArmiesToDispatch);
		this.distributeArmies(selection);
		
	}

}

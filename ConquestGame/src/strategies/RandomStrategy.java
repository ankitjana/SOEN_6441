package strategies;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import beans.Country;
import beans.EventType;
import beans.Player;
import gui.PhaseView;

/**
 * A random computer player strategy that reinforces random a random country,
 * attacks a random number of times a random country, and fortifies a random
 * country, all following the standard rules for each phase. 
 * @author ankit
 *
 */
public class RandomStrategy extends Strategy implements Serializable {
	private Player player = null;
	private Country attackingCountry = null;

	public RandomStrategy(Player player) {
		super(player);
		this.player = player;
	}
	
	
	public Country getRandomCountry() {
		List<Country> countries = player.getPlayerCountries();
		int minRange = 0;
		int maxRange = countries.size();
		
		Random rand = new Random();
		int randomCountryInt = rand.nextInt((maxRange - minRange) + 1) + minRange;
		
		Country randomCountry = countries.get(randomCountryInt);
		
		return randomCountry; 
	}

	public void randomAttack(Country attackingCountry, Country attackedCountry) {
		int minRange = 0;
		int maxRange = 5;
		Random rand = new Random();
		int randomChances = rand.nextInt((maxRange - minRange) + 1) + minRange;
		while (attackingCountry.getNumArmies() > 1 && attackedCountry.getOwner() != this.player && randomChances > 0) {
			// get max number of dices possible for attacker
			int numDiceAttacker = (attackingCountry.getNumArmies() > 3) ? 3 : attackingCountry.getNumArmies() - 1;
			int numDiceDefender = (attackedCountry.getNumArmies() >= 2) ? 2 : attackedCountry.getNumArmies();
			int[] attackerDice = rollDiceAttacker(attackingCountry, numDiceAttacker);
			int[] defenderDice = rollDiceDefender(attackedCountry, numDiceDefender);
			// battle
			int[] result = goToBattle(attackerDice, defenderDice);
			invade(result, attackingCountry, attackedCountry, numDiceAttacker);
			randomChances--;
		}

	}
	
	@Override
	public void reEnforce() {
		controller.setCurrentPhase("ReInforce");
		int newArmies = obtainNewArmies();
		player.notifyChanges(EventType.PHASE_NOTIFY);
		Country country = getRandomCountry();
		country.setNumArmies(newArmies);
		player.notifyChanges(EventType.REENFORCEMENT_NOTIFY);
//		Map<Country, Integer> list = controller.distributeArmies(newArmies);
//		this.distributeArmies(list);
//		for(Country rec:player.getPlayerCountries()) {
//			rec.setNumArmies(rec.getNumArmies()*2);
//		}
		
	}
	

	@Override
	public void attack() {
		controller.setCurrentPhase("Attack");
		PhaseView phaseView = new PhaseView();
		controller.registerObserver(phaseView, EventType.PHASE_VIEW_NOTIFY);
		
		List<Country> defendingNeighbours = null;
		for(;;) {
			Country attackingCountry = getRandomCountry();
			defendingNeighbours = getdefendingNeighbours(attackingCountry);
			
			if(defendingNeighbours.size() > 0)
				break;
		}
		//List<Country> defendingNeighbours = getdefendingNeighbours(attackingCountry);
		Country toAttack = null;
		for (Country rec : defendingNeighbours) {
			if (attackingCountry.getNumArmies() > rec.getNumArmies()) {
				toAttack = rec;
				player.setHasEnemy(true);
				break;
			} else {
				player.setHasEnemy(false);
			}
		}
		if (toAttack == null) {
			player.setHasEnemy(false);
		}
		if (attackingCountry.getNumArmies() > 2 && toAttack != null) {
			this.randomAttack(attackingCountry, toAttack);
		}
		player.notifyChanges(EventType.ATTACK_NOTIFY);
		
	}

	@Override
	public void fortify() {
		// TODO Auto-generated method stub
		controller.setCurrentPhase("Fortification");
		PhaseView phaseView = new PhaseView();
		controller.registerObserver(phaseView, EventType.PHASE_VIEW_NOTIFY);
		Country fromCountry = null, toCountry = null;
		
		
		for(;;) {
			fromCountry = getRandomCountry();
			if (fromCountry.getNumArmies() > 1) {
				int minRange = 1;
				int maxRange = fromCountry.getNumArmies();
				Random rand = new Random();
				int randomTransfer = rand.nextInt((maxRange - minRange) + 1) + minRange;
				
				List<String> adjCountries = fromCountry.getAdjacentCountries();
				maxRange = adjCountries.size();
				
				int randomToCountry = rand.nextInt((maxRange - minRange) + 1) + minRange;
				
				toCountry = player.getCountryByName(adjCountries.get(randomToCountry));
				
				fromCountry.setNumArmies(fromCountry.getNumArmies() - randomTransfer);
				toCountry.setNumArmies(toCountry.getNumArmies() + randomTransfer);
				break;
				
			}
			else if(fromCountry.getNumArmies() == 1) {
				int minRange = 1;
				int maxRange = 0;
				Random rand = new Random();				
				List<String> adjCountries = fromCountry.getAdjacentCountries();
				maxRange = adjCountries.size();
				
				int randomToCountry = rand.nextInt((maxRange - minRange) + 1) + minRange;
				
				toCountry = player.getCountryByName(adjCountries.get(randomToCountry));
				
				fromCountry.setNumArmies(fromCountry.getNumArmies() - 1);
				toCountry.setNumArmies(toCountry.getNumArmies() + 1);
				break;
			}
		}
//		toCountry = compareCountries("strongest", null);
//		ArrayList<Country> validCountriestoMove = (ArrayList<Country>) validCountryMove(toCountry);
//		while(true){
//			if(validCountriestoMove.size()!=0) {
//				break;
//			}else {
//				toCountry = compareCountries("strongest", toCountry);
//				validCountriestoMove = (ArrayList<Country>) validCountryMove(toCountry);
//			}
//			
//		}
//		for (Country rec : validCountriestoMove) {
//			if (rec.getNumArmies() > 1 || fromCountry.getName().equals(toCountry.getName())) {
//				fromCountry = rec;
//			}
//		}
//		int armiesToMove = 0;
//		// check id attacking country has any defending neighbor left.
//		if (getdefendingNeighbours(toCountry).size() == 0 || validCountriestoMove.size() == 0) {
//			fromCountry = toCountry;
//			validCountriestoMove.removeAll(validCountriestoMove);
//			validCountriestoMove = (ArrayList<Country>) validCountryMove(toCountry);
//			for (Country rec : validCountriestoMove) {
//				if (rec.getNumArmies() > toCountry.getNumArmies()
//						|| fromCountry.getName().equals(toCountry.getName())) {
//					fromCountry = rec;
//				}
//			}
//			armiesToMove = fromCountry.getNumArmies() - 1;
//		} else {
//			armiesToMove = 1;
//		}
//		if (fromCountry == null) {
//			throw new IllegalArgumentException(fromName + " is not a country you occupied!");
//		}
//
//		if (toCountry == null) {
//			throw new IllegalArgumentException("There is no adjacent countries occupied by you!");
//		} else {
//			toName = toCountry.getName();
//			fromName = fromCountry.getName();
//			this.moveArmies(fromName, toName, armiesToMove);
//			player.notifyChanges(EventType.FORTIFICATION_NOTIFY);
//		}
		player.notifyChanges(EventType.FORTIFICATION_NOTIFY);
	}

	@Override
	public void placeArmiesForSetup() {
		// TODO Auto-generated method stub
		
	}
}

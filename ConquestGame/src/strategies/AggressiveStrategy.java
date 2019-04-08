/**
 * 
 */
package strategies;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import beans.Country;
import beans.EventType;
import beans.Player;
import gui.PhaseView;

/**
 * Represents aggressive strategy in which computer player focuses on attack.
 * 
 * @author apoorvasharma
 *
 */
public class AggressiveStrategy extends Strategy implements Serializable {
	private Player player = null;
	private Country attackingCountry = null;

	public AggressiveStrategy(Player player) {
		super(player);
		this.player = player;
	}

	/*
	 * (non-Javadoc) reinforces its strongest country and then always attack with it
	 * until it cannot attack anymore.
	 * 
	 * @see strategies.Strategy#reEnforce()
	 */
	@Override
	public void reEnforce() {
		controller.setCurrentPhase("Reenforce");
		int newArmies = obtainNewArmies();
		attackingCountry = compareCountries("strongest", null);
		List<Country> defendingNeighbours = getdefendingNeighbours(attackingCountry);
		List<Country> toRemove =new ArrayList<Country>();
		while(defendingNeighbours.size()==0) {
			toRemove.add(attackingCountry);
			attackingCountry = compareCountries("strongest", toRemove);
			defendingNeighbours = getdefendingNeighbours(attackingCountry);
		}
		if (attackingCountry != null) {
			this.distributeArmies(generateArmyCountyMap(attackingCountry,newArmies));
		}
	}

	private boolean isAttackPossible(Country attackingCountry, Country defendingCountry) {
		boolean isAttackPossoble = false;
		if (attackingCountry.getNumArmies() > defendingCountry.getNumArmies()) {
			isAttackPossoble = true;
		}
		return isAttackPossoble;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see strategies.Strategy#attack()
	 */
	@Override
	public void attack() {
		controller.setCurrentPhase("Attack");
		PhaseView phaseView = new PhaseView();
		controller.registerObserver(phaseView, EventType.PHASE_VIEW_NOTIFY);
		player.notifyChanges(EventType.PHASE_VIEW_NOTIFY);
		//attackingCountry = compareCountries("strongest", null);
		List<Country> defendingNeighbours = getdefendingNeighbours(attackingCountry);
		Country toAttack = null;
		for (Country rec : defendingNeighbours) {
				toAttack = rec;
				player.setHasEnemy(true);
				break;
		}
		if (toAttack == null) {
			player.setHasEnemy(false);
		}
		if (toAttack != null && attackingCountry.getNumArmies()>1) {
			this.goAllOut(attackingCountry, toAttack);
		}else {
			player.setHasEnemy(false);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see strategies.Strategy#fortify()
	 */
	@Override
	public void fortify() {
		controller.setCurrentPhase("Fortification");
		PhaseView phaseView = new PhaseView();
		controller.registerObserver(phaseView, EventType.PHASE_VIEW_NOTIFY);
		player.notifyChanges(EventType.PHASE_VIEW_NOTIFY);
		Country fromCountry = null, toCountry = null;
		String toName, fromName = null;
		toCountry = compareCountries("strongest", null);
		ArrayList<Country> validCountriestoMove = (ArrayList<Country>) validCountryMove(toCountry);
		
		/*while(validCountriestoMove.size()==0 && player.getPlayerCountries().size()>1) {
			toCountry = compareCountries("strongest", (new (List<Country>) toCountry);
			validCountriestoMove = (ArrayList<Country>) validCountryMove(toCountry);
		 
		}*/
		
		List<Country> defendingNeighbours = getdefendingNeighbours(attackingCountry);
		List<Country> toRemove =new ArrayList<Country>();
		while(defendingNeighbours.size()==0 && validCountriestoMove.size()==0 ) {
			toRemove.add(toCountry);
			toCountry = compareCountries("strongest", toRemove);
			validCountriestoMove = (ArrayList<Country>) validCountryMove(toCountry);
			
		}
		fromCountry=toCountry;
		for (Country rec : validCountriestoMove) {
			if (rec.getNumArmies() > 1 || fromCountry.getName().equals(toCountry.getName())) {
				fromCountry = rec;
			}
		}
		int armiesToMove = 0;
		// check id attacking country has any defending neighbor left.
		
		
		if (getdefendingNeighbours(toCountry).size() == 0) {
			fromCountry = toCountry;
			validCountriestoMove.removeAll(validCountriestoMove);
			validCountriestoMove = (ArrayList<Country>) validCountryMove(toCountry);
			for (Country rec : validCountriestoMove) {
				if (rec.getNumArmies() > toCountry.getNumArmies()
						|| fromCountry.getName().equals(toCountry.getName())) {
					toCountry = rec;
				}
			}
			armiesToMove = fromCountry.getNumArmies() - 1;
		} else {
			armiesToMove = 1;
		}
		if (fromCountry == null) {
			throw new IllegalArgumentException(fromName + " is not a country you occupied!");
		}

		if (toCountry == null) {
			throw new IllegalArgumentException("There is no adjacent countries occupied by you!");
		} else {
			toName = toCountry.getName();
			fromName = fromCountry.getName();
			if(fromCountry.getNumArmies()>1 && fromName!=toName) {
				this.moveArmies(fromName, toName, armiesToMove);
			}
			player.notifyChanges(EventType.FORTIFICATION_NOTIFY);
		}

	}

	@Override
	public void placeArmiesForSetup() {
		player.notifyChanges(EventType.PHASE_NOTIFY);
		// get the country with with highest number of defenders and assign all the
		// remaining armies to it,
		//Country rec = compareCountries();
		//this.distributeArmies(generateArmyCountyMap(rec,player.getArmies()));
		
		Map<Country,Integer> armyCountryMap =new HashMap<Country,Integer>();
		Random r = new Random();
		int maxArmies =player.getArmies();
		for(Country rec:player.getPlayerCountries()) {
			int temp =r.nextInt((maxArmies - 0) + 1) + 0;
			armyCountryMap.put(rec, temp);
			maxArmies =maxArmies-temp;
		}
		
		this.distributeArmies(armyCountryMap);
	}

}

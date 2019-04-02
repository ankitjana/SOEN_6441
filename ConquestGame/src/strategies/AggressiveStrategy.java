/**
 * 
 */
package strategies;

import java.util.List;
import java.util.Map;

import beans.Country;
import beans.EventType;
import beans.Player;
import gui.PhaseView;

/**
 * Represents aggressive strategy in which computer player focuses on attack.
 * @author apoorvasharma
 *
 */
public class AggressiveStrategy extends Strategy{
	private Player player = null;
	private Country attackingCountry =null;
	private Country weakestDefender =null;
	public AggressiveStrategy(Player player) {
		super(player);
		this.player =player;
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * reinforces its strongest country and then always attack with it until it cannot attack anymore.
	 * @see strategies.Strategy#reEnforce()
	 */
	@Override
	public void reEnforce() {
		// TODO Auto-generated method stub
		int newArmies = obtainNewArmies();
		attackingCountry =compareCountries("strongest",attackingCountry);
		if(attackingCountry!=null) {
			attackingCountry.setNumArmies(newArmies);
			this.distributeArmies(generateArmyCountyMap(attackingCountry));
		}
	}
	
	private boolean isAttackPossible(Country attackingCountry, Country defendingCountry) {
		boolean isAttackPossoble=false;
		if(attackingCountry.getNumArmies()>defendingCountry.getNumArmies()) {
			isAttackPossoble=true;
		}
		return isAttackPossoble;
	}
	/* (non-Javadoc)
	 * @see strategies.Strategy#attack()
	 */
	@Override
	public void attack() {
		controller.setCurrentPhase("Attack");
		PhaseView phaseView = new PhaseView();
		controller.registerObserver(phaseView, EventType.PHASE_VIEW_NOTIFY);
		player.notifyChanges(EventType.PHASE_VIEW_NOTIFY);
		
		if(attackingCountry.getNumArmies() < 2) {
			throw new IllegalArgumentException("The attacking country must have at least 2 armies!");
		}
		
		List<Country> defendingNeighbours = getdefendingNeighbours(attackingCountry);
		Country toAttack=null;
		for(Country rec :defendingNeighbours) {
			if(attackingCountry.getNumArmies()>rec.getNumArmies()) {
				toAttack =rec;
				break;
			}
		}
		this.goAllOut(attackingCountry, toAttack);
		
		
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
		String toName, fromName = attackingCountry.getName();

		fromCountry = player.getCountryByName(attackingCountry.getName());
		toCountry = compareCountries("strongest", fromCountry);
		int armiesToMove = 0;
		// check id attacking country has any defending neighbor left.
		if (getdefendingNeighbours(attackingCountry).size() == 0) {
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
			this.moveArmies(fromName, toName, armiesToMove);
			player.notifyChanges(EventType.FORTIFICATION_NOTIFY);
		}

	}

	@Override
	public void placeArmiesForSetup() {
		// TODO Auto-generated method stub
		System.out.println("in aggressive strategu"+player);
		player.notifyChanges(EventType.PHASE_NOTIFY);
		// get the country with  with highest number of defenders and assign all the remaining armies to it,
		Country rec=compareCountries();
		int numArmiesToDispatch = player.getArmies() - player.getNumArmiesDispatched();
		rec.setNumArmies(numArmiesToDispatch);
		this.distributeArmies(generateArmyCountyMap(attackingCountry));
	}
	
}

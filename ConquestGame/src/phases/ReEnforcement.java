package phases;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import beans.CardSet;
import beans.CardType;
import beans.Player;
import controller.GameController;
import beans.Continent;
import beans.Country;

/**
 * Re-enforcement phase
 * @author vanduong
 *
 */
public class ReEnforcement implements TurnPhase{
	
	private GameController controller = null;
	private Player curPlayer = null;
	private final int MIN_NEW_ARMIES = 3;
	private int cardSetChoice = 0;
	
	
	
	public boolean nextPhase(GameController controller) {
		this.controller = controller;
		curPlayer = controller.getCurrentPlayer();
		int numArmies = obtainNewArmies();
		
//		HashMap<String, Integer> distributionList = distributeArmies();
		
		return false;
	}

	/**
	 * distribute number of armies to countries occupied by current player
	 * @return
	 */
	private void distributeArmies(Map<Country, Integer> list) {
		for (Map.Entry<Country, Integer> entry : list.entrySet()) {
			Country country = entry.getKey();
			int numArmies = entry.getValue();
			curPlayer.getCountryByName(country.getName()).setNumArmies(numArmies);;
		}
	}
	
	
	

	public int obtainNewArmies() {
		
		//player's choice of set of cards to be traded
		int setChoice = (cardSetChoice > 1) ? cardSetChoice : 1;
		//redeem armies by cards
		// TODO
//		int armiesByCards = redeemCards(setChoice);
		
		//obtain armies by number of territories occupied
		int numCountries = curPlayer.getPlayerCountries().size();
		int numArmies = numCountries / 3;
		int armiesByCountries = ((numArmies > 3)) ? numArmies : 3;
		
		//obtain armies by number of continents controlled
		List<Continent> continents= curPlayer.getPlayerContinents();
		int armiesByContinents = 0;
		for(Continent c : continents) {
			armiesByContinents += c.getMaxArmies();
		}
		
		//obtain armies by The specific territory pictured on a traded-in card
		//NOT APPLICABLE
		//TODO add armiesByCards later
		int totalNewArmies = armiesByCountries + armiesByContinents;
		curPlayer.increaseArmies(totalNewArmies);
		return totalNewArmies;
	}
	// TODO redeemCards
/**
	public int redeemCards(int setChoice) throws IllegalArgumentException{
		CardSet set = CardSet.convertInputToType(setChoice);
		int numCards = curPlayer.getNumCards();
		int controller.getNumSetCardsTraded();
		
		//check for validity then...
		//deduct number of cards of each type according to player's set choice
		if(set == CardSet.ALL_INFANTRY) {
			int numCards = curPlayer.getNumCards(CardType.INFANTRY);
			if (numCards < 3) {
				throw new IllegalArgumentException("Not enough infantry cards for this choice");
			} else {
				curPlayer.setNumCards(CardType.INFANTRY, numCards - 3);
			}
		}
		
		else if(set == CardSet.ALL_CAVALRY) {
			int numCards = curPlayer.getNumCards(CardType.CAVALRY);
			if (numCards < 3) {
				throw new IllegalArgumentException("Not enough cavalry cards for this choice");
			} else {
				curPlayer.setNumCards(CardType.CAVALRY, numCards - 3);
			}
		}
		
		else if(set == CardSet.ALL_ARTILLERY) {
			int numCards = curPlayer.getNumCards(CardType.ARTILLERY);
			if (numCards < 3) {
				throw new IllegalArgumentException("Not enough artillery cards for this choice");
			} else {
				curPlayer.setNumCards(CardType.ARTILLERY, numCards - 3);
			}
		}
		
		else if(set == CardSet.ONE_EACH) {
			int numCardsInf = curPlayer.getNumCards(CardType.INFANTRY);
			int numCardsCav = curPlayer.getNumCards(CardType.CAVALRY);
			int numCardsArt = curPlayer.getNumCards(CardType.ARTILLERY);
			if (numCardsInf < 1) {
				throw new IllegalArgumentException("Not enough infantry types for this choice");
			} if(numCardsCav < 1) {
				throw new IllegalArgumentException("Not enough cavalry types for this choice");
			} if(numCardsArt < 1) {
				throw new IllegalArgumentException("Not enough artillery types for this choice");
			} else {
				curPlayer.setNumCards(CardType.INFANTRY, numCardsInf - 1);
				curPlayer.setNumCards(CardType.CAVALRY, numCardsCav - 1);
				curPlayer.setNumCards(CardType.ARTILLERY, numCardsArt - 1);

			}
		}
		//set numCardSetsTraded
		
		//convert cards to armies
		int numArmies = convertCards
		return 0;
	}
	**/
	
	

}

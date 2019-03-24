package gui;

import java.util.List;

import beans.Player;
import controller.GameController;

/**
 * 
 * @author yadavsurbhi
 *
 */
public class CardExchangeView {
public GameController controller;
	
	public CardExchangeView() {
		controller = GameController.getInstance();
	}
	public void getCardProgress() {
		List<Player> players = controller.getPlayerList();
		
		int totalPlayersAvailable = controller.getNumPlayers();
		
		System.out.println("Total Players : " + totalPlayersAvailable);
		
		for(Player player : players) {
			
			String playerName = player.getPlayerName();
			System.out.println("*".repeat(16));
			System.out.println("PlayerName : " + playerName);
			System.out.println("Total cards acquired : "+ player.getCardsAcquired().size());
			System.out.println("-".repeat(10));
			}
		}
	}

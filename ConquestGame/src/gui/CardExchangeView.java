package gui;

import java.util.List;
import java.util.Scanner;

import beans.Player;
import controller.GameController;

/**
 * 
 * @author yadavsurbhi
 *
 */
public class CardExchangeView {
public GameController controller;
Player player;
boolean isExchangePossible= false;
Scanner scan= new Scanner(System.in);
	
	public CardExchangeView() {
		controller = GameController.getInstance();
	}
	public void getCardProgress() {
			player = controller.getCurrentPlayer();
			String playerName = player.getPlayerName();
			System.out.println("*".repeat(20));
			System.out.println("PlayerName : " + playerName);
			System.out.println("Total cards acquired : "+ player.getCardsAcquired().size());
			System.out.println("Cards acquired by this player are :" + player.getCardsAcquired());
			System.out.println("-".repeat(20));			
		}	
	
	public boolean isExchangeCardsPossible() {
			player = controller.getCurrentPlayer();
			int infantry=0; int artillery=0; int cavalry=0;
			if(player.getCardsAcquired()!= null && player.getCardsAcquired().size()>=3) {
				for(String cardList : player.getCardsAcquired()) {
					if(cardList == "INFANTRY") {
						infantry++;
					}
					if(cardList == "CAVALRY") {
						cavalry++;
					}
					if(cardList == "ARTILLERY") {
						artillery++;
					}
				}
				if((infantry == 1 && cavalry == 1 && artillery == 1) || infantry == 3 || cavalry == 3 || artillery == 3){
					isExchangePossible= true;
				}
			}
		return isExchangePossible;
	}
}

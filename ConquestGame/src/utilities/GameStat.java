package utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import config.Config;
import controller.GameController;
import exception.MapInvalidException;

/**
 * This class is used to save or load the status of the game.
 * @author sandeepchowdaryannabathuni
 *
 */
public class GameStat implements Serializable {
	
	private static GameStat obj = null;
	private GameController controller = null;
	
	private GameStat() {
		controller = GameController.getInstance();
	}
	
	public static GameStat getInstance() {
		if(obj == null)
			obj = new GameStat();
		return obj;
	}
	
	/**
	 * This method is used to save game.
	 * @throws IOException
	 */
	public void save() throws IOException{
		String saveStatToFile = Config.getProperty("saveTofile");
		
		try(FileOutputStream file = new FileOutputStream(saveStatToFile);
		ObjectOutputStream objectWriter = new ObjectOutputStream(file);) {
		
		objectWriter.writeObject(controller);
		
		}
		
	}
	
	
	/**
	 * This method is used to load the game from the previous checkpoint.
	 * @throws IOException
	 * @throws ClassNotFoundException ec
	 * @throws MapInvalidException 
	 */
	public void load() throws IOException, ClassNotFoundException, MapInvalidException {
		String loadStatFromFile = Config.getProperty("loadFromfile");
		
		try(FileInputStream file = new FileInputStream(loadStatFromFile);
		ObjectInputStream objectReader = new ObjectInputStream(file);) {
		
		GameController controllerObj = (GameController)objectReader.readObject();
		controller.setController(controllerObj);
		controller.setCurrentPhase(controllerObj.getCurrentPhase());
		controller.setCurrentPlayer(controllerObj.getCurrentPlayer());
		controller.setWorldDominationView(controllerObj.getWorldDominationView());
		controller.setPhaseView(controllerObj.getPhaseView());
		controller.setCardExchangeView(controllerObj.getCardExchangeView());;
		controller.setCountryList(controllerObj.getCountryList());
		controller.setNumberOfPlayers(controllerObj.getNumberOfPlayers());
		controller.setContinentListByName(controllerObj.getContinentListByName());;
		controller.setCountryOwnership(controllerObj.getCountryOwnership());;
		controller.setReadyForNextPhase(controllerObj.getReadyForNextPhase());
		controller.setPlayerList(controllerObj.getPlayerList());
		controller.setWinner(controllerObj.getWinner());
		controller.setUI(controllerObj.getUI());
		controller.setCustomMapCenerator(controllerObj.getCustomMapGenerator());
		controller.setContinentList(controllerObj.getContinetList());
		controller.setGameStat(controllerObj.getGameStat());
		
		
		}
		
	}
	
//	public static void main(String[] args) throws IOException {
//		GameStat obj = GameStat.getInstance();
//		obj.save();
//	}
}

/**
 * 
 */
package gui;

import beans.Observable;
import utilities.Tuple;

/**
 * The user interface of the application. 
 * @author vanduong
 *
 */
public class GUI implements Observer{

	/**
	 * @return a tuple containing: country to move armies from, country to move armies to, and number of armies
	 */
	public static Tuple getFortificationInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return true of user is done with fortification, false otherwise
	 */
	public static boolean isDoneFortification() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * display error messages
	 * @param message
	 */
	public static void handleExceptions(String message) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see gui.Observer#update(beans.Observable)
	 */
	@Override
	public void update(Observable sub) {
		// TODO Auto-generated method stub
		
	}


}

package gui;

import utilities.ReplicateMap;

/**
 * Implementation of a “phase view” using the Observer pattern. The phase view should display:
 * (1)The name of the game phase currently being played 
 * (2)The current player’s name
 * (3)Information about actions that are taking place during this phase. 
 * The phase view should be cleared at the beginning of every phase.
 * @author ankit
 *
 */
public class PhaseView {
	
	private static PhaseView phaseView = null;
	
	public static PhaseView getInstance() {
		if ( phaseView == null) {
			phaseView = new PhaseView();
		}
		return phaseView;
	}
	

}

/**
 * 
 */
package beans;

import java.io.Serializable;

/**
 * @author vanduong
 *
 */
public enum Phase implements Serializable {
	/** the enforcement phase. */
	REENFORCEMENT(0),
	
	/** the attack phase. */
	ATTACK(1),
	
	/** the Fortification. */
	FORTIFICATION(2);
	
	private final int inputValue;
	
	Phase(int input){
		this.inputValue = input;
	}
	
	public int getValue() {
		return inputValue;
	}
	
	
	public static Phase getPhase(int val) {
		Phase phase = null;
		switch(val) {
		case 0:
			phase = Phase.REENFORCEMENT;
			break;
		case 1:
			phase = Phase.ATTACK;
			break;
		case 2:
			phase = Phase.FORTIFICATION;
			break;
		default:
			phase = Phase.REENFORCEMENT;
		}
		
		return phase;
		
	}
	
}

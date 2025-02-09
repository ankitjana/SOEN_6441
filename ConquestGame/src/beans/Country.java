package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import gui.Observer;

// TODO: Auto-generated Javadoc
/**
 * The Class Country.
 */
public class Country implements Serializable{

	/** The name. */
	private String name = null;

	/** The number of armies currently occupied in this country. */
	private int numArmies;

	/** The owner. */
	private Player owner = null;

	/** The observer list. */
	private List<Observer> obList = null;

	/** The adjacent countries. */
	private List<String> adjacentCountries;
	
	/** The continent. */
	private String continent;
	
	/** The longitude. */
	private int longitude;
	
	/** The latitude. */
	private int latitude;

	/**
	 * Instantiates a new country.
	 *
	 * @param name the country name
	 */
	public Country(String name) {
		super();
		this.name = name;
		obList = new ArrayList<Observer>();
	}

	/**
	 * Instantiates a new country.
	 */
	public Country() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the number of armies.
	 *
	 * @return the numArmies
	 */
	public int getNumArmies() {
		return numArmies;
	}

	/**
	 * Sets the number armies occupied in this country.
	 *
	 * @param numArmies the numArmies to set
	 */
	public void setNumArmies(int numArmies) {
		this.numArmies = numArmies;
	}

	/**
	 * Gets the owner.
	 *
	 * @return the owner
	 */
	public Player getOwner() {
		return owner;
	}

	/**
	 * Sets the owner.
	 *
	 * @param owner the owner to set
	 */
	public void setOwner(Player owner) {
		this.owner = owner;
	}


	/**
	 * Gets the adjacent countries.
	 *
	 * @return the adjacent countries
	 */
	public List<String> getAdjacentCountries() {
		return adjacentCountries;
	}

	/**
	 * Sets the adjacent countries.
	 *
	 * @param adjacentCountries the new adjacent countries
	 */
	public void setAdjacentCountries(List<String> adjacentCountries) {
		this.adjacentCountries = adjacentCountries;
	}

	/**
	 * Gets the continent.
	 *
	 * @return the continent
	 */
	public String getContinent() {
		return continent;
	}

	/**
	 * Sets the continent.
	 *
	 * @param continent the new continent
	 */
	public void setContinent(String continent) {
		this.continent = continent;
	}

	/**
	 * Gets the longitude.
	 *
	 * @return the longitude
	 */
	public int getlongitude() {
		return longitude;
	}

	/**
	 * Sets the longitude.
	 *
	 * @param langitude the new longitude
	 */
	public void setlongitude(int langitude) {
		this.longitude = langitude;
	}

	/**
	 * Gets the latitude.
	 *
	 * @return the latitude
	 */
	public int getLatitude() {
		return latitude;
	}

	/**
	 * Sets the latitude.
	 *
	 * @param latitude the new latitude
	 */
	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

}

package utilities;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import beans.Continent;
import beans.Country;
import exception.MapInvalidException;

/**
 * This class is used to handle the parsing of map files
 * @author apoorvasharma
 * @version 1.0.0
 */

public class MapParser {
	
	/**
	 * @param inputFile the input map file
	 */
	private String inputFile= "src/resources/Duplicate_Territory.map";
	private Scanner sc;

	public MapParser(String inputFile) {
		this.inputFile = inputFile;
	}
	public MapParser() {
		
	}
	public static void main(String args[]) {
		MapParser mp= new MapParser();
		mp.readFile();
	}

	private static String buildMapFile;
	public static ArrayList<Country> countriesList = new ArrayList<Country>();
	public static ArrayList<Continent> continentsList = new ArrayList<Continent>();
	public static Map<String, ArrayList<Country>> worldMap = new HashMap<String, ArrayList<Country>>();

	/**
	 * This method reads the input file for parsing
	 * 
	 */
	public void readFile() {
		try {
			// System.out.println(System.getProperty("user.dir"));

			// Scanner sc = new Scanner(new
			// File("/Users/apoorvasharma/git/SOEN_6441/ConquestGame/src/resources/World.map"));
			Scanner sc = new Scanner(new File(inputFile));

			String tempStr = null;
			buildMapFile = "";
			while (sc.hasNext()) {
				tempStr = sc.nextLine();
				buildMapFile = buildMapFile + "" + tempStr + "\n";
			}

			// continentsList=parseContinents(buildMapFile.substring(buildMapFile.indexOf("[Continents]"),
			// buildMapFile.indexOf("[Territories]")));
			countriesList = parseCountries(buildMapFile.substring(buildMapFile.indexOf("[Territories]")));
			continentsList = parseContinents(buildMapFile.substring(buildMapFile.indexOf("[Continents]"),
					buildMapFile.indexOf("[Territories]")));
		} catch (Exception e) {
			System.out.println("Map file does not exist");
			//System.out.println("Exception :" + e.getStackTrace());
			//System.out.println("Exception :" + e.getMessage());
			//System.out.println("Exception :" + e.getClass());
		}

	}

	/**
	 * 
	 * @param continents list of continents in input Map
	 * @return list of continents parsed in input Map
	 */
	private static ArrayList<Continent> parseContinents(String continents) {
		ArrayList<Continent> continentList = new ArrayList<Continent>();
		// System.out.println("Parsing Continents!!"+continents);
		String continent[] = continents.split("\n");
		try {
			for (int i = 1; i < continent.length; i++) {
				if (!continent[i].equalsIgnoreCase("[Continents]") && continent[i] != null) {
					String[] temp = continent[i].trim().split("=");
					Continent con = new Continent();
					con.setMaxArmies(Integer.parseInt((temp[1].trim())));
					con.setName(temp[0].trim());
					con.setCountries(worldMap.get(temp[0].trim()));
					continentList.add(con);
				}
			}
		} catch (Exception e) {
			// TODO
			System.out.println("Exception :" + e.getStackTrace());
			System.out.println("Exception :" + e.getMessage());
			System.out.println("Exception :" + e.getClass());
		}
		return continentList;
	}

	/**
	 * This method parse all the countries in input Map
	 * @param country list of countries 
	 * @return
	 */
	private static ArrayList<Country> parseCountries(String countries) {
		ArrayList<Country> countryList = new ArrayList<Country>();
		// System.out.println("Parsing Countries!!");
		String country[] = countries.split("\n");
		// System.out.println(country.length);
		try {
			for (int i = 1; i < country.length; i++) {
				if (!country[i].isEmpty() && !country[i].equalsIgnoreCase("[Territories]")) {
					String[] temp = country[i].trim().split(","); // Alaska,70,126,North America,
					if (temp != null) {
						Country con = new Country();
						con.setName(temp[0].trim());
						con.setLatitude(Integer.parseInt(temp[1].trim()));
						con.setlongitude(Integer.parseInt(temp[2].trim()));
						con.setContinent(temp[3].trim());
						countryList.add(con);
						}
					}
				}
			addAdjacentCountries(country, countryList);
			// Country recToReturn = new Country();
			// recToReturn =getCountry("Alaska",countryList);
			// System.out.println(recToReturn.getAdjacentCountries());

		} catch (Exception e) {
			System.out.println("Exception :" + e.getStackTrace());
			System.out.println("Exception :" + e.getMessage());
			System.out.println("Exception :" + e.getClass());
		}
		return countryList;

	}

	public static Country getCountry(String name, ArrayList<Country> countryList) {
		Country recToReturn = new Country();
		for (Country rec : countryList) {
			if (rec.getName().equals(name)) {
				recToReturn = rec;
			}
		}

		return recToReturn;
	}

	/**
	 * 
	 * @param countries
	 * @return
	 */
	private static void addAdjacentCountries(String country[], ArrayList<Country> countryList) {
		for (int i = 1; i < country.length; i++) {
			String[] temp = country[i].trim().split(",");
			Country rec = getCountry(temp[0].trim(), countryList);
			ArrayList<String> adjCountry = new ArrayList<String>();
			for (int j = 4; j < temp.length; j++) {
				if (temp[j] != null) {
					adjCountry.add(temp[j].trim());
				}
			}
			rec.setAdjacentCountries(adjCountry);

			// continent-country

			if (worldMap.get(rec.getContinent()) != null) {
				ArrayList<Country> tempList = new ArrayList<Country>();
				tempList.addAll(worldMap.get(rec.getContinent()));
				tempList.add(rec);
				worldMap.put(rec.getContinent(), tempList);

			} else {
				ArrayList<Country> tempList = new ArrayList<Country>();
				tempList.add(rec);
				worldMap.put(rec.getContinent(), tempList);
			}

		}
	}

}

package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import beans.Continent;
import beans.Country;
import exception.MapInvalidException;
import utilities.MapFileWriter;
import java.lang.*;

// TODO: Auto-generated Javadoc
/**
 * This class is responsible for handling creation and deletion of countries and
 * continents. It is also responsible for handling addition or deletion of
 * adjacent Countries adjacent countries.
 * 
 * @author apoorvasharma 
 * @version 1.0.0
 */
public class MapController {
	private volatile static MapController mapCntrl;
	public ArrayList<Country> countriesDefault;
	public ArrayList<Continent> continentsDefault;
	private Map<String, ArrayList<Country>> worldMap;
	public Map<String, Continent> continentmap;
	utilities.MapParser mpsr;

	private MapController() {}
	public static MapController getInstance() {
		if (mapCntrl == null) {
			synchronized (MapController.class) {
				if (mapCntrl == null)
					mapCntrl = new MapController();
			}
		}
		return mapCntrl;
	}

	public void init(String OperationType,String inputFile) throws IOException, MapInvalidException {
		if("LoadMap".equalsIgnoreCase(OperationType)) {
			validateMap(inputFile);
		}else if("CreateMap".equalsIgnoreCase(OperationType)) {
			countriesDefault = new ArrayList<Country>();
			continentsDefault =new ArrayList<Continent>();
		}else if("EditMap".equalsIgnoreCase(OperationType)) {
			validateMap(inputFile);
		}
		if(!continentsDefault.isEmpty()) {
			continentmap = new HashMap <String, Continent>();
			for(Continent crec :continentsDefault) {
				continentmap.put(crec.getName(),crec);
			}
		}
		
	}

	/**
	 * Validate map.
	 *
	 * @param inputFile input map file
	 * @throws IOException         file handling exception
	 * @throws MapInvalidException invalid map exception
	 */
	public void validateMap(String inputFile) throws IOException, MapInvalidException {
		utilities.MapValidator mpsr = new utilities.MapValidator(inputFile);
		mpsr.createCountryGraph();
		countriesDefault = mpsr.countriesList;
		continentsDefault = mpsr.continentsList;
		worldMap = mpsr.worldMap;

	}

	/**
	 * Sets the map details.
	 *
	 * @param bw instance of Buffered Writer
	 * @throws IOException file handling exception
	 */
	private void setMapDetails(BufferedWriter bw) throws IOException {
		bw.write("[Map]");
	}

	/**
	 * Adds the continent.
	 *
	 * @param continentMap continent user wish to add in the input map file
	 * @param bw           instance of Buffered Writer
	 * @param isEdit       to decide if input file needs to be edited or newly
	 *                     created
	 * @param inputFile    input map file
	 * @throws IOException         file handling exception
	 * @throws MapInvalidException invalid map exception
	 */
	public void addContinent(Map<String, Integer> continentMap) throws IOException, MapInvalidException {
		try {
			Continent crec;
			for (String rec : continentMap.keySet()) {
				if (!continentsDefault.contains(rec)) {
					crec = new Continent();
					crec.setName(rec);
					crec.setMaxArmies(continentMap.get(rec));
					continentsDefault.add(crec);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception:"+e.getCause());
			throw new MapInvalidException("Error while adding new continent. Provide a valid input.");
		} 
	}

	/**
	 * Adds the country.
	 *
	 * @param countryArrayList list of
	 * @param bw               instance of Buffered Writer
	 * @param isEdit           to decide if input file needs to be edited or newly
	 *                         created
	 * @param inputFile        input map file
	 * @throws IOException         file handling exception
	 * @throws MapInvalidException invalid map exception
	 */
	public void addCountry(List<String> countryLst) throws IOException, MapInvalidException {
		try {
			Set<String> countryNameSet = new HashSet<String>();
			for (Country rec : countriesDefault) {
				countryNameSet.add(rec.getName());
			}
			System.out.println(" In add Country addCountry");
			for(String str :countryLst) {
				String[] temp = str.split(",");
				if (temp != null) {
					if(!countryNameSet.contains(temp[0].trim())) {
					Country con = new Country();
					ArrayList<String> adjCountry = new ArrayList<String>();
					con.setName(temp[0].trim());
					con.setLatitude(Integer.parseInt(temp[1].trim()));
					con.setlongitude(Integer.parseInt(temp[2].trim()));
					con.setContinent(temp[3].trim());
					for(int i=4;i<temp.length;i++) {
						adjCountry.add(temp[i]);
					}
					con.setAdjacentCountries(adjCountry);
					countriesDefault.add(con);
					}
				}
			}

		} catch (Exception e) {
			throw new MapInvalidException("Error while adding new country. Provide a valid input.");
		}

	}

	/**
	 * Adds the adj country.
	 *
	 * @param adjCountryMap list of adjacent countries of a particular country
	 * @param inputFile     input map file
	 * @throws IOException         file handling exception
	 * @throws MapInvalidException invalid map exception
	 */

	public void addAdjCountry(Map<String, List<String>> adjCountryMap)
			throws IOException, MapInvalidException {

		try {
			Set<String> countryname = new HashSet<String>();
			if (!countriesDefault.isEmpty()) {
				for (Country rec : countriesDefault) {
					countryname.add(rec.getName());
				}
			}
			
			for (String str : adjCountryMap.keySet()) {
				if (countryname.contains(str)) {
					List<String> adjStr = adjCountryMap.get(str);
					for (String arr : adjStr) {
						if (countryname.contains(arr)) {
							Country rec = mpsr.getCountry(str, countriesDefault);
							if (!rec.getAdjacentCountries().contains(arr)) {
								rec.getAdjacentCountries().add(arr);
								rec = mpsr.getCountry(arr, countriesDefault);
								rec.getAdjacentCountries().add(str);
							}
						} else {
							throw new MapInvalidException("Adjacent country does not exist");
						}
					}
				} else {
					throw new MapInvalidException(str + "Country does not exist. Add the country again");
				}

			}

		} catch (Exception e) {
			throw new MapInvalidException("Error while adding new adjacent country. Provide a valid input");
		}
	}

	/**
	 * Removes the continent.
	 *
	 * @param continentName continent name user wish to remove from map file
	 * @param inputFile     input map file
	 * @throws IOException         file handling exception
	 * @throws MapInvalidException invalid map exception
	 */

	public void removeContinent(ArrayList<String> continentName, String inputFile)
			throws IOException, MapInvalidException {
		try {
			Iterator<Continent> iter = continentsDefault.iterator();
			while (iter.hasNext()) {
				Continent conrec = iter.next();
				if (continentName.contains(conrec.getName())) {
					iter.remove();
				}
			}
			ArrayList<Country> countriesToRemove = new ArrayList<Country>();
			for (String key : worldMap.keySet()) {
				if (continentName.contains(key)) {
					countriesToRemove.addAll(worldMap.get(key));
				}
			}

			ArrayList<String> countriesToDelete = new ArrayList<String>();
			for (Country cRec : countriesToRemove) {
				countriesToDelete.add(cRec.getName());
			}

			if (countriesToDelete.size() != 0) {
				removeCountry(countriesToDelete, inputFile, false);
			}
		} catch (Exception e) {
			throw new MapInvalidException("Error while removing Continent. Provide a valid input to remove");
		}

	}

	/**
	 * Removes the country.
	 *
	 * @param countries   country user wish to remove from input map file
	 * @param inputFile   input map file
	 * @param isRecursion the is recursion
	 * @throws IOException         file handling exception
	 * @throws MapInvalidException invalid map exception
	 */

	public void removeCountry(ArrayList<String> countries, String inputFile, boolean isRecursion)
			throws IOException, MapInvalidException {
		int countrytoRemove = 0;
		try {
			Iterator<Country> iter = countriesDefault.iterator();
			while (iter.hasNext()) {
				Country conrec = iter.next();
				if (countries.contains(conrec.getName())) {
					worldMap.get(conrec.getContinent()).remove(conrec.getName());
					iter.remove();
					countrytoRemove++;
				}
			}
			if (countrytoRemove == 0) {
				throw new MapInvalidException("The country does not exist. Provide a valid input to remove.");
			}
			removeAdjCountry(countries, inputFile, null, true);
		} catch (MapInvalidException e) {
			throw new MapInvalidException(e.getMessage());
		} catch (Exception e) {
			throw new MapInvalidException("Error while removing country. Provide a valid input to remove.");
		}

	}

	/**
	 * Removes the adj country.
	 *
	 * @param countries adjacent country user wish to remove from input map file
	 * @param inputFile input map file
	 * @param adjMap    the adj map
	 * @param isFunCall the is fun call
	 * @throws IOException         file handling exception
	 * @throws MapInvalidException invalid map exception
	 */

	public void removeAdjCountry(ArrayList<String> countries, String inputFile, Map<String, List<String>> adjMap,
			boolean isFunCall) throws IOException, MapInvalidException {
		try {
			Iterator<Country> iter = countriesDefault.iterator();
			if (isFunCall) {
				while (iter.hasNext()) {
					Country conrec = iter.next();
					for (String str : countries) {
						if (conrec.getAdjacentCountries().contains(str)) {
							conrec.getAdjacentCountries().remove(str);

						}
					}
				}
			} else {
				while (iter.hasNext()) {
					Country conrec = iter.next();
					if (adjMap.containsKey(conrec.getName())) {
						for (String str : adjMap.get(conrec.getName())) {
							conrec.getAdjacentCountries().remove(str);
						}
					}
				}
			}

			// check for countries without any adjacent countries and remove them if they
			// exist
			Iterator<Country> iter1 = countriesDefault.iterator();
			while (iter1.hasNext()) {
				Country conrec = iter1.next();
				if (conrec.getAdjacentCountries().size() == 0) {
					worldMap.get(conrec.getContinent()).remove(conrec.getName());
					iter1.remove();
				}
			}
			// remove continents if no country is associated with the continent
			if (!worldMap.isEmpty()) {
				Iterator<Continent> iterCon = continentsDefault.iterator();
				while (iterCon.hasNext()) {
					Continent conrec = iterCon.next();
					if (worldMap.get(conrec.getName()) == null) {
						iterCon.remove();
					}
				}
			}
		} catch (Exception e) {
			throw new MapInvalidException("Error while removing Adjacent country. Provide a valid input to remove");
		}

	}

	public boolean mapWritter(
			String inputFile) throws MapInvalidException {
		MapFileWriter mfw = new MapFileWriter();
		try {
			System.out.println("continentsDefault"+continentsDefault.size());
			System.out.println("countriesDefault"+countriesDefault.size());
			System.out.println("inputFile"+inputFile);
			mfw.writeFile(continentsDefault, countriesDefault, inputFile);
			validateMap(inputFile);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
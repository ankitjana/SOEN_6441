package utilities;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import beans.Continent;
import beans.Country;
import controller.MapController;
import exception.MapInvalidException;

/**
 * This class test the updates made in the existing map file
 * 
 * @author yadavsurbhi
 *
 */

public class MapUpdateTest {
	private MapController mapController;
	EditMap editMap;
	Map<String, Integer> addContinents;
	Object[] addCountries;
	Map<String, List<String>> adjCountries;
	ArrayList<String> removeContinents;
	ArrayList<String> removeCountries;
	String expectedFile = "src/resources/testingworld.map";

	/**
	 * This method allocates resources before running test cases
	 * 
	 * @throws Exception invalid input exception
	 */
	@Before
	public void setUp() throws Exception {

		mapController = MapController.getInstance();
		//editMap = EditMap.getInstance();
		addContinents = new HashMap<String, Integer>();
		addCountries = new Object[10];
		adjCountries = new HashMap<String, List<String>>();
		removeContinents = new ArrayList<>();
		removeCountries = new ArrayList<>();
		String INPUTFILE = "src/resources/World.map";
		FileReader reader = new FileReader(INPUTFILE);
		FileWriter writer = new FileWriter(expectedFile);
		Scanner wrapReader = new Scanner(reader);
		BufferedWriter wrapWriter = new BufferedWriter(writer);
		while (wrapReader.hasNextLine()) {
			wrapWriter.write(wrapReader.nextLine());
			wrapWriter.newLine();
			wrapWriter.flush();
		}
		reader.close();
		writer.close();
		wrapReader.close();
		wrapWriter.close();

	}

	/**
	 * This method deallocates the resources after running test cases
	 * 
	 * @throws Exception invalid input exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * This method test if continents are added in input map
	 * 
	 * @throws IOException         file handling exception
	 * @throws MapInvalidException invalid map exception
	 */
	@Test
	public void addContinentTest() throws IOException, MapInvalidException {
		addContinents.put("ABCD", 101);
		String str = "test,1,1,ABCD,China";
		addCountries[0] = str;
		mapController.addContinent(addContinents);
		// mapController.addCountry(addCountries, null, true, expectedFile);
		assertNotNull(mapController.continentsDefault);
		assertTrue(mapController.continentsDefault.size() > 0);
	}

	/**
	 * This method test if adjacent countries are added in input map
	 * 
	 * @throws IOException         file handling exception
	 * @throws MapInvalidException invalid map exception
	 */
	@Test
	public void addAdjacentCountryTest() throws IOException, MapInvalidException {
		List<String> list = new ArrayList<>();
		list.add("Alberta");
		list.add("Alaska");
		adjCountries.put("India", list);
		mapController.addAdjCountry(adjCountries);
		assertNotNull(mapController.countriesDefault);
		assertTrue(mapController.countriesDefault.size() > 0);
	}

	/**
	 * This method test if continents are removed in input map
	 * 
	 * @throws IOException         file handling exception
	 * @throws MapInvalidException invalid map exception
	 */
	@Test
	public void removeContinentTest() throws IOException, MapInvalidException {
		removeContinents.add("Australia");
		mapController.removeContinent(removeContinents, expectedFile);
		assertNotNull(mapController.continentsDefault);
		assertTrue(mapController.continentsDefault.size() > 0);
	}

	/**
	 * This method test if countries are removed in input map
	 * 
	 * @throws IOException         file handling exception
	 * @throws MapInvalidException invalid map exception
	 */
	@Test
	public void removeCountryTest() throws IOException, MapInvalidException {
		removeCountries.add("China");
		mapController.removeCountry(removeCountries, expectedFile, false);
		assertNotNull(mapController.countriesDefault);
		assertTrue(mapController.countriesDefault.size() > 0);
	}
}
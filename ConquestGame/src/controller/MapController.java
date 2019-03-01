package controller;

import beans.Continent;
import beans.Country;
import exception.MapInvalidException;

import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Scanner;

public class MapController {

	public void validateMap(String inputFile) throws IOException, MapInvalidException {
		new utilities.MapValidator(inputFile).createCountryGraph();
	}

	private void editMap(String inputFile, String operation) {

		if ("Add Continent".equalsIgnoreCase(operation)) {

		} else if ("Remove Continent".equalsIgnoreCase(operation)) {

		} else if ("Add Country".equalsIgnoreCase(operation)) {

		} else if ("Remove Country".equalsIgnoreCase(operation)) {

		} else if ("Add Adjacent Country".equalsIgnoreCase(operation)) {

		} else if ("Remove Adjacent Country".equalsIgnoreCase(operation)) {

		}

	}

	private static void setMapDetails(BufferedWriter bw) throws IOException {
		bw.write("[Map]");
	}

	private static void addContinent(Map<String, Integer> continentMap, BufferedWriter bw, boolean isEdit,
			String inputFile) throws IOException {

		if (!isEdit) {
			bw.write("[Continents]" + "\n");
			for (String key : continentMap.keySet()) {
				bw.write(key + "=" + continentMap.get(key) + "\n");
			}
		} else {
			FileReader editFile = new FileReader(inputFile);
			Scanner sc = new Scanner(new File(inputFile));
			String tempStr = null;
			String buildMapFile = "";
			while (sc.hasNext()) {
				tempStr = sc.nextLine();
				buildMapFile = buildMapFile + "" + tempStr + "\n";
			}
			String temp = null, updatedStr = null;
			for (String key : continentMap.keySet()) {
				temp = (key + "=" + continentMap.get(key) + "\n");
			}
			// System.out.println(buildMapFile.substring(0,buildMapFile.indexOf("[Continents]")));
			updatedStr = buildMapFile.substring(0, buildMapFile.indexOf("[Territories]")) + temp
					+ buildMapFile.substring(buildMapFile.indexOf("[Territories]"));
			FileWriter writer = new FileWriter(inputFile);

			writer.write(updatedStr);
			writer.close();
		}

	}

	private static void addCountry(String[] arrayList, BufferedWriter bw, boolean isEdit, String inputFile)
			throws IOException {
		if (!isEdit) {
			bw.write("[Territories]" + "\n");
			for (int k = 0; k < arrayList.length; k++) {
				bw.write(arrayList[k] + "\n");
			}
		} else {
			FileReader editFile = new FileReader(inputFile);
			Scanner sc = new Scanner(new File(inputFile));
			String tempStr = null;
			String buildMapFile = "";
			while (sc.hasNext()) {
				tempStr = sc.nextLine();
				buildMapFile = buildMapFile + "" + tempStr + "\n";
			}
			String temp = null, updatedStr = null;
			for (int k = 0; k < arrayList.length; k++) {
				temp = arrayList[k] + "\n";
			}
			System.out.println(buildMapFile.substring(buildMapFile.indexOf("[Territories]")));
			System.out.println(temp);
			updatedStr = buildMapFile.substring(0, buildMapFile.indexOf("[Territories]"))
					+ buildMapFile.substring(buildMapFile.indexOf("[Territories]")) + temp;
			System.out.println(updatedStr);
			FileWriter writer = new FileWriter(inputFile);
			writer.write(updatedStr);
			writer.close();
		}

	}

	private void removeContinent(String[] continentName, String inputFile) {

	}

	private void removeCountry(String[] continentName, String inputFile) {

	}

}
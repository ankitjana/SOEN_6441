package utilities;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import org.jgrapht.*;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;

import beans.Continent;
import beans.Country;
import exception.MapInvalidException;

/**
 * @author apoorvasharma
 *
 */
public class MapValidator {
	private String inputFile;
	public static ArrayList<Country> countriesList = new ArrayList<Country>();
	public static ArrayList<Continent> continentsList = new ArrayList<Continent>();
	public static Graph<Country, DefaultEdge> countryGraph = new SimpleGraph<>(DefaultEdge.class);
	public static ArrayList<Graph<List<Country>, DefaultEdge>> subGraphsList = new ArrayList<Graph<List<Country>, DefaultEdge>>();

	public MapValidator(String inputFile) {
		this.inputFile = inputFile;
	}

	public void createCountryGraph() throws IOException, MapInvalidException {
		new utilities.MapParser(inputFile).readFile();
		countriesList = utilities.MapParser.countriesList;
		
		  if (continentsList.size() <= 1 || countriesList.size() <= 1) { throw new
		  MapInvalidException("There should be atleast one continent in Map");
		  }
		 
		try {
			// adding the nodes to the graph
			for (Country rec : countriesList) {
				countryGraph.addVertex(rec);
			}

			// adding the edges to the graph
			for (Country conRec : countryGraph.vertexSet()) {
				ArrayList<String> adjCountry = new ArrayList<String>();
				adjCountry.addAll(conRec.getAdjacentCountries());
				for (String str : adjCountry) {
					Country countryRec = utilities.MapParser.getCountry(str, countriesList);
					countryGraph.addEdge(conRec, countryRec);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception :" + e.getStackTrace());
			System.out.println("Exception :" + e.getMessage());
			System.out.println("Exception :" + e.getClass());
		}
		// get subgraph
		System.out.println("createCountrySubGraoh");
		continentsList = utilities.MapParser.continentsList;
		for (Continent rec : continentsList) {
			subGraphsList.add(createCountrySubGraph(rec.getCountries(), countryGraph));

		}

		boolean isConnected = mapisConnected();
		if (!isConnected) {
			throw new MapInvalidException("The input Map is not connected. Please provide a valid Map");
		}
		// mapVisual();
		System.out.println(subGraphsList.size());
		/***
		 * Check if edge exists!!! Country recToReturn = new Country(); recToReturn
		 * =utilities.MapParser.getCountry("Alaska",countriesList); Country recToReturn1
		 * = new Country(); recToReturn1
		 * =utilities.MapParser.getCountry("Alberta",countriesList);
		 * 
		 * //System.out.println(recToReturn.getAdjacentCountries());
		 * //System.out.println(countryGraph.getEdge(recToReturn, recToReturn1));
		 */

	}

	// For Map Visualization
	public void mapVisual() throws IOException {

		File imgFile = new File("src/resources/Worldmap.png");
		imgFile.createNewFile();
		JGraphXAdapter<Country, DefaultEdge> graphAdapter = new JGraphXAdapter<Country, DefaultEdge>(countryGraph);
		mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
		layout.execute(graphAdapter.getDefaultParent());

		BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
		ImageIO.write(image, "PNG", imgFile);

	}

	public boolean mapisConnected() {

		boolean isConnected = false;
		if (new ConnectivityInspector<>(countryGraph).isConnected()) {
			isConnected = true;
		} else {
			isConnected = false;
		}
		return false;

	}

	/**
	 * 
	 * @param countryList
	 * @param countryGraph
	 * @return
	 */
	private Graph<List<Country>, DefaultEdge> createCountrySubGraph(List<Country> countryList,
			Graph<Country, DefaultEdge> countryGraph) {
		Set<Country> countrySet = new HashSet<Country>(countryList);
		Graph<List<Country>, DefaultEdge> subGraph = new AsSubgraph(countryGraph, countrySet);
		return subGraph;
	}

}

package org.scribe.builder.api;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CSVReader {

	private String csvFile;
	private Map<Integer,Double> denseMap;
	
	public CSVReader(String file) {
		csvFile = file;
		denseMap = densByPop(file);
	}
	
	@SuppressWarnings("unchecked")
	public Map<Integer,Double> GetDensityMap(){
		if(denseMap!=null){
			return denseMap;
		} else {
			System.out.println("WTF!!!");
			return null;
		}
	}
	
	private Map<Integer,Double> densByPop(String file){
		System.out.println(file);
		Map<Integer,Double> map = new HashMap<Integer,Double>();
		
		//temporarily assign file to proper file
		file = "/Users/Mom/Documents/AndroidStuff/android-quickstart/PopDensByZip.csv";
		
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
	 
		try {
	 
			br = new BufferedReader(new FileReader(file));
			//System.out.println("Here's the columns:");
			//read first line, just titles
			line = br.readLine();
			while ((line = br.readLine()) != null) {
	 
				// use comma as separator
				String[] zipDens = line.split(cvsSplitBy);
	 
				map.put(Integer.parseInt(zipDens[0]), Double.parseDouble(zipDens[3]));
	 
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return map;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * 	Sample code below makes a reader and returns the density of
		 * 	Union Square in SF (50992.59259 homies per sq mile!)
		 * 
		 */
		
		//CSVReader reader = new CSVReader("hi!");
		//System.out.println(reader.GetDensityMap().get(94108));
	}

}

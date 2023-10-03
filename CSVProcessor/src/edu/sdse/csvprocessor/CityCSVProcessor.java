package edu.sdse.csvprocessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;

public class CityCSVProcessor {

	public List<CityRecord> readAndProcess(File file) {
		// Try with resource statement (as of Java 8)
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			// Discard header row
			br.readLine();

			List<CityRecord> allRecords = new ArrayList<>(10); // to store the read records

			String line;
			while ((line = br.readLine()) != null) {
				// Parse each line
				String[] rawValues = line.split(",");

				int id = convertToInt(rawValues[0]);
				int year = convertToInt(rawValues[1]);
				String city = convertToString(rawValues[2]);
				int population = convertToInt(rawValues[3]);

				CityRecord cr = new CityRecord(id, year, city, population);
				System.out.println("New city record loaded \n" + cr);

				allRecords.add(id, cr);

			}

			return allRecords;

		} catch (Exception e) {
			System.err.println("An error occurred:");
			e.printStackTrace();
		}

		return null;
	}

	private String cleanRawValue(String rawValue) {
		return rawValue.trim();
	}

	private int convertToInt(String rawValue) {
		rawValue = cleanRawValue(rawValue);
		return Integer.parseInt(rawValue);
	}

	private String convertToString(String rawValue) {
		rawValue = cleanRawValue(rawValue);

		if (rawValue.startsWith("\"") && rawValue.endsWith("\"")) {
			return rawValue.substring(1, rawValue.length() - 1);
		}

		return rawValue;
	}

	public static List<String> getUniqueCities(List<CityRecord> cityRecords) {
		// Use Java streams to get distinct cities from CityRecord objects
		List<String> uniqueCities = cityRecords.stream()
				.map(CityRecord::getCity) // Extract city names using the getCity() method
				.distinct() // Get distinct city names
				.collect(Collectors.toList());

		return uniqueCities;
	}

	private static List<CityRecord> filterCityRecords(List<CityRecord> cityRecords, String targetCity) {
		// Use Java streams to filter records for the specific city
		List<CityRecord> filteredRecords = cityRecords.stream()
				.filter(record -> record.getCity().equals(targetCity))
				.collect(Collectors.toList());

		return filteredRecords;
	}

	private static Map<String, List<CityRecord>> records2Map(List<CityRecord> records){
	
		Map<String, List<CityRecord>> recordMap = new HashMap<String, List<CityRecord>>(); // init the hash map
		List<String> cities = getUniqueCities(records);

		for (String city: cities){
			recordMap.put(city, filterCityRecords(records, city)); 
		}
		return recordMap;
	}

	public static final void main(String[] args) {
		CityCSVProcessor reader = new CityCSVProcessor();

		File dataDirectory = new File("CSVProcessor/data/");
		File csvFile = new File(dataDirectory, "Cities.csv");

		reader.readAndProcess(csvFile);
	}
}

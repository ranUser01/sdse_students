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

				allRecords.add(cr);

			}

			Map<String, List<CityRecord>> hm = records2Map(allRecords);

			city_stats(hm);

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

	private static Map<String, List<CityRecord>> records2Map(List<CityRecord> records) {

		Map<String, List<CityRecord>> recordMap = new HashMap<String, List<CityRecord>>(); // init the hash map
		List<String> cities = getUniqueCities(records);

		for (String city : cities) {
			recordMap.put(city, filterCityRecords(records, city));
		}
		return recordMap;
	}

	private void city_stats(Map<String, List<CityRecord>> cityRecordsMap) {
		for (Map.Entry<String, List<CityRecord>> entry : cityRecordsMap.entrySet()) {
			String cityName = entry.getKey(); // Get the city name (key)
			List<CityRecord> cityRecords = entry.getValue(); // Get the list of CityRecord objects (value)

			int count_entries = 0;
			int total_population = 0;
			int min_year = 2100;
			int max_year = 0;

			for (CityRecord record : cityRecords) {
				count_entries += 1;
				total_population += record.getPopulation();
				if (min_year > record.getYear()) {
					min_year = record.getYear();
				}
				if (max_year < record.getYear()) {
					max_year = record.getYear();
				}
			}

			System.out.println("City: " + cityName + ", Number of Entries: " + count_entries +
					", Total Population: " + total_population +
					", Average population: " + total_population/count_entries +
					", Minimum Year: " + min_year +
					", Maximum Year: " + max_year);

		}
	}

	public static final void main(String[] args) {
		CityCSVProcessor reader = new CityCSVProcessor();

		File dataDirectory = new File("CSVProcessor/data/");
		File csvFile = new File(dataDirectory, "Cities.csv");

		reader.readAndProcess(csvFile);
	}
}

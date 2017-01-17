package com.crossover.trial.weather.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

/**
 * A simple airport loader which reads a file from disk and sends entries to the
 * webservice
 *
 * TODO: Implement the Airport Loader
 * 
 * @author code test administrator
 */
public class AirportLoader {

	/** end point to supply updates */
	private WebTarget collect;

	public AirportLoader() {
		Client client = ClientBuilder.newClient();
		collect = client.target("http://localhost:9090/collect");
	}

	private void upload(File airportDataFile) throws IOException, URISyntaxException {
		List<List<String>> values = getValues(airportDataFile);

		for (List<String> line : values) {
			String iata = line.get(4).replaceAll("\"", "");
			String latitude = line.get(6);
			String longitude = line.get(7);

			collect
				.path(String.format("/airport/%s/%s/%s", iata, latitude, longitude))
				.request()
				.post(Entity.html(""));
		}

		System.out.println("Airports >> " + collect.path("/airports").request().get().readEntity(String.class));
	}

	private List<List<String>> getValues(File airportDataFile) throws IOException, URISyntaxException {
		List<List<String>> values = null;
		if (airportDataFile == null) {
			URL url = AirportLoader.class.getClassLoader().getResource("airports.dat");
			values = getValues(url);
		} else {
			values = getValues(airportDataFile.getPath());
		}
		return values;
	}

	private List<List<String>> getValues(URL airportFilePath) throws IOException, URISyntaxException {
		return getValues(Paths.get(airportFilePath.toURI()));
	}

	private List<List<String>> getValues(String airportFilePath) throws IOException {
		return getValues(Paths.get(airportFilePath));
	}

	private List<List<String>> getValues(Path path) throws IOException {
		return Files.lines(path).map(line -> Arrays.asList(line.split(","))).collect(Collectors.toList());
	}

	public static void main(String args[]) throws IOException, URISyntaxException {
		File airportDataFile = null;
		if (args.length > 0) {
			airportDataFile = new File(args[0]);
		}

		if (airportDataFile == null || !airportDataFile.exists() || airportDataFile.length() == 0) {
			System.err.println(airportDataFile + " is not a valid input. Reading mock input...");
		}

		AirportLoader loader = new AirportLoader();
		loader.upload(airportDataFile);
	}

}

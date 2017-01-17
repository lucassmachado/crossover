package com.crossover.trial.weather;

import static java.lang.String.format;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.HttpServerFilter;
import org.glassfish.grizzly.http.server.HttpServerProbe;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * This main method will be use by the automated functional grader. You
 * shouldn't move this class or remove the main method. You may change the
 * implementation, but we encourage caution.
 *
 * @author code test administrator
 */
public class WeatherServer {

	private static final String BASE_URL = "http://localhost:9090/";

	private static final String ENDPOINTS_PACKAGE = "com.crossover.trial.weather.endpoint";

	public static void main(String[] args) {
		try {
			System.out.println("Starting Weather App local testing server: " + BASE_URL);

			HttpServer server = createServer();
			addShutdownHook(server);
			addProbe(server);

			// the autograder waits for this output before running automated
			// tests, please don't remove it
			server.start();
			System.out.println(format("Weather Server started.\n url=%s\n", BASE_URL));

			// blocks until exit
			Thread.currentThread().join();
			server.shutdown();
			System.out.println(format("Weather Server stopped.\n url=%s\n", BASE_URL));
		} catch (IOException | InterruptedException ex) {
			Logger.getLogger(WeatherServer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static HttpServer createServer() {
		ResourceConfig resourceConfig = new ResourceConfig().packages(ENDPOINTS_PACKAGE);

		return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URL), resourceConfig, false);
	}

	private static void addShutdownHook(HttpServer server) {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				server.shutdownNow();
			}
		}));
	}

	@SuppressWarnings("rawtypes")
	private static void addProbe(HttpServer server) {
		HttpServerProbe probe = new HttpServerProbe.Adapter() {
			public void onRequestReceiveEvent(HttpServerFilter filter, Connection connection, Request request) {
				System.out.println(request.getRequestURI());
			}
		};

		server.getServerConfiguration().getMonitoringConfig().getWebServerConfig().addProbes(probe);
	}

}

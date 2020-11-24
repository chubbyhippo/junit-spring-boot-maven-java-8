package com.example.springboottest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.example.springboottest.beans.FlightBuilder;
import com.example.springboottest.model.Country;
import com.example.springboottest.model.CountryRepository;
import com.example.springboottest.model.Flight;
import com.example.springboottest.model.Passenger;
import com.example.springboottest.model.PassengerRepository;

@SpringBootApplication
@Import(FlightBuilder.class)
public class SpringBootTestApplication {
	
	@Autowired
	private Flight flight;

	@Autowired
	private Map<String, Country> countriesMap;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootTestApplication.class, args);
	}

	@Bean
	CommandLineRunner configureRepository(
			CountryRepository countryRepository,
			PassengerRepository passengerRepository) {
		return args -> {
			for (Country country : countriesMap.values()) {
				countryRepository.save(country);
			}
			for (Passenger passenger : flight.getPassengers()) {
				passengerRepository.save(passenger);
			}
		};
	}

}

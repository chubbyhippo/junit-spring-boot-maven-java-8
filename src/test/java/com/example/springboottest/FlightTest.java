package com.example.springboottest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.example.springboottest.beans.FlightBuilder;
import com.example.springboottest.model.Flight;
import com.example.springboottest.model.Passenger;
import com.example.springboottest.registration.PassengerRegistrationEvent;
import com.example.springboottest.registration.RegistrationManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(FlightBuilder.class)
public class FlightTest {

	@Autowired
	private Flight flight;

	@Autowired
	private RegistrationManager registrationManager;

	@Test
	void testFlightPassengersRegistration() {
		for (Passenger passenger : flight.getPassengers()) {
			assertFalse(passenger.isRegistered());
			registrationManager.getApplicationContext()
					.publishEvent(new PassengerRegistrationEvent(passenger));
		}

		for (Passenger passenger : flight.getPassengers()) {
			assertTrue(passenger.isRegistered());
		}
	}
}

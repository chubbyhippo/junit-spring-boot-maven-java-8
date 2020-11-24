package com.example.springboottest.model;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboottest.exceptions.PassengerNotFoundException;

@RestController
public class PassengerController {

	@Autowired
	private PassengerRepository repository;

	@Autowired
	private Map<String, Country> countriesMap;

	@GetMapping("/passengers")
	List<Passenger> findAll() {
		return repository.findAll();
	}

	@PostMapping("/passengers")
	@ResponseStatus(HttpStatus.CREATED)
	Passenger createPassenger(@RequestBody Passenger passenger) {
		return repository.save(passenger);
	}

	@GetMapping("/passengers/{id}")
	Passenger findPassenger(@PathVariable Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new PassengerNotFoundException(id));
	}

	@PatchMapping("/passengers/{id}")
	Passenger patchPassenger(@RequestBody Map<String, String> updates,
			@PathVariable Long id) {

		return repository.findById(id).map(passenger -> {

			String name = updates.get("name");
			if (null != name) {
				passenger.setName(name);
			}

			Country country = countriesMap.get(updates.get("country"));
			if (null != country) {
				passenger.setCountry(country);
			}

			String isRegistered = updates.get("isRegistered");
			if (null != isRegistered) {
				passenger.setIsRegistered(
						isRegistered.equalsIgnoreCase("true") ? true : false);
			}
			return repository.save(passenger);
		}).orElseGet(() -> {
			throw new PassengerNotFoundException(id);
		});

	}

	@DeleteMapping("/passengers/{id}")
	void deletePassenger(@PathVariable Long id) {
		repository.deleteById(id);
	}
}
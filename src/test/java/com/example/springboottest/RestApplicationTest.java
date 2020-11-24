package com.example.springboottest;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import com.example.springboottest.beans.FlightBuilder;
import com.example.springboottest.exceptions.PassengerNotFoundException;
import com.example.springboottest.model.Country;
import com.example.springboottest.model.CountryRepository;
import com.example.springboottest.model.Flight;
import com.example.springboottest.model.Passenger;
import com.example.springboottest.model.PassengerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Import(FlightBuilder.class)
public class RestApplicationTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private Flight flight;

	@Autowired
	private Map<String, Country> countriesMap;

	@MockBean
	private PassengerRepository passengerRepository;

	@MockBean
	private CountryRepository countryRepository;

	@Test
	void testGetAllCountries() throws Exception {
		when(countryRepository.findAll())
				.thenReturn(new ArrayList<>(countriesMap.values()));
		mvc.perform(get("/countries")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(3)));

		verify(countryRepository, times(1)).findAll();
	}

	@Test
	void testGetAllPassengers() throws Exception {
		when(passengerRepository.findAll())
				.thenReturn(new ArrayList<>(flight.getPassengers()));

		mvc.perform(get("/passengers")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(20)));

		verify(passengerRepository, times(1)).findAll();
	}

	@Test
	void testPassengerNotFound() {
		Throwable throwable = assertThrows(NestedServletException.class,
				() -> mvc.perform(get("/passengers/30"))
						.andExpect(status().isNotFound()));
		assertEquals(PassengerNotFoundException.class,
				throwable.getCause().getClass());
	}

	@Test
	void testPostPassenger() throws Exception {

		Passenger passenger = new Passenger("Peter Michelsen");
		passenger.setCountry(countriesMap.get("US"));
		passenger.setIsRegistered(false);
		when(passengerRepository.save(passenger)).thenReturn(passenger);

		mvc.perform(post("/passengers")
				.content(new ObjectMapper().writeValueAsString(passenger))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name", is("Peter Michelsen")))
				.andExpect(jsonPath("$.country.codeName", is("US")))
				.andExpect(jsonPath("$.country.name", is("USA")))
				.andExpect(jsonPath("$.registered", is(Boolean.FALSE)));

		verify(passengerRepository, times(1)).save(passenger);
	}

	@Test
  void testPatchPassenger() throws Exception {
      Passenger passenger = new Passenger("Sophia Graham");
      passenger.setCountry(countriesMap.get("UK"));
      passenger.setIsRegistered(false);
      when(passengerRepository.findById(1L))
           .thenReturn(Optional.of(passenger));
      when(passengerRepository.save(passenger))
           .thenReturn(passenger);
      String updates =
        "{\"name\":\"Sophia Jones\", \"country\":\"AU\",\"isRegistered\":\"true\"}";
 
      mvc.perform(patch("/passengers/1")
          .content(updates)
          .header(HttpHeaders.CONTENT_TYPE,
                              MediaType.APPLICATION_JSON))
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
 
      verify(passengerRepository, times(1)).findById(1L);
      verify(passengerRepository, times(1)).save(passenger);
  }

	@Test
	public void testDeletePassenger() throws Exception {

		mvc.perform(delete("/passengers/4")).andExpect(status().isOk());

		verify(passengerRepository, times(1)).deleteById(4L);
	}

}
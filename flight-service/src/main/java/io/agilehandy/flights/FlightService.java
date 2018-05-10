/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.agilehandy.flights;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Haytham Mohamed
 */

@Service
@Slf4j
public class FlightService {

	private final FlightRepository flightRepository;

	public FlightService(FlightRepository flightRepository) {
		this.flightRepository = flightRepository;
	}

	public List<Flight> getFlights(String origin, String distination, Date departure, Date arrival) {
		List<Flight> flights =
				flightRepository
						.findFlightsByCustomQueryDated(origin, distination, departure, arrival)
						;
		if (flights.size() == 0) {
			throw new FlightNotFoundExption("No flights found");
		}

		log.info("There are " + flights.size() + " flights found.");

		return flights;
	}

	public List<Flight> getFlights(String origin, String distination) {
		List<Flight> flights =
				flightRepository
						.findByOriginAndDistination(origin, distination);

		if (flights.size() == 0) {
			throw new FlightNotFoundExption("No flights found");
		}

		log.info("There are " + flights.size() + " flights found.");

		return flights;
	}

	public Flight getFlight(Long id) throws FlightNotFoundExption {
		return flightRepository.findById(id)
				.orElseThrow(() -> new FlightNotFoundExption("No Flight found!"));
	}

}

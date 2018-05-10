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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Haytham Mohamed
 */

@RestController
@Slf4j
public class FlightController {

	private final FlightService flightService;

	public FlightController(FlightService flightService) {
		this.flightService = flightService;
	}

	@GetMapping("/search/{from}/{to}")
	public List<Flight> search(@PathVariable String from, @PathVariable String to) {
		log.info("Searching for a flight from " + from + " to " + to);
		List<Flight> flights = flightService.getFlights(from, to);

		flights.stream().forEach(f -> log.info(f.toString()));

		return flights;
	}

	@GetMapping("/search/{from}/{to}/{depart:.+}/{arrival:.+}")
	public List<Flight> search(@PathVariable String from, @PathVariable String to,
			@PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") Date depart,
			@PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") Date arrival) {
		log.info("Searching for a flight from " + from + " to " + to);
		return flightService.getFlights(from, to, depart, arrival);
	}

	@GetMapping("/{id}")
	public Flight oneFlight(@PathVariable Long id) throws FlightNotFoundExption {
		log.info("finding flight with id " + id);
		return flightService.getFlight(id);
	}

	@ExceptionHandler(FlightNotFoundExption.class)
	public ExceptionMessage getFlightAlt(FlightNotFoundExption exception) {
		return new ExceptionMessage(exception.getExceptionMsg(),
				exception.getCause()==null? "" : exception.getCause().getMessage(),
				Arrays.stream(exception.getStackTrace())
						.map(sequence -> String.valueOf(sequence))
						.collect(Collectors.joining( "\n" )));
	}
}

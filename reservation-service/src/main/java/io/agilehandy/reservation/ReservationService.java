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

package io.agilehandy.reservation;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.agilehandy.reservation.flight.Flight;
import io.agilehandy.reservation.flight.FlightClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Haytham Mohamed
 */

@Service
@Slf4j
public class ReservationService {

	private final FlightClient flightClient;

	private final ReservationRepository reservationRepository;

	public ReservationService(FlightClient flightClient,
			ReservationRepository reservationRepository) {
		this.flightClient = flightClient;
		this.reservationRepository = reservationRepository;
	}

	@HystrixCommand(fallbackMethod = "reliableBooking")
	public Long book(String name, String origin, String destination) {
		log.info("reserving a flight from " + origin + " to " + destination + " for " + name);
		ResponseEntity<List<Flight>> response = flightClient.findFlights(origin, destination);

		List<Flight> flights =
				response != null ? response.getBody() : null;

		if (flights == null) {
			throw new ReservationException("No flight found!");
		}

		// for this demo just pick the first flight.
		Reservation reservation =
				reservationRepository.save(new Reservation(name, flights.get(0).getNbr()));

		return reservation.getId();
	}

	public Long reliableBooking(String name, String origin, String destination) {
		log.info("Something went wrong. Circuit breaker opens an a temporary booking is generated");
		return 0l;
	}

}

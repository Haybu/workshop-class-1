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

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author Haytham Mohamed
 */

@Entity
@Data
@NoArgsConstructor
@ToString
public class Flight {

	@Id
	@GeneratedValue(
			strategy= GenerationType.AUTO
	)
	private Long id;

	private String nbr;

	private String airline;

	private String origin;

	private String distination;

	private Integer stops;

	private Double price;

	private Integer capacity;

	private String plane;

	private Date departure;

	private Date arrival;

}

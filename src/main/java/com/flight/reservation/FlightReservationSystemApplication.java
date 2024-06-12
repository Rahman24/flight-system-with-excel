package com.flight.reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"com.flight.reservation"} )
public class FlightReservationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightReservationSystemApplication.class, args);
	}

}

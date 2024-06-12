package com.flight.reservation.service;

import com.flight.reservation.exception.FlightException.FlightNotFoundException;
import com.flight.reservation.exception.PassengerException.PassengerNotFoundException;
import com.flight.reservation.model.Booking;
import com.flight.reservation.model.Flight;
import com.flight.reservation.model.Passenger;
import com.flight.reservation.repository.BookingRepository;
import com.flight.reservation.repository.FlightRepository;
import com.flight.reservation.repository.PassengerRepository;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

        private static final Logger logInfo = LoggerFactory.getLogger(BookingService.class);

        @Autowired
        private BookingRepository bookingRepository;

        @Autowired
        private PassengerRepository passengerRepository;

        @Autowired
        private FlightRepository flightRepository;

        public Booking createBooking(String passengerId, String flightId) {

                Passenger passenger = passengerRepository.findById(passengerId)
                                .orElseThrow(() -> new PassengerNotFoundException(
                                                "Passenger not found with ID: " + passengerId));

                Flight flight = flightRepository.findById(flightId)
                                .orElseThrow(() -> new FlightNotFoundException(
                                                "Flight not found with ID: " + flightId));

                Booking booking = Booking.builder()
                                .passenger(passenger)
                                .flight(flight)
                                .bookingDateTime(LocalDateTime.now())
                                .cancelled(false)
                                .build();

                // Update the flight's available seats
                int updatedAvailableSeats = flight.getAvailableSeats() - 1;
                flight.setAvailableSeats(updatedAvailableSeats);
                flightRepository.save(flight);
                logInfo.info(" Booking Created for Single Passenger... ");
                return bookingRepository.save(booking);
        }

        public List<Booking> createBookingsForMultiplePassengers(List<String> passengerIds, String flightId) {
                Flight flight = flightRepository.findById(flightId)
                                .orElseThrow(() -> new FlightNotFoundException(
                                                "Flight not found with ID: " + flightId));

                List<Passenger> passengers = passengerRepository.findAllById(passengerIds);

                LocalDateTime bookingDateTime = LocalDateTime.now();

                List<Booking> bookings = passengers.stream()
                                .map(passenger -> Booking.builder()
                                                .passenger(passenger)
                                                .flight(flight)
                                                .bookingDateTime(bookingDateTime)
                                                .cancelled(false)
                                                .build())
                                .toList();
                                
                int updatedAvailableSeats = flight.getAvailableSeats() - passengerIds.size();
                flight.setAvailableSeats(updatedAvailableSeats);
                flightRepository.save(flight);
                logInfo.info("Booking Created for Multiple Passengers..");
                return bookingRepository.saveAll(bookings);
        }


        public Booking getBookingById(String bookingId) {
                logInfo.info("Retrieved Booking Details by Booking ID..");
                return bookingRepository.findById(bookingId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Booking not found with ID: " + bookingId));
        }

        public void cancelBooking(String bookingId) {
                logInfo.info("Cancelled Booking by Booking ID..");
                Booking booking = bookingRepository.findById(bookingId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Booking not found with ID: " + bookingId));

                if (!booking.isCancelled()) {
                     booking.cancelBooking();
                     bookingRepository.save(booking);
                        Flight flight = booking.getFlight();
                        int updatedAvailableSeats = flight.getAvailableSeats() + 1;
                        flight.setAvailableSeats(updatedAvailableSeats);
                        flightRepository.save(flight);
                }
        }

        public List<Booking> getBookingsById(String PassengerId) {
                if (!passengerRepository.existsById(PassengerId)) {
                        throw new PassengerNotFoundException("Passenger Not Found with ID : " + PassengerId);
                }
                logInfo.info("Retrieved Booking Details by Passenger ID..");
                return bookingRepository.getAllBookingByPassengerId(PassengerId);
        }

        public List<Booking> getAllBookings() {
                logInfo.info("Retrieved All Booking Details");
                return bookingRepository.findAll();
        }
}

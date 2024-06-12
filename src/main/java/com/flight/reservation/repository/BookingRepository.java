package com.flight.reservation.repository;
import com.flight.reservation.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> getAllBookingByPassengerId(String PassengerId);
}

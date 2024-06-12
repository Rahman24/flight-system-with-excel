package com.flight.reservation.repository;

import com.flight.reservation.model.BookingEnhancement;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookingEnhanceRepo extends MongoRepository<BookingEnhancement,String> {
}

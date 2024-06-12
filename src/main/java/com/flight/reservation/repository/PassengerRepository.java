package com.flight.reservation.repository;

import com.flight.reservation.model.Passenger;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends MongoRepository<Passenger,String> {
}

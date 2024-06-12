package com.flight.reservation.controller;

import java.util.*;
import com.flight.reservation.model.Passenger;
import com.flight.reservation.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PassengerController {
    @Autowired
    PassengerService passengerService;

    @GetMapping("/getallPassengers")
    public List<Passenger> getAllPassengers() {
        return passengerService.getAllPassengers();
    }

    @GetMapping("/passenger-id/{id}")
    public Passenger getPassengerById(@PathVariable String id) {
        return passengerService.getPassengerById(id);
    }

    @PostMapping("/new-passenger")
    public Passenger createPassenger(@RequestBody Passenger passenger) {
        return passengerService.createPassenger(passenger);
    }

    @PutMapping("/update-passenger/{id}")
    public Passenger updatePassenger(@PathVariable String id, @RequestBody Passenger updatedPassenger) {
        return passengerService.updatePassenger(id, updatedPassenger);
    }

    @DeleteMapping("/delete-passenger/{id}")
    public void deletePassenger(@PathVariable String id) {
        passengerService.deletePassenger(id);
    }
}

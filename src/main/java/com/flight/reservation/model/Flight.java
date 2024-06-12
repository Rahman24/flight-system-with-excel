package com.flight.reservation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
@Builder
public class Flight implements Serializable {
    @Id
    private String id;

    private String flightNumber;

    private String origin;

    private String destination;

    private LocalDateTime departureDateTime;

    private LocalDateTime arrivalDateTime;

    private int availableSeats;

    @DBRef
    @JsonIgnore
    private List<Booking> bookings;
}

package com.flight.reservation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Booking implements Serializable {

    @Id
    private String id;

    private Passenger passenger;

    private Flight flight;

    private LocalDateTime bookingDateTime;

    private boolean cancelled;

    public void cancelBooking()
    {
        this.cancelled =true;
    }
}

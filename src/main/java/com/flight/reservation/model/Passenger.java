package com.flight.reservation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Passenger implements Serializable {
    @Id
    private String id;

    private String firstName;

    private String lastName;

    private String email;

    @JsonIgnore
    @DBRef
    private List<Booking> bookings;
}

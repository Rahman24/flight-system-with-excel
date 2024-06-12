package com.flight.reservation.model;


import com.mongodb.MongoClientSettings;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bookingEnhancements")
@Builder
public class BookingEnhancement  implements Serializable {

    @Id
    public  String bookingId;

    public  String  departureTime;

    public  String  arrivalTime;

    public String PassengerName;

    public String flightNumber;

    public String destination;

    public String origin;
}

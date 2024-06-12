package com.flight.reservation.service;

import com.flight.reservation.model.BookingEnhancement;
import com.flight.reservation.repository.BookingEnhanceRepo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


@Service
public class BookingEnhanceService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BookingEnhanceRepo bookingEnhanceRepo;

    public List<Document> getDesiredData() {
        AggregationExpression formatDepartureDateTime = context -> context.getMappedObject(
                new Document("$dateToString", new Document("format", "%Y-%m-%d %H:%M:%S")
                        .append("timezone", "Asia/Kolkata")
                        .append("date", "$flight.departureDateTime"))
        );

        AggregationExpression formatArrivalDateTime = context -> context.getMappedObject(
                new Document("$dateToString", new Document("format", "%Y-%m-%d %H:%M:%S")
                        .append("timezone", "Asia/Kolkata")
                        .append("date", "$flight.arrivalDateTime"))
        );

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("cancelled").is(false)),
                Aggregation.project()
                        .and(ConvertOperators.ToString.toString("$_id")).as("BookingId")
                        .and(formatDepartureDateTime).as("DepartureTime")
                        .and(formatArrivalDateTime).as("ArrivalTime")
                        .andExpression("concat('$passenger.firstName', ' ', '$passenger.lastName')").as("PassengerName")
                        .and("flight.flightNumber").as("flightNumber")
                        .and("flight.destination").as("Destination")
                        .and("flight.origin").as("Origin")
                        .andExclude("_id")
        );
        return mongoTemplate.aggregate(aggregation, "booking", Document.class).getMappedResults();
    }
    public byte[] generateExcelBytes(List<BookingEnhancement> bookingData) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Bookings");

        // Create header row
        String[] headers = {"Booking ID", "Departure Time", "Arrival Time", "Passenger Name", "Flight Number", "Destination", "Origin"};
        Row headerRow = sheet.createRow(0);
        IntStream.range(0, headers.length).forEach(i -> {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        });

        // Fill data rows
        int rowNum = 1;
        for (BookingEnhancement booking : bookingData) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(booking.getBookingId());
            row.createCell(1).setCellValue(booking.getDepartureTime());
            row.createCell(2).setCellValue(booking.getArrivalTime());
            row.createCell(3).setCellValue(booking.getPassengerName());
            row.createCell(4).setCellValue(booking.getFlightNumber());
            row.createCell(5).setCellValue(booking.getDestination());
            row.createCell(6).setCellValue(booking.getOrigin());
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }
    public List<BookingEnhancement> getDesiredDataForExcel() {
        List<Document> rawData = getDesiredData();
        List<BookingEnhancement> excelData = new ArrayList<>();

        for (Document document : rawData) {
            String bookingId = document.getString("BookingId");
            String departureTime = document.getString("DepartureTime");
            String arrivalTime = document.getString("ArrivalTime");
            String passengerName = document.getString("PassengerName");
            String flightNumber = document.getString("flightNumber");
            String destination = document.getString("Destination");
            String origin = document.getString("Origin");
            BookingEnhancement bookingEnhancement = BookingEnhancement.builder()
                                                  .bookingId(bookingId)
                                                  .departureTime(departureTime)
                                                  .arrivalTime(arrivalTime)
                                                  .flightNumber(flightNumber)
                                                  .PassengerName(passengerName)
                                                  .destination(destination)
                                                  .origin(origin)
                                                  .build();

            excelData.add(bookingEnhancement);
        }
        return excelData;
    }

}
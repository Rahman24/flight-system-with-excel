package com.flight.reservation.controller;

import com.flight.reservation.model.BookingEnhancement;
import com.flight.reservation.service.BookingEnhanceService;
import com.flight.reservation.service.BookingService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
@RestController
public class BookingEnhanceController {

    @Autowired
    private BookingEnhanceService bookingEnhanceService;

    @GetMapping("/desiredData")
    public List<Document> getDesiredData() {
        return bookingEnhanceService.getDesiredData();
    }
    @GetMapping("/download_bookings_excel")
    public ResponseEntity<byte[]> downloadBookingsExcel() throws IOException {
        List<BookingEnhancement> bookingData = bookingEnhanceService.getDesiredDataForExcel();

        byte[] excelBytes = bookingEnhanceService.generateExcelBytes(bookingData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "bookings.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelBytes);
    }

}

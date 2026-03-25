package com.example.demo.controller;

import com.example.demo.common.ResponseHandler;
import com.example.demo.dto.common.ResponseDTO;
import com.example.demo.dto.res.DonationRes;
import com.example.demo.enums.StatusCode;
import com.example.demo.service.DonationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/green_earth/donation")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class DonationController {
    private final DonationService donationService;

    @GetMapping()
    public ResponseEntity<ResponseDTO<List<DonationRes>>> getAllDonations() {
        try {
            return ResponseHandler.success(donationService.getAllDonations(), "Thành công");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<DonationRes>> findDonationById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(donationService.findById(id), "Thành công");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }
}
package com.example.demo.controller;

import com.example.demo.common.ResponseHandler;
import com.example.demo.dto.common.ResponseDTO;
import com.example.demo.dto.req.DonationReq;
import com.example.demo.dto.res.DonationRes;
import com.example.demo.enums.StatusCode;
import com.example.demo.service.DonationService;
import com.example.demo.util.ApiPaging;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/green_earth/donation")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class DonationController {
    private final DonationService donationService;

    @GetMapping()
    public ResponseEntity<ResponseDTO<Object>> getAllDonations(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        try {
            if (ApiPaging.isPagedRequest(q, page, size)) {
                return ResponseHandler.success(
                        (Object) donationService.searchDonations(
                                q != null ? q : "",
                                ApiPaging.pageOrZero(page),
                                ApiPaging.sizeBounded(size, 20)
                        ),
                        "Success"
                );
            }
            return ResponseHandler.success((Object) donationService.getAllDonations(), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<DonationRes>> findDonationById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(donationService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<DonationRes>> createDonation(@RequestBody DonationReq req) {
        try {
            return ResponseHandler.success(donationService.createDonation(req), "Donation successful!");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }
}

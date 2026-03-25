package com.example.demo.controller;

import com.example.demo.common.ResponseHandler;
import com.example.demo.dto.common.ResponseDTO;
import com.example.demo.dto.req.PartnerReq;
import com.example.demo.dto.res.PartnerRes;
import com.example.demo.enums.StatusCode;
import com.example.demo.service.PartnerService;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/green_earth/partner")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class PartnerController {
    private final PartnerService partnerService;

    @GetMapping()
    public ResponseEntity<ResponseDTO<List<PartnerRes>>> getAllPartners() {
        try {
            return ResponseHandler.success(partnerService.getAllPartners(), "Thành công");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<PartnerRes>> findPartnerById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(partnerService.findById(id), "Thành công");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<ResponseDTO<PartnerRes>> createPartner(@RequestBody PartnerReq req) {
        try {
            return ResponseHandler.success(partnerService.create(req), "Thành công");
        } catch (ValidationException v) {
            return ResponseHandler.error(StatusCode.VALIDATION_ERROR, v.getMessage());
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<PartnerRes>> updatePartner(@PathVariable Long id, @RequestBody PartnerReq req) {
        try {
            return ResponseHandler.success(partnerService.update(id, req), "Thành công");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> deletePartner(@PathVariable Long id) {
        try {
            partnerService.delete(id);
            return ResponseHandler.success("Xoá đối tác thành công", "Thành công");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }
}
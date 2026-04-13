package com.example.demo.controller;

import com.example.demo.common.ResponseHandler;
import com.example.demo.dto.common.ResponseDTO;
import com.example.demo.dto.req.CelebrityReq;
import com.example.demo.dto.res.CelebrityRes;
import com.example.demo.enums.StatusCode;
import com.example.demo.service.CelebrityService;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/green_earth/celebrity")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class CelebrityController {
    private final CelebrityService celebrityService;

    @GetMapping()
    public ResponseEntity<ResponseDTO<List<CelebrityRes>>> getAllCelebrities() {
        try {
            return ResponseHandler.success(celebrityService.getAllCelebrities(), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<CelebrityRes>> findCelebrityById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(celebrityService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<ResponseDTO<CelebrityRes>> createCelebrity(@RequestBody CelebrityReq req) {
        try {
            return ResponseHandler.success(celebrityService.create(req), "Success");
        } catch (ValidationException v) {
            return ResponseHandler.error(StatusCode.VALIDATION_ERROR, v.getMessage());
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<CelebrityRes>> updateCelebrity(@PathVariable Long id, @RequestBody CelebrityReq req) {
        try {
            return ResponseHandler.success(celebrityService.update(id, req), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> deleteCelebrity(@PathVariable Long id) {
        try {
            celebrityService.delete(id);
            return ResponseHandler.success("Celebrity deleted successfully", "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }
}
package com.example.demo.controller;

import com.example.demo.common.ResponseHandler;
import com.example.demo.dto.common.ResponseDTO;
import com.example.demo.dto.req.EventReq;
import com.example.demo.dto.res.EventRes;
import com.example.demo.enums.StatusCode;
import com.example.demo.service.EventService;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/green_earth/event")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class EventController {
    private final EventService eventService;

    @GetMapping()
    public ResponseEntity<ResponseDTO<List<EventRes>>> getAllEvents() {
        try {
            return ResponseHandler.success(eventService.getAllEvents(), "Thành công");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<EventRes>> findEventById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(eventService.findById(id), "Thành công");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<ResponseDTO<EventRes>> createEvent(@RequestBody EventReq req) {
        try {
            return ResponseHandler.success(eventService.create(req), "Thành công");
        } catch (ValidationException v) {
            return ResponseHandler.error(StatusCode.VALIDATION_ERROR, v.getMessage());
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<EventRes>> updateEvent(@PathVariable Long id, @RequestBody EventReq req) {
        try {
            return ResponseHandler.success(eventService.update(id, req), "Thành công");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> deleteEvent(@PathVariable Long id) {
        try {
            eventService.delete(id);
            return ResponseHandler.success("Xoá sự kiện thành công", "Thành công");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }
}

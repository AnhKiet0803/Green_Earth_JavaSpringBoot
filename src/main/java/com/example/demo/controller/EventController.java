package com.example.demo.controller;

import com.example.demo.common.ResponseHandler;
import com.example.demo.dto.common.ResponseDTO;
import com.example.demo.dto.req.EventReq;
import com.example.demo.dto.res.EventRes;
import com.example.demo.enums.StatusCode;
import com.example.demo.service.EventService;
import com.example.demo.util.ApiPaging;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/green_earth/event")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class EventController {
    private final EventService eventService;

    @GetMapping()
    public ResponseEntity<ResponseDTO<Object>> getAllEvents(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        try {
            if (ApiPaging.isPagedRequest(q, page, size)) {
                return ResponseHandler.success(
                        (Object) eventService.searchEvents(
                                q != null ? q : "",
                                ApiPaging.pageOrZero(page),
                                ApiPaging.sizeBounded(size, 20)
                        ),
                        "Success"
                );
            }
            return ResponseHandler.success((Object) eventService.getAllEvents(), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<EventRes>> findEventById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(eventService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<ResponseDTO<EventRes>> createEvent(@RequestBody EventReq req) {
        try {
            return ResponseHandler.success(eventService.create(req), "Success");
        } catch (ValidationException v) {
            return ResponseHandler.error(StatusCode.VALIDATION_ERROR, v.getMessage());
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<EventRes>> updateEvent(@PathVariable Long id, @RequestBody EventReq req) {
        try {
            return ResponseHandler.success(eventService.update(id, req), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> deleteEvent(@PathVariable Long id) {
        try {
            eventService.delete(id);
            return ResponseHandler.success("Event deleted successfully", "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }
}

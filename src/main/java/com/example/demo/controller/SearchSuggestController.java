package com.example.demo.controller;

import com.example.demo.common.ResponseHandler;
import com.example.demo.dto.common.ResponseDTO;
import com.example.demo.dto.res.SuggestItemRes;
import com.example.demo.enums.StatusCode;
import com.example.demo.service.SearchSuggestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/green_earth/search")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SearchSuggestController {
    private final SearchSuggestService searchSuggestService;

    @GetMapping("/suggest")
    public ResponseEntity<ResponseDTO<List<SuggestItemRes>>> suggest(
            @RequestParam String q,
            @RequestParam(defaultValue = "10") int limit
    ) {
        try {
            return ResponseHandler.success(searchSuggestService.suggest(q, limit), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }
}

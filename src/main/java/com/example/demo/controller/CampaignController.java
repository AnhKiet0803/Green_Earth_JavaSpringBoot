package com.example.demo.controller;

import com.example.demo.common.ResponseHandler;
import com.example.demo.dto.common.ResponseDTO;
import com.example.demo.dto.req.CampaignReq;
import com.example.demo.dto.res.CampaignRes;
import com.example.demo.enums.StatusCode;
import com.example.demo.service.CampaignService;
import com.example.demo.util.ApiPaging;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/green_earth/campaign")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class CampaignController {
    private final CampaignService campaignService;

    @GetMapping()
    public ResponseEntity<ResponseDTO<Object>> getAllCampaigns(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        try {
            if (ApiPaging.isPagedRequest(q, page, size)) {
                return ResponseHandler.success(
                        (Object) campaignService.searchCampaigns(
                                q != null ? q : "",
                                ApiPaging.pageOrZero(page),
                                ApiPaging.sizeBounded(size, 20)
                        ),
                        "Success"
                );
            }
            return ResponseHandler.success((Object) campaignService.getAllCampaigns(), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<CampaignRes>> findCampaignById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(campaignService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<ResponseDTO<CampaignRes>> createCampaign(@RequestBody CampaignReq req) {
        try {
            return ResponseHandler.success(campaignService.create(req), "Success");
        } catch (ValidationException v) {
            return ResponseHandler.error(StatusCode.VALIDATION_ERROR, v.getMessage());
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<CampaignRes>> updateCampaign(@PathVariable Long id, @RequestBody CampaignReq req) {
        try {
            return ResponseHandler.success(campaignService.update(id, req), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> deleteCampaign(@PathVariable Long id) {
        try {
            campaignService.delete(id);
            return ResponseHandler.success("Campaign deleted successfully", "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }
}

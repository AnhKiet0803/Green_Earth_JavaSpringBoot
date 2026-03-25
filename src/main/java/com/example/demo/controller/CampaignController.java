package com.example.demo.controller;

import com.example.demo.common.ResponseHandler;
import com.example.demo.dto.common.ResponseDTO;
import com.example.demo.dto.req.CampaignReq;
import com.example.demo.dto.res.CampaignRes;
import com.example.demo.enums.StatusCode;
import com.example.demo.service.CampaignService;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/green_earth/campaign")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class CampaignController {
    private final CampaignService campaignService;

    @GetMapping()
    public ResponseEntity<ResponseDTO<List<CampaignRes>>> getAllCampaigns() {
        try {
            return ResponseHandler.success(campaignService.getAllCampaigns(), "Thành công");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<CampaignRes>> findCampaignById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(campaignService.findById(id), "Thành công");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<ResponseDTO<CampaignRes>> createCampaign(@RequestBody CampaignReq req) {
        try {
            return ResponseHandler.success(campaignService.create(req), "Thành công");
        } catch (ValidationException v) {
            return ResponseHandler.error(StatusCode.VALIDATION_ERROR, v.getMessage());
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<CampaignRes>> updateCampaign(@PathVariable Long id, @RequestBody CampaignReq req) {
        try {
            return ResponseHandler.success(campaignService.update(id, req), "Thành công");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> deleteCampaign(@PathVariable Long id) {
        try {
            campaignService.delete(id);
            return ResponseHandler.success("Xoá chiến dịch thành công", "Thành công");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }
}

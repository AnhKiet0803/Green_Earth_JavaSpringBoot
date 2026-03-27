package com.example.demo.controller;

import com.example.demo.common.ResponseHandler;
import com.example.demo.dto.common.ResponseDTO;
import com.example.demo.dto.req.OrganizationInfoReq;
import com.example.demo.dto.res.OrganizationInfoRes;
import com.example.demo.enums.StatusCode;
import com.example.demo.service.OrganizationInfoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/green_earth/organization_info")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class OrganizationInfoController {
    private final OrganizationInfoService organizationInfoService;

    @GetMapping()
    public ResponseEntity<ResponseDTO<OrganizationInfoRes>> getOrganizationInfo() {
        try {
            OrganizationInfoRes organizationInfo = organizationInfoService.getOrganizationInfo();
            return organizationInfo != null
                    ? ResponseHandler.success(organizationInfo, "Thành công")
                    : ResponseHandler.error(StatusCode.NOT_FOUND, "Organization info not found");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<OrganizationInfoRes>> updateOrganizationInfo(@PathVariable Long id, @RequestBody OrganizationInfoReq req) {
        try {
            return ResponseHandler.success(organizationInfoService.update(id, req), "Thành công");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }
}
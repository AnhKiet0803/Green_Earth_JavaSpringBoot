package com.example.demo.controller;

import com.example.demo.common.ResponseHandler;
import com.example.demo.dto.common.ResponseDTO;
import com.example.demo.util.ApiPaging;
import com.example.demo.dto.req.PartnerApplicationReq;
import com.example.demo.dto.req.PartnerApplicationReviewReq;
import com.example.demo.dto.req.PartnerAssetUploadReq;
import com.example.demo.dto.req.PartnerLoginReq;
import com.example.demo.dto.req.DevPartnerPasswordResetReq;
import com.example.demo.dto.req.PartnerMessageCreateReq;
import com.example.demo.dto.res.PartnerApplicationRes;
import com.example.demo.dto.res.PartnerAssetRes;
import com.example.demo.dto.res.PartnerLoginRes;
import com.example.demo.dto.res.DevPartnerPasswordResetRes;
import com.example.demo.dto.res.PartnerMessageRes;
import com.example.demo.enums.StatusCode;
import com.example.demo.service.PartnerApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/green_earth/partner-applications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PartnerApplicationController {
    private final PartnerApplicationService partnerApplicationService;

    @Value("${app.dev.partner-reset-enabled:false}")
    private boolean devPartnerResetEnabled;

    @PostMapping("/public-request")
    public ResponseEntity<ResponseDTO<PartnerApplicationRes>> submitPublicRequest(@RequestBody PartnerApplicationReq req) {
        try {
            return ResponseHandler.success(partnerApplicationService.submitApplication(req), "Partner request submitted");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<Object>> getAllApplications(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        try {
            if (ApiPaging.isPagedRequest(q, page, size)) {
                return ResponseHandler.success(
                        (Object) partnerApplicationService.searchApplications(
                                q != null ? q : "",
                                ApiPaging.pageOrZero(page),
                                ApiPaging.sizeBounded(size, 20)
                        ),
                        "Success"
                );
            }
            return ResponseHandler.success((Object) partnerApplicationService.getAllApplications(), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}/review")
    public ResponseEntity<ResponseDTO<PartnerApplicationRes>> reviewApplication(
            @PathVariable Long id,
            @RequestBody PartnerApplicationReviewReq req
    ) {
        try {
            return ResponseHandler.success(partnerApplicationService.reviewApplication(id, req), "Application updated");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{id}/issue-credentials")
    public ResponseEntity<ResponseDTO<PartnerApplicationRes>> issuePartnerCredentials(@PathVariable Long id) {
        try {
            return ResponseHandler.success(partnerApplicationService.issuePartnerCredentials(id), "Partner account created. Welcome email was attempted; check SMTP config if inbox does not receive.");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/partner-auth/login")
    public ResponseEntity<ResponseDTO<PartnerLoginRes>> loginPartner(@RequestBody PartnerLoginReq req) {
        try {
            return ResponseHandler.success(partnerApplicationService.loginPartner(req), "Login success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/dev/reset-password")
    public ResponseEntity<ResponseDTO<DevPartnerPasswordResetRes>> devResetPartnerPassword(@RequestBody DevPartnerPasswordResetReq req) {
        try {
            if (!devPartnerResetEnabled) {
                return ResponseHandler.error(StatusCode.FORBIDDEN, "Dev reset endpoint is disabled");
            }
            String password = partnerApplicationService.devResetPartnerPassword(req.getEmail());
            return ResponseHandler.success(new DevPartnerPasswordResetRes(req.getEmail(), password), "Password reset");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/dev/cleanup-duplicates")
    public ResponseEntity<ResponseDTO<Integer>> devCleanupDuplicateApplications() {
        try {
            if (!devPartnerResetEnabled) {
                return ResponseHandler.error(StatusCode.FORBIDDEN, "Dev cleanup endpoint is disabled");
            }
            int deleted = partnerApplicationService.cleanupDuplicateApplicationsKeepLatest();
            return ResponseHandler.success(deleted, "Duplicate partner requests cleaned");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{applicationId}/assets")
    public ResponseEntity<ResponseDTO<List<PartnerAssetRes>>> getAssets(@PathVariable Long applicationId) {
        try {
            return ResponseHandler.success(partnerApplicationService.getAssetsByApplicationId(applicationId), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{applicationId}/assets/mock-upload")
    public ResponseEntity<ResponseDTO<PartnerAssetRes>> mockUploadAsset(
            @PathVariable Long applicationId,
            @RequestBody PartnerAssetUploadReq req
    ) {
        try {
            return ResponseHandler.success(partnerApplicationService.mockUploadAsset(applicationId, req), "Uploaded");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/assets/{assetId}")
    public ResponseEntity<ResponseDTO<Boolean>> deleteAsset(@PathVariable Long assetId) {
        try {
            partnerApplicationService.deleteAsset(assetId);
            return ResponseHandler.success(true, "Deleted");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{applicationId}/messages")
    public ResponseEntity<ResponseDTO<List<PartnerMessageRes>>> getMessages(@PathVariable Long applicationId) {
        try {
            return ResponseHandler.success(partnerApplicationService.getMessagesByApplicationId(applicationId), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{applicationId}/messages")
    public ResponseEntity<ResponseDTO<PartnerMessageRes>> createMessage(
            @PathVariable Long applicationId,
            @RequestBody PartnerMessageCreateReq req
    ) {
        try {
            return ResponseHandler.success(partnerApplicationService.createPartnerMessage(applicationId, req), "Created");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }
}

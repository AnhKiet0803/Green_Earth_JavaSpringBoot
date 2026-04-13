package com.example.demo.service;

import com.example.demo.dto.common.PageResult;
import com.example.demo.dto.req.PartnerApplicationReq;
import com.example.demo.dto.req.PartnerApplicationReviewReq;
import com.example.demo.dto.req.PartnerAssetUploadReq;
import com.example.demo.dto.req.PartnerLoginReq;
import com.example.demo.dto.req.PartnerMessageCreateReq;
import com.example.demo.dto.res.PartnerApplicationRes;
import com.example.demo.dto.res.PartnerAssetRes;
import com.example.demo.dto.res.PartnerLoginRes;
import com.example.demo.dto.res.PartnerMessageRes;
import com.example.demo.entity.PartnerApplication;
import com.example.demo.entity.PartnerAsset;
import com.example.demo.entity.PartnerMessage;
import com.example.demo.entity.User;
import com.example.demo.repository.PartnerApplicationRepository;
import com.example.demo.repository.PartnerAssetRepository;
import com.example.demo.repository.PartnerMessageRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartnerApplicationService {
    private final PartnerApplicationRepository partnerApplicationRepository;
    private final PartnerAssetRepository partnerAssetRepository;
    private final PartnerMessageRepository partnerMessageRepository;
    private final UserRepository userRepository;
    private final PartnerNotificationService partnerNotificationService;
    @Value("${app.partner.portal-login-url:http://localhost:5173/partner-login}")
    private String partnerLoginUrl;

    @Transactional(rollbackFor = Exception.class)
    public PartnerApplicationRes submitApplication(PartnerApplicationReq req) {
        PartnerApplication application = new PartnerApplication();
        application.setOrganizationName(req.getOrganizationName());
        application.setContactName(req.getContactName());
        application.setEmail(req.getEmail());
        application.setPhoneNumber(req.getPhoneNumber());
        application.setWebsite(req.getWebsite());
        application.setCountry(req.getCountry());
        application.setCompanySize(req.getCompanySize());
        application.setIndustry(req.getIndustry());
        application.setProgramType(req.getProgramType());
        application.setExpectedContribution(req.getExpectedContribution());
        application.setTimeline(req.getTimeline());
        application.setMessage(req.getMessage());
        application.setStatus("new");
        application.setSubmittedAt(new Timestamp(System.currentTimeMillis()));

        PartnerApplication saved = partnerApplicationRepository.save(application);
        // Keep request submission successful even when SMTP credentials are not configured.
        // This guarantees admin can still review requests from DB.
        try {
            partnerNotificationService.sendSubmissionReceivedEmail(
                    saved.getEmail(),
                    saved.getOrganizationName()
            );
        } catch (Exception e) {
            log.warn("Failed to send submission confirmation email to {}", saved.getEmail(), e);
        }
        partnerNotificationService.sendNewRequestToAdmins(
                saved.getOrganizationName(),
                saved.getContactName(),
                saved.getEmail(),
                saved.getProgramType()
        );

        return PartnerApplicationRes.toJson(saved, buildPartnerLoginLink(saved.getEmail()));
    }

    public List<PartnerApplicationRes> getAllApplications() {
        return partnerApplicationRepository.findAll()
                .stream()
                .map(PartnerApplicationRes::toJson)
                .toList();
    }

    public PageResult<PartnerApplicationRes> searchApplications(String q, int page, int size) {
        Pageable pg = PageRequest.of(page, Math.min(Math.max(size, 1), 100));
        Page<PartnerApplication> p = (q == null || q.isBlank())
                ? partnerApplicationRepository.findAll(pg)
                : partnerApplicationRepository.searchByKeyword(q.trim(), pg);
        List<PartnerApplicationRes> content = p.getContent().stream()
                .map(PartnerApplicationRes::toJson)
                .toList();
        return new PageResult<>(content, p.getTotalElements(), p.getTotalPages(), p.getNumber(), p.getSize());
    }

    public PartnerApplicationRes reviewApplication(Long id, PartnerApplicationReviewReq req) {
        PartnerApplication application = partnerApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partner application not found"));

        String normalizedStatus = req.getStatus().trim().toLowerCase(Locale.ROOT);
        if (!List.of("new", "in_review", "accepted", "rejected").contains(normalizedStatus)) {
            throw new RuntimeException(
                    "Invalid review status. Use accepted to approve the application, then POST /{id}/issue-credentials to create the partner account and send email."
            );
        }

        application.setStatus(normalizedStatus);
        application.setAdminNote(req.getAdminNote());

        return PartnerApplicationRes.toJson(partnerApplicationRepository.save(application));
    }

    /**
     * After admin sets status to {@code accepted}, call this to create/update the portal user
     * and send welcome email to the email stored on the application.
     */
    @Transactional(rollbackFor = Exception.class)
    public PartnerApplicationRes issuePartnerCredentials(Long id) {
        PartnerApplication application = partnerApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partner application not found"));

        if (!"accepted".equals(application.getStatus())) {
            throw new RuntimeException("Application must be in accepted status before issuing credentials");
        }

        String tempPassword = "GE-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        User user = userRepository.findByEmail(application.getEmail());
        if (user == null) {
            user = new User();
            user.setName(application.getContactName());
            user.setEmail(application.getEmail());
            user.setPhone(null);
            user.setRole(User.Role.user);
            user.setAvatar(null);
        }
        user.setPassword(tempPassword);
        User savedUser = userRepository.save(user);

        application.setApprovedUserId(savedUser.getId());
        application.setApprovedAt(new Timestamp(System.currentTimeMillis()));
        application.setStatus("approved");

        PartnerApplication saved = partnerApplicationRepository.save(application);

        try {
            partnerNotificationService.sendApprovedEmail(
                    application.getEmail(),
                    application.getOrganizationName(),
                    tempPassword
            );
        } catch (Exception e) {
            log.warn("Failed to send approved email to {}", application.getEmail(), e);
        }

        return PartnerApplicationRes.toJson(saved);
    }

    public PartnerLoginRes loginPartner(PartnerLoginReq req) {
        String normalizedEmail = req.getEmail() == null ? "" : req.getEmail().trim();
        String normalizedPassword = req.getPassword() == null ? "" : req.getPassword().trim();

        User user = userRepository.findByEmail(normalizedEmail);
        if (user == null || user.getPassword() == null || !user.getPassword().equals(normalizedPassword)) {
            throw new RuntimeException("Invalid email or password");
        }

        Optional<PartnerApplication> approvedApplication = partnerApplicationRepository
                .findTopByEmailAndStatusOrderBySubmittedAtDesc(normalizedEmail, "approved");
        if (approvedApplication.isEmpty()) {
            throw new RuntimeException("Your partner request is not approved yet");
        }

        return new PartnerLoginRes(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole() != null ? user.getRole().name() : "user",
                approvedApplication.get().getId(),
                approvedApplication.get().getStatus()
        );
    }

    public String devResetPartnerPassword(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new RuntimeException("Email is required");
        }
        String normalizedEmail = email.trim();

        Optional<PartnerApplication> approvedApplication = partnerApplicationRepository
                .findTopByEmailAndStatusOrderBySubmittedAtDesc(normalizedEmail, "approved");
        if (approvedApplication.isEmpty()) {
            throw new RuntimeException("Partner is not approved yet");
        }

        User user = userRepository.findByEmail(normalizedEmail);
        if (user == null) {
            throw new RuntimeException("User not found for this email");
        }

        String newPassword = "GE-RESET-" + UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase(Locale.ROOT);
        user.setPassword(newPassword);
        userRepository.save(user);
        return newPassword;
    }

    @Transactional(rollbackFor = Exception.class)
    public int cleanupDuplicateApplicationsKeepLatest() {
        List<PartnerApplication> all = new ArrayList<>(partnerApplicationRepository.findAll());
        all.sort(
                Comparator.comparing((PartnerApplication p) -> p.getEmail() == null ? "" : p.getEmail().trim().toLowerCase(Locale.ROOT))
                        .thenComparing(PartnerApplication::getSubmittedAt, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(PartnerApplication::getId, Comparator.reverseOrder())
        );

        Set<String> seenEmails = new HashSet<>();
        List<Long> deleteIds = new ArrayList<>();
        for (PartnerApplication app : all) {
            String key = app.getEmail() == null ? "" : app.getEmail().trim().toLowerCase(Locale.ROOT);
            if (seenEmails.contains(key)) {
                deleteIds.add(app.getId());
            } else {
                seenEmails.add(key);
            }
        }
        if (!deleteIds.isEmpty()) {
            partnerApplicationRepository.deleteAllById(deleteIds);
        }
        return deleteIds.size();
    }

    public List<PartnerAssetRes> getAssetsByApplicationId(Long applicationId) {
        ensureApplicationExists(applicationId);
        return partnerAssetRepository.findByApplicationIdOrderByUploadedAtDesc(applicationId)
                .stream()
                .map(PartnerAssetRes::toJson)
                .toList();
    }

    public PartnerAssetRes mockUploadAsset(Long applicationId, PartnerAssetUploadReq req) {
        ensureApplicationExists(applicationId);
        if (req.getFileName() == null || req.getFileName().trim().isEmpty()) {
            throw new RuntimeException("fileName is required");
        }

        PartnerAsset asset = new PartnerAsset();
        asset.setApplicationId(applicationId);
        asset.setFileName(req.getFileName().trim());
        asset.setFileType(req.getFileType());
        asset.setFileSize(req.getFileSize() != null ? req.getFileSize() : 0L);
        asset.setUploadedAt(new Timestamp(System.currentTimeMillis()));
        return PartnerAssetRes.toJson(partnerAssetRepository.save(asset));
    }

    public void deleteAsset(Long assetId) {
        PartnerAsset asset = partnerAssetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found"));
        partnerAssetRepository.delete(asset);
    }

    public List<PartnerMessageRes> getMessagesByApplicationId(Long applicationId) {
        ensureApplicationExists(applicationId);
        return partnerMessageRepository.findByApplicationIdOrderByCreatedAtDesc(applicationId)
                .stream()
                .map(PartnerMessageRes::toJson)
                .toList();
    }

    public PartnerMessageRes createPartnerMessage(Long applicationId, PartnerMessageCreateReq req) {
        ensureApplicationExists(applicationId);
        if (req.getContent() == null || req.getContent().trim().isEmpty()) {
            throw new RuntimeException("content is required");
        }

        PartnerMessage message = new PartnerMessage();
        message.setApplicationId(applicationId);
        message.setSenderType("partner");
        message.setContent(req.getContent().trim());
        message.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return PartnerMessageRes.toJson(partnerMessageRepository.save(message));
    }

    private void ensureApplicationExists(Long applicationId) {
        partnerApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Partner application not found"));
    }

    private String buildPartnerLoginLink(String email) {
        if (email == null || email.trim().isEmpty()) {
            return partnerLoginUrl;
        }
        return partnerLoginUrl + "?email=" + URLEncoder.encode(email.trim(), StandardCharsets.UTF_8);
    }
}

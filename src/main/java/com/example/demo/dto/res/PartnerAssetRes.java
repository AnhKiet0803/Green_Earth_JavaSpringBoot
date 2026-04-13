package com.example.demo.dto.res;

import com.example.demo.entity.PartnerAsset;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class PartnerAssetRes {
    private Long id;
    private Long applicationId;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private Timestamp uploadedAt;

    public static PartnerAssetRes toJson(PartnerAsset entity) {
        return new PartnerAssetRes(
                entity.getId(),
                entity.getApplicationId(),
                entity.getFileName(),
                entity.getFileType(),
                entity.getFileSize(),
                entity.getUploadedAt()
        );
    }
}


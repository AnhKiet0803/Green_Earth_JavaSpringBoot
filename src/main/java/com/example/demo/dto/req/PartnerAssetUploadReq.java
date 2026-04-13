package com.example.demo.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerAssetUploadReq {
    private String fileName;
    private String fileType;
    private Long fileSize;
}


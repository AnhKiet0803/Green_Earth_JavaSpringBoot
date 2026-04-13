package com.example.demo.dto.res;

import com.example.demo.entity.PartnerMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class PartnerMessageRes {
    private Long id;
    private Long applicationId;
    private String senderType;
    private String content;
    private Timestamp createdAt;

    public static PartnerMessageRes toJson(PartnerMessage entity) {
        return new PartnerMessageRes(
                entity.getId(),
                entity.getApplicationId(),
                entity.getSenderType(),
                entity.getContent(),
                entity.getCreatedAt()
        );
    }
}


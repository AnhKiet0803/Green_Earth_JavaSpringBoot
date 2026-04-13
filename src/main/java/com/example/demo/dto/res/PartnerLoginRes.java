package com.example.demo.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PartnerLoginRes {
    private Long userId;
    private String name;
    private String email;
    private String role;
    private Long applicationId;
    private String status;
}

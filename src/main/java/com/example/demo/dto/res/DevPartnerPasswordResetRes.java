package com.example.demo.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DevPartnerPasswordResetRes {
    private String email;
    private String password;
}


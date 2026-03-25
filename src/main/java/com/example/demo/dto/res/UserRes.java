package com.example.demo.dto.res;

import com.example.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserRes {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String avatar;

    public static UserRes toJson(User user) {
        return new UserRes(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole().name(),
                user.getAvatar()
        );
    }
}
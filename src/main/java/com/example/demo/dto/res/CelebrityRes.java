package com.example.demo.dto.res;

import com.example.demo.entity.Celebrity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CelebrityRes {
    private Long id;
    private String name;
    private String description;
    private String image;
    private String socialLink;

    public static CelebrityRes toJson(Celebrity celebrity) {
        return new CelebrityRes(
                celebrity.getId(),
                celebrity.getName(),
                celebrity.getDescription(),
                celebrity.getImage(),
                celebrity.getSocialLink()
        );
    }
}
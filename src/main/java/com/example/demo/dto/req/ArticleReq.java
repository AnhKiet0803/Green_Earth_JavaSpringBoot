package com.example.demo.dto.req;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleReq {
    private Long id;

    @NotBlank(message = "Title is required")
    @Min(value = 5, message = "Title must have at least 5 characters")
    private String title;

    private String content;

    private String image;

    private Long authorId;

    private Long categoryId;
}
package com.example.demo.dto.req;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EventReq {
    private Long id;

    @NotBlank(message = "Event title is required")
    @Min(value = 5, message = "Event title must have at least 5 characters")
    private String title;

    private String description;

    private String location;

    private Date eventDate;

    private String image;

    private Long createdBy;
}
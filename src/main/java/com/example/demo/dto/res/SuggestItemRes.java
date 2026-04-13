package com.example.demo.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuggestItemRes {
    /** "campaign" | "article" | "event" | "page" */
    private String type;
    private Long id;
    private String title;
    /** e.g. location, category name */
    private String hint;
    /** When type = page: frontend path, e.g. /about */
    private String path;
}

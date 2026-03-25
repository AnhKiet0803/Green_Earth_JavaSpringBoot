package com.example.demo.dto.res;

import com.example.demo.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class EventRes {
    private Long id;
    private String title;
    private String description;
    private String location;
    private Date eventDate;
    private String image;

    public static EventRes toJson(Event event){
        return new EventRes(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getLocation(),
                event.getEventDate(),
                event.getImage()
        );
    }
}

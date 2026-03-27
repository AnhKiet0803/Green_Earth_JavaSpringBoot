package com.example.demo.dto.res;

import com.example.demo.entity.Event;
import com.example.demo.entity.EventParticipant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<String> participantNames;
    private String status;

    public static EventRes toJson(Event event){
        List<String> names = new ArrayList<>();
        if (event.getParticipants() != null) {
            names = event.getParticipants().stream()
                    .filter(p -> p.getUser() != null)
                    .map(p -> p.getUser().getName())
                    .collect(Collectors.toList());
        }
        return new EventRes(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getLocation(),
                event.getEventDate(),
                event.getImage(),
                names,
                event.getStatus()
        );
    }
}

package com.example.demo.service;

import com.example.demo.dto.req.EventReq;
import com.example.demo.dto.res.EventRes;
import com.example.demo.entity.Event;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public List<EventRes> getAllEvents() {
        return eventRepository.findAll().stream().map(EventRes::toJson).toList();
    }

    public EventRes findById(Long id) {
        return EventRes.toJson(eventRepository.findById(id).get());
    }

    public EventRes create(EventReq req) {
        try {
            Event event = new Event();
            event.setTitle(req.getTitle());
            event.setDescription(req.getDescription());
            event.setLocation(req.getLocation());
            event.setEventDate(req.getEventDate());
            event.setImage(req.getImage());
            event.setCreatedBy(userRepository.findById(req.getCreatedBy()).get());
            return EventRes.toJson(eventRepository.save(event));
        }catch (Exception e){
            return null;
        }
    }

    public EventRes update(Long id, EventReq req) {
        try {
            Event event = eventRepository.findById(id).get();
            event.setTitle(req.getTitle());
            event.setDescription(req.getDescription());
            event.setLocation(req.getLocation());
            event.setEventDate(req.getEventDate());
            event.setImage(req.getImage());
            event.setCreatedBy(userRepository.findById(req.getCreatedBy()).get());
            return EventRes.toJson(eventRepository.save(event));
        }catch (Exception e){
            return null;
        }
    }

    public void delete(Long id) {
        eventRepository.deleteById(id);
    }
}
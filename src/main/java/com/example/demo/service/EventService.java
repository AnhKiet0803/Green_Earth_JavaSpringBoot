package com.example.demo.service;

import com.example.demo.dto.common.PageResult;
import com.example.demo.dto.req.EventReq;
import com.example.demo.dto.res.EventRes;
import com.example.demo.entity.Event;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public PageResult<EventRes> searchEvents(String q, int page, int size) {
        Pageable pg = PageRequest.of(page, Math.min(Math.max(size, 1), 100));
        Page<Event> p = (q == null || q.isBlank())
                ? eventRepository.findAll(pg)
                : eventRepository.searchByKeyword(q.trim(), pg);
        List<EventRes> content = p.getContent().stream().map(EventRes::toJson).toList();
        return new PageResult<>(content, p.getTotalElements(), p.getTotalPages(), p.getNumber(), p.getSize());
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
            event.setStatus(req.getStatus());
            event.setSearchKeywords(req.getSearchKeywords());
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
            event.setStatus(req.getStatus());
            event.setSearchKeywords(req.getSearchKeywords());
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
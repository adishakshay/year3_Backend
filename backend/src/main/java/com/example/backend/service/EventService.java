package com.example.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.model.Event;
import com.example.backend.repository.EventRepo;

@Service
public class EventService {
    
    @Autowired
    EventRepo er;

    public Event create(Event ev)
    {
        return er.save(ev);
    }

    public Event getById(int eventId)
    {
        return er.findById(eventId).orElse(null);
    }

    public List<Event> getAll()
    {
        return er.findAll();
    }

    public boolean updateEvent(int eventId, Event ev) {
        Optional<Event> existingEvent = er.findById(eventId);
        if (!existingEvent.isPresent()) {
            return false;
        }
        try {
            ev.setEventId(eventId); // Ensure the eventId is set before saving
            er.save(ev);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    

    public boolean deleteEvent(int eventId) {
        if (this.er.findById(eventId).isEmpty()) {
            return false;
        }
        er.deleteById(eventId);
        return true;
    }
    
}

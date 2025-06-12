package com.example.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.model.Event;
import com.example.backend.service.EventService;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
public class EventController {
 
    @Autowired
    EventService es;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/event/add")
    public ResponseEntity<Event> addEvent(@RequestBody Event ev) {

        Event obj = es.create(ev);       
        return new ResponseEntity<>(obj,HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/event/getId/{eventId}")
    public ResponseEntity<Event> get(@PathVariable("eventId") int eventId) {

        try{
            Event obj = es.getById(eventId);
            return new ResponseEntity<>(obj,HttpStatus.OK);
        }
        catch(Exception e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/event/verify")
    public ResponseEntity<Boolean> verifyEvent(@RequestBody Event ev) {
        try {
            List<Event> events = es.getAll();
            for (Event event : events) {
                if (event.getName().equals(ev.getName()) &&
                    event.getPhoneNumber().equals(ev.getPhoneNumber()) &&
                    event.getEventType().equals(ev.getEventType())) {
                    return new ResponseEntity<>(true, HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/adminuser/events/count")
    public ResponseEntity<Integer> getEventCount() {
        int count = es.getAll().size(); // Assuming getAll() returns a list of events
        return new ResponseEntity<>(count, HttpStatus.OK);
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/event/getAll")
    public ResponseEntity<List<Event>> getAll() 
    {
        try
        {
            List<Event> obj = es.getAll();
            return new ResponseEntity<>(obj,HttpStatus.OK);
        }
        catch(Exception e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/adminuser/register/{eventId}")
    public ResponseEntity<Event> putMethod(@PathVariable("eventId") int eventId, @RequestBody Event ev) {
        ev.setEventId(eventId); // Ensure the eventId in the URL is set in the request body
        if (es.updateEvent(eventId, ev)) {
            return new ResponseEntity<>(ev, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
 
    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/adminuser/register/{eventId}")
    public ResponseEntity<Boolean> deleteMethod(@PathVariable("eventId") int eventId) {
        System.out.println("Received request to delete event with ID: " + eventId); // Add this line
        try {
            boolean isDeleted = es.deleteEvent(eventId);
            if (isDeleted) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
}

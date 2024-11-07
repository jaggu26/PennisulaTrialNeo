package com.peninsula.event.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.peninsula.event.service.EventService;


@RestController
public class EventController {
	
	@Autowired
	private EventService eventService; 

	Map<String, Object> response = new HashMap<>();
	
	@PostMapping("/createEvent")
	public ResponseEntity<Map<String, Object>> createEvent(@RequestBody HashMap<String, Object> data) {
		
		response=eventService.createEvent(data);
		
		if(response.get("status").toString().equals("Error"))
		{
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}else {
			return ResponseEntity.ok(response);
		}
		
	}
	
	@GetMapping("/getEventByDate")
	public ResponseEntity<Map<String, Object>>getEventByDate(@RequestParam Map<String,Object> data) {
		
		//get the data as list from the service 
		response= eventService.getEventByDate(data);

		if(response.get("status").toString().equals("Error"))
		{
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}else {
			return ResponseEntity.ok(response);
		}
	}
	
	@PostMapping("/updateEvent")
	public ResponseEntity<Map<String, Object>> updateEvent(@RequestBody HashMap<String, Object> data) {
		
		response=eventService.updateEvent(data);
		
		if(response.get("status").toString().equals("Error"))
		{
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}else {
			return ResponseEntity.ok(response);
		}
		
	}
}

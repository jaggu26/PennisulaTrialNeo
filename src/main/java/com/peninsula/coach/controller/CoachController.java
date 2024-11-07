package com.peninsula.coach.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.peninsula.coach.service.CoachService;

@RestController
public class CoachController {

	@Autowired
	public CoachService coachService;

	Map<String, Object> response = new HashMap<>();

	@PostMapping("/createCoach")
	public ResponseEntity<Map<String, Object>> createCoach(@RequestBody HashMap<String, Object> data) {
		response = coachService.addCoach(data);
		if (response.get("status").toString().equals("Error")) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		} else {
			return ResponseEntity.ok(response);
		}
	}

	@PostMapping("/updateCoach")
	public ResponseEntity<Map<String, Object>> updateCoach(@RequestBody HashMap<String, Object> data) {
		response = coachService.updateCoach(data);
		if (response.get("status").toString().equals("Error")) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		} else {
			return ResponseEntity.ok(response);
		}

	}

}

package com.peninsula.user.management.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.peninsula.user.management.service.UserManagementService;

@RestController
public class UserManagementController {
	
	@Autowired
	public UserManagementService userManagementService;
	
	@PostMapping("/userBlocking")
	public ResponseEntity<Map<String, Object>>userManagement(@RequestBody Map<String, Object> data){
		Map<String, Object> response = new HashMap<>();
		response = userManagementService.userManagement(data);
		if(response.get("status").toString().equals("Error")) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}
		else {
			return ResponseEntity.ok(response);			
		}
	}

}

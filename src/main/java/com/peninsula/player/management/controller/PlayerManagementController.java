package com.peninsula.player.management.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.peninsula.player.management.service.PlayerManagementService;

@RestController
public class PlayerManagementController {

	@Autowired
	public PlayerManagementService playerManagementService;

	@PostMapping("/playerApprovalStatusRequest")
	public ResponseEntity<Map<String, Object>> playerManagement(@RequestBody Map<String, Object> data) {
		Map<String, Object> response = new HashMap<>();
		response = playerManagementService.playerManagement(data);
		if(response.get("status").toString().equals("Error")) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}
		else {
			return ResponseEntity.ok(response);			
		}
	}

}

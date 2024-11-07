package com.peninsula.batch.controller;

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

import com.peninsula.batch.service.BatchService;

@RestController
public class BatchController {
	
	@Autowired
	private BatchService batchService;
	
	Map<String, Object> response=new HashMap<>();
	
	
	@PostMapping("/createBatch")
	public ResponseEntity<Map<String, Object>> createBatch(@RequestBody HashMap<String, Object> data){
		response=batchService.addBatch(data);
		if(response.get("status").toString().equals("Error")) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}
		else {
			return ResponseEntity.ok(response);
			
		}
	}
	
	
	//player and batch controllers-->
	
	@PostMapping("/assignPlayerToBatch")
	public ResponseEntity<Map<String, Object>> assignPlayerToBatch(@RequestBody HashMap<String, Object> data){
		response=batchService.assignPlayerToBatch(data);
		if(response.get("status").toString().equals("Error")) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}
		else {
			return ResponseEntity.ok(response);
			
		}
	}
	
	@GetMapping("/getBatchOfaPlayer")
	public ResponseEntity<Map<String, Object>> getBatchOfaPlayer(@RequestParam HashMap<String, Object> data){//Solution:Have to use Request Body
		response=batchService.getBatchOfaPlayer(data);
		if(response.get("status").toString().equals("Error")) {		
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}
		else {
			return ResponseEntity.ok(response);
			
		}
	}
	
	@GetMapping("/getPlayersFromaBatch")
	public ResponseEntity<Map<String, Object>> getPlayersFromaBatch(@RequestParam HashMap<String, Object> data){
		response=batchService.getPlayersFromaBatch(data);
		if(response.get("status").toString().equals("Error")) {		
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}
		else {
			return ResponseEntity.ok(response);
			
		}
		
	}
	
	
	//coach and batch controllers--->
	@PostMapping("/assignCoachToBatch")
	public ResponseEntity<Map<String, Object>> assignCoachToBatch(@RequestBody HashMap<String, Object> data){
		response=batchService.assignCoachToBatch(data);
		if(response.get("status").toString().equals("Error")) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}
		else {
			return ResponseEntity.ok(response);
			
		}
	}
	
	@GetMapping("/getBatchOfaCoach")
	public ResponseEntity<Map<String, Object>> getBatchOfaCoach(@RequestParam HashMap<String, Object> data){
		response=batchService.getBatchOfaCoach(data);
		if(response.get("status").toString().equals("Error")) {		
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}
		else {
			return ResponseEntity.ok(response);
			
		}
	}
	
	@GetMapping("/getCoachesFromaBatch")
	public ResponseEntity<Map<String, Object>> getsCoachesFromaBatch(@RequestParam HashMap<String, Object> data){
		response=batchService.getCoachesFromaBatch(data);
		if(response.get("status").toString().equals("Error")) {		
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}
		else {
			return ResponseEntity.ok(response);
			
		}
		
	}
	
}

package com.peninsula.user.management.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.peninsula.common.repository.UserCredentialRepository;
import com.peninsula.communication.email.model.EmailRequest;
import com.peninsula.registration.model.UserCredentials;
import com.peninsula.registration.service.RegistrationService;

@Service
public class UserManagementService {

	@Autowired
	public UserCredentialRepository userCredentialRepository;

	

	private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

	RestTemplate restTemplate = new RestTemplate();
	private static final String EmailAPI = "http://localhost:8080/send-email";

	public Map<String, Object> userManagement(Map<String, Object> data) {
		Map<String, Object> response = new HashMap<>();
		try {
			Long userId = Long.parseLong(data.get("userId").toString().trim());

			// Check if the user exists by finding the user personal details with the given
			// email
			UserCredentials userCredentials = userCredentialRepository.findByUserId(userId);
			if (userCredentials == null) {

				response.put("status", "Error");
				response.put("message", "User does not exist");
				return response;

			} else {
				// If user exists, fetch personal details and update status to 'blocked'

				userCredentials.getUserPersonalDetails().setStatus("blocked");
				userCredentialRepository.save(userCredentials);

				// Notify user via email about account block
				sendBlockEmail(userCredentials.getUserPersonalDetails().getEmail(),
						userCredentials.getUserPersonalDetails().getFullName());

				// Response indicating successful block
				response.put("status", "Success");
				response.put("message", "Account blocked successfully");
				return response;
			}

		} catch (Exception e) {
			logger.info("There is an error", e);
			response.put("status", "Error");
			return response;
		}

	}

	// for sending email
	private void sendBlockEmail(String email, String fullName) {
		EmailRequest emailRequest = new EmailRequest();
		emailRequest.setTo(email);
		emailRequest.setSubject("Regret to inform !");
		EmailRequest.Body body = new EmailRequest.Body();
		body.setGreeting("Dear, " + fullName + "!");
		body.setMain("Your account is blocked");
		body.setFooter("Best Regards,Company");
		emailRequest.setBody(body);
		restTemplate.postForObject(EmailAPI, emailRequest, Void.class);
	}

}

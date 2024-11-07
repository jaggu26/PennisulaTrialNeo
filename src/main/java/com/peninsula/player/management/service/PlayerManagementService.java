package com.peninsula.player.management.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
public class PlayerManagementService {

	private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

	

	@Autowired
	public UserCredentialRepository userCredentialRepository;

	RestTemplate restTemplate = new RestTemplate();
	private static final String EmailAPI = "http://localhost:8080/send-email";

	public Map<String, Object> playerManagement(Map<String, Object> data) {
		Map<String, Object> response = new HashMap<>();

		try {

			// Extract status and email from the input data
			String approvalStatus = data.get("approvalStatus").toString().trim();
			Long userId = Long.parseLong(data.get("userId").toString().trim());
			UserCredentials userCredentials = userCredentialRepository.findByUserId(userId);

			if (userCredentials == null) {
				// Response for case when user credentials are not found
				response.put("status", "Error");
				response.put("message", "The user doesn't  exist");
				return response;

			} else {

				// Handling approval request
				if (!"approved".equals(approvalStatus) && !"rejected".equals(approvalStatus)) {
					response.put("status", "Error");
					response.put("message", "invalid request");
					return response;
				}
				// Handling rejection request
				else if ("approved".equals(approvalStatus)) {
					String playerId = generatePlayerId();
					userCredentials.setUsername(playerId);
					userCredentials.setRole("player");
					userCredentialRepository.save(userCredentials);

					// Update user status to approved
					userCredentials.getUserPersonalDetails().setStatus("approved");
					userCredentialRepository.save(userCredentials);

					// Sent approval email with playerid
					sendApproval(userCredentials.getUserPersonalDetails().getEmail(), playerId,
							userCredentials.getUserPersonalDetails().getFullName());

					response.put("status", "Success");
					response.put("message", "Your Request Has Been Approved");

					return response;
				} else {
					// Update user status to rejected
					userCredentials.getUserPersonalDetails().setStatus("rejected");
					userCredentialRepository.save(userCredentials);

					// Send rejection email
					sendRejection(userCredentials.getUserPersonalDetails().getEmail(),
							userCredentials.getUserPersonalDetails().getFullName());

					response.put("status", "Success");
					response.put("message", "SORRY, Your request has been rejected");
					return response;
				}

			}

		}
		// Log any exceptions that occur during processing
		catch (Exception e) {
			logger.info("There is an error", e);
			response.put("status", "Error");
			return response;

		}

	}

	// for sending email
	private void sendApproval(String email, String newPlayerId, String fullName) {
		EmailRequest emailRequest = new EmailRequest();
		emailRequest.setTo(email);
		emailRequest.setSubject("Welcome to our Peninsula Community!");
		EmailRequest.Body body = new EmailRequest.Body();
		body.setGreeting("Dear, " + fullName + "!");
		body.setMain("Thank you for registering with us. We are glad to have you on board. Your new player id is  "
				+ newPlayerId);
		body.setFooter("Best Regards,Company");
		emailRequest.setBody(body);
		restTemplate.postForObject(EmailAPI, emailRequest, Void.class);
	}

	private void sendRejection(String email, String fullName) {
		EmailRequest emailRequest = new EmailRequest();
		emailRequest.setTo(email);
		emailRequest.setSubject("About Your Application !");
		EmailRequest.Body body = new EmailRequest.Body();
		body.setGreeting("Dear, " + fullName + "!");
		body.setMain(
				"we looked through you application unfortunatily some requirement is not met, please try again later");
		body.setFooter("Best Regards,Company");
		emailRequest.setBody(body);
		restTemplate.postForObject(EmailAPI, emailRequest, Void.class);
	}

	// Generates a unique player ID
	public static String generatePlayerId() {
		String prefix = "PEN";
		// Get the last two digits of the current year
		String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		String lastTwoDigitsOfYear = year.substring(year.length() - 2);
		String suffix = "PID";

		// Generates a random 3-digit number
		int randomNumber = new Random().nextInt(900) + 100; // 100 to 999

		return prefix + lastTwoDigitsOfYear + suffix + randomNumber;
	}

}

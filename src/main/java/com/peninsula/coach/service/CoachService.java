package com.peninsula.coach.service;

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
import com.peninsula.common.repository.UserPersonalDetailsRepository;
import com.peninsula.common.utils.Utils;
import com.peninsula.communication.email.model.EmailRequest;
import com.peninsula.registration.model.UserCredentials;
import com.peninsula.registration.model.UserPersonalDetails;
import com.peninsula.registration.service.RegistrationService;

@Service
public class CoachService {

	private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

	@Autowired
	private UserPersonalDetailsRepository userPersonalDetailsRepository;

	@Autowired
	private UserCredentialRepository userCredentialRepository;

	RestTemplate restTemplate = new RestTemplate();
	private static final String EmailAPI = "http://localhost:8080/send-email";

	// for adding coach
	public Map<String, Object> addCoach(HashMap<String, Object> data) {
		Map<String, Object> response = new HashMap<>();
		try {
			String email = data.get("email").toString().trim();
			boolean isEmailExist = userPersonalDetailsRepository.findEmailExistOrNot(email);
			if (isEmailExist) {
				response.put("status", "Error");
				response.put("message", "This email is already exist");
				return response;
			} else {
				// saving to user credential table
				UserCredentials userCredentials = new UserCredentials();
				// Generate a unique employee ID
				String uniqueEmployeeId = generateEmployeeId();
				// Set the unique ID as the userId
				userCredentials.setUsername(uniqueEmployeeId);
				String generatedPassword = Utils.generatePassword();
				sendUserCredential(data.get("email").toString().trim(), generatedPassword, uniqueEmployeeId,
						data.get("fullName").toString().trim());// calling sending email
				userCredentials.setPassword(Utils.hashPassword(generatedPassword));
				userCredentials.setRole("coach");
				userCredentialRepository.save(userCredentials);

				// saving to personal details table
				UserPersonalDetails userPersonalDetails = new UserPersonalDetails();
				userPersonalDetails.setEmail(data.get("email").toString().trim());
				userPersonalDetails.setFullName(data.get("fullName").toString().trim());
				userPersonalDetails.setPhoneNumber(data.get("phoneNumber").toString().trim());
				userPersonalDetails.setUserCredentials(userCredentials);
				userCredentials.setUserPersonalDetails(userPersonalDetails);
				userPersonalDetailsRepository.save(userPersonalDetails);

				response.put("status", "Success");
				response.put("message", "Successfully Created");
				response.put("userId", userCredentials.getUsername());
				return response;
			}
		} catch (Exception e) {
			logger.info("There is a error", e);
			response.put("status", "Error");
			return response;
		}

	}

	// for Updating Data of Coach
	public Map<String, Object> updateCoach(HashMap<String, Object> data) {
		Map<String, Object> response = new HashMap<>();
		try {
			Long coachId = Long.parseLong(data.get("coachId").toString().trim());
			String email = data.get("email").toString().trim();
			String fullName = data.get("fullName").toString().trim();
			String phoneNumber = data.get("phoneNumber").toString().trim();
			String gender = data.get("gender") == null ? "" : data.get("gender").toString().trim();
			UserCredentials coachDetails = userCredentialRepository.findByUserId(coachId);

			if (coachDetails == null) {
				response.put("status", "Error");
				response.put("message", "Invalid !!");
				return response;
			} else {
				coachDetails.getUserPersonalDetails().setEmail(email);
				coachDetails.getUserPersonalDetails().setFullName(fullName);
				coachDetails.getUserPersonalDetails().setPhoneNumber(phoneNumber);
				coachDetails.getUserPersonalDetails().setGender(gender);
				userCredentialRepository.save(coachDetails);
				response.put("status", "Success");
				response.put("message", "Successfully Updated");
				return response;
			}
		} catch (Exception e) {
			logger.info("There is a error", e);
			response.put("status", "Error");
			return response;
		}
	}

	// for sending email
	private void sendUserCredential(String email, String generatedPassword, String uniqueEmployeeId, String fullName) {
		EmailRequest emailRequest = new EmailRequest();
		emailRequest.setTo(email);
		emailRequest.setSubject("Welcome to our Peninsula Community!");
		EmailRequest.Body body = new EmailRequest.Body();
		body.setGreeting("Dear, " + fullName + "!");
		body.setMain("We’re thrilled to welcome you to the Peninsula family!.Your EmployeeId is " + uniqueEmployeeId
				+ " Your Password is  " + generatedPassword);
		body.setFooter("Best Regards,Company");
		emailRequest.setBody(body);
		restTemplate.postForObject(EmailAPI, emailRequest, Void.class);

	}

	// Creating Unique Employment Id
	public static String generateEmployeeId() {
		String prefix = "EMP";
		// Get the last two digits of the current year
		String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		String lastTwoDigitsOfYear = year.substring(year.length() - 2);
		String suffix = "EID";
		// Generates a random 3-digit number
		int randomNumber = new Random().nextInt(900) + 100; // 100 to 999
		return prefix + lastTwoDigitsOfYear + suffix + randomNumber;
	}

}

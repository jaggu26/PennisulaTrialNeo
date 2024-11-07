package com.peninsula.batch.service;

import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peninsula.batch.model.AssignToBatch;
import com.peninsula.batch.model.BatchDetails;
import com.peninsula.batch.repository.AssignToBatchRepository;
import com.peninsula.batch.repository.BatchDetailsRepository;
import com.peninsula.common.repository.UserCredentialRepository;
import com.peninsula.registration.model.UserCredentials;
import com.peninsula.registration.model.UserPersonalDetails;
import com.peninsula.registration.service.RegistrationService;

@Service
public class BatchService {
	private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

	@Autowired
	private BatchDetailsRepository batchDetailsRepository;

	@Autowired
	private AssignToBatchRepository assignToBatchRepository;

	@Autowired
	private UserCredentialRepository userCredentialRepository;

	Map<String, Object> response = new HashMap<>();

	// adding batch method
	public Map<String, Object> addBatch(HashMap<String, Object> data) {
		try {
			response.clear();
			// checking the batch name already exist or not
			boolean isBatchNameExist = batchDetailsRepository
					.batchNameExistOrNot(data.get("batchName").toString().trim());
			if (isBatchNameExist) {
				response.put("status", "Error");
				response.put("message", "Batch name already exist");
				return response;
			} else {
				BatchDetails batchDetails = new BatchDetails();
				batchDetails.setBatchName(data.get("batchName").toString().trim());
				batchDetails.setBatchType(data.get("batchType").toString().trim());
				batchDetails.setBatchCode(generateBatchCode(data.get("batchType").toString().trim(),
						data.get("batchName").toString().trim()));
				batchDetails.setBatchStartingDate(data.get("batchStartingDate").toString().trim());
				batchDetails.setBatchEndingDate(data.get("batchEndingDate").toString().trim());

				batchDetailsRepository.save(batchDetails);

				response.put("status", "Success");
				response.put("message", "Batch Successfully cerated");
				response.put("batchId", batchDetails.getBatchId());
				response.put("batchCode", batchDetails.getBatchCode());

				return response;
			}
		} catch (Exception e) {

			logger.info("Error occured", e);
			response.put("status", "Error");
			return response;
		}

	}

	//player and batch methods--------->
	
	// assignPlayerToBatch method
	public Map<String, Object> assignPlayerToBatch(HashMap<String, Object> data) {
		try {
			response.clear();
			BatchDetails batchDetails = batchDetailsRepository
					.getBatchByBatchId(Long.parseLong(data.get("batchId").toString().trim()));
			UserCredentials userCredentials = userCredentialRepository
					.getUserIdByPlayerId(data.get("playerId").toString().trim());
			// checking the batch code is valid or not
			if (batchDetails == null) {
				response.put("status", "Error");
				response.put("message", "Invalid batch code");
				return response;
			}
			// checking the user name is valid or not (user name refered to playerId and
			// Vice versa)
			else if (userCredentials == null) {
				response.put("status", "Error");
				response.put("message", "Invalid playerId");
				return response;
			} else {
				// checking the player already assigned to a batch or not
				if (assignToBatchRepository.playerAlreadyAssignedOrNot(data.get("playerId").toString().trim())) {
					response.put("status", "Error");
					response.put("message", "player already assigned");
					return response;
				} else {

					AssignToBatch assignPlayersToBatch = new AssignToBatch();
					assignPlayersToBatch.setBatchDetails(batchDetails);
					assignPlayersToBatch.setUserCredentials(userCredentials);

					assignToBatchRepository.save(assignPlayersToBatch);

					response.put("status", "Success");
					response.put("message", "player  assigned successfully ");
					return response;
				}

			}

		} catch (Exception e) {
			logger.info("Error occured", e);
			response.put("status", "Error");
			return response;
		}
	}

	// get batch of a player method

	public Map<String, Object> getBatchOfaPlayer(HashMap<String, Object> data) {
		try {
			response.clear();
			// checking the player id exist
			if (userCredentialRepository.getUserIdByPlayerId(data.get("playerId").toString().trim()) == null) {
				response.put("status", "Error");
				response.put("message", "Invalid playerId");
				return response;
			} else {
				// checking the player assigned to a batch or not
				if (!assignToBatchRepository
						.playerAlreadyAssignedOrNot(data.get("playerId").toString().trim())) {
					response.put("status", "Error");
					response.put("message", "player not assigned to any batch");
					return response;
				} else {
					// getting batch details by player id
					BatchDetails batchDetails = assignToBatchRepository
							.findBatchDetailsByPlayerId(data.get("playerId").toString().trim());
					response.put("status", "Success");
					response.put("message", "batch details fetched successfully");
					response.put("batchName", batchDetails.getBatchName());
					response.put("batchCode", batchDetails.getBatchCode());
					response.put("batchStartingDate", batchDetails.getBatchStartingDate());
					response.put("batchEndingDate", batchDetails.getBatchEndingDate());

					return response;

				}

			}

		} catch (Exception e) {
			logger.info("Error occured", e);
			response.put("status", "Error");
			return response;
		}

	}

	// get players from batch method
	public Map<String, Object> getPlayersFromaBatch(HashMap<String, Object> data) {
		try {
			response.clear();
			// checking the batch code is valid or not
			if (batchDetailsRepository.getBatchByBatchCode(data.get("batchCode").toString().trim()) == null) {
				response.put("status", "Error");
				response.put("message", "Invalid batch code");
				return response;
			} else {
				// checking the player assigned to a batch or not
				if (!assignToBatchRepository.batchHavePlayersOrNot(data.get("batchCode").toString().trim())) {
					response.put("status", "Error");
					response.put("message", "There is no players assigned to this batch");
					return response;
				} else {
					List<UserPersonalDetails> players = assignToBatchRepository
							.findPlayerDetailsByBatchCode(data.get("batchCode").toString().trim());
					Map<String,Object> playerData=new HashMap<>();
					//getting each player
					for(UserPersonalDetails player:players) {
						playerData.put("playerEmail", player.getEmail());
						playerData.put("playerPhoneNumber", player.getPhoneNumber());
						
						response.put(player.getFullName(),playerData );
					}
					response.put("status", "Success");
					response.put("message", "players details  fetched successfully ");
					return response;
				}

			}

		} catch (Exception e) {
			logger.info("Error occured", e);
			response.put("status", "Error");
			return response;
		}
	}

	//coach and batch methods------------>
	
	
	//assign coach to batch
	public Map<String, Object> assignCoachToBatch(HashMap<String, Object> data) {
		try {
			response.clear();
			BatchDetails batchDetails = batchDetailsRepository
					.getBatchByBatchId(Long.parseLong(data.get("batchId").toString().trim()));
			UserCredentials userCredentials = userCredentialRepository
					.getUserIdByCoachId(data.get("coachId").toString().trim());
			// checking the batch code is valid or not
			if (batchDetails == null) {
				response.put("status", "Error");
				response.put("message", "Invalid batch code");
				return response;
			}
			// checking the user name is valid or not (user name refered to playerId and
			// Vice versa)
			else if (userCredentials == null) {
				response.put("status", "Error");
				response.put("message", "Invalid coachId");
				return response;
			} else {
				// checking the player already assigned to a batch or not
				if (assignToBatchRepository.coachAlreadyAssignedOrNotToSameBatch(data.get("coachId").toString().trim(),data.get("batchCode").toString().trim())) {
					response.put("status", "Error");
					response.put("message", "coach already assigned to this batch");
					return response;
				} else {

					AssignToBatch assignPlayersToBatch = new AssignToBatch();
					assignPlayersToBatch.setBatchDetails(batchDetails);
					assignPlayersToBatch.setUserCredentials(userCredentials);

					assignToBatchRepository.save(assignPlayersToBatch);

					response.put("status", "Success");
					response.put("message", "coach  assigned successfully ");
					return response;
				}

			}

		} catch (Exception e) {
			logger.info("Error occured", e);
			response.put("status", "Error");
			return response;
		}
	}

	//get batches of a coach method
	public Map<String, Object> getBatchOfaCoach(HashMap<String, Object> data) {
		try {
			response.clear();
			// checking the player id exist
			if (userCredentialRepository.getUserIdByCoachId(data.get("coachId").toString().trim()) == null) {
				response.put("status", "Error");
				response.put("message", "Invalid coachId");
				return response;
			} else {
				// checking the player assigned to a batch or not
				if (!assignToBatchRepository
						.coachAlreadyAssignedOrNot(data.get("coachId").toString().trim())) {
					response.put("status", "Error");
					response.put("message", "coach not assigned to any batch");
					return response;
				} else {
					// getting batch details by player id
					List<BatchDetails> batches = assignToBatchRepository
							.findBatchDetailsByCoachId(data.get("coachId").toString().trim());
					Map<String , Object> batchData=new HashMap<>();
					for(BatchDetails batch:batches) {
						batchData.put("batchCode", batch.getBatchCode());
						batchData.put("batchStartingDate", batch.getBatchStartingDate());
						batchData.put("batchEndingDate", batch.getBatchEndingDate());
						response.put(batch.getBatchName(), batchData);
					}
					response.put("status", "Success");
					response.put("message", "batch details fetched successfully");
					return response;

				}

			}

		} catch (Exception e) {
			logger.info("Error occured", e);
			response.put("status", "Error");
			return response;
		}
	}
	
	
	//get coaches from a batch method
	public Map<String, Object> getCoachesFromaBatch(HashMap<String, Object> data) {
		try {
			response.clear();
			// checking the batch code is valid or not
			if (batchDetailsRepository.getBatchByBatchCode(data.get("batchCode").toString().trim()) == null) {
				response.put("status", "Error");
				response.put("message", "Invalid batch code");
				return response;
			} else {
				// checking the player assigned to a batch or not
				if (!assignToBatchRepository.batchHaveCoachesOrNot(data.get("batchCode").toString().trim())) {
					response.put("status", "Error");
					response.put("message", "There is no coaches assigned to this batch");
					return response;
				} else {
					List<UserPersonalDetails> coaches = assignToBatchRepository
							.findPlayerDetailsByBatchCode(data.get("batchCode").toString().trim());
					Map<String,Object> coachData=new HashMap<>();
					//getting each player
					for(UserPersonalDetails coach:coaches) {
						coachData.put("coachEmail", coach.getEmail());
						coachData.put("coachPhoneNumber", coach.getPhoneNumber());
						
						response.put(coach.getFullName(),coachData );
					}
					response.put("status", "Success");
					response.put("message", "coaches details  fetched successfully ");
					return response;
				}

			}

		} catch (Exception e) {
			logger.info("Error occured", e);
			response.put("status", "Error");
			return response;
		}
	}
	
	
	
	// batch code auto generation
	public String generateBatchCode(String batchType, String batchName) {
		String prefix = "PEN"; // Fixed prefix represent peninsula
		String yearCode = String.valueOf(Year.now().getValue()).substring(2); // Last two digits of current year
		String batchTypeCode = (batchType != null && !batchType.isEmpty()) ? batchType.substring(0, 1).toUpperCase()
				: "X";// first letter of batch type
		String nameCode = (batchName != null && batchName.length() >= 2) ? batchName.substring(0, 2).toUpperCase()
				: "XX";// first two letter of batch name

		return String.format("%s%s%s%s", prefix, yearCode, batchTypeCode, nameCode);
	}
	
	



}

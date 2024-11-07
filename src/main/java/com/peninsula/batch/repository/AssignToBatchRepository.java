package com.peninsula.batch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.peninsula.batch.model.AssignToBatch;
import com.peninsula.batch.model.BatchDetails;
import com.peninsula.registration.model.UserPersonalDetails;

public interface AssignToBatchRepository extends JpaRepository<AssignToBatch, Long> {
	// for player
	@Query(value = "select COUNT(a) > 0 from AssignToBatch a where a.userCredentials.username = :playerId")
	boolean playerAlreadyAssignedOrNot(@Param("playerId") String playerId);

	@Query(value = "select b from BatchDetails b where b.batchId = "
			+ "(select ab.batchDetails.batchId from AssignToBatch ab where ab.userCredentials.username = :playerId)")
	BatchDetails findBatchDetailsByPlayerId(@Param("playerId") String playerId);

	// for batch
	@Query(value = "select COUNT(a) > 0 from AssignToBatch a where a.batchDetails.batchCode = :batchCode")
	boolean batchHavePlayersOrNot(@Param("batchCode") String batchCode);

	@Query("SELECT p FROM UserPersonalDetails p WHERE p.userCredentials.userId IN "
			+ "(SELECT ab.userCredentials.userId FROM AssignToBatch ab WHERE ab.batchDetails.batchCode = :batchCode)")
	List<UserPersonalDetails> findPlayerDetailsByBatchCode(@Param("batchCode") String batchCode);

	@Query(value = "select COUNT(a) > 0 from AssignToBatch a where a.batchDetails.batchCode = :batchCode")
	boolean batchHaveCoachesOrNot(@Param("batchCode") String batchCode);

	// for coach
	@Query(value="select COUNT(a) > 0 from AssignToBatch a where a.userCredentials.username = :coachId and a.batchDetails.batchCode = :batchCode")
	boolean coachAlreadyAssignedOrNotToSameBatch (String coachId, String batchCode);
	
	@Query(value="select COUNT(a) > 0 from AssignToBatch a where a.userCredentials.username = :coachId")
	boolean coachAlreadyAssignedOrNot(@Param("coachId")String coachId);
	
	@Query(value = "select b from BatchDetails b where b.batchId = "
			+ "(select ab.batchDetails.batchId from AssignToBatch ab where ab.userCredentials.username = :coachId)")
	List<BatchDetails> findBatchDetailsByCoachId(@Param("coachId") String coachId);
	


}

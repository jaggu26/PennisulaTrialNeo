package com.peninsula.batch.model;

import com.peninsula.registration.model.UserCredentials;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class AssignToBatch {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long assignToBatchId;
	

    @ManyToOne
    @JoinColumn(name = "batchId", referencedColumnName = "batchId")
    private BatchDetails batchDetails;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private UserCredentials userCredentials;

	public Long getAssignPlayersToBatchId() {
		return assignToBatchId;
	}

	public void setAssignPlayersToBatchId(Long assignPlayersToBatchId) {
		this.assignToBatchId = assignPlayersToBatchId;
	}

	public BatchDetails getBatchDetails() {
		return batchDetails;
	}

	public void setBatchDetails(BatchDetails batchDetails) {
		this.batchDetails = batchDetails;
	}

	public UserCredentials getUserCredentials() {
		return userCredentials;
	}

	public void setUserCredentials(UserCredentials userCredentials) {
		this.userCredentials = userCredentials;
	}
	
	
	
}

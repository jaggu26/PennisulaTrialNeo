package com.peninsula.batch.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class BatchDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long batchId;
	
	@Column
	private String batchName;
	
	@Column
	private String batchType;
	
	@Column
	private String batchStartingDate;
	
	@Column
	private String batchEndingDate;
	
	@Column
	private String batchCode;

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public String getBatchType() {
		return batchType;
	}

	public void setBatchType(String batchType) {
		this.batchType = batchType;
	}

	public String getBatchStartingDate() {
		return batchStartingDate;
	}

	public void setBatchStartingDate(String batchStartingDate) {
		this.batchStartingDate = batchStartingDate;
	}

	public String getBatchEndingDate() {
		return batchEndingDate;
	}

	public void setBatchEndingDate(String batchEndingDate) {
		this.batchEndingDate = batchEndingDate;
	}
	
	
}

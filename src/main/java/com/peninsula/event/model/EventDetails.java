package com.peninsula.event.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class EventDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long eventId;
	
	@Column
	private String eventName;
	
	@Column
	private String eventDate;
	
	@Column
	private String  eventTime;
	
	@Column
	private String eventVenue;
	
	@Column
	private String eventType;
	
	@Column
	private String eventLink;

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public String getEventTime() {
		return eventTime;
	}

	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}

	public String getEventVenue() {
		return eventVenue;
	}

	public void setEventVenue(String eventVenue) {
		this.eventVenue = eventVenue;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getEventLink() {
		return eventLink;
	}

	public void setEventLink(String eventLink) {
		this.eventLink = eventLink;
	}
	
	
}

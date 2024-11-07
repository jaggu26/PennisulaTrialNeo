package com.peninsula.event.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peninsula.event.model.EventDetails;
import com.peninsula.event.repository.EventRepository;
import com.peninsula.registration.service.RegistrationService;

@Service
public class EventService {

	private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);


	@Autowired
	private EventRepository eventRepository;

	// method to create a new event
	public HashMap<String, Object> createEvent(HashMap<String, Object> data) {
		HashMap<String, Object> response = new HashMap<>();

		try {

			// getting all the parameters from request
			String eventName = data.get("eventName").toString().trim();
			String eventDate = data.get("eventDate").toString().trim();
			String eventTime = data.get("eventTime").toString().trim();
			String eventVenue = data.get("eventVenue").toString().trim();
			String eventType = data.get("eventType").toString().trim();
			String eventLink = data.get("eventLink").toString().trim();

			// checking if all the parameters are entered or not
			if (eventName.equals(null) || eventDate.equals(null) || eventTime.equals(null) || eventVenue.equals(null)
					|| eventType.equals(null) || eventLink.equals(null)) {
				response.put("status", "Error");
				response.put("message", "All parameters are required");
				return response;
			}

			// setting all the values in the corresponding fields of the table
			else {
				EventDetails eventDetails=new EventDetails();
				eventDetails.setEventName(eventName);
				eventDetails.setEventDate(eventDate);
				eventDetails.setEventTime(eventTime);
				eventDetails.setEventVenue(eventVenue);
				eventDetails.setEventType(eventType);
				eventDetails.setEventLink(eventLink);

				// saving the table with data
				eventRepository.save(eventDetails);

				response.put("status", "Success");
				response.put("message", "Event created successfully");

				return response;
			}

		} catch (Exception e) {

			logger.info("There is an error", e);
			response.put("status", "Error");
			return response;

		}
	}

	// method to get the events of a day
	public Map<String, Object> getEventByDate(Map<String, Object> data) {
		Map<String, Object> response = new HashMap<>();
		try {
			String eventDate = data.get("eventDate").toString().trim();
			boolean isEventExist = eventRepository.findByEventDate(eventDate);

			// checking if any event exist in this date or not
			if (!isEventExist) // if not exist this will execute
			{
				response.put("status", "Error");
				response.put("message", "No events in selected date");
				return response;
			}
			// if the event exist in this date, this part will execute
			else {
				// get the list of events from the tables and return as list to controller
				List<EventDetails> eventsDetails = eventRepository.findEventsByDate(eventDate);
				Map<String, Object> eventDetailsResponse = new HashMap<>();
				for (EventDetails eventDetails : eventsDetails) {
					eventDetailsResponse.put("eventDate", eventDetails.getEventDate());
					eventDetailsResponse.put("eventTime", eventDetails.getEventTime());
					eventDetailsResponse.put("eventVenue", eventDetails.getEventVenue());
					eventDetailsResponse.put("eventType", eventDetails.getEventType());
					eventDetailsResponse.put("eventLink", eventDetails.getEventLink());
					response.put(eventDetails.getEventName(), eventDetailsResponse);
				}

				response.put("status", "Success");
				response.put("message", "Event details fetched successfully");
				return response;
			}

		} catch (Exception e) {

			logger.info("Error occured", e);
			response.put("status", "Error");
			return response;
		}
	}

	// updating event
	public HashMap<String, Object> updateEvent(HashMap<String, Object> data) {
		HashMap<String, Object> response = new HashMap<>();

		try {

			// getting all the parameters from request
			Long eventId = Long.parseLong(data.get("eventId").toString().trim());
			String eventName = data.get("eventName").toString().trim();
			String eventDate = data.get("eventDate").toString().trim();
			String eventTime = data.get("eventTime").toString().trim();
			String eventVenue = data.get("eventVenue").toString().trim();
			String eventType = data.get("eventType").toString().trim();
			String eventLink = data.get("eventLink").toString().trim();

			EventDetails eventDetails = eventRepository.findByEventId(eventId);

			if (eventDetails == null) {
				response.put("status", "Error");
				response.put("message", "There is no event in this id");
				return response;
			}

			else {
				// setting all the values in the corresponding fields of the table
				eventDetails.setEventName(eventName);
				eventDetails.setEventDate(eventDate);
				eventDetails.setEventTime(eventTime);
				eventDetails.setEventVenue(eventVenue);
				eventDetails.setEventType(eventType);
				eventDetails.setEventLink(eventLink);

				// updating event in the table with new data
				eventRepository.save(eventDetails);

				response.put("status", "Success");
				response.put("message", "Event updated successfully");

				return response;
			}

		} catch (Exception e) {

			logger.info("An error occured", e);
			response.put("status", "Error");
			return response;
		}

	}
}

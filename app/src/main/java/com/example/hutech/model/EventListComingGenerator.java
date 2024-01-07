package com.example.hutech.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventListComingGenerator {

    // Assuming you have a method to get the current date
    private static Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    // Method to generate EventListComing from the list of all events
    public static List<EventListComing> generateEventListComing(List<Events> allEvents) {
        List<EventListComing> eventListComing = new ArrayList<>();
        Date currentDate = getCurrentDate();

        // Iterate through all events
        for (Events event : allEvents) {
            // Check if the event's beginTime is greater than the current date
            if (event.getBeginTime() != null && event.getBeginTime().toDate().after(currentDate)) {
                // Create an EventListComing object and add it to the list
                EventListComing comingEvent = new EventListComing(
                        event.getId(),
                        event.getName(),
                        event.getDescription(),
                        event.getLocation(),
                        event.getPoster(),
                        event.getFaculty(),
                        event.getBeginTime(),
                        event.getQuantity()
                );
                eventListComing.add(comingEvent);
            }
        }

        return eventListComing;
    }
}

package ru.juriasan.timestats.event;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class EventHandler {

    private static final int MAX_LIST_SIZE = 4000000;
    private Deque<Event> eventList;
    private Event lastEvent;
    private long timePeriod;
    private String timePeriodName;
    private long counter;

    public EventHandler(long timePeriod, String timePeriodName) {
        this.eventList = new ArrayDeque<>();
        this.lastEvent = null;
        this.timePeriod = timePeriod;
        this.timePeriodName = timePeriodName;
        this.counter = 0;
    }

    public long getCounter() {
        return this.counter;
    }

    public long getTimePeriod() {
        return this.timePeriod;
    }

    public long getEventsPerTimePeriod() {
        return this.counter + this.eventList.size();
    }

    private void updateCounter() {
        if (eventList.size() >= MAX_LIST_SIZE) {
            counter += eventList.size();
            eventList.clear();
        }
    }

    public boolean handleEvent(Event event) {
        if (event != null) {
            if (!handleTimePeriodBorder(event))
                return false;
            updateCounter();
        }
        clearList();
        return true;
    }

    private long clearList() {
        long removed = 0;
        Iterator<Event> it = eventList.iterator();
        while(it.hasNext()){
            Event next = it.next();
            if (System.currentTimeMillis() - next.getTime() > timePeriod) {
                it.remove();
                removed ++;
            }
            else break;
        }
        return removed;
    }

    private boolean handleTimePeriodBorder(Event currentEvent) {
        if (lastEvent == null) {
            this.lastEvent = currentEvent;
        } else if (currentEvent != null) {
            long diff = currentEvent.getTime() - lastEvent.getTime();
            if (diff <= timePeriod) {
                eventList.add(currentEvent);
            }
            else {
                counter = 0;
                this.lastEvent = currentEvent;
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("Event handler: %d events per %s", getEventsPerTimePeriod(),
                Event.formatTime(timePeriodName, timePeriod));
    }
}

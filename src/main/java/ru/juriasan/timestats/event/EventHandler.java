package ru.juriasan.timestats.event;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * This class provides base functionality for event handling.
 * This handler can be considered as a handler per single time gap:
 * per minute, per hour and per day. All the corresponding parameters
 * are passed to a constructor. Event handler counts the number of event
 * occurencies per time gap, handles the proper handling of time period
 * border (when the second has ended, for example) and provides necessary
 * statistics.
 */
public class EventHandler {

    private static final int MAX_LIST_SIZE = 4000000;
    private int maxListSize;
    private Deque<Event> eventList;
    private Event lastEvent;
    private long timePeriod;
    private String timePeriodName;
    private long counter;

    /**
     * EventHandler constructor.
     *
     * @param timePeriod time period in milliseconds.
     *                   TimePeriod is a single time unit (second, hour, day),
     *                   represented in milliseconds. Could be 1000, 60 or any other.
     * @param timePeriodName String name of a time period. Could be "second, minute" or any other.
     * @param maxEventListSize optional parameter, which indicates the maximum size of the inner
     *                         list for storing events.
     */
    public EventHandler(long timePeriod, String timePeriodName, int maxEventListSize) {
        this.eventList = new ArrayDeque<>();
        this.lastEvent = null;
        this.timePeriod = timePeriod;
        this.timePeriodName = timePeriodName;
        this.counter = 0;
        this.maxListSize = maxEventListSize;
    }

    public EventHandler(long timePeriod, String timePeriodName) {
        this.eventList = new ArrayDeque<>();
        this.lastEvent = null;
        this.timePeriod = timePeriod;
        this.timePeriodName = timePeriodName;
        this.counter = 0;
        this.maxListSize = MAX_LIST_SIZE;
    }


    public long getTimePeriod() {
        return this.timePeriod;
    }

    /**
     * Returns the full number of the events per
     * timePeriod.
    / */
    public long getEventsPerTimePeriod() {
        return this.counter + this.eventList.size();
    }

    /**
     *  When the inner list reaches maximum size, the
     *  sum from it should be accumulated and gathered
     *  somewhere else. The counter provides this functionality
     *  and the number of elements are added to the counter.
     *  The counter contains the accumulated part of the sum.
     */
    private void updateCounter() {
        if (eventList.size() >= maxListSize) {
            counter += eventList.size();
            eventList.clear();
        }
    }

    /**
     * An entry point for event handling.
     *
      * @param event event
     * @return true if event was handled successfully, else false.
     */
    public boolean handleEvent(Event event) {
        if (event != null) {
            if (!handleEventTime(event))
                return false;
            updateCounter();
        }
        clearList();
        return true;
    }

    /**
     * Clears "older" events from the list. The "age" of the event is
     * determined by time, if it doesn't suit in the timegap then it's
     * deleted from the list.
     * @return
     */
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

    /**
     * Takes the last event, which has happened a timePeriod
     * milliseconds ago and compares the difference between times.
     * If the difference doesn't suit to timePeriod then it's
     * considered that event hasn't been handled and should
     * be passed to other method (or possibly there doesn't
     * exist suitable handler at all). So false is returned.
     * When the event passes in the time gap, the true is returned
     * and event is also added to a eventList.
     *
     * @param currentEvent an event
    */
    private boolean handleEventTime(Event currentEvent) {
        if (lastEvent == null) {
            if (System.currentTimeMillis() - currentEvent.getTime() > timePeriod)
                return false;
            this.lastEvent = currentEvent;
        } else if (currentEvent != null) {
            long eventsDiff = currentEvent.getTime() - lastEvent.getTime();
            long nowDiff = System.currentTimeMillis() - currentEvent.getTime();
            if (eventsDiff <= timePeriod && nowDiff  <= timePeriod) {
                eventList.add(currentEvent);
            }
            else {
                counter = 0;
                this.eventList.clear();
                this.lastEvent = currentEvent;
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("Event %s handler: %d events per %s",
                timePeriodName, getEventsPerTimePeriod(),
                Event.formatTime(timePeriod));
    }
}

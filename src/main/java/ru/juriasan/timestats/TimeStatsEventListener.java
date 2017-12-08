package ru.juriasan.timestats;

import ru.juriasan.timestats.event.Event;
import ru.juriasan.timestats.event.EventHandler;
import ru.juriasan.timestats.util.Logger;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class is an implementation for a TimeStatsEventListener
 * object, which listens for the events in the system, and when
 * they occur, determines the time gap of the event: minute,
 * hour or day, and increments the counter of events per last
 * minute/hour/day respectively.
 * This class implements Runnable interface, where the "run" method
 * is an entry point for listener process.
* */
public class TimeStatsEventListener implements Runnable {

    BlockingQueue<Event> sharedQueue = new LinkedBlockingQueue<>();

    private static TimeStatsEventListener instance;
    private EventHandler minuteHandler;
    private EventHandler hourHandler;
    private EventHandler dayHandler;

    private TimeStatsEventListener() {
        this.minuteHandler = new EventHandler(Event.MIN, Event.MINUTES);
        this.hourHandler = new EventHandler(Event.HOUR, Event.HOURS);
        this.dayHandler = new EventHandler(Event.DAY, Event.DAYS);
    }

    public static TimeStatsEventListener getInstance() {
        if (instance == null) {
            synchronized (TimeStatsEventListener.class) {
                if (instance == null)
                    instance = new TimeStatsEventListener();
            }
        }
        return instance;
    }

    /**
     * Another threads can call this method and pass an event e there.
     * This method just adds the event to the tasks queue, which another
     * thread, represented by a "run" method, reads from.
     *
     * @param e an event
     */
    public void accept(Event e) {
        if (e != null)
            this.sharedQueue.add(e);
    }

    /**
     * This method is an entry point for a listener process.
     * It polls the task queue in the loop and, if there are any tasks,
     * gets the last and tries to handle it and check, which time gap this
     * event belongs to. If there is no any gap for an event, the message
     * is written to a logger, and process continues. The process finishes,
     * when interrupted.
     */
    public void run() {
        try {
            int timeoutCount = 0;
            while(true) {
                if (Thread.currentThread().isInterrupted())
                    throw new InterruptedException();

                Event e = sharedQueue.poll();
                if (e != null) {
                    boolean eventHasBeenHandled = minuteHandler.handleEvent(e);
                    eventHasBeenHandled |= hourHandler.handleEvent(e);
                    eventHasBeenHandled |= dayHandler.handleEvent(e);
                    if (!eventHasBeenHandled)
                        Logger.getInstance().info(String.format("%s %s", "The event doesn't suit to any time range",
                                e.toString()));
                    else {
                        Logger.getInstance().info(toString());
                    }

                }
            }
        }
        catch (InterruptedException ex) {
            Logger.getInstance().error(ex.getMessage());
        }
        finally {
            Logger.getInstance().info("TimeStatsEventListened is finishing its work.");
        }
    }

    @Override
    public String toString() {
        return String.format("%d per min, %d per hour %d per day, time %s",
                minuteHandler.getEventsPerTimePeriod(),
                hourHandler.getEventsPerTimePeriod(),
                dayHandler.getEventsPerTimePeriod(),
                new Date().toString());
    }
}

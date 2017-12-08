package ru.juriasan.timestats;

import ru.juriasan.timestats.event.Event;
import ru.juriasan.timestats.event.EventHandler;
import ru.juriasan.timestats.util.Logger;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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

    public void accept(Event e) {
        if (e != null)
            this.sharedQueue.add(e);
    }

    public void run() {
        try {
            while(true) {
                if (Thread.currentThread().isInterrupted())
                    throw new InterruptedException();

                Event e = sharedQueue.poll();
                if (e != null) {
                    boolean eventHasBeenHandled = false;
                    eventHasBeenHandled = minuteHandler.handleEvent(e);
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
            ex.printStackTrace();
            Logger.getInstance().info("Producer finished its work.");
        }

    }

    @Override
    public String toString() {
        return String.format("%d mins, %d hours %d days, time %s",
                minuteHandler.getEventsPerTimePeriod(),
                hourHandler.getEventsPerTimePeriod(),
                dayHandler.getEventsPerTimePeriod(),
                new Date().toString());
    }
}

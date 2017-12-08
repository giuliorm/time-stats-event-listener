package ru.juriasan.timestats;

import ru.juriasan.timestats.event.Event;
import ru.juriasan.timestats.event.EventHandler;
import ru.juriasan.timestats.util.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TimeStatsEventListener implements Runnable {

    BlockingQueue<Event> sharedQueue = new LinkedBlockingQueue<>();

    private static TimeStatsEventListener instance;
    private EventHandler minuteHandler;
    private EventHandler hourHandler;
    private EventHandler dayHandler;

    private TimeStatsEventListener() {
        this.minuteHandler = new EventHandler(Event.MIN);
        this.hourHandler = new EventHandler(Event.HOUR);
        this.dayHandler = new EventHandler(Event.DAY);
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
        while(true) {

            Event e = sharedQueue.poll();
            if (e != null) {
                boolean eventHasBeenHandled = false;
                eventHasBeenHandled = minuteHandler.handleEvent(e);
                eventHasBeenHandled |= hourHandler.handleEvent(e);
                eventHasBeenHandled |= dayHandler.handleEvent(e);
                if (!eventHasBeenHandled)
                    Logger.getInstance().info(String.format("%s %s", "The event doesn't suit to any time range",
                            e.toString()));
                else
                    Logger.getInstance().info(String.format("%s %s", "Successfully handled", e.toString()));

            }
        }
    }
}

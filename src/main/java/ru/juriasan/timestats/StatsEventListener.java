package ru.juriasan.timestats;

import ru.juriasan.timestats.event.Event;
import ru.juriasan.timestats.event.EventHandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class StatsEventListener implements Runnable {

    BlockingQueue<Event> sharedQueue = new LinkedBlockingQueue<Event>();
    private static final int MAX_LIST_SIZE = 4000000;

    public static final int SEC = 1000;
    public static final int MIN = SEC * 60;
    public static final int HOUR = MIN * 60;
    public static final int DAY = HOUR * 24;

    private static StatsEventListener instance;
    private EventHandler minuteHandler;
    private EventHandler hourHandler;
    private EventHandler dayHandler;

    private StatsEventListener() {
        this.minuteHandler = new EventHandler(MIN);
        this.hourHandler = new EventHandler(HOUR);
        this.dayHandler = new EventHandler(DAY);
    }

    public static StatsEventListener getInstance() {
        if (instance == null) {
            synchronized (StatsEventListener.class) {
                if (instance == null)
                    instance = new StatsEventListener();
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
                minuteHandler.handleEvent(e);
                hourHandler.handleEvent(e);
                dayHandler.handleEvent(e);
            }
        }
    }
}

package ru.juriasan.timestats.event;

import java.time.DateTimeException;
import java.util.Date;

public class Event {

    public static final int SEC = 1000;
    public static final int MIN = SEC * 60;
    public static final int HOUR = MIN * 60;
    public static final int DAY = HOUR * 24;

    public static final String SECONDS = "sec";
    public static final String MINUTES = "min";
    public static final String HOURS = "hour";
    public static final String DAYS = "day";

    private String name;
    private long timeMillis;

    public Event(long timeMillis) {
        this.timeMillis = timeMillis;
    }

    public Event(String name, long timeMillis) {
        this.name = name;
        this.timeMillis = timeMillis;
    }

    public long getTime() {
        return this.timeMillis;
    }

    public String getName() {
        return name;
    }

    public static String formatTime(long time) {
//        long days = System.currentTimeMillis() / DAY - time / DAY;
//        long hour = System.currentTimeMillis() / HOUR -  time  / HOUR;
//        long min = System.currentTimeMillis() / MIN  - time / MIN;
//        long sec = System.currentTimeMillis() / SEC - time / SEC;

     //   return String.format("%d days, %d hours, %d minutes, %d seconds, %d millis",
       //         days, hour, min, sec, System.currentTimeMillis() - time);
        return String.format("%s ", new Date().toString());
    }
    @Override
    public String toString() {

        return String.format(" event %s, time %s. ", name == null ? "" : name, formatTime(this.timeMillis));
    }
}

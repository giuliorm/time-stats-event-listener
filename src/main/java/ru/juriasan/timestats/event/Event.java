package ru.juriasan.timestats.event;

public class Event {

    public static final int SEC = 1000;
    public static final int MIN = SEC * 60;
    public static final int HOUR = MIN * 60;
    public static final int DAY = HOUR * 24;

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

    public static String formatTime(String name, long time) {
        long days = time / DAY;
        long hour = time  / HOUR;
        long min = time / MIN;
        long sec = time / SEC;

        return String.format("%d days, %d hours, %d minutes, %d seconds, %d millis",
                name, days, hour, min, sec, time);
    }
    @Override
    public String toString() {

        return String.format(" time %s. ", formatTime(name == null ? "" : name,  this.timeMillis));
    }
}

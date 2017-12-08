package ru.juriasan.timestats.event;

public class Event {

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

    @Override
    public String toString() {
        return this.name == null ? super.toString() : name;
    }
}

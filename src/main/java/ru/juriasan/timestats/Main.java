package ru.juriasan.timestats;

public class Main {

    public static void main(String[] args) {
        StatsEventListener listener = StatsEventListener.getInstance();
        EventProducer producer = new EventProducer(listener.getSharedQueue());
        producer.runProducers();
        listener.run();
    }
}
